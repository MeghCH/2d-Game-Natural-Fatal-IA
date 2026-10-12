package com.shootemup.state;

import com.shootemup.core.Game;
import com.shootemup.entity.Player;
import com.shootemup.entity.Potion;
import com.shootemup.entity.PotionSpawner;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayingStateTest {

    /** Fait tomber une seule potion dès la première image, au lieu d'attendre 6 à 12 s. */
    private static class OnePotionSpawner extends PotionSpawner {

        private boolean spawned = false;

        @Override
        public List<Potion> update(double dt) {
            if (spawned) {
                return List.of();
            }
            spawned = true;
            return List.of(new Potion(0, -Potion.SIZE));
        }
    }

    private static void placeOn(Potion potion, Player player) {
        potion.setX(player.getX());
        potion.setY(player.getY());
    }

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

    @Test
    void playerTouchingAPotionIsHealed() {
        PlayingState state = new PlayingState(new Game(), new OnePotionSpawner());
        Player player = state.getPlayers().get(0);
        player.takeDamage(15);
        state.update(0.016);

        placeOn(state.getPotions().get(0), player);
        state.update(0.016);

        assertEquals(25, player.getHp());
    }

    @Test
    void collectedPotionIsRemovedAfterItsEffect() {
        PlayingState state = new PlayingState(new Game(), new OnePotionSpawner());
        Player player = state.getPlayers().get(0);
        state.update(0.016);
        Potion potion = state.getPotions().get(0);

        placeOn(potion, player);
        state.update(0.016);
        assertTrue(potion.isCollected());

        state.update(0.5);

        assertTrue(state.getPotions().isEmpty());
    }

    @Test
    void potionsLeavingTheScreenAreRemoved() {
        PlayingState state = new PlayingState(new Game(), new OnePotionSpawner());
        state.update(0.016);
        assertEquals(1, state.getPotions().size());

        state.update(5);

        assertTrue(state.getPotions().isEmpty());
    }
}
