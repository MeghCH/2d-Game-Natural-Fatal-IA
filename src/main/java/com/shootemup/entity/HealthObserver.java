package com.shootemup.entity;

/**
 * Objet à prévenir quand les points de vie d'un personnage changent.
 * <p>
 * Implémente le pattern <b>Observer</b> : un {@link Character} garde une liste
 * d'observateurs et les prévient à chaque perte de points de vie, sans savoir
 * qui ils sont. Le PlayerStatusPanel, par exemple, l'implémente pour mettre à jour la barre
 * de vie sans que la logique du jeu dépende de l'affichage.
 */
public interface HealthObserver {

    /**
     * Appelée chaque fois que les points de vie d'un personnage changent.
     *
     * @param c le personnage dont les points de vie ont changé
     */
    void onHealthChanged(Character c);
}