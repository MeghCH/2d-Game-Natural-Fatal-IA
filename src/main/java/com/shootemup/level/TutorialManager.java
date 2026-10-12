package com.shootemup.level;

import com.shootemup.enemy.EnemyFactory;
import java.util.List;
import java.util.Objects;

/**
 * Assemble la progression complète du jeu : tutoriel, puis boss final.
 */
public final class TutorialManager {

    /** Non instanciable : que des méthodes statiques. */
    private TutorialManager() {
    }

    /**
     * Crée la progression : moustiques, guêpes, corbeaux, puis IA maléfique.
     *
     * @param factory la fabrique qui construit les ennemis des niveaux
     * @return le gestionnaire prêt à jouer, sur le premier niveau
     */
    public static LevelManager createProgression(EnemyFactory factory) {
        Objects.requireNonNull(factory, "La fabrique ne peut pas être null.");
        return new LevelManager(List.of(
                new MosquitoLevel(factory),
                new WaspLevel(factory),
                new CrowLevel(factory),
                new BossLevel(factory)));
    }
}
