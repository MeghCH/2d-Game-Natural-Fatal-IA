package com.shootemup.level;

import com.shootemup.core.Game;
import com.shootemup.enemy.Enemy;
import com.shootemup.enemy.EnemyFactory;
import com.shootemup.enemy.EnemyType;
import com.shootemup.enemy.EvilAI;
import com.shootemup.enemy.Mosquito;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WaveTest {

    private final EnemyFactory factory = new EnemyFactory();

    @Test
    void spawnCreatesTheRightNumberOfEnemies() {
        Wave wave = new Wave(EnemyType.MOSQUITO, 5);

        List<Enemy> enemies = wave.spawn(factory);

        assertEquals(5, enemies.size());
        for (Enemy enemy : enemies) {
            assertInstanceOf(Mosquito.class, enemy);
        }
    }

    @Test
    void spawnPlacesEnemiesAtTheTopOfTheScreen() {
        Wave wave = new Wave(EnemyType.MOSQUITO, 3);

        List<Enemy> enemies = wave.spawn(factory);

        for (Enemy enemy : enemies) {
            assertEquals(-40, enemy.getY());
            assertTrue(enemy.getX() >= 0);
            assertTrue(enemy.getX() + enemy.getWidth() <= Game.WIDTH);
        }
    }

    @Test
    void spawnPlacesTheBossVisibleBelowTheTop() {
        Wave wave = new Wave(EnemyType.EVIL_AI, 1);

        List<Enemy> enemies = wave.spawn(factory);

        assertEquals(1, enemies.size());
        assertInstanceOf(EvilAI.class, enemies.get(0));
        assertEquals(60, enemies.get(0).getY());
    }

    @Test
    void keepsItsTypeAndCount() {
        Wave wave = new Wave(EnemyType.WASP, 4);

        assertEquals(EnemyType.WASP, wave.getType());
        assertEquals(4, wave.getCount());
    }
}
