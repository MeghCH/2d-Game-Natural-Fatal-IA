package com.shootemup.state;

import com.shootemup.core.Game;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StateTransitionTest {

    @Test
    void escapeInPlayingStatePausesTheGame() {
        Game game = new Game();
        PlayingState playing = new PlayingState(game);
        game.setState(playing);

        game.keyPressed(KeyCode.ESCAPE);

        assertInstanceOf(PauseState.class, game.getCurrentState());
    }

    @Test
    void escapeInPauseResumesThePreviousState() {
        Game game = new Game();
        PlayingState playing = new PlayingState(game);
        game.setState(playing);

        game.keyPressed(KeyCode.ESCAPE);
        game.keyPressed(KeyCode.ESCAPE);

        assertSame(playing, game.getCurrentState());
    }

    @Test
    void tInPauseOpensTheControlsScreen() {
        Game game = new Game();
        PlayingState playing = new PlayingState(game);
        game.setState(playing);

        game.keyPressed(KeyCode.ESCAPE);
        game.keyPressed(KeyCode.T);

        assertInstanceOf(ControlsState.class, game.getCurrentState());
    }

    @Test
    void keysChangedDuringPauseAreKeptWhenResuming() {
        Game game = new Game();
        PlayingState playing = new PlayingState(game);
        game.setState(playing);

        game.keyPressed(KeyCode.ESCAPE);
        game.keyPressed(KeyCode.T);
        game.keyPressed(KeyCode.UP);
        game.keyPressed(KeyCode.ENTER);
        game.keyPressed(KeyCode.K);
        game.keyPressed(KeyCode.ESCAPE);
        assertInstanceOf(PauseState.class, game.getCurrentState());
        game.keyPressed(KeyCode.ESCAPE);
        assertSame(playing, game.getCurrentState());

        game.keyPressed(KeyCode.SPACE);
        game.update(0.016);
        assertTrue(playing.getProjectiles().isEmpty());

        game.keyPressed(KeyCode.K);
        game.update(0.016);
        assertEquals(1, playing.getProjectiles().size());
    }

    @Test
    void otherKeysDoNotLeavePause() {
        Game game = new Game();
        PlayingState playing = new PlayingState(game);
        game.setState(new PauseState(game, playing));

        game.keyPressed(KeyCode.UP);

        assertInstanceOf(PauseState.class, game.getCurrentState());
    }
}