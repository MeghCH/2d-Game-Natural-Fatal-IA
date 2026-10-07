package com.shootemup.entity;

import com.shootemup.core.Game;
import com.shootemup.input.InputController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectileTest {

    private static final double DELTA = 0.001;

    @Test
    void movesUpwardsWithNegativeVerticalSpeed() {
        Projectile projectile = new Projectile(100, 100, 0, -600, 1, null);

        projectile.update(0.1);

        assertEquals(100, projectile.getX(), DELTA);
        assertEquals(40, projectile.getY(), DELTA);
    }

    @Test
    void movesDiagonallyWithBothSpeeds() {
        Projectile projectile = new Projectile(100, 100, 100, -100, 1, null);

        projectile.update(1);

        assertEquals(200, projectile.getX(), DELTA);
        assertEquals(0, projectile.getY(), DELTA);
    }

    @Test
    void isNotOutOfScreenWhenInside() {
        Projectile projectile = new Projectile(100, 100, 0, -600, 1, null);

        assertFalse(projectile.isOutOfScreen());
    }

    @Test
    void isOutOfScreenWhenAboveTheTop() {
        Projectile projectile = new Projectile(100, -20, 0, -600, 1, null);

        assertTrue(projectile.isOutOfScreen());
    }

    @Test
    void isOutOfScreenWhenBelowTheBottom() {
        Projectile projectile = new Projectile(100, Game.HEIGHT + 1, 0, 600, 1, null);

        assertTrue(projectile.isOutOfScreen());
    }

    @Test
    void destroyRemovesProjectileFromPlay() {
        Projectile projectile = new Projectile(100, 100, 0, -600, 1, null);

        projectile.destroy();

        assertFalse(projectile.isActive());
    }

    @Test
    void keepsItsDamageAndOwner() {
        Player owner = new Player(1, 100, 100, InputController.player1());
        Projectile projectile = new Projectile(100, 100, 0, -600, 3, owner);

        assertTrue(projectile.isActive());
        assertEquals(3, projectile.getDamage());
        assertSame(owner, projectile.getOwner());
    }
}
