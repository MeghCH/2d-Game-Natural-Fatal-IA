package com.shootemup.input;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InputControllerTest {

    @Test
    void pressedKeyIsDetected() {
        InputController controls = InputController.player1();

        controls.keyPressed(KeyCode.Z);

        assertTrue(controls.isUp());
    }

    @Test
    void qwertyKeysAlsoWork() {
        InputController controls = InputController.player1();

        controls.keyPressed(KeyCode.W);
        controls.keyPressed(KeyCode.A);

        assertTrue(controls.isUp());
        assertTrue(controls.isLeft());
    }

    @Test
    void releasedKeyIsNoLongerDetected() {
        InputController controls = InputController.player1();

        controls.keyPressed(KeyCode.D);
        controls.keyReleased(KeyCode.D);

        assertFalse(controls.isRight());
    }

    @Test
    void otherPlayerKeysAreIgnored() {
        InputController controls = InputController.player1();

        controls.keyPressed(KeyCode.UP);

        assertFalse(controls.isUp());
    }

    @Test
    void shootKeyIsDetected() {
        InputController controls = InputController.player2();

        controls.keyPressed(KeyCode.ENTER);

        assertTrue(controls.isShooting());
    }

    @Test
    void clearForgetsAllKeys() {
        InputController controls = InputController.player1();
        controls.keyPressed(KeyCode.Z);
        controls.keyPressed(KeyCode.D);

        controls.clear();

        assertFalse(controls.isUp());
        assertFalse(controls.isRight());
    }

    @Test
    void keysKeepTheirOrder() {
        InputController controls = InputController.player1();

        assertEquals(List.of(KeyCode.Z, KeyCode.W), List.copyOf(controls.getKeys(Action.UP)));
    }

    @Test
    void setKeysReplacesTheOldKeys() {
        InputController controls = InputController.player1();

        controls.setKeys(Action.UP, Set.of(KeyCode.K));
        controls.keyPressed(KeyCode.Z);
        assertFalse(controls.isUp());

        controls.keyPressed(KeyCode.K);
        assertTrue(controls.isUp());
    }

    @Test
    void removeKeyTellsWhichActionUsedIt() {
        InputController controls = InputController.player1();

        assertEquals(Action.LEFT, controls.removeKey(KeyCode.Q));
        assertEquals(Set.of(KeyCode.A), controls.getKeys(Action.LEFT));
    }

    @Test
    void removeUnusedKeyReturnsNull() {
        InputController controls = InputController.player1();

        assertNull(controls.removeKey(KeyCode.P));
    }
}
