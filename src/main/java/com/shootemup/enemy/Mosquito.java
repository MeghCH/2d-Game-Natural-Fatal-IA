package com.shootemup.enemy;

import com.shootemup.asset.Animation;
import com.shootemup.asset.AssetManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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

    /** Battement d'ailes : ligne 0 de la planche 48x48, 2 images de 24x24. */
    private static final Animation FLY = new Animation(24, 24, 0, 2, 10);

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
     * Dessine le moustique avec son sprite animé. Si la planche est introuvable,
     * dessine un carré orange à la place. Un moustique mort n'est pas dessiné.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        if (!isAlive()) {
            return;
        }
        Image sheet = AssetManager.getInstance().getImage("/sprites/mosquito.png");
        if (sheet == null) {
            gc.setFill(Color.web("#E8965A"));
            gc.fillRect(x, y, width, height);
            return;
        }
        FLY.draw(gc, sheet, animationTime, x, y, width, height);
    }
}
