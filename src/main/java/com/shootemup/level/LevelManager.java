package com.shootemup.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Enchaîne les niveaux dans l'ordre : le suivant démarre quand l'actuel est terminé.
 */
public class LevelManager {

    /** Niveaux de la progression, dans l'ordre. */
    private final List<Level> levels;

    /** Position du niveau en cours, en partant de 0. */
    private int currentIndex = 0;

    /**
     * Crée le gestionnaire avec la progression donnée, et prépare le premier niveau.
     *
     * @param levels les niveaux dans l'ordre, au moins 1
     */
    public LevelManager(List<Level> levels) {
        Objects.requireNonNull(levels, "La liste des niveaux ne peut pas être null.");
        if (levels.isEmpty()) {
            throw new IllegalArgumentException("Il faut au moins un niveau.");
        }
        this.levels = new ArrayList<>(levels);
        getCurrentLevel().start();
    }

    /**
     * Met à jour le niveau en cours. Ne fait rien quand la progression est terminée.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    public void update(double dt) {
        if (!isFinished()) {
            getCurrentLevel().update(dt);
        }
    }

    /**
     * Passe au niveau suivant et le prépare.
     *
     * @return {@code true} si un niveau suivant existe, {@code false} si c'était le dernier
     */
    public boolean nextLevel() {
        if (currentIndex + 1 >= levels.size()) {
            return false;
        }
        currentIndex++;
        getCurrentLevel().start();
        return true;
    }

    /**
     * Indique si la progression est terminée.
     *
     * @return {@code true} quand le dernier niveau est terminé
     */
    public boolean isFinished() {
        return currentIndex == levels.size() - 1 && getCurrentLevel().isCompleted();
    }

    /**
     * Indique le niveau en cours.
     *
     * @return le niveau en cours
     */
    public Level getCurrentLevel() {
        return levels.get(currentIndex);
    }

    /**
     * Indique le numéro du niveau en cours, en partant de 1.
     *
     * @return le numéro du niveau en cours
     */
    public int getCurrentLevelNumber() {
        return currentIndex + 1;
    }
}
