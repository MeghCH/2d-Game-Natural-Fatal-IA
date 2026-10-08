package com.shootemup.movement;

import com.shootemup.entity.Entity;

/**
 * Déplacement en zigzag vers le bas, comme le vol d'un moustique.
 * <p>
 * Sans état propre : le zigzag est calculé depuis la position actuelle,
 * une même instance peut être partagée par plusieurs ennemis.
 */
public class ZigzagMovement implements MovementStrategy {

    /** Vitesse de descente, en pixels par seconde. */
    private static final double SPEED = 100;

    /** Largeur du zigzag, en pixels par seconde. */
    private static final double AMPLITUDE = 80;

    /** Fréquence du zigzag. */
    private static final double FREQUENCY = 0.05;

    /**
     * Crée un déplacement en zigzag.
     */
    public ZigzagMovement() {
    }

    /**
     * Fait descendre l'entité en zigzag.
     *
     * @param e l'entité à déplacer
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void move(Entity e, double dt) {
        e.setY(e.getY() + SPEED * dt);
        e.setX(e.getX() + Math.cos(e.getY() * FREQUENCY) * AMPLITUDE * dt);
    }
}
