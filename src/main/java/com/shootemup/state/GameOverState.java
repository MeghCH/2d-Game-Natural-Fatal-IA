package com.shootemup.state;

import com.shootemup.core.Game;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Écran de fin de partie : score final, Entrée pour rejouer, Échap pour le menu.
 */
public class GameOverState implements GameState {

    /** Le jeu, utilisé pour changer d'écran. */
    private final Game game;

    /** Score final à afficher. */
    private final int score;

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
     * L'écran de fin est fixe : rien à mettre à jour.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void update(double dt) {
    }

    /**
     * Dessine le titre, le score et les choix (rejouer ou menu).
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.web("#101820"));
        gc.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(Font.font(48));
        gc.fillText("GAME OVER", Game.WIDTH / 2.0, Game.HEIGHT / 2.0 - 60);
        gc.setFont(Font.font(24));
        gc.fillText("SCORE : " + score, Game.WIDTH / 2.0, Game.HEIGHT / 2.0);
        gc.setFont(Font.font(20));
        gc.fillText("ENTREE : REJOUER   -   ECHAP : MENU", Game.WIDTH / 2.0, Game.HEIGHT / 2.0 + 60);
    }

    /**
     * Rejoue sur Entrée, retourne au menu sur Échap.
     *
     * @param code la touche enfoncée
     */
    @Override
    public void keyPressed(KeyCode code) {
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
