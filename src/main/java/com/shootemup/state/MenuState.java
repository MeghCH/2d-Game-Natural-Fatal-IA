package com.shootemup.state;

import com.shootemup.core.Game;
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

    /**
     * Crée le menu.
     *
     * @param game le jeu auquel appartient cet écran
     */
    public MenuState(Game game) {
        this.game = game;
    }

    /**
     * Aucune préparation nécessaire.
     */
    @Override
    public void enter() {
    }

    /**
     * Le menu est fixe : rien à mettre à jour.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void update(double dt) {
    }

    /**
     * Dessine le titre, les contrôles et l'invite à jouer.
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
        gc.fillText("NATURAL FATAL IA", Game.WIDTH / 2.0, Game.HEIGHT / 2.0 - 80);
        gc.setFont(Font.font(20));
        gc.fillText("J1 : ZQSD + ESPACE", Game.WIDTH / 2.0, Game.HEIGHT / 2.0);
        gc.fillText("J2 : FLECHES + ENTREE", Game.WIDTH / 2.0, Game.HEIGHT / 2.0 + 30);
        gc.fillText("ENTREE : JOUER", Game.WIDTH / 2.0, Game.HEIGHT / 2.0 + 80);
    }

    /**
     * Lance la partie quand le joueur appuie sur Entrée.
     *
     * @param code la touche enfoncée
     */
    @Override
    public void keyPressed(KeyCode code) {
        if (code == KeyCode.ENTER) {
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
