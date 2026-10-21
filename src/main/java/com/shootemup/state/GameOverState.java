package com.shootemup.state;

import com.shootemup.core.Game;
import com.shootemup.ui.ArcadeTheme;
import com.shootemup.ui.Starfield;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

/**
 * Écran de fin de partie : score final, Entrée pour rejouer, Échap pour le menu.
 * <p>
 * Présenté comme un écran d'arcade « GAME OVER » : titre qui grésille,
 * panneau de score et invite à insérer une pièce.
 */
public class GameOverState implements GameState {

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

    /**
     * Crée l'écran de fin de partie.
     *
     * @param game le jeu auquel appartient cet écran
     * @param score le score final à afficher
     */
    public GameOverState(Game game, int score) {
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
     * Dessine l'écran « GAME OVER » : fond rouge, titre qui grésille,
     * panneau de score et choix (rejouer ou menu).
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
                Game.HEIGHT * 0.67, ArcadeTheme.RED, elapsed * 0.5);
        ArcadeTheme.vignette(gc, Game.WIDTH, Game.HEIGHT);

        ArcadeTheme.centered(gc, "SIGNAL LOST // CONNECTION TERMINATED",
                centerX, 46, 12, ArcadeTheme.RED, ArcadeTheme.VOID);

        double titleY = 168;
        if (elapsed % 2.4 < 0.12) {
            ArcadeTheme.centered(gc, "GAME OVER", centerX - 6, titleY, 72,
                    ArcadeTheme.CYAN, null);
            ArcadeTheme.centered(gc, "GAME OVER", centerX + 6, titleY, 72,
                    ArcadeTheme.MAGENTA, null);
        }
        ArcadeTheme.centered(gc, "GAME OVER", centerX + 4, titleY + 4, 72,
                ArcadeTheme.withAlpha(ArcadeTheme.RED, 0.45), null);
        ArcadeTheme.glowCentered(gc, "GAME OVER", centerX, titleY,
                72, ArcadeTheme.RED, ArcadeTheme.RED);

        ArcadeTheme.centered(gc, "THE LAST FIGHTER HAS FALLEN",
                centerX, 222, 14, ArcadeTheme.MUTED, ArcadeTheme.VOID);

        double panelX = centerX - 230;
        double panelY = 258;
        ArcadeTheme.panel(gc, panelX, panelY, 460, 96, ArcadeTheme.RED);
        ArcadeTheme.centered(gc, "FINAL SCORE", centerX, panelY + 24,
                12, ArcadeTheme.MUTED, null);
        ArcadeTheme.centered(gc, String.format("%06d", score), centerX,
                panelY + 62, 38, ArcadeTheme.AMBER, ArcadeTheme.MAGENTA);

        if (elapsed % 1.0 < 0.62) {
            ArcadeTheme.centered(gc, "PRESS ENTER // CONTINUE",
                    centerX, 404, 20, ArcadeTheme.AMBER, ArcadeTheme.MAGENTA);
        }
        ArcadeTheme.centered(gc, "ESC // TITLE SCREEN", centerX, 438,
                12, ArcadeTheme.MUTED, ArcadeTheme.VOID);

        if (elapsed % 1.4 < 0.8) {
            ArcadeTheme.centered(gc, "INSERT COIN", centerX, Game.HEIGHT - 60,
                    12, ArcadeTheme.RED, null);
        }
        ArcadeTheme.centered(gc, "© 1987 NATURAL FATAL SOFT",
                centerX, Game.HEIGHT - 36, 11, ArcadeTheme.MUTED, null);

        ArcadeTheme.scanlines(gc, Game.WIDTH, Game.HEIGHT);
        ArcadeTheme.crtFrame(gc, Game.WIDTH, Game.HEIGHT);
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
     * Ne fait rien sur l'écran de fin.
     *
     * @param code la touche relâchée
     */
    @Override
    public void keyReleased(KeyCode code) {
    }
}
