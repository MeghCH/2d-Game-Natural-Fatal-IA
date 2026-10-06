package com.shootemup.state;

import com.shootemup.core.Game;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.Set;

/**
 * Écran de la partie en cours.
 * <p>
 * Version provisoire : un carré déplacé avec les flèches, qui sera remplacé
 * par le joueur, les ennemis et les niveaux.
 */
public class PlayingState implements GameState {

    /** Vitesse de déplacement du carré, en pixels par seconde. */
    private static final double SPEED = 300;

    /** Taille du carré, en pixels. */
    private static final double SIZE = 40;

    /** Le jeu, utilisé pour changer d'écran (pause, game over). */
    private final Game game;

    /** Touches actuellement enfoncées. */
    private final Set<KeyCode> pressed = new HashSet<>();

    /** Position horizontale du carré. */
    private double x = 100;

    /** Position verticale du carré. */
    private double y = Game.HEIGHT / 2.0;

    /**
     * Crée l'écran de partie.
     *
     * @param game le jeu auquel appartient cet écran
     */
    public PlayingState(Game game) {
        this.game = game;
    }

    /**
     * Oublie les touches enfoncées, pour éviter qu'une touche relâchée
     * pendant la pause reste considérée comme enfoncée à la reprise.
     */
    @Override
    public void enter() {
        pressed.clear();
    }

    /**
     * Déplace le carré selon les flèches enfoncées, sans le laisser sortir de l'écran.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void update(double dt) {
        if (pressed.contains(KeyCode.UP)) {
            y -= SPEED * dt;
        }
        if (pressed.contains(KeyCode.DOWN)) {
            y += SPEED * dt;
        }
        if (pressed.contains(KeyCode.LEFT)) {
            x -= SPEED * dt;
        }
        if (pressed.contains(KeyCode.RIGHT)) {
            x += SPEED * dt;
        }

        x = Math.max(0, Math.min(x, Game.WIDTH - SIZE));
        y = Math.max(0, Math.min(y, Game.HEIGHT - SIZE));
    }

    /**
     * Dessine le fond puis le carré.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.web("#101820"));
        gc.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        gc.setFill(Color.web("#5DCAA5"));
        gc.fillRect(x, y, SIZE, SIZE);
    }

    /**
     * Passe en pause sur Échap, sinon mémorise la touche enfoncée.
     *
     * @param code la touche enfoncée
     */
    @Override
    public void keyPressed(KeyCode code) {
        if (code == KeyCode.ESCAPE) {
            game.setState(new PauseState(game, this));
            return;
        }
        pressed.add(code);
    }

    /**
     * Oublie la touche relâchée.
     *
     * @param code la touche relâchée
     */
    @Override
    public void keyReleased(KeyCode code) {
        pressed.remove(code);
    }
}