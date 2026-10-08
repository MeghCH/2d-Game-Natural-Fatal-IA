package com.shootemup.enemy.boss;

import com.shootemup.enemy.EvilAI;

/**
 * Phase de comportement du boss : chaque phase déplace le boss à sa manière.
 * <p>
 * Le boss change de phase quand ses points de vie passent sous un seuil,
 * sans suite de {@code if} dans sa boucle de jeu.
 */
public interface BossPhase {

    /**
     * Exécute le comportement de la phase.
     *
     * @param boss le boss contrôlé
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    void execute(EvilAI boss, double dt);

    /**
     * Indique la phase suivante.
     *
     * @return la phase suivante, ou cette phase si c'est la dernière
     */
    BossPhase next();

    /**
     * Fait balayer le boss de gauche à droite et retour, sans sortir de l'écran.
     *
     * @param boss le boss contrôlé
     * @param dt temps écoulé depuis la dernière image, en secondes
     * @param speed vitesse de balayage, en pixels par seconde
     */
    default void sweep(EvilAI boss, double dt, double speed) {
        double maxX = com.shootemup.core.Game.WIDTH - boss.getWidth();
        double nextX = boss.getX() + speed * dt * boss.getDirection();
        if (nextX < 0) {
            nextX = 0;
            boss.reverseDirection();
        } else if (nextX > maxX) {
            nextX = maxX;
            boss.reverseDirection();
        }
        boss.setX(nextX);
    }
}
