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
    void otherKeysDoNotLeavePause() {
        Game game = new Game();
        PlayingState playing = new PlayingState(game);
        game.setState(new PauseState(game, playing));

        game.keyPressed(KeyCode.UP);

        assertInstanceOf(PauseState.class, game.getCurrentState());
    }
}