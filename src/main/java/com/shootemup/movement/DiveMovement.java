package com.shootemup.movement;

import com.shootemup.enemy.Enemy;
import com.shootemup.entity.Entity;

/**
 * Déplacement en piqué vers le bas, en deux temps : descente lente
 * puis plongeon rapide, comme un corbeau qui fond sur les joueurs.
 * <p>
 * La phase est déduite du temps d'animation de l'ennemi, donc sans état
 * propre : une même instance peut être partagée par plusieurs ennemis.
 */
public class DiveMovement implements MovementStrategy {

    /** Descente d'approche avant le piqué, en pixels par seconde. */
    private static final double SLOW_SPEED = 70;

    /** Vitesse du piqué, en pixels par seconde. */
    private static final double DIVE_SPEED = 425;

    /** Durée de la descente lente avant le piqué, en secondes. */
    private static final double DIVE_DELAY = 0.8;

    /**
     * Crée un déplacement en piqué.
     */
    public DiveMovement() {
    }

    /**
     * Fait descendre l'entité lentement, puis piquer d'un coup vers le bas.
     *
     * @param e l'entité à déplacer
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void move(Entity e, double dt) {
        double speed = DIVE_SPEED;
        if (e instanceof Enemy enemy && enemy.getAnimationTime() < DIVE_DELAY) {
            speed = SLOW_SPEED;
        }
        e.setY(e.getY() + speed * dt);
    }
}
