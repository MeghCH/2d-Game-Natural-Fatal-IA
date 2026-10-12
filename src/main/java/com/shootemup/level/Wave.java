package com.shootemup.level;

import com.shootemup.core.Game;
import com.shootemup.enemy.Enemy;
import com.shootemup.enemy.EnemyFactory;
import com.shootemup.enemy.EnemyType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Vague d'ennemis : un type d'ennemi, en un nombre donné.
 */
public class Wave {

    /** Type d'ennemi de la vague. */
    private final EnemyType type;

    /** Nombre d'ennemis de la vague. */
    private final int count;

    /**
     * Crée une vague.
     *
     * @param type le type d'ennemi de la vague
     * @param count le nombre d'ennemis, au moins 1
     */
    public Wave(EnemyType type, int count) {
        this.type = Objects.requireNonNull(type, "Le type ne peut pas être null.");
        this.count = count;
    }

    /**
     * Crée les ennemis de la vague, répartis en haut de l'écran.
     *
     * @param factory la fabrique qui construit les ennemis
     * @return les ennemis créés, jamais {@code null}
     */
    public List<Enemy> spawn(EnemyFactory factory) {
        List<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            // Le boss reste visible en haut : il ne fait que balayer, il ne descend pas.
            double y = (type == EnemyType.EVIL_AI) ? 60 : -40;
            Enemy enemy = factory.create(type, 0, y);
            enemy.setX((i + 1) * Game.WIDTH / (count + 1.0) - enemy.getWidth() / 2);
            enemies.add(enemy);
        }
        return enemies;
    }

    /**
     * Indique le type d'ennemi de la vague.
     *
     * @return le type d'ennemi de la vague
     */
    public EnemyType getType() {
        return type;
    }

    /**
     * Indique le nombre d'ennemis de la vague.
     *
     * @return le nombre d'ennemis de la vague
     */
    public int getCount() {
        return count;
    }
}
