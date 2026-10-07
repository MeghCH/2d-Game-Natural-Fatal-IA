package com.shootemup.entity;

import com.shootemup.input.InputController;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeaponTest {

    private static final double DELTA = 0.001;

    private Player shooter() {
        return new Player(1, 100, 300, InputController.player1());
    }

    @Test
    void isReadyAtStart() {
        Weapon weapon = new Weapon();

        assertTrue(weapon.canFire());
    }

    @Test
    void firesOneProjectileGoingUp() {
        Weapon weapon = new Weapon();
        Player shooter = shooter();

        List<Projectile> shots = weapon.fire(shooter);

        assertEquals(1, shots.size());
        Projectile projectile = shots.get(0);
        assertSame(shooter, projectile.getOwner());

        double startY = projectile.getY();
        projectile.update(0.1);
        assertTrue(projectile.getY() < startY);
    }

    @Test
    void projectileStartsCenteredAboveShooter() {
        Weapon weapon = new Weapon();

        Projectile projectile = weapon.fire(shooter()).get(0);

        assertEquals(100 + 40 / 2.0 - Projectile.SIZE / 2, projectile.getX(), DELTA);
        assertEquals(300 - Projectile.SIZE, projectile.getY(), DELTA);
    }

    @Test
    void cannotFireTwiceImmediately() {
        Weapon weapon = new Weapon();
        Player shooter = shooter();
        weapon.fire(shooter);

        List<Projectile> secondShot = weapon.fire(shooter);

        assertTrue(secondShot.isEmpty());
    }

    @Test
    void isStillReloadingBeforeTheDelay() {
        Weapon weapon = new Weapon();
        weapon.fire(shooter());

        weapon.update(0.1);

        assertFalse(weapon.canFire());
    }

    @Test
    void canFireAgainAfterTheDelay() {
        Weapon weapon = new Weapon();
        Player shooter = shooter();
        weapon.fire(shooter);

        weapon.update(0.2);

        assertEquals(1, weapon.fire(shooter).size());
    }
}
