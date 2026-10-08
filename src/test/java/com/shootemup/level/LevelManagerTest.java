package com.shootemup.level;

import com.shootemup.enemy.Enemy;
import com.shootemup.enemy.EnemyFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevelManagerTest {

    private final EnemyFactory factory = new EnemyFactory();

    private LevelManager twoLevels() {
        return new LevelManager(List.of(new MosquitoLevel(factory), new BossLevel(factory)));
    }

    private void finishCurrentLevel(LevelManager manager) {
        Level level = manager.getCurrentLevel();
        while (!level.isCompleted()) {
            for (Enemy enemy : level.getActiveEnemies()) {
                enemy.takeDamage(1000);
            }
            level.update(0.1);
        }
    }

    @Test
    void startsOnTheFirstLevel() {
        LevelManager manager = twoLevels();

        assertInstanceOf(MosquitoLevel.class, manager.getCurrentLevel());
        assertEquals(1, manager.getCurrentLevelNumber());
        assertFalse(manager.isFinished());
    }

    @Test
    void nextLevelAdvancesAndPreparesTheLevel() {
        LevelManager manager = twoLevels();

        assertTrue(manager.nextLevel());

        assertInstanceOf(BossLevel.class, manager.getCurrentLevel());
        assertEquals(2, manager.getCurrentLevelNumber());
    }

    @Test
    void nextLevelReturnsFalseOnTheLastLevel() {
        LevelManager manager = twoLevels();
        manager.nextLevel();

        assertFalse(manager.nextLevel());
    }

    @Test
    void isFinishedWhenTheLastLevelIsCompleted() {
        LevelManager manager = twoLevels();
        manager.nextLevel();
        finishCurrentLevel(manager);

        assertTrue(manager.isFinished());
    }

    @Test
    void updateDoesNothingWhenFinished() {
        LevelManager manager = twoLevels();
        manager.nextLevel();
        finishCurrentLevel(manager);

        manager.update(1);

        assertTrue(manager.isFinished());
    }

    @Test
    void rejectsNullAndEmptyLists() {
        assertThrows(NullPointerException.class, () -> new LevelManager(null));
        assertThrows(IllegalArgumentException.class, () -> new LevelManager(List.of()));
    }
}
