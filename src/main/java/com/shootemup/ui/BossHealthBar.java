package com.shootemup.ui;

import com.shootemup.core.Game;
import com.shootemup.enemy.EvilAI;
import com.shootemup.entity.Renderable;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Barre de vie du boss final, affichée en haut au centre quand il est en jeu.
 * <p>
 * Simple affichage : aucun abonnement, la barre est relue à chaque image
 * depuis le boss suivi. Quand il n'y a pas de boss, rien n'est dessiné.
 */
public class BossHealthBar implements Renderable {

    /** Largeur de la barre, en pixels. */
    private static final double BAR_WIDTH = 300;

    /** Hauteur de la barre, en pixels. */
    private static final double BAR_HEIGHT = 14;

    /** Boss suivi, ou {@code null} hors du combat final. */
    private EvilAI boss;

    /**
     * Crée une barre sans boss suivi.
     */
    public BossHealthBar() {
    }

    /**
     * Change le boss suivi, par exemple quand le niveau du boss commence.
     *
     * @param boss le boss à suivre, ou {@code null} pour cacher la barre
     */
    public void setBoss(EvilAI boss) {
        this.boss = boss;
    }

    /**
     * Indique le boss actuellement suivi.
     *
     * @return le boss suivi, ou {@code null}
     */
    public EvilAI getBoss() {
        return boss;
    }

    /**
     * Dessine la barre du boss avec ses points de vie. Ne dessine rien
     * quand il n'y a pas de boss ou qu'il est mort.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        if (boss == null || !boss.isAlive()) {
            return;
        }
        double barX = (Game.WIDTH - BAR_WIDTH) / 2.0;
        double barY = 40;

        gc.setFill(Color.web("#2C2C2A"));
        gc.fillRect(barX, barY, BAR_WIDTH, BAR_HEIGHT);

        double filled = BAR_WIDTH * boss.getHp() / (double) boss.getMaxHp();
        gc.setFill(Color.web("#E24B7A"));
        gc.fillRect(barX, barY, filled, BAR_HEIGHT);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(12));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.TOP);
        gc.fillText("EVIL AI  " + boss.getHp() + "/" + boss.getMaxHp(),
                Game.WIDTH / 2.0, barY + BAR_HEIGHT + 4);
    }
}
