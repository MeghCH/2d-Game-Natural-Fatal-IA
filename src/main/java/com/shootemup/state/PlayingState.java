package com.shootemup.state;

import com.shootemup.core.Game;
import com.shootemup.entity.Player;
import com.shootemup.entity.Projectile;
import com.shootemup.input.InputController;
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
        addPlayer(new Player(2, Game.WIDTH - sideMargin - Player.SIZE, startY, InputController.player2()));
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
     * Met à jour les joueurs et leurs tirs, fait avancer les balles,
     * retire celles qui ne sont plus en jeu, puis met à jour le panneau d'état.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void update(double dt) {
        for (Player player : players) {
            player.update(dt);
            projectiles.addAll(player.shoot());
        }

        for (Projectile projectile : projectiles) {
            projectile.update(dt);
        }
        projectiles.removeIf(projectile -> projectile.isOutOfScreen() || !projectile.isActive());

        statusPanel.update(dt);
    }

    /**
     * Dessine le fond, les joueurs, puis le panneau d'état par-dessus.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.web("#101820"));
        gc.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

        for (Player player : players) {
            player.render(gc);
        }

        for (Projectile projectile : projectiles) {
            projectile.render(gc);
        }
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

        // DEBUG : blesse le joueur 1 pour tester le panneau. À retirer quand les ennemis existeront.
        if (code == KeyCode.H) {
            players.get(0).takeDamage(2);
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