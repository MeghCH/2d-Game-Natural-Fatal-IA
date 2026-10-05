package com.shootemup.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une entité qui possède des points de vie : joueur, ennemi ou boss.
 * <p>
 * Implémente le pattern <b>Observer</b> : un personnage garde une liste de
 * {@link HealthObserver} et les prévient chaque fois que ses points de vie
 * changent, sans savoir qui ils sont (HUD, sons, effets visuels...).
 */
public abstract class Character extends Entity implements Damageable {

    /** Points de vie actuels, jamais négatifs. */
    protected int hp;

    /** Points de vie maximum, au début de la partie. */
    protected int maxHp;

    /** Observateurs à prévenir quand les points de vie changent. */
    private final List<HealthObserver> observers = new ArrayList<>();

    /**
     * Crée un personnage en pleine santé.
     *
     * @param x      position horizontale du coin supérieur gauche
     * @param y      position verticale du coin supérieur gauche
     * @param width  largeur du personnage
     * @param height hauteur du personnage
     * @param maxHp  points de vie maximum, qui sont aussi les points de vie de départ
     */
    protected Character(double x, double y, double width, double height, int maxHp) {
        super(x, y, width, height);
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    /**
     * Retire des points de vie au personnage, sans descendre en dessous de 0,
     * puis prévient les observateurs.
     *
     * @param amount nombre de points de vie retirés
     */
    @Override
    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
        notifyObservers();
    }

    /**
     * @return {@code true} s'il reste des points de vie au personnage, {@code false} sinon
     */
    @Override
    public boolean isAlive() {
        return hp > 0;
    }

    /**
     * @return les points de vie actuels
     */
    public int getHp() {
        return this.hp;
    }

    /**
     * @return les points de vie maximum
     */
    public int getMaxHp() {
        return this.maxHp;
    }

    /**
     * Abonne un observateur aux changements de points de vie.
     *
     * @param o l'observateur à prévenir
     */
    public void addObserver(HealthObserver o) {
        observers.add(o);
    }

    /**
     * Prévient tous les observateurs que les points de vie ont changé.
     */
    protected void notifyObservers() {
        for (HealthObserver o : observers) {
            o.onHealthChanged(this);
        }
    }
}
