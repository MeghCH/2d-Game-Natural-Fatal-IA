package com.shootemup.state;

import com.shootemup.core.Game;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VictoryStateTest {

    @Test
    void keysAreIgnoredDuringTheDelay() {
        Game game = new Game();
        game.setState(new VictoryState(game, 100));

        game.keyPressed(KeyCode.ENTER);
        game.keyPressed(KeyCode.ESCAPE);

        assertInstanceOf(VictoryState.class, game.getCurrentState());
    }

    @Test
    void enterRestartsTheGameOnceDelayed() {
        Game game = new Game();
        game.setState(new VictoryState(game, 100));
        game.update(1.2);

        game.keyPressed(KeyCode.ENTER);

        assertInstanceOf(PlayingState.class, game.getCurrentState());
    }

    @Test
    void escapeReturnsToTheMenuOnceDelayed() {
        Game game = new Game();
        game.setState(new VictoryState(game, 100));
        game.update(1.2);

        game.keyPressed(KeyCode.ESCAPE);

        assertInstanceOf(MenuState.class, game.getCurrentState());
    }
}
