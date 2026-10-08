package com.shootemup.enemy;

import com.shootemup.core.Game;
import com.shootemup.enemy.boss.AngryPhase;
import com.shootemup.enemy.boss.BersherkPhase;
import com.shootemup.enemy.boss.CalmPhase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvilAITest {

    private static final double DELTA = 0.001;

    @Test
    void startsInCalmPhase() {
        EvilAI boss = new EvilAI(100, 0);

        assertInstanceOf(CalmPhase.class, boss.getPhase());
    }

    @Test
    void switchesToAngryBelowTwoThirds() {
        EvilAI boss = new EvilAI(100, 0);
        boss.takeDamage(40);

        boss.update(0.1);

        assertInstanceOf(AngryPhase.class, boss.getPhase());
    }

    @Test
    void switchesToBersherkBelowOneThird() {
        EvilAI boss = new EvilAI(100, 0);
        boss.takeDamage(40);
        boss.update(0.1);
        boss.takeDamage(40);

        boss.update(0.1);

        assertInstanceOf(BersherkPhase.class, boss.getPhase());
    }

    @Test
    void deadBossDoesNotMove() {
        EvilAI boss = new EvilAI(100, 50);
        boss.takeDamage(200);

        boss.update(1);

        assertEquals(50, boss.getY(), DELTA);
    }

    @Test
    void bossStaysOnScreenInEveryPhase() {
        EvilAI boss = new EvilAI(100, 60);
        boss.takeDamage(80);

        for (int i = 0; i < 600; i++) {
            boss.update(0.1);
        }

        assertTrue(boss.getX() >= 0);
        assertTrue(boss.getX() + EvilAI.SIZE <= Game.WIDTH);
        assertTrue(boss.getY() >= 0);
        assertTrue(boss.getY() + EvilAI.SIZE <= Game.HEIGHT);
    }
}
