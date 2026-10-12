package com.shootemup.ui;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class StarfieldTest {

    @Test
    void containsStars() {
        Starfield sky = new Starfield(960, 540, new Random(42));

        assertEquals(120, sky.getStarCount());
        assertEquals(120, sky.getStars().size());
    }

    @Test
    void starsFallDown() {
        Starfield sky = new Starfield(960, 540, new Random(42));
        Starfield.Star star = sky.getStars().get(0);
        double before = star.getY();
        double speed = star.isFast() ? 60 : 20;

        sky.update(1);

        assertEquals((before + speed) % 540, star.getY(), 0.001);
    }

    @Test
    void starsStayOnScreen() {
        Starfield sky = new Starfield(960, 540, new Random(42));

        sky.update(100);

        for (Starfield.Star star : sky.getStars()) {
            assertTrue(star.getY() >= 0);
            assertTrue(star.getY() <= 540);
        }
    }
}
