package com.shootemup.state;

import com.shootemup.core.Game;
import com.shootemup.enemy.Enemy;
import com.shootemup.enemy.EnemyFactory;
import com.shootemup.enemy.EvilAI;
import com.shootemup.entity.Player;
import com.shootemup.entity.Projectile;
import com.shootemup.entity.Potion;
import com.shootemup.entity.PotionSpawner;
import com.shootemup.input.InputController;
import com.shootemup.level.Level;
import com.shootemup.level.LevelManager;
import com.shootemup.level.TutorialManager;
import com.shootemup.ui.ArcadeTheme;
import com.shootemup.ui.BossHealthBar;
import com.shootemup.ui.LevelBackdrop;
import com.shootemup.ui.PlayerStatusPanel;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Écran de la partie en cours.
 * <p>
 * Gère les joueurs et le panneau d'état, transmet les touches aux contrôleurs
 * des joueurs, et passe en pause quand on appuie sur Échap.
 */
public class PlayingState implements GameState {

    /** Le jeu, utilisé pour changer d'écran (pause, game over). */
    private final Game game;

    /** Les joueurs de la partie. */
    private final List<Player> players = new ArrayList<>();

    /** Balles actuellement en vol. */
    private final List<Projectile> projectiles = new ArrayList<>();

    /** La progression des niveaux : vagues d'ennemis, dans l'ordre. */
    private final LevelManager levelManager;

    /** Le panneau qui affiche la vie et le score des joueurs. */
    private final PlayerStatusPanel statusPanel = new PlayerStatusPanel();

    /** La barre de vie du boss, visible pendant le combat final. */
    private final BossHealthBar bossHealthBar = new BossHealthBar();

    /** Potions actuellement à l'écran. */
    private final List<Potion> potions = new ArrayList<>();

    /** Fait tomber des potions au hasard. */
    private final PotionSpawner potionSpawner;

    /** Décor animé du niveau : dégradé, poussières et grille d'horizon. */
    private final LevelBackdrop backdrop = new LevelBackdrop(Game.WIDTH, Game.HEIGHT);

    
    /**
     * Crée une partie à deux joueurs, avec un générateur de potions ordinaire.
     *
     * @param game le jeu auquel appartient cet écran
     */
    public PlayingState(Game game) {
        this(game, new PotionSpawner());
    }

    /**
     * Crée une partie à deux joueurs, placés en bas de l'écran,
     * le joueur 1 à gauche et le joueur 2 à droite.
     *
     * @param game          le jeu auquel appartient cet écran
     * @param potionSpawner le générateur de potions, par exemple avec un hasard fixe dans les tests
     */
    public PlayingState(Game game, PotionSpawner potionSpawner) {
        this.game = game;
        this.potionSpawner = potionSpawner;
        double startY = Game.HEIGHT - 60;
        double sideMargin = 60;
        addPlayer(new Player(1, sideMargin, startY, InputController.player1()));
        addPlayer(new Player(2, Game.WIDTH - sideMargin - Player.WIDTH, startY, InputController.player2()));
        this.levelManager = TutorialManager.createProgression(new EnemyFactory());
    }

    /**
     * Ajoute un joueur à la partie et l'affiche dans le panneau d'état.
     *
     * @param player le joueur à ajouter
     */
    private void addPlayer(Player player) {
        players.add(player);
        statusPanel.track(player);
    }

    /**
     * Indique les joueurs de la partie.
     *
     * @return les joueurs de la partie, en lecture seule
     */
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    /**
     * Indique les balles actuellement en vol.
     *
     * @return les balles actuellement en vol, en lecture seule
     */
    public List<Projectile> getProjectiles() {
        return Collections.unmodifiableList(projectiles);
    }

    /**
     * Indique les potions actuellement à l'écran.
     *
     * @return les potions actuellement à l'écran, en lecture seule
     */
    public List<Potion> getPotions() {
        return Collections.unmodifiableList(potions);
    }

    /**
     * Indique la progression des niveaux de la partie.
     *
     * @return la progression des niveaux
     */
    public LevelManager getLevelManager() {
        return levelManager;
    }

    /**
     * Oublie les touches enfoncées de tous les joueurs, pour éviter qu'une touche
     * relâchée pendant la pause reste considérée comme enfoncée à la reprise.
     */
    @Override
    public void enter() {
        for (Player player : players) {
            player.getControls().clear();
        }
    }

