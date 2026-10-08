package com.shootemup.movement;

import com.shootemup.entity.Entity;

/**
 * Algorithme de déplacement d'une entité.
 * <p>
 * Le déplacement est sorti de l'ennemi et confié à un objet interchangeable,
 * ce qui permet de combiner librement chaque ennemi avec un déplacement
 * sans multiplier les sous-classes.
 */
public interface MovementStrategy {

    /**
     * Déplace l'entité.
     *
     * @param e l'entité à déplacer
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    void move(Entity e, double dt);
}
