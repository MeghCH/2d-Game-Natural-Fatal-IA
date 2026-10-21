package com.shootemup.core;

import com.shootemup.input.InputController;
import com.shootemup.state.GameState;
import com.shootemup.state.MenuState;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

import java.util.List;
import java.util.Objects;

/**
 * Cœur du jeu : garde l'écran courant et lui délègue tout le travail.
 * <p>
 * Joue le rôle de <b>contexte</b> dans le pattern <b>State</b> : {@code Game}
 * ne sait pas quel écran est affiché (partie, pause, menu...). Elle transmet
 * simplement chaque mise à jour, affichage et touche à l'état courant.
 * Changer d'écran se fait avec {@link #setState(GameState)}.
 * <p>
 * Garde aussi les touches des joueurs, pour que les touches choisies
 * dans l'écran des touches restent les mêmes d'une partie à l'autre.
 */
public class Game {

    /** L'écran actuellement actif. */
    private GameState currentState;

    /** La boucle de jeu, qui appelle update et render à chaque image. */
    private GameLoop loop;

    /** Les touches des joueurs : joueur 1 puis joueur 2. */
    private final List<InputController> playerControls =
            List.of(InputController.player1(), InputController.player2());

    /** Largeur de la fenêtre de jeu, en pixels. */
    public static final int WIDTH = 960;

    /** Hauteur de la fenêtre de jeu, en pixels. */
    public static final int HEIGHT = 540;

    /**
     * Crée le cœur du jeu, sans écran actif.
     */
    public Game() {
    }

    /**
     * Démarre le jeu : affiche le menu puis lance la boucle.
     *
     * @param loop la boucle de jeu à lancer
     */
    public void start(GameLoop loop) {
        this.loop = Objects.requireNonNull(loop, "La boucle ne peut pas être null.");
        setState(new MenuState(this));
        loop.start();
    }

    /**
     * Arrête la boucle de jeu. Ne fait rien si le jeu n'est pas démarré.
     */
    public void stop() {
        if (loop != null) {
            loop.stop();
        }
    }

    /**
     * Change l'écran courant, puis prévient le nouvel écran qu'il devient actif.
     *
     * @param newState le nouvel écran, jamais {@code null}
     * @throws NullPointerException si {@code newState} est {@code null}
     */
    public void setState(GameState newState) {
        this.currentState = Objects.requireNonNull(newState, "L'état ne peut pas être null.");
        newState.enter();
    }

    /**
     * Indique l'écran actuellement actif.
     *
     * @return l'écran actuellement actif, ou {@code null} si aucun n'a été défini
     */
    public GameState getCurrentState() {
        return this.currentState;
    }

    /**
     * Indique les touches des joueurs, partagées par toutes les parties.
     *
     * @return les touches du joueur 1 puis du joueur 2, en lecture seule
     */
    public List<InputController> getPlayerControls() {
        return playerControls;
    }

    /**
     * Met à jour l'écran courant.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    public void update(double dt) {
        state().update(dt);
    }

    /**
     * Dessine l'écran courant.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    public void render(GraphicsContext gc) {
        state().render(gc);
    }

    /**
     * Transmet l'appui sur une touche à l'écran courant.
     *
     * @param code la touche enfoncée
     */
    public void keyPressed(KeyCode code) {
        state().keyPressed(code);
    }

    /**
     * Transmet le relâchement d'une touche à l'écran courant.
     *
     * @param code la touche relâchée
     */
    public void keyReleased(KeyCode code) {
        state().keyReleased(code);
    }

    /**
     * Renvoie l'écran courant en vérifiant qu'il existe.
     *
     * @return l'écran courant
     * @throws IllegalStateException si aucun écran n'a encore été défini
     */
    private GameState state() {
        if (currentState == null) {
            throw new IllegalStateException(
                    "Aucun état défini : appelez setState() avant de lancer le jeu.");
        }
        return currentState;
    }
}