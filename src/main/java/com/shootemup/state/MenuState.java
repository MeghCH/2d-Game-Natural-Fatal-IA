package com.shootemup.state;

import com.shootemup.core.Game;
import com.shootemup.ui.Starfield;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Écran du menu : titre, contrôles, Entrée pour jouer.
 */
public class MenuState implements GameState {

    /** Le jeu, utilisé pour lancer la partie. */
    private final Game game;

    /** Le ciel étoilé qui défile derrière le menu. */
    private final Starfield background = new Starfield(Game.WIDTH, Game.HEIGHT);

    /** Temps écoulé pour le clignotement, en secondes. */
    private double elapsed = 0;

    /** Délai avant d'accepter une touche, en secondes : évite de sauter l'écran avec une touche restée enfoncée. */
    private static final double INPUT_DELAY = 1.0;

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
     * Dessine le fond étoilé, le titre en néon, les contrôles en couleurs
     * et l'invite à jouer clignotante.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.web("#101820"));
        gc.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        background.render(gc);
        drawGrid(gc);

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(Font.font(48));
        gc.setFill(Color.web("#E24B7A"));
        gc.fillText("NATURAL FATAL IA", Game.WIDTH / 2.0 + 3, Game.HEIGHT / 2.0 - 77);
        gc.setFill(Color.WHITE);
        gc.fillText("NATURAL FATAL IA", Game.WIDTH / 2.0, Game.HEIGHT / 2.0 - 80);

        gc.setFont(Font.font(20));
        gc.setFill(Color.web("#5DCAA5"));
        gc.fillText("J1 : ZQSD + ESPACE", Game.WIDTH / 2.0, Game.HEIGHT / 2.0);
        gc.setFill(Color.web("#F0997B"));
        gc.fillText("J2 : FLECHES + ENTREE", Game.WIDTH / 2.0, Game.HEIGHT / 2.0 + 30);

        if (elapsed % 1.0 < 0.6) {
            gc.setFill(Color.WHITE);
            gc.fillText("ENTREE : JOUER", Game.WIDTH / 2.0, Game.HEIGHT / 2.0 + 80);
        }

        gc.setFont(Font.font(14));
        gc.setFill(Color.web("#8A93A6"));
        gc.fillText("SHOOT'EM UP COOPERATIF - 1 A 2 JOUEURS", Game.WIDTH / 2.0, Game.HEIGHT - 40);

        drawScanlines(gc);
    }

    /**
     * Dessine le sol en grille néon, lignes roses convergentes vers l'horizon.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    private void drawGrid(GraphicsContext gc) {
        double horizon = Game.HEIGHT * 0.68;
        gc.setStroke(Color.web("#E24B7A"));
        gc.setLineWidth(2);
        for (int i = 0; i < 4; i++) {
            double y = horizon + i * i * 8 + i * 12;
            gc.strokeLine(0, y, Game.WIDTH, y);
        }
        for (int i = -6; i <= 6; i++) {
            gc.strokeLine(Game.WIDTH / 2.0 + i * 40, horizon, Game.WIDTH / 2.0 + i * 140, Game.HEIGHT);
        }
    }

    /**
     * Dessine des lignes sombres sur tout l'écran, effet écran cathodique.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    private void drawScanlines(GraphicsContext gc) {
        gc.setFill(Color.rgb(0, 0, 0, 0.15));
        for (int y = 0; y < Game.HEIGHT; y += 4) {
            gc.fillRect(0, y, Game.WIDTH, 2);
        }
    }

    /**
     * Lance la partie sur Entrée, une fois le délai passé.
     *
     * @param code la touche enfoncée
     */
    @Override
    public void keyPressed(KeyCode code) {
        if (code == KeyCode.ENTER && elapsed >= INPUT_DELAY) {
            game.setState(new PlayingState(game));
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
