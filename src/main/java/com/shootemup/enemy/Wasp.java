package com.shootemup.enemy;

import com.shootemup.asset.Animation;
import com.shootemup.asset.AssetManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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

    /** Battement d'ailes, variante A : ligne 0 de la planche 48x48, 2 images de 24x24. */
    private static final Animation FLY_A = new Animation(24, 24, 0, 2, 8);

    /** Battement d'ailes, variante B : ligne 1 de la planche, 2 images de 24x24. */
    private static final Animation FLY_B = new Animation(24, 24, 1, 2, 8);

    /** Variante visuelle de cette guêpe : 0 (ligne 0) ou 1 (ligne 1). */
    private final int variant;

    /**
     * Crée une guêpe en pleine santé, avec une variante visuelle tirée au hasard.
     *
     * @param x position horizontale de départ
     * @param y position verticale de départ
     */
    public Wasp(double x, double y) {
        this(x, y, (int) (Math.random() * 2));
    }

    /**
     * Crée une guêpe en pleine santé, avec la variante visuelle demandée.
     *
     * @param x position horizontale de départ
     * @param y position verticale de départ
     * @param variant 0 pour la ligne 0 de la planche, 1 pour la ligne 1
     */
    public Wasp(double x, double y, int variant) {
        super(x, y, SIZE, SIZE, MAX_HP, DAMAGE, SCORE,
                new com.shootemup.movement.StraightMovement());
        if (variant != 0 && variant != 1) {
            throw new IllegalArgumentException("La variante doit être 0 ou 1.");
        }
        this.variant = variant;
    }

    /**
     * Indique la variante visuelle de cette guêpe.
     *
     * @return 0 (ligne 0) ou 1 (ligne 1)
     */
    public int getVariant() {
        return variant;
    }

    /**
     * Dessine la guêpe avec son sprite animé. Si la planche est introuvable,
     * dessine un carré jaune à la place. Une guêpe morte n'est pas dessinée.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        if (!isAlive()) {
            return;
        }
        Image sheet = AssetManager.getInstance().getImage("/sprites/wasp.png");
        if (sheet == null) {
            gc.setFill(Color.web("#E8C84A"));
            gc.fillRect(x, y, width, height);
            return;
        }
        (variant == 0 ? FLY_A : FLY_B).draw(gc, sheet, animationTime, x, y, width, height);
    }
}
