package com.shootemup.level;

import com.shootemup.enemy.EnemyFactory;
import com.shootemup.enemy.EnemyType;

/**
 * Niveau 3 : des corbeaux en vagues de plus en plus grandes.
 */
public class CrowLevel extends Level {

    /**
     * Crée le niveau des corbeaux.
     *
     * @param factory la fabrique qui construit les ennemis des vagues
     */
    public CrowLevel(EnemyFactory factory) {
        super(factory);
    }

    /**
     * Décrit les vagues du niveau : 3, puis 5, puis 8 corbeaux.
     */
    @Override
    protected void buildWaves() {
        waves.add(new Wave(EnemyType.CROW, 3));
        waves.add(new Wave(EnemyType.CROW, 5));
        waves.add(new Wave(EnemyType.CROW, 8));
    }

    /**
     * Indique la couleur de fond du niveau : bleu nuit de la traversée.
     *
     * @return la couleur de fond, en hexadécimal
     */
    @Override
    public String getBackgroundColor() {
        return "#16223C";
    }

    /**
     * Indique la couleur d'accent du niveau : bleu glacé de la traversée.
     *
     * @return la couleur d'accent, en hexadécimal
     */
    @Override
    public String getAccentColor() {
        return "#59B8FF";
    }

    /**
     * Indique le nom du secteur : le ciel noir.
     *
     * @return le nom du secteur
     */
    @Override
    public String getLevelName() {
        return "CIEL NOIR";
    }
}
