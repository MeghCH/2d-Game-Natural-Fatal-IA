package com.shootemup.entity;

import com.shootemup.asset.Animation;
import com.shootemup.asset.AssetManager;
import com.shootemup.core.Game;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Potion de soin qui tombe du haut de l'écran.
 * <p>
 * Quand un joueur la touche, elle lui rend des points de vie, puis joue un court
 * effet de ramassage avant de disparaître. Si personne ne la ramasse, elle
 * disparaît en sortant par le bas de l'écran.
 */
public class Potion extends Entity {

    /** Taille de la potion à l'écran, en pixels : 16 pixels de sprite agrandis par 2. */
    public static final double SIZE = 32;

    /** Vitesse de chute, en pixels par seconde. */
    private static final double FALL_SPEED = 120;

    /** Points de vie rendus au joueur qui ramasse la potion. */
    private static final int HEAL_AMOUNT = 10;

    /** Animation de chute : ligne 0, 2 images, 4 images par seconde, en boucle. */
    private static final Animation FALLING = new Animation(16, 16, 0, 2, 4);

    /** Effet de ramassage : ligne 1, 2 images, 6 images par seconde, joué une seule fois. */
    private static final Animation COLLECTED = new Animation(16, 16, 1, 0, 2, 6, false);

    /** {@code true} tant que la potion est en jeu, y compris pendant son effet de ramassage. */
    private boolean active = true;

    /** {@code true} une fois que la potion a été ramassée. */
    private boolean collected = false;

    /** Temps écoulé depuis l'apparition de la potion, pour l'animation de chute. */
    private double animationTime = 0;

    /** Temps écoulé depuis le ramassage, pour l'effet de ramassage. */
    private double collectTime = 0;

    /**
     * Crée une potion à la position donnée.
     *
     * @param x position horizontale de départ
     * @param y position verticale de départ
     */
    public Potion(double x, double y) {
        super(x, y, SIZE, SIZE);
    }

    /**
     * Fait tomber la potion. Une fois ramassée, elle ne tombe plus : elle joue
     * son effet de ramassage, puis se retire du jeu quand l'effet est terminé.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void update(double dt) {
        if (collected) {
            collectTime += dt;
            if (COLLECTED.isFinished(collectTime)) {
                active = false;
            }
            return;
        }

        y += FALL_SPEED * dt;
        animationTime += dt;
    }

    /**
     * Dessine la potion qui tombe, ou son effet de ramassage.
     * Si la planche est introuvable, dessine un carré rose à la place.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        Image sheet = AssetManager.getInstance().getImage("/sprites/potion.png");

        if (sheet == null) {
            if (!collected) {
                gc.setFill(Color.web("#ED93B1"));
                gc.fillRect(x, y, width, height);
            }
            return;
        }

        if (collected) {
            COLLECTED.draw(gc, sheet, collectTime, x, y, width, height);
        } else {
            FALLING.draw(gc, sheet, animationTime, x, y, width, height);
        }
    }

    /**
     * @return {@code true} si la potion est sortie par le bas de l'écran
     */
    public boolean isOutOfScreen() {
        return y > Game.HEIGHT;
    }

    /**
     * Rend des points de vie au personnage qui ramasse la potion.
     * Une potion déjà ramassée ne soigne plus.
     *
     * @param c le personnage qui ramasse la potion
     */
    public void collect(Character c) {
        if (collected) {
            return;
        }
        c.heal(HEAL_AMOUNT);
        collected = true;
    }

    /**
     * @return {@code true} si la potion a déjà été ramassée
     */
    public boolean isCollected() {
        return collected;
    }

    /**
     * @return {@code true} tant que la potion est en jeu
     */
    public boolean isActive() {
        return active;
    }
}