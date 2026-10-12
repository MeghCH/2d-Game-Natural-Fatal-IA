package com.shootemup.asset;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimationTest {

    @Test
    void startsOnTheFirstFrame() {
        Animation animation = new Animation(24, 24, 0, 2, 8);

        assertEquals(0, animation.frameIndex(0));
    }

    @Test
    void movesToTheNextFrameOverTime() {
        Animation animation = new Animation(24, 24, 0, 2, 8);

        assertEquals(1, animation.frameIndex(0.15));
    }

    @Test
    void loopingAnimationGoesBackToTheFirstFrame() {
        Animation animation = new Animation(24, 24, 0, 2, 8);

        assertEquals(0, animation.frameIndex(0.25));
    }

    @Test
    void oneShotAnimationStaysOnItsLastFrame() {
        Animation animation = new Animation(8, 8, 0, 2, 2, 12, false);

        assertEquals(1, animation.frameIndex(1));
    }

    @Test
    void loopingAnimationNeverFinishes() {
        Animation animation = new Animation(24, 24, 0, 2, 8);

        assertFalse(animation.isFinished(100));
    }

    @Test
    void oneShotAnimationIsNotFinishedBeforeItsLastFrame() {
        Animation animation = new Animation(8, 8, 0, 2, 2, 12, false);

        assertFalse(animation.isFinished(0.1));
    }

    @Test
    void oneShotAnimationFinishesAfterAllItsFrames() {
        Animation animation = new Animation(8, 8, 0, 2, 2, 12, false);

        assertTrue(animation.isFinished(0.2));
    }
}
