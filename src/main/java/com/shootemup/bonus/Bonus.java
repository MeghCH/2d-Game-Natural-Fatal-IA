package com.shootemup.bonus;

import com.shootemup.asset.Animation;
import com.shootemup.asset.AssetManager;
import com.shootemup.entity.Entity;
import com.shootemup.entity.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Bonus de soin : tombe lentement du haut de l'écran, et rend des points
 * de vie au joueur qui l'attrape en se plaçant dessous.
 */
public class Bonus extends Entity {

    /** Taille du bonus, en pixels. */
    public static final double SIZE = 24;

    /** Points de vie rendus au joueur qui attrape le bonus. */
    public static final int HEAL = 4;

    /** Vitesse de chute, en pixels par seconde : assez lente pour l'attraper. */
    private static final double FALL_SPEED = 60;

    /** Scintillement : ligne 0 de la planche 32x32, 2 images de 16x16. */
    private static final Animation SPARKLE = new Animation(16, 16, 0, 2, 4);

    /** Temps écoulé, qui permet à l'animation de choisir son image. */
    private double animationTime = 0;

    /**
     * Crée un bonus.
     *
     * @param x position horizontale de départ
     * @param y position verticale de départ
     */
    public Bonus(double x, double y) {
        super(x, y, SIZE, SIZE);
    }

    /**
     * Fait tomber le bonus en ligne droite.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void update(double dt) {
        y += FALL_SPEED * dt;
        animationTime += dt;
    }

    /**
     * Donne ses points de vie au joueur qui l'attrape.
     *
     * @param player le joueur qui attrape le bonus
     */
    public void collect(Player player) {
        player.heal(HEAL);
    }

    /**
     * Dessine le bonus avec son sprite animé. Si la planche est introuvable,
     * dessine un carré vert à la place. Un bonus sorti de l'écran n'est plus
     * dessiné par la partie de toute façon.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        Image sheet = AssetManager.getInstance().getImage("/sprites/potion.png");
        if (sheet == null) {
            gc.setFill(Color.web("#4AE88A"));
            gc.fillRect(x, y, width, height);
            return;
        }
        SPARKLE.draw(gc, sheet, animationTime, x, y, width, height);
    }
}
