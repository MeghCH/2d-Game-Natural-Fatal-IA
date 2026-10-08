package com.shootemup.level;

import com.shootemup.enemy.EnemyFactory;
import com.shootemup.enemy.EnemyType;

/**
 * Niveau 3 : des corbeaux en vagues de plus en plus grandes.
 */
public class CrowLevel extends Level {

    /**
     * Crée le niveau des corbeaux.
     *
     * @param factory la fabrique qui construit les ennemis des vagues
     */
    public CrowLevel(EnemyFactory factory) {
        super(factory);
    }

    /**
     * Décrit les vagues du niveau : 3, puis 5, puis 8 corbeaux.
     */
    @Override
    protected void buildWaves() {
        waves.add(new Wave(EnemyType.CROW, 3));
        waves.add(new Wave(EnemyType.CROW, 5));
        waves.add(new Wave(EnemyType.CROW, 8));
    }
}
