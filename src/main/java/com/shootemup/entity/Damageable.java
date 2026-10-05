package com.shootemup.entity;

/**
 * Représente un élément du jeu qui possède des points de vie et peut être détruit.
 * <p>
 * Implémentée par les personnages (joueur, ennemis, boss), elle permet au
 * gestionnaire de collisions d'appliquer des dégâts sans connaître le type exact
 * de l'élément touché.
 */
public interface Damageable {
    /**
     * Retire des points de vie à l'élément.
     *
     * @param amount nombre de points de vie retirés, par exemple 2 pour un moustique
     */
    void takeDamage(int amount);
    /**
     * Indique si l'élément est encore en vie.
     *
     * @return {@code true} s'il reste des points de vie à l'élément, {@code false} sinon
     */
    boolean isAlive();
}