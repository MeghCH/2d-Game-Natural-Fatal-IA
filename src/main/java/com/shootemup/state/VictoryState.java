package com.shootemup.state;

import com.shootemup.core.Game;
import com.shootemup.ui.ArcadeTheme;
import com.shootemup.ui.Starfield;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

/**
 * Écran de victoire : le boss final est vaincu, score final et rang,
 * Entrée pour rejouer, Échap pour le menu.
 * <p>
 * Présenté comme un écran de fin de mission arcade : « MISSION COMPLETE »,
 * bilan de score et rang à la manière des vieux shoot'em up.
 */
public class VictoryState implements GameState {

    /** Le jeu, utilisé pour changer d'écran. */
    private final Game game;

    /** Score final à afficher. */
    private final int score;

    /** Temps écoulé depuis l'affichage, en secondes. */
    private double elapsed = 0;

    /** Le ciel étoilé qui défile derrière l'écran, comme au menu. */
    private final Starfield background = new Starfield(Game.WIDTH, Game.HEIGHT);

    /** Délai avant d'accepter une touche, en secondes : évite de sauter l'écran avec une touche restée enfoncée. */
    private static final double INPUT_DELAY = 1.0;

    /** Score à partir duquel le joueur obtient le rang S. */
    private static final int RANK_S = 9000;

    /** Score à partir duquel le joueur obtient le rang A. */
    private static final int RANK_A = 7000;

    /** Score à partir duquel le joueur obtient le rang B. */
    private static final int RANK_B = 4500;

    /**
     * Crée l'écran de victoire.
     *
     * @param game le jeu auquel appartient cet écran
     * @param score le score final à afficher
     */
    public VictoryState(Game game, int score) {
        this.game = game;
        this.score = score;
    }

    /**
     * Aucune préparation nécessaire.
     */
    @Override
    public void enter() {
    }

    /**
     * Fait avancer le délai avant d'accepter les touches et défiler le fond.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void update(double dt) {
        elapsed += dt;
        background.update(dt);
    }

    /**
     * Dessine l'écran de victoire : fond vert, titre « MISSION COMPLETE »,
     * bilan de score, rang et choix (rejouer ou menu).
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        double centerX = Game.WIDTH / 2.0;

        gc.setFill(ArcadeTheme.VOID);
        gc.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        background.render(gc);
        ArcadeTheme.horizonGrid(gc, Game.WIDTH, Game.HEIGHT,
                Game.HEIGHT * 0.67, ArcadeTheme.GREEN, elapsed * 0.5);
        ArcadeTheme.vignette(gc, Game.WIDTH, Game.HEIGHT);

        ArcadeTheme.centered(gc, "MISSION REPORT // SECTOR CLEARED",
                centerX, 46, 12, ArcadeTheme.GREEN, ArcadeTheme.VOID);

        double bob = Math.sin(elapsed * 2.6) * 4;
        ArcadeTheme.ship(gc, 120, 160 + bob, ArcadeTheme.PLAYER_ONE, 1.2, elapsed);
        ArcadeTheme.ship(gc, Game.WIDTH - 120, 160 - bob, ArcadeTheme.PLAYER_TWO, 1.2, elapsed);

        ArcadeTheme.centered(gc, "MISSION COMPLETE", centerX + 4, 164,
                52, ArcadeTheme.withAlpha(ArcadeTheme.GREEN, 0.45), null);
        ArcadeTheme.glowCentered(gc, "MISSION COMPLETE", centerX, 160,
                52, ArcadeTheme.GREEN, ArcadeTheme.AMBER);

        ArcadeTheme.centered(gc, "EVIL AI DESTROYED", centerX, 216,
                18, ArcadeTheme.AMBER, ArcadeTheme.MAGENTA);

        double panelX = centerX - 230;
        double panelY = 252;
        double panelWidth = 460;
        double panelHeight = 104;
        ArcadeTheme.panel(gc, panelX, panelY, panelWidth, panelHeight, ArcadeTheme.GREEN);

        double dividerX = panelX + 310;
        gc.setStroke(ArcadeTheme.withAlpha(ArcadeTheme.GREEN, 0.45));
        gc.setLineWidth(1);
        gc.strokeLine(dividerX, panelY + 16, dividerX, panelY + panelHeight - 16);

        ArcadeTheme.centered(gc, "FINAL SCORE", panelX + 155, panelY + 26,
                12, ArcadeTheme.MUTED, null);
        ArcadeTheme.centered(gc, String.format("%06d", score), panelX + 155,
                panelY + 66, 36, ArcadeTheme.AMBER, ArcadeTheme.MAGENTA);

        ArcadeTheme.centered(gc, "RANK", panelX + 385, panelY + 26,
                12, ArcadeTheme.MUTED, null);
        ArcadeTheme.centered(gc, rank(), panelX + 385, panelY + 64,
                40, rankColor(), ArcadeTheme.VOID);

        if (elapsed % 1.0 < 0.62) {
            ArcadeTheme.centered(gc, "PRESS ENTER // NEW GAME",
                    centerX, 410, 20, ArcadeTheme.AMBER, ArcadeTheme.MAGENTA);
        }
        ArcadeTheme.centered(gc, "ESC // TITLE SCREEN", centerX, 442,
                12, ArcadeTheme.MUTED, ArcadeTheme.VOID);

        ArcadeTheme.centered(gc, "THANK YOU FOR PLAYING", centerX,
                Game.HEIGHT - 58, 12, ArcadeTheme.GREEN, null);
        ArcadeTheme.centered(gc, "© 1987 NATURAL FATAL SOFT",
                centerX, Game.HEIGHT - 36, 11, ArcadeTheme.MUTED, null);

        ArcadeTheme.scanlines(gc, Game.WIDTH, Game.HEIGHT);
        ArcadeTheme.crtFrame(gc, Game.WIDTH, Game.HEIGHT);
    }

    /**
     * Calcule le rang obtenu selon le score final.
     *
     * @return le rang, de S (meilleur) à C
     */
    private String rank() {
        if (score >= RANK_S) {
            return "S";
        }
        if (score >= RANK_A) {
            return "A";
        }
        if (score >= RANK_B) {
            return "B";
        }
        return "C";
    }

    /**
     * Renvoie la couleur associée au rang obtenu.
     *
     * @return la couleur du rang
     */
    private Color rankColor() {
        return switch (rank()) {
            case "S" -> ArcadeTheme.AMBER;
            case "A" -> ArcadeTheme.GREEN;
            case "B" -> ArcadeTheme.CYAN;
            default -> ArcadeTheme.MUTED;
        };
    }

    /**
     * Rejoue sur Entrée, retourne au menu sur Échap, une fois le délai passé.
     *
     * @param code la touche enfoncée
     */
    @Override
    public void keyPressed(KeyCode code) {
        if (elapsed < INPUT_DELAY) {
            return;
        }
        if (code == KeyCode.ENTER) {
            game.setState(new PlayingState(game));
        } else if (code == KeyCode.ESCAPE) {
            game.setState(new MenuState(game));
        }
    }

    /**
     * Ne fait rien sur l'écran de victoire.
     *
     * @param code la touche relâchée
     */
    @Override
    public void keyReleased(KeyCode code) {
    }
}
