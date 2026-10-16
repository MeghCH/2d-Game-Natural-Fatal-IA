package com.shootemup.enemy.boss;

import com.shootemup.enemy.EvilAI;

/**
 * Dernière phase du boss : balayage rapide avec va-et-vient vertical,
 * pour ne jamais rester immobile sous les tirs des joueurs.
 * <p>
 * Le nom du fichier reprend l'arborescence existante.
 */
public class BersherkPhase implements BossPhase {

    /** Vitesse de balayage horizontal, en pixels par seconde. */
    private static final double SPEED_X = 220;

    /** Vitesse du va-et-vient vertical, en pixels par seconde. */
    private static final double SPEED_Y = 130;

    /** Hauteur minimale du boss, en pixels. */
    private static final double MIN_Y = 60;

    /** Hauteur maximale du boss, en pixels. */
    private static final double MAX_Y = com.shootemup.core.Game.HEIGHT / 2.0;

    /** Sens vertical : 1 vers le bas, -1 vers le haut. */
    private int yDirection = 1;

    /**
     * Crée la dernière phase du boss.
     */
    public BersherkPhase() {
    }

    /**
     * Fait balayer le boss vite de gauche à droite tout en montant et
     * descendant, sans sortir de l'écran ni rester immobile.
     *
     * @param boss le boss contrôlé
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void execute(EvilAI boss, double dt) {
        sweep(boss, dt, SPEED_X);
        double nextY = boss.getY() + SPEED_Y * dt * yDirection;
        if (nextY <= MIN_Y) {
            nextY = MIN_Y;
            yDirection = 1;
        } else if (nextY >= MAX_Y) {
            nextY = MAX_Y;
            yDirection = -1;
        }
        boss.setY(nextY);
    }

    /**
     * @return cette phase, car c'est la dernière
     */
    @Override
    public BossPhase next() {
        return this;
    }
}
