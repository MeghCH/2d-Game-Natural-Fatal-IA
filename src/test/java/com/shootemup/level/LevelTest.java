package com.shootemup.level;

import com.shootemup.enemy.Enemy;
import com.shootemup.enemy.EnemyFactory;
import com.shootemup.enemy.EvilAI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {

    private final EnemyFactory factory = new EnemyFactory();

    @Test
    void firstWaveSpawnsOnFirstUpdate() {
        Level level = new MosquitoLevel(factory);
        level.start();

        level.update(0.1);

        assertEquals(3, level.getActiveEnemies().size());
    }

    @Test
    void nextWaveSpawnsWhenPreviousIsCleared() {
        Level level = new MosquitoLevel(factory);
        level.start();
        level.update(0.1);

        for (Enemy enemy : level.getActiveEnemies()) {
            enemy.takeDamage(1000);
        }
        level.update(0.1);
        level.update(0.1);

        assertEquals(6, level.getActiveEnemies().size());
        assertFalse(level.isCompleted());
    }

    @Test
    void levelIsCompletedWhenAllWavesAreCleared() {
        Level level = new MosquitoLevel(factory);
        level.start();

        for (int i = 0; i < 3; i++) {
            level.update(0.1);
            for (Enemy enemy : level.getActiveEnemies()) {
                enemy.takeDamage(1000);
            }
            level.update(0.1);
        }

        assertTrue(level.isCompleted());
    }

    @Test
    void enemiesLeavingTheBottomAreRemoved() {
        Level level = new MosquitoLevel(factory);
        level.start();

        level.update(10);

        assertTrue(level.getActiveEnemies().isEmpty() || level.getActiveEnemies().get(0).getY() <= 540);
    }

    @Test
    void bossLevelHasASingleBoss() {
        Level level = new BossLevel(factory);
        level.start();

        level.update(0.1);

        assertEquals(1, level.getActiveEnemies().size());
        assertInstanceOf(EvilAI.class, level.getActiveEnemies().get(0));
    }

    @Test
    void startResetsTheLevel() {
        Level level = new MosquitoLevel(factory);
        level.start();
        level.update(0.1);

        level.start();
        level.update(0.1);

        assertEquals(3, level.getActiveEnemies().size());
        assertFalse(level.isCompleted());
    }

    @Test
    void eachLevelHasItsBackgroundColor() {
        assertEquals("#0F2E23", new MosquitoLevel(factory).getBackgroundColor());
        assertEquals("#2A2513", new WaspLevel(factory).getBackgroundColor());
        assertEquals("#16223C", new CrowLevel(factory).getBackgroundColor());
        assertEquals("#221638", new BossLevel(factory).getBackgroundColor());
    }

    @Test
    void eachLevelHasItsAccentColorAndSectorName() {
        assertEquals("#55F08B", new MosquitoLevel(factory).getAccentColor());
        assertEquals("#FFC93C", new WaspLevel(factory).getAccentColor());
        assertEquals("#59B8FF", new CrowLevel(factory).getAccentColor());
        assertEquals("#FF3D9A", new BossLevel(factory).getAccentColor());

        assertEquals("JARDIN", new MosquitoLevel(factory).getLevelName());
        assertEquals("NID DE GUEPES", new WaspLevel(factory).getLevelName());
        assertEquals("CIEL NOIR", new CrowLevel(factory).getLevelName());
        assertEquals("COEUR DE L'IA", new BossLevel(factory).getLevelName());
    }
}
