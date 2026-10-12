package com.shootemup.entity;

import com.shootemup.core.Game;
import com.shootemup.input.InputController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PotionTest {

    private static final double DELTA = 0.001;

    private Player woundedPlayer() {
        Player player = new Player(1, 100, 100, InputController.player1());
        player.takeDamage(20);
        return player;
    }

    @Test
    void fallsDownOverTime() {
        Potion potion = new Potion(100, 0);

        potion.update(1);

        assertEquals(120, potion.getY(), DELTA);
    }

    @Test
    void isOutOfScreenBelowTheBottom() {
        Potion potion = new Potion(100, Game.HEIGHT + 1);

        assertTrue(potion.isOutOfScreen());
    }

    @Test
    void collectHealsTheCharacter() {
        Player player = woundedPlayer();
        Potion potion = new Potion(100, 100);

        potion.collect(player);

        assertEquals(20, player.getHp());
        assertTrue(potion.isCollected());
    }

    @Test
    void cannotHealTwice() {
        Player player = woundedPlayer();
        Potion potion = new Potion(100, 100);

        potion.collect(player);
        potion.collect(player);

        assertEquals(20, player.getHp());
    }

    @Test
    void stopsFallingOnceCollected() {
        Potion potion = new Potion(100, 100);
        potion.collect(woundedPlayer());

        potion.update(0.1);

        assertEquals(100, potion.getY(), DELTA);
    }

    @Test
    void staysDuringItsEffectThenDisappears() {
        Potion potion = new Potion(100, 100);
        potion.collect(woundedPlayer());

        potion.update(0.1);
        assertTrue(potion.isActive());

        potion.update(0.5);
        assertFalse(potion.isActive());
    }
}
