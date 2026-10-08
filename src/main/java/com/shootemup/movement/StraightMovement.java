package com.shootemup.movement;

import com.shootemup.entity.Entity;

/**
 * Déplacement en ligne droite vers le bas.
 * <p>
 * Sans état propre : une même instance peut être partagée par plusieurs ennemis.
 */
public class StraightMovement implements MovementStrategy {

    /** Vitesse de descente, en pixels par seconde. */
    private static final double SPEED = 100;

    /**
     * Crée un déplacement en ligne droite.
     */
    public StraightMovement() {
    }

    /**
     * Fait descendre l'entité en ligne droite.
     *
     * @param e l'entité à déplacer
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void move(Entity e, double dt) {
        e.setY(e.getY() + SPEED * dt);
    }
}
