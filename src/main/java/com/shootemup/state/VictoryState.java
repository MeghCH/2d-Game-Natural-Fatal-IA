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
 * Écran de victoire : le boss final est vaincu, score final,
 * Entrée pour rejouer, Échap pour le menu.
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
     * Dessine le fond rétro (étoiles, grille néon, scanlines), le titre,
     * le score et les choix (rejouer ou menu).
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
        gc.setFill(Color.web("#1F5C46"));
        gc.fillText("VICTOIRE", Game.WIDTH / 2.0 + 3, Game.HEIGHT / 2.0 - 57);
        gc.setFill(Color.web("#5DCAA5"));
        gc.fillText("VICTOIRE", Game.WIDTH / 2.0, Game.HEIGHT / 2.0 - 60);
        gc.setFont(Font.font(24));
        gc.setFill(Color.WHITE);
        gc.fillText("IA FATALE VAINCUE", Game.WIDTH / 2.0, Game.HEIGHT / 2.0 - 10);
        gc.fillText("SCORE : " + score, Game.WIDTH / 2.0, Game.HEIGHT / 2.0 + 25);

        if (elapsed % 1.0 < 0.6) {
            gc.setFont(Font.font(20));
            gc.fillText("ENTREE : REJOUER   -   ECHAP : MENU", Game.WIDTH / 2.0, Game.HEIGHT / 2.0 + 70);
        }
        drawScanlines(gc);
    }

    /**
     * Dessine le sol en grille néon, lignes vertes convergentes vers l'horizon.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    private void drawGrid(GraphicsContext gc) {
        double horizon = Game.HEIGHT * 0.68;
        gc.setStroke(Color.web("#5DCAA5"));
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
