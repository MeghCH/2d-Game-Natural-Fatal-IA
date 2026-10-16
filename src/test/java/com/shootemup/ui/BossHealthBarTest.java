package com.shootemup.ui;

import com.shootemup.core.Game;
import com.shootemup.enemy.EvilAI;
import com.shootemup.state.PlayingState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BossHealthBarTest {

    @Test
    void noBossTrackedInitially() {
        BossHealthBar bar = new BossHealthBar();

        assertNull(bar.getBoss());
    }

    @Test
    void setBossTracksTheBoss() {
        BossHealthBar bar = new BossHealthBar();
        EvilAI boss = new EvilAI(100, 60);

        bar.setBoss(boss);

        assertSame(boss, bar.getBoss());
    }

    @Test
    void setNullBossHidesTheBar() {
        BossHealthBar bar = new BossHealthBar();
        bar.setBoss(new EvilAI(100, 60));

        bar.setBoss(null);

        assertNull(bar.getBoss());
    }

    @Test
    void playingStateHasABossBarWithoutBossOnFirstLevel() {
        PlayingState state = new PlayingState(new Game());

        state.update(0.016);

        assertNotNull(state.getBossHealthBar());
        assertNull(state.getBossHealthBar().getBoss());
    }
}
