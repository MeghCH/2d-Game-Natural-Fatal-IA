package com.shootemup.level;

import com.shootemup.enemy.EnemyFactory;
import com.shootemup.enemy.EnemyType;

/**
 * Niveau 1, tutoriel : des moustiques en vagues de plus en plus grandes.
 */
public class MosquitoLevel extends Level {

    /**
     * Crée le niveau des moustiques.
     *
     * @param factory la fabrique qui construit les ennemis des vagues
     */
    public MosquitoLevel(EnemyFactory factory) {
        super(factory);
    }

    /**
     * Décrit les vagues du niveau : 3, puis 6, puis 10 moustiques.
     */
    @Override
    protected void buildWaves() {
        waves.add(new Wave(EnemyType.MOSQUITO, 3));
        waves.add(new Wave(EnemyType.MOSQUITO, 6));
        waves.add(new Wave(EnemyType.MOSQUITO, 10));
    }
}
