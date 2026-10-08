package com.shootemup.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Moustique du niveau 1 : descend en zigzag et inflige 2 dégâts au contact.
 */
public class Mosquito extends Enemy {

    /** Taille du moustique, en pixels. */
    public static final double SIZE = 32;

    /** Points de vie de départ. */
    private static final int MAX_HP = 2;

    /** Points de dégâts infligés au joueur touché. */
    private static final int DAMAGE = 2;

    /** Points de score donnés quand le moustique est détruit. */
    private static final int SCORE = 100;

    /**
     * Crée un moustique en pleine santé.
     *
     * @param x position horizontale de départ
     * @param y position verticale de départ
     */
    public Mosquito(double x, double y) {
        super(x, y, SIZE, SIZE, MAX_HP, DAMAGE, SCORE,
                new com.shootemup.movement.ZigzagMovement());
    }

    /**
     * Dessine le moustique sous la forme d'un carré orange. Un moustique mort
     * n'est pas dessiné.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        if (!isAlive()) {
            return;
        }
        gc.setFill(Color.web("#E8965A"));
        gc.fillRect(x, y, width, height);
    }
}
