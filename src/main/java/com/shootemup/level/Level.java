package com.shootemup.level;

import com.shootemup.core.Game;
import com.shootemup.enemy.Enemy;
import com.shootemup.enemy.EnemyFactory;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

/**
 * Niveau du jeu : le déroulement est commun, seules les vagues changent.
 * <p>
 * Implémente le pattern <b>Template Method</b> : {@link #update(double)} fixe
 * l'ordre des étapes (faire apparaître, mettre à jour, retirer), et chaque
 * niveau ne redéfinit que {@link #buildWaves()}.
 */
public abstract class Level {

    /** Vagues restant à faire apparaître. */
    protected final Queue<Wave> waves = new ArrayDeque<>();

    /** Ennemis actuellement en jeu. */
    protected final List<Enemy> activeEnemies = new ArrayList<>();

    /** Fabrique qui construit les ennemis des vagues. */
    private final EnemyFactory factory;

    /**
     * Crée un niveau vide, à préparer avec {@link #start()}.
     *
     * @param factory la fabrique qui construit les ennemis des vagues
     */
    protected Level(EnemyFactory factory) {
        this.factory = Objects.requireNonNull(factory, "La fabrique ne peut pas être null.");
    }

    /**
     * Prépare le niveau : vide les listes puis construit ses vagues.
     */
    public final void start() {
        waves.clear();
        activeEnemies.clear();
        buildWaves();
    }

    /**
     * Fait apparaître la vague suivante quand l'écran est vide, met à jour
     * les ennemis, puis retire les morts et ceux sortis en bas (esquivés).
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    public final void update(double dt) {
        if (activeEnemies.isEmpty() && !waves.isEmpty()) {
            activeEnemies.addAll(waves.poll().spawn(factory));
        }
        for (Enemy enemy : activeEnemies) {
            enemy.update(dt);
        }
        activeEnemies.removeIf(enemy -> !enemy.isAlive() || enemy.getY() > Game.HEIGHT);
    }

    /**
     * Décrit les vagues du niveau. Appelée par {@link #start()}.
     */
    protected abstract void buildWaves();

    /**
     * Indique la couleur de fond du niveau, reprise des croquis.
     *
     * @return la couleur de fond, en hexadécimal (par exemple "#101820")
     */
    public abstract String getBackgroundColor();

    /**
     * Indique la couleur d'accent du niveau, utilisée par le décor et le HUD.
     * <p>
     * Chaque secteur peut la redéfinir pour garder son ambiance ; la valeur
     * par défaut convient aux niveaux qui n'en définissent pas.
     *
     * @return la couleur d'accent, en hexadécimal
     */
    public String getAccentColor() {
        return "#55F0C8";
    }

    /**
     * Indique le nom du secteur affiché dans le HUD.
     *
     * @return le nom du secteur
     */
    public String getLevelName() {
        return "SECTEUR";
    }

    /**
     * Indique si le niveau est terminé.
     *
     * @return {@code true} quand toutes les vagues sont apparues et qu'il ne reste aucun ennemi
     */
    public boolean isCompleted() {
        return waves.isEmpty() && activeEnemies.isEmpty();
    }

    /**
     * Indique les ennemis actuellement en jeu.
     *
     * @return les ennemis en jeu, en lecture seule
     */
    public List<Enemy> getActiveEnemies() {
        return Collections.unmodifiableList(activeEnemies);
    }
}
