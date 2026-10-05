package com.shootemup.entity;

/**
* Représente un élément du jeu qui évolue au fil du temps.
* <p>
* Toute entité qui implémente cette interface (joueur, ennemi, projectile)
* est mise à jour à chaque image par la boucle de jeu.
*/
public interface Updatable {
    /**
    * Met à jour l'état de l'élément (position, minuteries, comportement).
    *
    * @param dt temps écoulé depuis la dernière image, en secondes
    */
    void update(double dt);
}