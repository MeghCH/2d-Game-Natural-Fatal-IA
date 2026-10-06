package com.shootemup.input;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;

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
}
