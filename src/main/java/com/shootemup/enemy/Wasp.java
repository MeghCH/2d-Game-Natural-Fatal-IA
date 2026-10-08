package com.shootemup.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Guêpe du niveau 2 : descend en ligne droite et inflige 6 dégâts au contact.
 */
public class Wasp extends Enemy {

    /** Taille de la guêpe, en pixels. */
    public static final double SIZE = 32;

    /** Points de vie de départ. */
    private static final int MAX_HP = 3;

    /** Points de dégâts infligés au joueur touché. */
    private static final int DAMAGE = 6;

    /** Points de score donnés quand la guêpe est détruite. */
    private static final int SCORE = 200;

    /**
     * Crée une guêpe en pleine santé.
     *
     * @param x position horizontale de départ
     * @param y position verticale de départ
     */
    public Wasp(double x, double y) {
        super(x, y, SIZE, SIZE, MAX_HP, DAMAGE, SCORE,
                new com.shootemup.movement.StraightMovement());
    }

    /**
     * Dessine la guêpe sous la forme d'un carré jaune. Une guêpe morte
     * n'est pas dessinée.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        if (!isAlive()) {
            return;
        }
        gc.setFill(Color.web("#E8C84A"));
        gc.fillRect(x, y, width, height);
    }
}
