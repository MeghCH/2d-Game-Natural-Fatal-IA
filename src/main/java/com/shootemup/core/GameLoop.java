package com.shootemup.core;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import java.util.Objects;

/**
 * Boucle de jeu : appelle la mise à jour puis le dessin à chaque image.
 */
public class GameLoop extends AnimationTimer {

    /** Le jeu mis à jour et dessiné. */
    private final Game game;

    /** Le canvas sur lequel dessiner. */
    private final GraphicsContext gc;

    /** Date de l'image précédente, en nanosecondes. */
    private long last = 0;

    /** {@code true} tant que la boucle tourne. */
    private boolean running = false;

    /**
     * Crée la boucle, sans la démarrer.
     *
     * @param game le jeu mis à jour et dessiné
     * @param gc le canvas sur lequel dessiner
     */
    public GameLoop(Game game, GraphicsContext gc) {
        this.game = Objects.requireNonNull(game, "Le jeu ne peut pas être null.");
        this.gc = Objects.requireNonNull(gc, "Le contexte graphique ne peut pas être null.");
    }

    /**
     * Calcule le temps écoulé, met à jour le jeu puis le dessine.
     *
     * @param now la date actuelle, en nanosecondes
     */
    @Override
    public void handle(long now) {
        if (last == 0) {
            last = now;
            return;
        }
        double dt = (now - last) / 1e9;
        last = now;
        game.update(dt);
        game.render(gc);
    }

    /**
     * Démarre la boucle.
     */
    @Override
    public void start() {
        last = 0;
        super.start();
        running = true;
    }

    /**
     * Arrête la boucle.
     */
    @Override
    public void stop() {
        super.stop();
        running = false;
    }

    /**
     * Indique si la boucle tourne.
     *
     * @return {@code true} tant que la boucle tourne
     */
    public boolean isRunning() {
        return running;
    }
}
