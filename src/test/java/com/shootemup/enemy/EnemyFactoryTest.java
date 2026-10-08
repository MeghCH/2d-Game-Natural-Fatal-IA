package com.shootemup.enemy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnemyFactoryTest {

    private final EnemyFactory factory = new EnemyFactory();

    @Test
    void createsMosquitoWithCorrectDamage() {
        Enemy enemy = factory.create(EnemyType.MOSQUITO, 100, 0);

        assertInstanceOf(Mosquito.class, enemy);
        assertEquals(2, enemy.getContactDamage());
        assertTrue(enemy.isAlive());
    }

    @Test
    void createsWaspWithCorrectDamage() {
        Enemy enemy = factory.create(EnemyType.WASP, 100, 0);

        assertInstanceOf(Wasp.class, enemy);
        assertEquals(6, enemy.getContactDamage());
    }

    @Test
    void createsCrowWithCorrectDamage() {
        Enemy enemy = factory.create(EnemyType.CROW, 100, 0);

        assertInstanceOf(Crow.class, enemy);
        assertEquals(10, enemy.getContactDamage());
    }

    @Test
    void createsBoss() {
        Enemy enemy = factory.create(EnemyType.EVIL_AI, 100, 0);

        assertInstanceOf(EvilAI.class, enemy);
    }
}
