package com.shootemup.enemy.boss;

import com.shootemup.enemy.EvilAI;

/**
 * Deuxième phase du boss : balayage horizontal rapide.
 */
public class AngryPhase implements BossPhase {

    /** Vitesse de balayage, en pixels par seconde. */
    private static final double SPEED = 140;

    /**
     * Crée la deuxième phase du boss.
     */
    public AngryPhase() {
    }

    /**
     * Fait balayer le boss rapidement, en restant dans l'écran.
     *
     * @param boss le boss contrôlé
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void execute(EvilAI boss, double dt) {
        sweep(boss, dt, SPEED);
    }

    /**
     * @return la phase suivante, la dernière
     */
    @Override
    public BossPhase next() {
        return new BersherkPhase();
    }
}
