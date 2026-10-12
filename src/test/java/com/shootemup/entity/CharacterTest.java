package com.shootemup.entity;

import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {

    private static class TestCharacter extends Character {

        TestCharacter(int maxHp) {
            super(0, 0, 50, 50, maxHp);
        }

        @Override
        public void update(double dt) {
        }

        @Override
        public void render(GraphicsContext gc) {
        }
    }

    private static class CountingObserver implements HealthObserver {

        int calls = 0;
        Character lastNotified;

        @Override
        public void onHealthChanged(Character c) {
            calls++;
            lastNotified = c;
        }
    }

    @Test
    void startsWithFullHealth() {
        Character character = new TestCharacter(10);

        assertEquals(10, character.getHp());
        assertEquals(10, character.getMaxHp());
    }

    @Test
    void takeDamageReducesHealth() {
        Character character = new TestCharacter(10);

        character.takeDamage(2);

        assertEquals(8, character.getHp());
    }

    @Test
    void healthNeverGoesBelowZero() {
        Character character = new TestCharacter(10);

        character.takeDamage(15);

        assertEquals(0, character.getHp());
    }

    @Test
    void isAliveWhileHealthRemains() {
        Character character = new TestCharacter(10);

        character.takeDamage(6);

        assertTrue(character.isAlive());
    }

    @Test
    void isDeadWhenHealthReachesZero() {
        Character character = new TestCharacter(10);

        character.takeDamage(10);

        assertFalse(character.isAlive());
    }

    @Test
    void observerIsNotifiedOnDamage() {
        Character character = new TestCharacter(10);
        CountingObserver observer = new CountingObserver();
        character.addObserver(observer);

        character.takeDamage(2);

        assertEquals(1, observer.calls);
        assertSame(character, observer.lastNotified);
    }

    @Test
    void allObserversAreNotified() {
        Character character = new TestCharacter(10);
        CountingObserver first = new CountingObserver();
        CountingObserver second = new CountingObserver();
        character.addObserver(first);
        character.addObserver(second);

        character.takeDamage(2);

        assertEquals(1, first.calls);
        assertEquals(1, second.calls);
    }

        @Test
    void healRestoresHealth() {
        Character character = new TestCharacter(30);
        character.takeDamage(20);

        character.heal(5);

        assertEquals(15, character.getHp());
    }

    @Test
    void healNeverExceedsMaxHealth() {
        Character character = new TestCharacter(30);
        character.takeDamage(2);

        character.heal(10);

        assertEquals(30, character.getHp());
    }
}