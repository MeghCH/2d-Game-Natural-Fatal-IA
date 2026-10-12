package com.shootemup.enemy;

import com.shootemup.asset.Animation;
import com.shootemup.asset.AssetManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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

    /** Battement d'ailes, variante A : ligne 0 de la planche 64x64, 2 images de 32x32. */
    private static final Animation FLY_A = new Animation(32, 32, 0, 2, 6);

    /** Battement d'ailes, variante B : ligne 1 de la planche, 2 images de 32x32. */
    private static final Animation FLY_B = new Animation(32, 32, 1, 2, 6);

    /** Variante visuelle de ce corbeau : 0 (ligne 0) ou 1 (ligne 1). */
    private final int variant;

    /**
     * Crée un corbeau en pleine santé, avec une variante visuelle tirée au hasard.
     *
     * @param x position horizontale de départ
     * @param y position verticale de départ
     */
    public Crow(double x, double y) {
        this(x, y, (int) (Math.random() * 2));
    }

    /**
     * Crée un corbeau en pleine santé, avec la variante visuelle demandée.
     *
     * @param x position horizontale de départ
     * @param y position verticale de départ
     * @param variant 0 pour la ligne 0 de la planche, 1 pour la ligne 1
     */
    public Crow(double x, double y, int variant) {
        super(x, y, SIZE, SIZE, MAX_HP, DAMAGE, SCORE,
                new com.shootemup.movement.DiveMovement());
        if (variant != 0 && variant != 1) {
            throw new IllegalArgumentException("La variante doit être 0 ou 1.");
        }
        this.variant = variant;
    }

    /**
     * Indique la variante visuelle de ce corbeau.
     *
     * @return 0 (ligne 0) ou 1 (ligne 1)
     */
    public int getVariant() {
        return variant;
    }

    /**
     * Dessine le corbeau avec son sprite animé. Si la planche est introuvable,
     * dessine un carré bleu à la place. Un corbeau mort n'est pas dessiné.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        if (!isAlive()) {
            return;
        }
        Image sheet = AssetManager.getInstance().getImage("/sprites/crow.png");
        if (sheet == null) {
            gc.setFill(Color.web("#7A8BB5"));
            gc.fillRect(x, y, width, height);
            return;
        }
        (variant == 0 ? FLY_A : FLY_B).draw(gc, sheet, animationTime, x, y, width, height);
    }
}
