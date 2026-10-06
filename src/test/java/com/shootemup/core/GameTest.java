package com.shootemup.core;

import com.shootemup.state.GameState;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private static class RecordingState implements GameState {

        int enterCalls = 0;
        int updateCalls = 0;
        double lastDt = -1;
        KeyCode lastPressed;
        KeyCode lastReleased;

        @Override
        public void enter() {
            enterCalls++;
        }

        @Override
        public void update(double dt) {
            updateCalls++;
            lastDt = dt;
        }

        @Override
        public void render(GraphicsContext gc) {
        }

        @Override
        public void keyPressed(KeyCode code) {
            lastPressed = code;
        }

        @Override
        public void keyReleased(KeyCode code) {
            lastReleased = code;
        }
    }

    @Test
    void setStateCallsEnter() {
        Game game = new Game();
        RecordingState state = new RecordingState();

        game.setState(state);

        assertEquals(1, state.enterCalls);
        assertSame(state, game.getCurrentState());
    }

    @Test
    void updateIsDelegatedToCurrentState() {
        Game game = new Game();
        RecordingState state = new RecordingState();
        game.setState(state);

        game.update(0.016);

        assertEquals(1, state.updateCalls);
        assertEquals(0.016, state.lastDt);
    }

    @Test
    void keysAreDelegatedToCurrentState() {
        Game game = new Game();
        RecordingState state = new RecordingState();
        game.setState(state);

        game.keyPressed(KeyCode.UP);
        game.keyReleased(KeyCode.DOWN);

        assertEquals(KeyCode.UP, state.lastPressed);
        assertEquals(KeyCode.DOWN, state.lastReleased);
    }

    @Test
    void onlyNewStateReceivesCallsAfterChange() {
        Game game = new Game();
        RecordingState first = new RecordingState();
        RecordingState second = new RecordingState();
        game.setState(first);

        game.setState(second);
        game.update(0.016);

        assertEquals(0, first.updateCalls);
        assertEquals(1, second.updateCalls);
    }

    @Test
    void updateWithoutStateThrows() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, () -> game.update(0.016));
    }

    @Test
    void setStateRejectsNull() {
        Game game = new Game();

        assertThrows(NullPointerException.class, () -> game.setState(null));
    }
}