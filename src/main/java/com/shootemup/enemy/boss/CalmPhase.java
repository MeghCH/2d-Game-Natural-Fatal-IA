package com.shootemup.enemy.boss;

import com.shootemup.enemy.EvilAI;

/**
 * Première phase du boss : balayage horizontal lent en haut de l'écran.
 */
public class CalmPhase implements BossPhase {

    /** Vitesse de balayage, en pixels par seconde. */
    private static final double SPEED = 60;

    /**
     * Crée la première phase du boss.
     */
    public CalmPhase() {
    }

    /**
     * Fait balayer le boss lentement, en restant dans l'écran.
     *
     * @param boss le boss contrôlé
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void execute(EvilAI boss, double dt) {
        sweep(boss, dt, SPEED);
    }

    /**
     * @return la phase suivante, plus rapide
     */
    @Override
    public BossPhase next() {
        return new AngryPhase();
    }
}
