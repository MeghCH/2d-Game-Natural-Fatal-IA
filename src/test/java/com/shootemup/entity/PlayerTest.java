package com.shootemup.entity;

import com.shootemup.core.Game;
import com.shootemup.input.InputController;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private static final double DELTA = 0.001;

    @Test
    void startsWithFullHealthAndNoScore() {
        Player player = new Player(1, 100, 100, InputController.player1());

        assertEquals(30, player.getHp());
        assertEquals(0, player.getScore());
    }

    @Test
    void movesRightWhenRightKeyIsPressed() {
        Player player = new Player(1, 100, 100, InputController.player1());
        player.getControls().keyPressed(KeyCode.D);

        player.update(0.1);

        assertEquals(130, player.getX(), DELTA);
    }

    @Test
    void cannotLeaveLeftEdge() {
        Player player = new Player(1, 0, 100, InputController.player1());
        player.getControls().keyPressed(KeyCode.Q);

        player.update(1);

        assertEquals(0, player.getX(), DELTA);
    }

    @Test
    void cannotLeaveRightEdge() {
        double rightEdge = Game.WIDTH - 40;
        Player player = new Player(1, rightEdge, 100, InputController.player1());
        player.getControls().keyPressed(KeyCode.D);

        player.update(1);

        assertEquals(rightEdge, player.getX(), DELTA);
    }

    @Test
    void deadPlayerDoesNotMove() {
        Player player = new Player(1, 100, 100, InputController.player1());
        player.takeDamage(30);
        player.getControls().keyPressed(KeyCode.D);

        player.update(0.1);

        assertEquals(100, player.getX(), DELTA);
    }

    @Test
    void addScoreIncreasesScore() {
        Player player = new Player(1, 100, 100, InputController.player1());

        player.addScore(50);
        player.addScore(25);

        assertEquals(75, player.getScore());
    }
}
