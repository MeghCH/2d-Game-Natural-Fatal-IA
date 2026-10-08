package com.shootemup.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Corbeau du niveau 3 : pique vers le bas et inflige 10 dégâts au contact.
 */
public class Crow extends Enemy {

    /** Taille du corbeau, en pixels. */
    public static final double SIZE = 40;

    /** Points de vie de départ. */
    private static final int MAX_HP = 5;

    /** Points de dégâts infligés au joueur touché. */
    private static final int DAMAGE = 10;

    /** Points de score donnés quand le corbeau est détruit. */
    private static final int SCORE = 300;

    /**
     * Crée un corbeau en pleine santé.
     *
     * @param x position horizontale de départ
     * @param y position verticale de départ
     */
    public Crow(double x, double y) {
        super(x, y, SIZE, SIZE, MAX_HP, DAMAGE, SCORE,
                new com.shootemup.movement.DiveMovement());
    }

    /**
     * Dessine le corbeau sous la forme d'un carré bleu. Un corbeau mort
     * n'est pas dessiné.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        if (!isAlive()) {
            return;
        }
        gc.setFill(Color.web("#7A8BB5"));
        gc.fillRect(x, y, width, height);
    }
}
