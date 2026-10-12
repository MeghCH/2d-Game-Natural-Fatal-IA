package com.shootemup.entity;

import com.shootemup.core.Game;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PotionSpawnerTest {

    private static final double DELTA = 0.001;

    @Test
    void noPotionBeforeTheMinimumDelay() {
        PotionSpawner spawner = new PotionSpawner(new Random(42));

        assertTrue(spawner.update(5.9).isEmpty());
    }

    @Test
    void onePotionAfterTheMaximumDelay() {
        PotionSpawner spawner = new PotionSpawner(new Random(42));

        assertEquals(1, spawner.update(12.1).size());
    }

    @Test
    void potionStartsAboveTheScreenAndInsideItsWidth() {
        PotionSpawner spawner = new PotionSpawner(new Random(42));

        Potion potion = spawner.update(12.1).get(0);

        assertEquals(-Potion.SIZE, potion.getY(), DELTA);
        assertTrue(potion.getX() >= 0);
        assertTrue(potion.getX() <= Game.WIDTH - Potion.SIZE);
    }

    @Test
    void sameSeedGivesTheSameResult() {
        PotionSpawner first = new PotionSpawner(new Random(42));
        PotionSpawner second = new PotionSpawner(new Random(42));

        List<Potion> a = first.update(12.1);
        List<Potion> b = second.update(12.1);

        assertEquals(a.get(0).getX(), b.get(0).getX(), DELTA);
    }
}
