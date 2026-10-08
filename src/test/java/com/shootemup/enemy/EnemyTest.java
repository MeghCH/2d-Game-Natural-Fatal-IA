package com.shootemup.enemy;

import com.shootemup.entity.Player;
import com.shootemup.input.InputController;
import com.shootemup.movement.StraightMovement;
import com.shootemup.movement.ZigzagMovement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnemyTest {

    private static final double DELTA = 0.001;

    @Test
    void attackRemovesContactDamageFromPlayer() {
        Mosquito mosquito = new Mosquito(100, 100);
        Player player = new Player(1, 100, 100, InputController.player1());

        mosquito.attack(player);

        assertEquals(28, player.getHp());
    }

    @Test
    void deadEnemyDoesNotMove() {
        Mosquito mosquito = new Mosquito(100, 100);
        mosquito.takeDamage(10);

        mosquito.update(1);

        assertEquals(100, mosquito.getX(), DELTA);
        assertEquals(100, mosquito.getY(), DELTA);
    }

    @Test
    void setMovementChangesBehaviour() {
        Mosquito mosquito = new Mosquito(100, 100);

        mosquito.setMovement(new StraightMovement());

        assertInstanceOf(StraightMovement.class, mosquito.getMovement());
    }

    @Test
    void enemiesHaveTheirScoreValues() {
        assertEquals(100, new Mosquito(0, 0).getScoreValue());
        assertEquals(200, new Wasp(0, 0).getScoreValue());
        assertEquals(300, new Crow(0, 0).getScoreValue());
    }

    @Test
    void zigzagChangesHorizontalPosition() {
        Mosquito mosquito = new Mosquito(100, 100);
        mosquito.setMovement(new ZigzagMovement());

        mosquito.update(1);

        assertNotEquals(100, mosquito.getX(), DELTA);
    }
}
