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
}
