package com.shootemup.ui;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Palette et primitives graphiques communes aux écrans du jeu.
 * <p>
 * Regroupe le look « borne d'arcade » des années 80-90 : fond sombre,
 * typographie monospace, cadres CRT, scanlines, vignettage et petites
 * formes pixel art. Les écrans (menu, partie, game over, victoire) s'appuient
 * dessus pour rester visuellement cohérents.
 */
public final class ArcadeTheme {

    /** Fond presque noir des écrans hors partie. */
    public static final Color VOID = Color.web("#05070D");

    /** Fond des panneaux translucides. */
    public static final Color PANEL = Color.web("#0B1220");

    /** Cyan des éléments « système ». */
    public static final Color CYAN = Color.web("#37E6F2");

    /** Magenta des titres et accents forts. */
    public static final Color MAGENTA = Color.web("#FF3D9A");

    /** Ambre des scores et invitations. */
    public static final Color AMBER = Color.web("#FFC93C");

    /** Vert des validations et de la victoire. */
    public static final Color GREEN = Color.web("#55F08B");

    /** Rouge des alertes et du game over. */
    public static final Color RED = Color.web("#FF4B5C");

    /** Blanc cassé du texte principal. */
    public static final Color WHITE = Color.web("#F2F6FF");

    /** Gris bleuté du texte secondaire. */
    public static final Color MUTED = Color.web("#7786A8");

    /** Couleur du joueur 1. */
    public static final Color PLAYER_ONE = Color.web("#55F0C8");

    /** Couleur du joueur 2. */
    public static final Color PLAYER_TWO = Color.web("#FFA24B");

    /** Nombre de segments des barres de vie. */
    public static final int BAR_SEGMENTS = 12;

    private ArcadeTheme() {
    }

    /**
     * Renvoie la police monospace en gras utilisée partout dans l'interface.
     *
     * @param size taille de la police, en pixels
     * @return la police arcade
     */
    public static Font font(double size) {
        return Font.font("Monospaced", FontWeight.BOLD, size);
    }

