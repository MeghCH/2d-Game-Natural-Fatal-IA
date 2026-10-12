package com.shootemup.ui;

import com.shootemup.entity.Renderable;
import com.shootemup.entity.Updatable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Ciel étoilé qui défile vers le bas, en deux couches pour la parallaxe.
 * <p>
 * Décor procédural : aucune image à charger. Les positions sont tirées avec
 * un {@link Random}, que les tests fixent avec une graine.
 */
public class Starfield implements Updatable, Renderable {

    /** Nombre d'étoiles. */
    private static final int STAR_COUNT = 120;

    /** Vitesse de la couche lente, en pixels par seconde. */
    private static final double SLOW_SPEED = 20;

    /** Vitesse de la couche rapide, en pixels par seconde. */
    private static final double FAST_SPEED = 60;

    /**
     * Une étoile : position et couche (taille et vitesse).
     */
    public static final class Star {

        /** Position horizontale, en pixels. */
        private double x;

        /** Position verticale, en pixels. */
        private double y;

        /** {@code true} pour la couche rapide (devant), {@code false} pour la lente. */
        private final boolean fast;

        /**
         * Crée une étoile.
         *
         * @param x position horizontale
         * @param y position verticale
         * @param fast {@code true} pour la couche rapide
         */
        Star(double x, double y, boolean fast) {
            this.x = x;
            this.y = y;
            this.fast = fast;
        }

        /**
         * Indique la position horizontale.
         *
         * @return la position horizontale
         */
        public double getX() {
            return x;
        }

        /**
         * Indique la position verticale.
         *
         * @return la position verticale
         */
        public double getY() {
            return y;
        }

        /**
         * Indique si l'étoile est dans la couche rapide.
         *
         * @return {@code true} pour la couche rapide
         */
        public boolean isFast() {
            return fast;
        }
    }

    /** Largeur du ciel, en pixels. */
    private final int width;

    /** Hauteur du ciel, en pixels. */
    private final int height;

    /** Les étoiles du ciel. */
    private final List<Star> stars = new ArrayList<>();

    /**
     * Crée un ciel étoilé.
     *
     * @param width largeur du ciel, en pixels
     * @param height hauteur du ciel, en pixels
     */
    public Starfield(int width, int height) {
        this(width, height, new Random());
    }

    /**
     * Crée un ciel étoilé avec un tirage fixé (tests).
     *
     * @param width largeur du ciel, en pixels
     * @param height hauteur du ciel, en pixels
     * @param random tirage des positions et des couches
     */
    Starfield(int width, int height, Random random) {
        this.width = width;
        this.height = height;
        for (int i = 0; i < STAR_COUNT; i++) {
            stars.add(new Star(random.nextDouble() * width, random.nextDouble() * height,
                    random.nextBoolean()));
        }
    }

    /**
     * Fait défiler les étoiles vers le bas, avec retour en haut de l'écran.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void update(double dt) {
        for (Star star : stars) {
            star.y += (star.fast ? FAST_SPEED : SLOW_SPEED) * dt;
            star.y %= height;
        }
    }

    /**
     * Dessine les étoiles : petites et grises au loin, plus grosses et blanches devant.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        for (Star star : stars) {
            if (star.fast) {
                gc.setFill(Color.web("#FFFFFF"));
                gc.fillRect(star.x, star.y, 3, 3);
            } else {
                gc.setFill(Color.web("#8A93A6"));
                gc.fillRect(star.x, star.y, 2, 2);
            }
        }
    }

    /**
     * Indique le nombre d'étoiles.
     *
     * @return le nombre d'étoiles
     */
    public int getStarCount() {
        return stars.size();
    }

    /**
     * Indique les étoiles du ciel.
     *
     * @return les étoiles, en lecture seule
     */
    public List<Star> getStars() {
        return Collections.unmodifiableList(stars);
    }
}
