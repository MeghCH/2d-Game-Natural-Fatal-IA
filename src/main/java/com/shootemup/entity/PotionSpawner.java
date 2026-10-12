package com.shootemup.entity;

import com.shootemup.core.Game;

import java.util.List;
import java.util.Random;

/**
 * Fait tomber des potions de soin à intervalles et à des positions aléatoires.
 * <p>
 * Le générateur reçoit son objet {@link Random} dans son constructeur au lieu de
 * le créer lui-même (injection de dépendance). Dans le jeu, on lui donne un
 * {@code Random} ordinaire ; dans les tests, un {@code Random} avec une graine fixe,
 * qui tire toujours la même suite de nombres. Le hasard devient ainsi
 * reproductible, et donc testable.
 */
public class PotionSpawner {

    /** Attente minimum entre deux potions, en secondes. */
    private static final double MIN_DELAY = 6;

    /** Attente maximum entre deux potions, en secondes. */
    private static final double MAX_DELAY = 12;

    /** Source de hasard pour les attentes et les positions. */
    private final Random random;

    /** Temps restant avant la prochaine potion, en secondes. */
    private double timer;

    /**
     * Crée un générateur qui utilise la source de hasard donnée.
     *
     * @param random la source de hasard, par exemple {@code new Random(42)} dans un test
     */
    public PotionSpawner(Random random) {
        this.random = random;
        timer = nextDelay();
    }

    /**
     * Crée un générateur avec une source de hasard ordinaire, pour le jeu.
     */
    public PotionSpawner() {
        this(new Random());
    }

    /**
     * Fait passer le temps, et fait apparaître une potion quand l'attente est écoulée.
     * La potion apparaît juste au-dessus de l'écran, pour entrer en douceur en tombant.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     * @return la potion apparue, ou une liste vide s'il n'est pas encore temps
     */
    public List<Potion> update(double dt) {
        timer -= dt;
        if (timer > 0) {
            return List.of();
        }

        timer = nextDelay();
        double x = random.nextDouble() * (Game.WIDTH - Potion.SIZE);
        double y = -Potion.SIZE;
        return List.of(new Potion(x, y));
    }

    /**
     * @return une attente au hasard entre {@code MIN_DELAY} et {@code MAX_DELAY} secondes
     */
    private double nextDelay() {
        return MIN_DELAY + random.nextDouble() * (MAX_DELAY - MIN_DELAY);
    }
}