    /**
     * Dessine un texte aligné à gauche avec une ombre portée décalée.
     *
     * @param gc     contexte graphique
     * @param text   texte à dessiner
     * @param x      position horizontale
     * @param y      position verticale
     * @param size   taille de la police
     * @param fill   couleur du texte
     * @param shadow couleur de l'ombre, ou {@code null}
     */
    public static void text(GraphicsContext gc, String text, double x, double y,
                            double size, Color fill, Color shadow) {
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.BASELINE);
        gc.setFont(font(size));
        if (shadow != null) {
            gc.setFill(shadow);
            gc.fillText(text, x + 2, y + 2);
        }
        gc.setFill(fill);
        gc.fillText(text, x, y);
    }

    /**
     * Dessine un texte centré horizontalement avec une ombre portée décalée.
     *
     * @param gc      contexte graphique
     * @param text    texte à dessiner
     * @param centerX abscisse du centre du texte
     * @param y       position verticale du milieu du texte
     * @param size    taille de la police
     * @param fill    couleur du texte
     * @param shadow  couleur de l'ombre, ou {@code null}
     */
    public static void centered(GraphicsContext gc, String text, double centerX, double y,
                                double size, Color fill, Color shadow) {
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(font(size));
        if (shadow != null) {
            gc.setFill(shadow);
            gc.fillText(text, centerX + 2, y + 2);
        }
        gc.setFill(fill);
        gc.fillText(text, centerX, y);
    }

    /**
     * Dessine un texte centré entouré d'un halo lumineux.
     *
     * @param gc      contexte graphique
     * @param text    texte à dessiner
     * @param centerX abscisse du centre du texte
     * @param y       position verticale du milieu du texte
     * @param size    taille de la police
     * @param fill    couleur du texte
     * @param glow    couleur du halo
     */
    public static void glowCentered(GraphicsContext gc, String text, double centerX, double y,
                                    double size, Color fill, Color glow) {
        gc.save();
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(font(size));
        gc.setEffect(new DropShadow(14, glow));
        gc.setFill(fill);
        gc.fillText(text, centerX, y);
        gc.restore();
    }

    /**
     * Dessine un panneau translucide à double bordure avec des coins accentués.
     *
     * @param gc     contexte graphique
     * @param x      position horizontale du coin supérieur gauche
     * @param y      position verticale du coin supérieur gauche
     * @param width  largeur du panneau
     * @param height hauteur du panneau
     * @param border couleur de la bordure
     */
    public static void panel(GraphicsContext gc, double x, double y,
                             double width, double height, Color border) {
        gc.save();
        gc.setFill(new Color(PANEL.getRed(), PANEL.getGreen(), PANEL.getBlue(), 0.78));
        gc.fillRect(x, y, width, height);

        gc.setStroke(withAlpha(border, 0.85));
        gc.setLineWidth(2);
        gc.strokeRect(x + 1, y + 1, width - 2, height - 2);

        gc.setStroke(withAlpha(border, 0.32));
        gc.setLineWidth(1);
        gc.strokeRect(x + 5, y + 5, width - 10, height - 10);

        gc.setStroke(border);
        gc.setLineWidth(3);
        drawCorner(gc, x, y, 1, 1);
        drawCorner(gc, x + width, y, -1, 1);
        drawCorner(gc, x, y + height, 1, -1);
        drawCorner(gc, x + width, y + height, -1, -1);
        gc.restore();
    }

    /**
     * Dessine un coin en équerre.
     *
     * @param gc contexte graphique
     * @param x  position horizontale du coin
     * @param y  position verticale du coin
     * @param dx sens horizontal (1 ou -1)
     * @param dy sens vertical (1 ou -1)
     */
    private static void drawCorner(GraphicsContext gc, double x, double y, int dx, int dy) {
        double length = 12;
        gc.strokeLine(x, y, x + dx * length, y);
        gc.strokeLine(x, y, x, y + dy * length);
    }

    /**
     * Dessine un cadre CRT discret autour de l'écran, avec des coins en équerre.
     *
     * @param gc     contexte graphique
     * @param width  largeur de l'écran
     * @param height hauteur de l'écran
     */
    public static void crtFrame(GraphicsContext gc, double width, double height) {
        gc.save();
        gc.setStroke(withAlpha(CYAN, 0.35));
        gc.setLineWidth(1);
        gc.strokeRect(8.5, 8.5, width - 17, height - 17);

        gc.setStroke(withAlpha(CYAN, 0.8));
        gc.setLineWidth(2);
        double margin = 16;
        double length = 26;
        gc.strokeLine(margin, margin, margin + length, margin);
        gc.strokeLine(margin, margin, margin, margin + length);
        gc.strokeLine(width - margin, margin, width - margin - length, margin);
        gc.strokeLine(width - margin, margin, width - margin, margin + length);
        gc.strokeLine(margin, height - margin, margin + length, height - margin);
        gc.strokeLine(margin, height - margin, margin, height - margin - length);
        gc.strokeLine(width - margin, height - margin, width - margin - length, height - margin);
        gc.strokeLine(width - margin, height - margin, width - margin, height - margin - length);
        gc.restore();
    }

    /**
     * Dessine des lignes horizontales sombres pour imiter un écran cathodique.
     *
     * @param gc     contexte graphique
     * @param width  largeur de l'écran
     * @param height hauteur de l'écran
     */
    public static void scanlines(GraphicsContext gc, double width, double height) {
        gc.save();
        gc.setFill(Color.rgb(0, 0, 0, 0.12));
        for (double y = 0; y < height; y += 4) {
            gc.fillRect(0, y, width, 2);
        }
        gc.restore();
    }

    /**
     * Assombrit les bords de l'écran pour l'effet de tube cathodique.
     *
     * @param gc     contexte graphique
     * @param width  largeur de l'écran
     * @param height hauteur de l'écran
     */
    public static void vignette(GraphicsContext gc, double width, double height) {
        gc.save();
        RadialGradient shade = new RadialGradient(0, 0, 0.5, 0.5, 0.72, true,
                CycleMethod.NO_CYCLE,
                new Stop(0.52, Color.TRANSPARENT),
                new Stop(1, Color.rgb(0, 0, 0, 0.62)));
        gc.setFill(shade);
        gc.fillRect(0, 0, width, height);
        gc.restore();
    }

    /**
     * Dessine une grille en perspective qui défile vers le bas, façon sol néon.
     *
     * @param gc      contexte graphique
     * @param width   largeur de l'écran
     * @param height  hauteur de l'écran
     * @param horizon ordonnée de la ligne d'horizon
     * @param color   couleur de la grille
     * @param phase   décalage d'animation, en lignes
     */
    public static void horizonGrid(GraphicsContext gc, double width, double height,
                                   double horizon, Color color, double phase) {
        gc.save();
        gc.setStroke(withAlpha(color, 0.28));
        gc.setLineWidth(1);

        int rows = 14;
        for (int i = 0; i < rows; i++) {
            double t = (i + phase % 1.0) / rows;
            double y = horizon + Math.pow(t, 2.2) * (height - horizon);
            if (y < horizon) {
                continue;
            }
            gc.strokeLine(0, y, width, y);
        }
        for (int i = -10; i <= 10; i++) {
            gc.strokeLine(width / 2.0 + i * 34, horizon,
                    width / 2.0 + i * 170, height);
        }
        gc.restore();
    }

    /**
     * Dessine une barre de vie segmentée, typique des bornes d'arcade.
     *
     * @param gc     contexte graphique
     * @param x      position horizontale
     * @param y      position verticale
     * @param width  largeur de la barre
     * @param height hauteur de la barre
     * @param ratio  proportion remplie, entre 0 et 1
     * @param color  couleur de la partie remplie
     */
    public static void segmentedBar(GraphicsContext gc, double x, double y,
                                    double width, double height, double ratio, Color color) {
        gc.save();
        gc.setFill(Color.rgb(3, 6, 12, 0.92));
        gc.fillRect(x, y, width, height);

        double clamped = Math.max(0, Math.min(1, ratio));
        gc.setFill(color);
        gc.fillRect(x + 1, y + 1, (width - 2) * clamped, height - 2);

        gc.setFill(Color.rgb(3, 6, 12, 0.95));
        double segment = width / BAR_SEGMENTS;
        for (int i = 1; i < BAR_SEGMENTS; i++) {
            gc.fillRect(x + i * segment - 1, y + 1, 2, height - 2);
        }

        gc.setStroke(withAlpha(color, 0.85));
        gc.setLineWidth(1);
        gc.strokeRect(x + 0.5, y + 0.5, width - 1, height - 1);
        gc.restore();
    }

    /**
     * Dessine un petit vaisseau en pixel art, utilisé comme décoration.
     *
     * @param gc    contexte graphique
     * @param cx    abscisse du centre du vaisseau
     * @param cy    ordonnée du centre du vaisseau
     * @param color couleur du vaisseau
     * @param scale taille relative du vaisseau
     * @param time  temps écoulé, pour animer la flamme
     */
    public static void ship(GraphicsContext gc, double cx, double cy,
                            Color color, double scale, double time) {
        gc.save();
        gc.setFill(color);
        gc.fillRect(cx - 3 * scale, cy - 10 * scale, 6 * scale, 16 * scale);
        gc.fillRect(cx - 13 * scale, cy + 1 * scale, 26 * scale, 4 * scale);
        gc.fillRect(cx - 8 * scale, cy + 5 * scale, 16 * scale, 3 * scale);
        gc.fillPolygon(
                new double[]{cx - 3 * scale, cx + 3 * scale, cx},
                new double[]{cy - 10 * scale, cy - 10 * scale, cy - 18 * scale}, 3);

        gc.setFill(Color.web("#EAF6FF"));
        gc.fillRect(cx - 1 * scale, cy - 4 * scale, 2 * scale, 5 * scale);

        boolean flame = ((int) (time * 18)) % 2 == 0;
        gc.setFill(flame ? AMBER : MAGENTA);
        gc.fillRect(cx - 2 * scale, cy + 10 * scale, 4 * scale, (flame ? 7 : 4) * scale);
        gc.restore();
    }

    /**
     * Renvoie une couleur identique avec une transparence donnée.
     *
     * @param color couleur d'origine
     * @param alpha transparence, entre 0 et 1
     * @return la couleur translucide
     */
    public static Color withAlpha(Color color, double alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
