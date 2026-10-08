package com.shootemup.enemy.boss;

import com.shootemup.enemy.EvilAI;

/**
 * Dernière phase du boss : descente vers les joueurs.
 * <p>
 * Le nom du fichier reprend l'arborescence existante.
 */
public class BersherkPhase implements BossPhase {

    /** Vitesse de descente, en pixels par seconde. */
    private static final double SPEED = 80;

    /**
     * Crée la dernière phase du boss.
     */
    public BersherkPhase() {
    }

    /**
     * Fait descendre le boss vers les joueurs, sans dépasser la moitié de l'écran.
     *
     * @param boss le boss contrôlé
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void execute(EvilAI boss, double dt) {
        double maxY = com.shootemup.core.Game.HEIGHT / 2.0;
        boss.setY(Math.min(boss.getY() + SPEED * dt, maxY));
    }

    /**
     * @return cette phase, car c'est la dernière
     */
    @Override
    public BossPhase next() {
        return this;
    }
}
