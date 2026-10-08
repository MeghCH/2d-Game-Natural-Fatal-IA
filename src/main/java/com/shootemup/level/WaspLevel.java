package com.shootemup.level;

import com.shootemup.enemy.EnemyFactory;
import com.shootemup.enemy.EnemyType;

/**
 * Niveau 2 : des guêpes en vagues de plus en plus grandes.
 */
public class WaspLevel extends Level {

    /**
     * Crée le niveau des guêpes.
     *
     * @param factory la fabrique qui construit les ennemis des vagues
     */
    public WaspLevel(EnemyFactory factory) {
        super(factory);
    }

    /**
     * Décrit les vagues du niveau : 3, puis 5, puis 8 guêpes.
     */
    @Override
    protected void buildWaves() {
        waves.add(new Wave(EnemyType.WASP, 3));
        waves.add(new Wave(EnemyType.WASP, 5));
        waves.add(new Wave(EnemyType.WASP, 8));
    }
}
