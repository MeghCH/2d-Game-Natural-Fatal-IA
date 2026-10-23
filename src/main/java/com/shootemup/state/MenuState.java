package com.shootemup.state;

import com.shootemup.core.Game;
import com.shootemup.input.Action;
import com.shootemup.input.InputController;
import com.shootemup.ui.ArcadeTheme;
import com.shootemup.ui.Starfield;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Set;

/**
 * Écran du menu : titre, touches actuelles des joueurs, Entrée pour jouer,
 * T pour changer les touches.
 */
public class MenuState implements GameState {

    /** Le jeu, utilisé pour lancer la partie et lire les touches des joueurs. */
    private final Game game;

    /** Le ciel étoilé qui défile derrière le menu. */
    private final Starfield background = new Starfield(Game.WIDTH, Game.HEIGHT);

    /** Temps écoulé pour le clignotement, en secondes. */
    private double elapsed = 0;

    /** Délai avant d'accepter une touche, en secondes : évite de sauter l'écran avec une touche restée enfoncée. */
    private static final double INPUT_DELAY = 1.0;

    /** Largeur d'un panneau de contrôles, en pixels. */
    private static final double CONTROL_PANEL_WIDTH = 340;

    /** Hauteur d'un panneau de contrôles, en pixels. */
    private static final double CONTROL_PANEL_HEIGHT = 82;

    /**
     * Crée le menu.
     *
     * @param game le jeu auquel appartient cet écran
     */
    public MenuState(Game game) {
        this.game = game;
    }

    /**
     * Remet le clignotement à zéro.
     */
    @Override
    public void enter() {
        elapsed = 0;
    }

    /**
     * Fait défiler le fond et avancer le clignotement.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void update(double dt) {
        elapsed += dt;
        background.update(dt);
    }

    /**
     * Dessine l'écran titre : logo néon, rappel des contrôles dans des panneaux
     * arcade, invitation clignotante et cadre CRT.
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
                Game.HEIGHT * 0.66, ArcadeTheme.CYAN, elapsed * 0.6);
        ArcadeTheme.vignette(gc, Game.WIDTH, Game.HEIGHT);

        ArcadeTheme.centered(gc, "NATURAL FATAL SOFT // ARCADE SYSTEM",
                centerX, 36, 12, ArcadeTheme.MUTED, null);

        ArcadeTheme.centered(gc, "NATURAL FATAL IA", centerX + 4, 158,
                52, ArcadeTheme.MAGENTA, null);
        ArcadeTheme.glowCentered(gc, "NATURAL FATAL IA", centerX, 154,
                52, ArcadeTheme.WHITE, ArcadeTheme.MAGENTA);

        ArcadeTheme.centered(gc, "VERTICAL SHOOT'EM UP  //  1-2 PLAYERS",
                centerX, 202, 15, ArcadeTheme.CYAN, ArcadeTheme.VOID);

        gc.setStroke(ArcadeTheme.withAlpha(ArcadeTheme.CYAN, 0.5));
        gc.setLineWidth(1);
        gc.strokeLine(centerX - 300, 226, centerX + 300, 226);

        List<InputController> controls = game.getPlayerControls();
        double panelY = 258;
        drawControlPanel(gc, centerX - CONTROL_PANEL_WIDTH - 12, panelY,
                "1P", keysOf(controls.get(0)), ArcadeTheme.PLAYER_ONE);
        drawControlPanel(gc, centerX + 12, panelY,
                "2P", keysOf(controls.get(1)), ArcadeTheme.PLAYER_TWO);

        double promptY = 408;
        double bob = Math.sin(elapsed * 3.0) * 3;
        ArcadeTheme.ship(gc, centerX - 250, promptY + bob, ArcadeTheme.PLAYER_ONE, 1.0, elapsed);
        ArcadeTheme.ship(gc, centerX + 250, promptY + bob, ArcadeTheme.PLAYER_TWO, 1.0, elapsed);

        if (elapsed % 1.0 < 0.62) {
            ArcadeTheme.centered(gc, "> PRESS ENTER TO START <", centerX, promptY, 22, ArcadeTheme.AMBER, ArcadeTheme.MAGENTA);
            ArcadeTheme.centered(gc, "T // CONFIGURER LES TOUCHES", centerX,
                    promptY + 34, 12, ArcadeTheme.MUTED, ArcadeTheme.VOID);
        }

        ArcadeTheme.centered(gc, "FREE PLAY", 70, Game.HEIGHT - 38,
                11, ArcadeTheme.MUTED, null);
        ArcadeTheme.centered(gc, "CREDIT 01", Game.WIDTH - 70, Game.HEIGHT - 38,
                11, ArcadeTheme.AMBER, null);
        ArcadeTheme.centered(gc, "© 1987 NATURAL FATAL SOFT",
                centerX, Game.HEIGHT - 38, 11, ArcadeTheme.MUTED, null);
        ArcadeTheme.centered(gc, "F11 // FULLSCREEN",
                centerX, Game.HEIGHT - 18, 9, ArcadeTheme.MUTED, null);

        ArcadeTheme.scanlines(gc, Game.WIDTH, Game.HEIGHT);
        ArcadeTheme.crtFrame(gc, Game.WIDTH, Game.HEIGHT);
    }

    /**
     * Résume les touches d'un joueur, par exemple « Z Q S D + ESPACE ».
     * Seule la première touche de chaque action est affichée.
     *
     * @param controls touches du joueur
     * @return le résumé des touches du joueur
     */
    private static String keysOf(InputController controls) {
        return firstKey(controls, Action.UP) + " "
                + firstKey(controls, Action.LEFT) + " "
                + firstKey(controls, Action.DOWN) + " "
                + firstKey(controls, Action.RIGHT) + " + "
                + firstKey(controls, Action.SHOOT);
    }

    /**
     * Donne le nom de la première touche d'une action.
     *
     * @param controls touches du joueur
     * @param action   l'action
     * @return le nom de la première touche, ou « --- » si l'action n'en a aucune
     */
    private static String firstKey(InputController controls, Action action) {
        Set<KeyCode> keys = controls.getKeys(action);
        return keys.isEmpty() ? "---" : ControlsState.keyName(keys.iterator().next());
    }

    /**
     * Dessine un panneau de contrôles pour un joueur.
     *
     * @param gc    contexte graphique
     * @param x     position horizontale du panneau
     * @param y     position verticale du panneau
     * @param label nom du joueur, par exemple « 1P »
     * @param keys  touches à afficher
     * @param color couleur du joueur
     */
    private void drawControlPanel(GraphicsContext gc, double x, double y,
                                  String label, String keys, Color color) {
        ArcadeTheme.panel(gc, x, y, CONTROL_PANEL_WIDTH, CONTROL_PANEL_HEIGHT, color);
        ArcadeTheme.text(gc, label, x + 20, y + 30, 20, color, ArcadeTheme.VOID);
        ArcadeTheme.text(gc, keys, x + 20, y + 62, 16, ArcadeTheme.WHITE, ArcadeTheme.VOID);
    }

    /**
     * Lance la partie sur Entrée, ouvre l'écran des touches sur T, une fois le délai passé.
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
        } else if (code == KeyCode.T) {
            game.setState(new ControlsState(game, this));
        }
    }

    /**
     * Ne fait rien dans le menu.
     *
     * @param code la touche relâchée
     */
    @Override
    public void keyReleased(KeyCode code) {
    }
}
