package com.shootemup.bonus;

import com.shootemup.entity.Player;
import com.shootemup.input.InputController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BonusTest {

    private static final double DELTA = 0.001;

    @Test
    void fallsDownSlowly() {
        Bonus bonus = new Bonus(100, 100);

        bonus.update(1);

        assertEquals(160, bonus.getY(), DELTA);
    }

    @Test
    void collectHealsThePlayer() {
        Bonus bonus = new Bonus(100, 100);
        Player player = new Player(1, 100, 100, InputController.player1());
        player.takeDamage(10);

        bonus.collect(player);

        assertEquals(24, player.getHp());
    }

    @Test
    void collectNeverExceedsMaxHealth() {
        Bonus bonus = new Bonus(100, 100);
        Player player = new Player(1, 100, 100, InputController.player1());
        player.takeDamage(2);

        bonus.collect(player);

        assertEquals(30, player.getHp());
    }

    @Test
    void spawnerIsNotReadyAtFirstThenBecomesReady() {
        BonusSpawner spawner = new BonusSpawner();

        assertFalse(spawner.isReady());

        spawner.update(20);

        assertTrue(spawner.isReady());
    }

    @Test
    void spawnRestartsTheDelayAndStaysOnScreen() {
        BonusSpawner spawner = new BonusSpawner();
        spawner.update(20);

        Bonus bonus = spawner.spawn();

        assertFalse(spawner.isReady());
        assertTrue(bonus.getX() >= 0);
        assertTrue(bonus.getX() + Bonus.SIZE <= 960);
    }
}
