package com.shootemup.entity;

import java.util.List;

/**
 * Arme d'un personnage, qui crée des balles en respectant une cadence de tir.
 * <p>
 * Après chaque tir, l'arme doit attendre {@code FIRE_DELAY} secondes avant de
 * pouvoir tirer à nouveau. Sans cette attente, garder la touche de tir enfoncée
 * créerait une balle à chaque image. La méthode {@link #fire(Character)} renvoie
 * une liste pour permettre plus tard des tirs de plusieurs balles à la fois.
 */
public class Weapon implements Updatable {

    /** Temps minimum entre deux tirs, en secondes. */
    private static final double FIRE_DELAY = 0.2;

    /** Vitesse des balles, en pixels par seconde. */
    private static final double BULLET_SPEED = 600;

    /** Points de dégâts infligés par chaque balle. */
    private static final int DAMAGE = 1;

    /** Temps restant avant de pouvoir tirer à nouveau, en secondes. */
    private double cooldown = 0;

    /**
     * Fait passer le temps de recharge.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void update(double dt) {
        cooldown = Math.max(0, cooldown - dt);
    }

    /**
     * @return {@code true} si l'arme est prête à tirer
     */
    public boolean canFire() {
        return cooldown <= 0;
    }

    /**
     * Tire une balle vers le haut depuis le centre du tireur, si l'arme est prête.
     *
     * @param shooter le personnage qui tire
     * @return la liste des balles tirées, vide si l'arme est encore en recharge
     */
    public List<Projectile> fire(Character shooter) {
        if (!canFire()) {
            return List.of();
        }
        else {cooldown = FIRE_DELAY;}

        double x = shooter.getX() + shooter.getWidth() / 2 - Projectile.SIZE / 2;
        double y = shooter.getY() - Projectile.SIZE;
        Projectile projectile = new Projectile(x, y, 0, -BULLET_SPEED, DAMAGE, shooter);
        return List.of(projectile);
    }
}