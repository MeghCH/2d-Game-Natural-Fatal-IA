package com.shootemup.level;

import com.shootemup.enemy.EnemyFactory;
import com.shootemup.enemy.EnemyType;

/**
 * Niveau 4, combat final : l'IA maléfique, seule.
 */
public class BossLevel extends Level {

    /**
     * Crée le niveau du boss.
     *
     * @param factory la fabrique qui construit les ennemis des vagues
     */
    public BossLevel(EnemyFactory factory) {
        super(factory);
    }

    /**
     * Décrit les vagues du niveau : un seul boss.
     */
    @Override
    protected void buildWaves() {
        waves.add(new Wave(EnemyType.EVIL_AI, 1));
    }
}
