package com.shootemup.state;

import com.shootemup.core.Game;
import com.shootemup.bonus.Bonus;
import com.shootemup.bonus.BonusSpawner;
import com.shootemup.enemy.Enemy;
import com.shootemup.enemy.EnemyFactory;
import com.shootemup.enemy.EvilAI;
import com.shootemup.entity.Player;
import com.shootemup.entity.Projectile;
import com.shootemup.input.InputController;
import com.shootemup.level.LevelManager;
import com.shootemup.level.TutorialManager;
import com.shootemup.ui.PlayerStatusPanel;
import com.shootemup.ui.Starfield;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

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

    /** Les bonus de soin actuellement en chute. */
    private final List<Bonus> bonuses = new ArrayList<>();

    /** Le planificateur qui fait tomber les bonus. */
    private final BonusSpawner bonusSpawner = new BonusSpawner();

    /** Le ciel étoilé qui défile derrière la partie. */
    private final Starfield background = new Starfield(Game.WIDTH, Game.HEIGHT);

    /** Le panneau qui affiche la vie et le score des joueurs. */
    private final PlayerStatusPanel statusPanel = new PlayerStatusPanel();

    /**
     * Crée une partie à deux joueurs, placés à gauche de l'écran.
     *
     * @param game le jeu auquel appartient cet écran
     */
    public PlayingState(Game game) {
        this.game = game;
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
     * puis met à jour le panneau d'état. Termine la partie quand tous les joueurs
     * sont morts ou quand la progression est finie.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void update(double dt) {
        background.update(dt);
        for (Player player : players) {
            player.update(dt);
            projectiles.addAll(player.shoot());
        }

        levelManager.update(dt);
        handleCollisions();
        updateBonuses(dt);

        for (Projectile projectile : projectiles) {
            projectile.update(dt);
        }
        projectiles.removeIf(projectile -> projectile.isOutOfScreen() || !projectile.isActive());

        if (levelManager.getCurrentLevel().isCompleted()) {
            levelManager.nextLevel();
        }

        statusPanel.update(dt);

        if (!anyPlayerAlive() || levelManager.isFinished()) {
            game.setState(new GameOverState(game, totalScore()));
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
     * Fait tomber les bonus, les donne aux joueurs placés dessous,
     * et retire ceux qui sont attrapés ou sortis en bas.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    private void updateBonuses(double dt) {
        bonusSpawner.update(dt);
        if (bonusSpawner.isReady()) {
            bonuses.add(bonusSpawner.spawn());
        }
        List<Bonus> taken = new ArrayList<>();
        for (Bonus bonus : bonuses) {
            bonus.update(dt);
            for (Player player : players) {
                if (player.isAlive() && player.collidesWith(bonus)) {
                    bonus.collect(player);
                    taken.add(bonus);
                    break;
                }
            }
            if (bonus.getY() > Game.HEIGHT) {
                taken.add(bonus);
            }
        }
        bonuses.removeAll(taken);
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
     * Dessine le fond, les ennemis, les joueurs, les balles, le numéro du niveau,
     * puis le panneau d'état par-dessus.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.web(levelManager.getBackgroundColor()));
        gc.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        background.render(gc);

        for (Enemy enemy : levelManager.getCurrentLevel().getActiveEnemies()) {
            enemy.render(gc);
        }

        for (Bonus bonus : bonuses) {
            bonus.render(gc);
        }

        for (Player player : players) {
            player.render(gc);
        }

        for (Projectile projectile : projectiles) {
            projectile.render(gc);
        }

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(14));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.TOP);
        gc.fillText("NIVEAU " + levelManager.getCurrentLevelNumber(), Game.WIDTH / 2.0, 20);

        statusPanel.render(gc);
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