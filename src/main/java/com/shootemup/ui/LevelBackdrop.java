package com.shootemup.ui;

import com.shootemup.entity.Updatable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

import java.util.Random;

/**
 * Décor de fond des niveaux : dégradé teinté, poussières en parallaxe,
 * grille d'horizon et finitions CRT.
 * <p>
 * Le fond s'adapte à la couleur du niveau courant : chaque secteur garde
 * son ambiance (jardin, ciel, nid, cœur de l'IA) tout en partageant le même
 * langage visuel arcade.
 */
public class LevelBackdrop implements Updatable {

    /** Nombre de poussières/stries en mouvement. */
    private static final int PARTICLE_COUNT = 90;

    /** Largeur de l'écran. */
    private final int width;

    /** Hauteur de l'écran. */
    private final int height;

    /** Positions horizontales des particules. */
    private final double[] xs = new double[PARTICLE_COUNT];

    /** Positions verticales des particules. */
    private final double[] ys = new double[PARTICLE_COUNT];

    /** Vitesse de chute de chaque particule, en pixels par seconde. */
    private final double[] speeds = new double[PARTICLE_COUNT];

    /** Longueur du trait de chaque particule, en pixels. */
    private final double[] lengths = new double[PARTICLE_COUNT];

    /** Opacité de chaque particule. */
    private final double[] alphas = new double[PARTICLE_COUNT];

    /** {@code true} si la particule est blanche, {@code false} si elle prend la couleur du niveau. */
    private final boolean[] whites = new boolean[PARTICLE_COUNT];

    /** Temps écoulé, pour l'animation de la grille. */
    private double time;

    /**
     * Crée un décor pour un écran de la taille donnée.
     *
     * @param width  largeur de l'écran, en pixels
     * @param height hauteur de l'écran, en pixels
     */
    public LevelBackdrop(int width, int height) {
        this.width = width;
        this.height = height;
        Random random = new Random();
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            xs[i] = random.nextDouble() * width;
            ys[i] = random.nextDouble() * height;
            speeds[i] = 18 + random.nextDouble() * 70;
            lengths[i] = 2 + random.nextDouble() * 10;
            alphas[i] = 0.12 + random.nextDouble() * 0.38;
            whites[i] = random.nextBoolean();
        }
    }

    /**
     * Fait avancer la grille et tomber les particules.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void update(double dt) {
        if (dt <= 0) {
            return;
        }
        time += dt;
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            ys[i] += speeds[i] * dt;
            if (ys[i] > height) {
                ys[i] -= height;
            }
        }
    }

    /**
     * Dessine le décor du niveau avec ses couleurs.
     *
     * @param gc          contexte graphique
     * @param background  couleur de fond du niveau, en hexadécimal
     * @param accent      couleur d'accent du niveau
     */
    public void render(GraphicsContext gc, String background, String accent) {
        Color base = Color.web(background);
        Color accentColor = Color.web(accent);
        Color deep = base.interpolate(Color.BLACK, 0.58);
        Color luminous = base.interpolate(Color.WHITE, 0.12);

        gc.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, deep),
                new Stop(0.58, base),
                new Stop(1, luminous)));
        gc.fillRect(0, 0, width, height);

        gc.setFill(new RadialGradient(0, 0, 0.5, 0.62, 0.62, true,
                CycleMethod.NO_CYCLE,
                new Stop(0, ArcadeTheme.withAlpha(accentColor, 0.10)),
                new Stop(1, Color.TRANSPARENT)));
        gc.fillRect(0, 0, width, height);

        ArcadeTheme.horizonGrid(gc, width, height, height * 0.74, accentColor, time * 0.45);

        for (int i = 0; i < PARTICLE_COUNT; i++) {
            gc.setFill(ArcadeTheme.withAlpha(
                    whites[i] ? Color.WHITE : accentColor, alphas[i]));
            gc.fillRect(xs[i], ys[i], whites[i] ? 2 : 1, lengths[i]);
        }

        ArcadeTheme.vignette(gc, width, height);
        ArcadeTheme.scanlines(gc, width, height);
    }
}
