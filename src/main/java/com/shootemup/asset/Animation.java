package com.shootemup.asset;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Une animation découpée dans une planche de sprites.
 * <p>
 * Une planche est une grille d'images de même taille : chaque ligne correspond
 * à une animation (par exemple « au repos » ou « attaque »), et chaque colonne
 * à une étape de cette animation. Selon le temps écoulé, l'animation choisit
 * quelle image afficher, la découpe dans la planche et la dessine à l'écran.
 * <p>
 * L'animation ne contient pas l'image elle-même : la planche est fournie par
 * {@link AssetManager} au moment de dessiner.
 */
public class Animation {

    /** Largeur d'une image dans la planche, en pixels. */
    private final double frameWidth;

    /** Hauteur d'une image dans la planche, en pixels. */
    private final double frameHeight;

    /** Ligne de l'animation dans la planche, en commençant à 0. */
    private final int row;

    /** Colonne de la première image de l'animation, en commençant à 0. */
    private final int col;

    /** Nombre d'images de l'animation. */
    private final int frames;

    /** Vitesse de l'animation, en images par seconde. */
    private final double fps;

    /** {@code true} si l'animation recommence au début une fois terminée. */
    private final boolean loop;

    /**
     * Crée une animation à partir de sa position dans la planche.
     *
     * @param frameWidth  largeur d'une image dans la planche
     * @param frameHeight hauteur d'une image dans la planche
     * @param row         ligne de l'animation dans la planche
     * @param col         colonne de la première image
     * @param frames      nombre d'images de l'animation
     * @param fps         vitesse, en images par seconde
     * @param loop        {@code true} pour une animation en boucle
     */
    public Animation(double frameWidth, double frameHeight, int row, int col, int frames, double fps, boolean loop) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.row = row;
        this.col = col;
        this.frames = frames;
        this.fps = fps;
        this.loop = loop;
    }

    /**
     * Crée une animation en boucle qui commence à la première colonne de sa ligne,
     * le cas le plus courant.
     *
     * @param frameWidth  largeur d'une image dans la planche
     * @param frameHeight hauteur d'une image dans la planche
     * @param row         ligne de l'animation dans la planche
     * @param frames      nombre d'images de l'animation
     * @param fps         vitesse, en images par seconde
     */
    public Animation(double frameWidth, double frameHeight, int row, int frames, double fps) {
        this(frameWidth, frameHeight, row, 0, frames, fps, true);
    }

    /**
     * Calcule quelle image afficher selon le temps écoulé. Une animation en boucle
     * recommence au début ; sinon, elle reste sur sa dernière image.
     *
     * @param time temps écoulé depuis le début de l'animation, en secondes
     * @return le numéro de l'image à afficher, entre 0 et {@code frames - 1}
     */
    public int frameIndex(double time) {
        int index = (int) (time * fps);
        if (loop == true) {
            return index % frames;
        } else {
            return Math.min(index, frames - 1);
        }
    }

    /**
     * Indique si une animation jouée une seule fois est arrivée à son terme.
     * Une animation en boucle ne se termine jamais.
     *
     * @param time temps écoulé depuis le début de l'animation, en secondes
     * @return {@code true} si l'animation est terminée
     */
    public boolean isFinished(double time) {
        if (loop) {
            return false;
        } else {
            return (time * fps) >= frames;
        }
    }

    /**
     * Découpe l'image correspondant au temps écoulé dans la planche, puis la dessine
     * à la position et à la taille données.
     *
     * @param gc     contexte graphique du canvas sur lequel dessiner
     * @param sheet  la planche de sprites
     * @param time   temps écoulé depuis le début de l'animation, en secondes
     * @param x      position horizontale à l'écran
     * @param y      position verticale à l'écran
     * @param width  largeur d'affichage à l'écran
     * @param height hauteur d'affichage à l'écran
     */
    public void draw(GraphicsContext gc, Image sheet, double time, double x, double y, double width, double height) {
        int index = frameIndex(time);
        double sx = (col + index) * frameWidth;
        double sy = row * frameHeight;

        gc.drawImage(sheet, sx, sy, frameWidth, frameHeight, x, y, width, height);
    }
}