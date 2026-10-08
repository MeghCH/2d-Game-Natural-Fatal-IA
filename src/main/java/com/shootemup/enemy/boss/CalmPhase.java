package com.shootemup.enemy.boss;

import com.shootemup.core.Game;
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
     * Fait glisser le boss vers la droite, avec retour au bord gauche
     * quand il sort de l'écran.
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
     * @return la phase suivante, plus rapide
     */
    @Override
    public BossPhase next() {
        return new AngryPhase();
    }
}
