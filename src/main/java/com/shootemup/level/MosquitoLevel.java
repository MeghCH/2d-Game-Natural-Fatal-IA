package com.shootemup.level;

import com.shootemup.enemy.EnemyFactory;
import com.shootemup.enemy.EnemyType;

/**
 * Niveau 1, tutoriel : des moustiques en vagues de plus en plus grandes.
 */
public class MosquitoLevel extends Level {

    /**
     * Crée le niveau des moustiques.
     *
     * @param factory la fabrique qui construit les ennemis des vagues
     */
    public MosquitoLevel(EnemyFactory factory) {
        super(factory);
    }

    /**
     * Décrit les vagues du niveau : 3, puis 6, puis 10 moustiques.
     */
    @Override
    protected void buildWaves() {
        waves.add(new Wave(EnemyType.MOSQUITO, 3));
        waves.add(new Wave(EnemyType.MOSQUITO, 6));
        waves.add(new Wave(EnemyType.MOSQUITO, 10));
    }

    /**
     * Indique la couleur de fond du niveau : vert sombre du jardin.
     *
     * @return la couleur de fond, en hexadécimal
     */
    @Override
    public String getBackgroundColor() {
        return "#0F2E23";
    }

    /**
     * Indique la couleur d'accent du niveau : vert lumineux du jardin.
     *
     * @return la couleur d'accent, en hexadécimal
     */
    @Override
    public String getAccentColor() {
        return "#55F08B";
    }

    /**
     * Indique le nom du secteur : le jardin.
     *
     * @return le nom du secteur
     */
    @Override
    public String getLevelName() {
        return "JARDIN";
    }
}
