package com.shootemup.level;

import com.shootemup.enemy.EnemyFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TutorialManagerTest {

    @Test
    void progressionHasFourLevelsInOrder() {
        LevelManager manager = TutorialManager.createProgression(new EnemyFactory());

        assertInstanceOf(MosquitoLevel.class, manager.getCurrentLevel());
        assertTrue(manager.nextLevel());
        assertInstanceOf(WaspLevel.class, manager.getCurrentLevel());
        assertTrue(manager.nextLevel());
        assertInstanceOf(CrowLevel.class, manager.getCurrentLevel());
        assertTrue(manager.nextLevel());
        assertInstanceOf(BossLevel.class, manager.getCurrentLevel());
        assertFalse(manager.nextLevel());
    }
}
