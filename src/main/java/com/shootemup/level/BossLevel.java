package com.shootemup.level;

import com.shootemup.enemy.EnemyFactory;
import com.shootemup.enemy.EnemyType;

/**
 * Niveau 4, combat final : l'IA maléfique, seule.
 */
public class BossLevel extends Level {

    /**
     * Crée le niveau du boss.
     *
     * @param factory la fabrique qui construit les ennemis des vagues
     */
    public BossLevel(EnemyFactory factory) {
        super(factory);
    }

    /**
     * Décrit les vagues du niveau : un seul boss.
     */
    @Override
    protected void buildWaves() {
        waves.add(new Wave(EnemyType.EVIL_AI, 1));
    }

    /**
     * Indique la couleur de fond du niveau : violet sombre de l'IA.
     *
     * @return la couleur de fond, en hexadécimal
     */
    @Override
    public String getBackgroundColor() {
        return "#221638";
    }

    /**
     * Indique la couleur d'accent du niveau : magenta du cœur de l'IA.
     *
     * @return la couleur d'accent, en hexadécimal
     */
    @Override
    public String getAccentColor() {
        return "#FF3D9A";
    }

    /**
     * Indique le nom du secteur : le cœur de l'IA.
     *
     * @return le nom du secteur
     */
    @Override
    public String getLevelName() {
        return "COEUR DE L'IA";
    }
}
