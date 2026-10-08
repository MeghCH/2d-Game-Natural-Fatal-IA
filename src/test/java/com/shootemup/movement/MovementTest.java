package com.shootemup.movement;

import com.shootemup.enemy.Mosquito;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovementTest {

    private static final double DELTA = 0.001;

    @Test
    void straightMovesDownOnly() {
        Mosquito mosquito = new Mosquito(100, 100);
        mosquito.setMovement(new StraightMovement());

        mosquito.update(1);

        assertEquals(100, mosquito.getX(), DELTA);
        assertEquals(200, mosquito.getY(), DELTA);
    }

    @Test
    void diveMovesDownFasterThanStraight() {
        Mosquito slow = new Mosquito(100, 100);
        slow.setMovement(new StraightMovement());
        Mosquito fast = new Mosquito(100, 100);
        fast.setMovement(new DiveMovement());

        slow.update(1);
        fast.update(1);

        assertTrue(fast.getY() > slow.getY());
    }

    @Test
    void zigzagMovesDownAndSideways() {
        Mosquito mosquito = new Mosquito(100, 100);
        mosquito.setMovement(new ZigzagMovement());

        mosquito.update(1);

        assertTrue(mosquito.getY() > 100);
        assertNotEquals(100, mosquito.getX(), DELTA);
    }
}
