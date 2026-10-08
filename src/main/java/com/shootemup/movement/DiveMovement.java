package com.shootemup.movement;

import com.shootemup.entity.Entity;

/**
 * Déplacement en piqué vers le bas : descente rapide en ligne droite.
 * <p>
 * Sans état propre : une même instance peut être partagée par plusieurs ennemis.
 */
public class DiveMovement implements MovementStrategy {

    /** Vitesse de descente, en pixels par seconde. */
    private static final double SPEED = 250;

    /**
     * Crée un déplacement en piqué.
     */
    public DiveMovement() {
    }

    /**
     * Fait piquer l'entité vers le bas.
     *
     * @param e l'entité à déplacer
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void move(Entity e, double dt) {
        e.setY(e.getY() + SPEED * dt);
    }
}
