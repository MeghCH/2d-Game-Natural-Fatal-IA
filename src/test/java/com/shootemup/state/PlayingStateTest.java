package com.shootemup.state;

import com.shootemup.core.Game;
import com.shootemup.entity.Player;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayingStateTest {

    @Test
    void playersStartAtTheBottomOnEachSide() {
        PlayingState state = new PlayingState(new Game());

        Player first = state.getPlayers().get(0);
        Player second = state.getPlayers().get(1);

        assertTrue(first.getX() < Game.WIDTH / 2.0);
        assertTrue(second.getX() > Game.WIDTH / 2.0);
        assertEquals(Game.HEIGHT - 60, first.getY());
        assertEquals(Game.HEIGHT - 60, second.getY());
    }

    @Test
    void pressingShootKeyCreatesAProjectile() {
        PlayingState state = new PlayingState(new Game());

        state.keyPressed(KeyCode.SPACE);
        state.update(0.016);

        assertEquals(1, state.getProjectiles().size());
    }

    @Test
    void projectilesLeavingTheScreenAreRemoved() {
        PlayingState state = new PlayingState(new Game());
        state.keyPressed(KeyCode.SPACE);
        state.update(0.016);
        state.keyReleased(KeyCode.SPACE);

        state.update(2);

        assertTrue(state.getProjectiles().isEmpty());
    }
}
