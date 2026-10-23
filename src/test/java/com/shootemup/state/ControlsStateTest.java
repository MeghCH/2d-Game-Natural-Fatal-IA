package com.shootemup.state;

import com.shootemup.core.Game;
import com.shootemup.input.Action;
import com.shootemup.input.InputController;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ControlsStateTest {

    private static Game gameOnControlsScreen() {
        Game game = new Game();
        game.setState(new ControlsState(game, new MenuState(game)));
        return game;
    }

    private static InputController player(Game game, int index) {
        return game.getPlayerControls().get(index);
    }

    @Test
    void enterThenAKeyChangesTheSelectedAction() {
        Game game = gameOnControlsScreen();

        game.keyPressed(KeyCode.ENTER);
        game.keyPressed(KeyCode.K);

        assertEquals(Set.of(KeyCode.K), player(game, 0).getKeys(Action.UP));
    }

    @Test
    void arrowsChooseThePlayerAndTheAction() {
        Game game = gameOnControlsScreen();

        game.keyPressed(KeyCode.DOWN);
        game.keyPressed(KeyCode.RIGHT);
        game.keyPressed(KeyCode.ENTER);
        game.keyPressed(KeyCode.K);

        assertEquals(Set.of(KeyCode.K), player(game, 1).getKeys(Action.DOWN));
        assertEquals(Set.of(KeyCode.S), player(game, 0).getKeys(Action.DOWN));
    }

    @Test
    void upFromTheFirstLineWrapsToTheLastOne() {
        Game game = gameOnControlsScreen();

        game.keyPressed(KeyCode.UP);
        game.keyPressed(KeyCode.ENTER);
        game.keyPressed(KeyCode.K);

        assertEquals(Set.of(KeyCode.K), player(game, 0).getKeys(Action.SHOOT));
    }

    @Test
    void escapeCancelsTheChange() {
        Game game = gameOnControlsScreen();

        game.keyPressed(KeyCode.ENTER);
        game.keyPressed(KeyCode.ESCAPE);

        assertEquals(Set.of(KeyCode.Z, KeyCode.W), player(game, 0).getKeys(Action.UP));
        assertInstanceOf(ControlsState.class, game.getCurrentState());
    }

    @Test
    void escapeGoesBackToThePreviousScreen() {
        Game game = new Game();
        MenuState menu = new MenuState(game);
        game.setState(new ControlsState(game, menu));

        game.keyPressed(KeyCode.ESCAPE);

        assertSame(menu, game.getCurrentState());
    }

    @Test
    void keyOfAnotherActionIsSwapped() {
        Game game = gameOnControlsScreen();

        game.keyPressed(KeyCode.ENTER);
        game.keyPressed(KeyCode.S);

        assertEquals(Set.of(KeyCode.S), player(game, 0).getKeys(Action.UP));
        assertEquals(Set.of(KeyCode.Z, KeyCode.W), player(game, 0).getKeys(Action.DOWN));
    }

    @Test
    void keyOfTheOtherPlayerIsSwapped() {
        Game game = gameOnControlsScreen();

        game.keyPressed(KeyCode.ENTER);
        game.keyPressed(KeyCode.UP);

        assertEquals(Set.of(KeyCode.UP), player(game, 0).getKeys(Action.UP));
        assertEquals(Set.of(KeyCode.Z, KeyCode.W), player(game, 1).getKeys(Action.UP));
    }

    @Test
    void keySharedWithAnotherKeyIsOnlyRemoved() {
        Game game = gameOnControlsScreen();

        game.keyPressed(KeyCode.ENTER);
        game.keyPressed(KeyCode.A);

        assertEquals(Set.of(KeyCode.A), player(game, 0).getKeys(Action.UP));
        assertEquals(Set.of(KeyCode.Q), player(game, 0).getKeys(Action.LEFT));
    }

    @Test
    void newKeysAreUsedInTheNextGame() {
        Game game = gameOnControlsScreen();
        game.keyPressed(KeyCode.UP);
        game.keyPressed(KeyCode.ENTER);
        game.keyPressed(KeyCode.K);

        PlayingState playing = new PlayingState(game);
        game.setState(playing);
        game.keyPressed(KeyCode.SPACE);
        game.update(0.016);
        assertTrue(playing.getProjectiles().isEmpty());

        game.keyPressed(KeyCode.K);
        game.update(0.016);
        assertEquals(1, playing.getProjectiles().size());
    }

    @Test
    void escapeCannotBeChosenAsANewKey() {
        Game game = gameOnControlsScreen();

        game.keyPressed(KeyCode.ENTER);
        game.keyPressed(KeyCode.ESCAPE);

        assertEquals(Set.of(KeyCode.Z, KeyCode.W), player(game, 0).getKeys(Action.UP));
        assertInstanceOf(ControlsState.class, game.getCurrentState());
    }

    @Test
    void keysAreDescribedInOrder() {
        assertEquals("Z / W", ControlsState.describe(InputController.player1().getKeys(Action.UP)));
        assertEquals("ESPACE", ControlsState.describe(Set.of(KeyCode.SPACE)));
        assertEquals("---", ControlsState.describe(Set.of()));
    }
}
