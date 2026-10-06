package com.shootemup.ui;

import com.shootemup.entity.Player;
import com.shootemup.input.InputController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerStatusPanelTest {

    @Test
    void trackedPlayerDoesNotFlashInitially() {
        PlayerStatusPanel panel = new PlayerStatusPanel();
        Player player = new Player(1, 100, 100, InputController.player1());
        panel.track(player);

        assertFalse(panel.isFlashing(player));
    }

    @Test
    void damagedTrackedPlayerFlashes() {
        PlayerStatusPanel panel = new PlayerStatusPanel();
        Player player = new Player(1, 100, 100, InputController.player1());
        panel.track(player);

        player.takeDamage(2);

        assertTrue(panel.isFlashing(player));
    }

    @Test
    void flashIsStillActiveBeforeItsEnd() {
        PlayerStatusPanel panel = new PlayerStatusPanel();
        Player player = new Player(1, 100, 100, InputController.player1());
        panel.track(player);
        player.takeDamage(2);

        panel.update(0.1);

        assertTrue(panel.isFlashing(player));
    }

    @Test
    void flashStopsAfterItsDuration() {
        PlayerStatusPanel panel = new PlayerStatusPanel();
        Player player = new Player(1, 100, 100, InputController.player1());
        panel.track(player);
        player.takeDamage(2);

        panel.update(0.31);

        assertFalse(panel.isFlashing(player));
    }

    @Test
    void untrackedPlayerDoesNotFlash() {
        PlayerStatusPanel panel = new PlayerStatusPanel();
        Player player = new Player(1, 100, 100, InputController.player1());

        player.takeDamage(2);

        assertFalse(panel.isFlashing(player));
    }
}
