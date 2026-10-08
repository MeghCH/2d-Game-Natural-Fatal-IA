package com.shootemup.enemy.boss;

import com.shootemup.core.Game;
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
     * Fait glisser le boss vers la droite, plus vite que la phase calme,
     * avec retour au bord gauche quand il sort de l'écran.
     *
     * @param boss le boss contrôlé
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void execute(EvilAI boss, double dt) {
        double nextX = boss.getX() + SPEED * dt;
        if (nextX > Game.WIDTH - boss.getWidth()) {
            nextX = 0;
        }
        boss.setX(nextX);
    }

    /**
     * @return la phase suivante, la dernière
     */
    @Override
    public BossPhase next() {
        return new BersherkPhase();
    }
}