    /**
     * Met à jour les joueurs et leurs tirs, fait avancer les balles et les ennemis,
     * applique les collisions, passe au niveau suivant quand il est terminé,
     * puis met à jour le panneau d'état. Partie perdue quand tous les joueurs
     * sont morts, gagnée quand la progression est finie (boss vaincu).
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void update(double dt) {
        for (Player player : players) {
            player.update(dt);
            projectiles.addAll(player.shoot());
        }

        levelManager.update(dt);
        backdrop.update(dt);
        handleCollisions();
        updatePotions(dt);

        for (Projectile projectile : projectiles) {
            projectile.update(dt);
        }
        projectiles.removeIf(projectile -> projectile.isOutOfScreen() || !projectile.isActive());

        if (levelManager.getCurrentLevel().isCompleted()) {
            levelManager.nextLevel();
        }

        statusPanel.update(dt);
        bossHealthBar.setBoss(findBoss());

        if (!anyPlayerAlive()) {
            game.setState(new GameOverState(game, totalScore()));
        } else if (levelManager.isFinished()) {
            game.setState(new VictoryState(game, totalScore()));
        }
    }

    /**
     * Applique les collisions : les balles détruisent les ennemis et donnent
     * leur score au tireur, les ennemis blessent les joueurs au contact.
     * Un petit ennemi est détruit quand il touche ; le boss encaisse et continue.
     */
    private void handleCollisions() {
        List<Enemy> enemies = levelManager.getCurrentLevel().getActiveEnemies();
        for (Projectile projectile : projectiles) {
            if (!projectile.isActive()) {
                continue;
            }
            for (Enemy enemy : enemies) {
                if (enemy.isAlive() && projectile.getOwner() != enemy && enemy.collidesWith(projectile)) {
                    enemy.takeDamage(projectile.getDamage());
                    projectile.destroy();
                    if (!enemy.isAlive() && projectile.getOwner() instanceof Player shooter) {
                        shooter.addScore(enemy.getScoreValue());
                    }
                    break;
                }
            }
        }
        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) {
                continue;
            }
            for (Player player : players) {
                if (player.isAlive() && player.collidesWith(enemy)) {
                    enemy.attack(player);
                    if (!(enemy instanceof EvilAI)) {
                        enemy.takeDamage(1000);
                    }
                }
            }
        }
    }

    /**
     * Fait apparaître et tomber les potions, les donne aux joueurs qui les touchent,
     * et retire celles qui ont fini leur effet ou sont sorties en bas.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    private void updatePotions(double dt) {
        potions.addAll(potionSpawner.update(dt));
        for (Potion potion : potions) {
            potion.update(dt);
            for (Player player : players) {
                if (player.isAlive() && !potion.isCollected() && player.collidesWith(potion)) {
                    potion.collect(player);
                }
            }
        }
        potions.removeIf(potion -> !potion.isActive() || potion.isOutOfScreen());
    }

    /**
     * Cherche le boss parmi les ennemis en jeu, pour sa barre de vie.
     *
     * @return le boss en jeu, ou {@code null} hors du combat final
     */
    private EvilAI findBoss() {
        for (Enemy enemy : levelManager.getCurrentLevel().getActiveEnemies()) {
            if (enemy instanceof EvilAI boss) {
                return boss;
            }
        }
        return null;
    }

    /**
     * Indique la barre de vie du boss.
     *
     * @return la barre de vie du boss
     */
    public BossHealthBar getBossHealthBar() {
        return bossHealthBar;
    }

    /**
     * Indique si au moins un joueur est encore en vie.
     *
     * @return {@code true} si au moins un joueur est en vie
     */
    private boolean anyPlayerAlive() {
        for (Player player : players) {
            if (player.isAlive()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Additionne les scores des joueurs.
     *
     * @return la somme des scores
     */
    private int totalScore() {
        int total = 0;
        for (Player player : players) {
            total += player.getScore();
        }
        return total;
    }

    /**
     * Dessine le décor animé du niveau, les ennemis, les joueurs, les balles,
     * les potions, le badge de stage, puis le panneau d'état par-dessus.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        Level level = levelManager.getCurrentLevel();
        backdrop.render(gc, level.getBackgroundColor(), level.getAccentColor());

        for (Enemy enemy : level.getActiveEnemies()) {
            enemy.render(gc);
        }

        for (Potion potion : potions) {
            potion.render(gc);
        }

        for (Player player : players) {
            player.render(gc);
        }

        for (Projectile projectile : projectiles) {
            projectile.render(gc);
        }

        drawStageBadge(gc, level);

        statusPanel.render(gc);
        bossHealthBar.render(gc);
        ArcadeTheme.crtFrame(gc, Game.WIDTH, Game.HEIGHT);
    }

    /**
     * Dessine le badge « STAGE » au centre du haut de l'écran.
     *
     * @param gc    contexte graphique
     * @param level le niveau en cours
     */
    private void drawStageBadge(GraphicsContext gc, Level level) {
        double width = 300;
        double x = (Game.WIDTH - width) / 2.0;
        gc.setFill(ArcadeTheme.withAlpha(ArcadeTheme.PANEL, 0.72));
        gc.fillRect(x, 8, width, 24);
        gc.setStroke(ArcadeTheme.withAlpha(Color.web(level.getAccentColor()), 0.7));
        gc.setLineWidth(1);
        gc.strokeRect(x + 0.5, 8.5, width - 1, 23);

        gc.setFill(Color.web(level.getAccentColor()));
        gc.fillRect(x, 30, width, 2);

        ArcadeTheme.centered(gc,
                String.format("STAGE %02d // %s",
                        levelManager.getCurrentLevelNumber(), level.getLevelName()),
                Game.WIDTH / 2.0, 20, 12, ArcadeTheme.WHITE, ArcadeTheme.VOID);
    }

    /**
     * Passe en pause sur Échap, sinon transmet la touche à tous les joueurs.
     *
     * @param code la touche enfoncée
     */
    @Override
    public void keyPressed(KeyCode code) {
        if (code == KeyCode.ESCAPE) {
            game.setState(new PauseState(game, this));
            return;
        }

        for (Player player : players) {
            player.getControls().keyPressed(code);
        }
    }

    /**
     * Transmet la touche relâchée à tous les joueurs.
     *
     * @param code la touche relâchée
     */
    @Override
    public void keyReleased(KeyCode code) {
        for (Player player : players) {
            player.getControls().keyReleased(code);
        }
    }
}