package com.shootemup.bonus;

import com.shootemup.core.Game;
import java.util.Random;

/**
 * Fait tomber des bonus à des moments aléatoires, toutes les 8 à 15 secondes.
 */
public class BonusSpawner {

    /** Délai minimum entre deux bonus, en secondes. */
    private static final double MIN_DELAY = 8;

    /** Délai maximum entre deux bonus, en secondes. */
    private static final double MAX_DELAY = 15;

    /** Tirage des délais et des positions. */
    private final Random random = new Random();

    /** Temps restant avant le prochain bonus, en secondes. */
    private double timer;

    /**
     * Crée le planificateur, avec un premier délai tiré au hasard.
     */
    public BonusSpawner() {
        timer = nextDelay();
    }

    /**
     * Fait passer le temps avant le prochain bonus.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    public void update(double dt) {
        timer -= dt;
    }

    /**
     * Indique si un bonus peut tomber.
     *
     * @return {@code true} quand le délai est écoulé
     */
    public boolean isReady() {
        return timer <= 0;
    }

    /**
     * Fait tomber un bonus à une position horizontale au hasard, et relance le délai.
     *
     * @return le bonus créé, en haut de l'écran
     */
    public Bonus spawn() {
        timer = nextDelay();
        double x = random.nextDouble() * (Game.WIDTH - Bonus.SIZE);
        return new Bonus(x, -Bonus.SIZE);
    }

    /**
     * Tire un délai au hasard entre les bornes.
     *
     * @return le délai tiré, en secondes
     */
    private double nextDelay() {
        return MIN_DELAY + random.nextDouble() * (MAX_DELAY - MIN_DELAY);
    }
}
