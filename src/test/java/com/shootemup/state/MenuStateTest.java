package com.shootemup.state;

import com.shootemup.core.Game;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuStateTest {

    @Test
    void enterStartsTheGameAfterTheDelay() {
        Game game = new Game();
        game.setState(new MenuState(game));

        game.keyPressed(KeyCode.ENTER);

        assertInstanceOf(MenuState.class, game.getCurrentState());
    }

    @Test
    void enterStartsTheGameOnceDelayed() {
        Game game = new Game();
        game.setState(new MenuState(game));
        game.update(1.2);

        game.keyPressed(KeyCode.ENTER);

        assertInstanceOf(PlayingState.class, game.getCurrentState());
    }

    @Test
    void otherKeysDoNothing() {
        Game game = new Game();
        game.setState(new MenuState(game));
        game.update(1.2);

        game.keyPressed(KeyCode.SPACE);

        assertInstanceOf(MenuState.class, game.getCurrentState());
    }

    @Test
    void tOpensTheControlsScreenOnceDelayed() {
        Game game = new Game();
        game.setState(new MenuState(game));
        game.update(1.2);

        game.keyPressed(KeyCode.T);

        assertInstanceOf(ControlsState.class, game.getCurrentState());
    }
}
