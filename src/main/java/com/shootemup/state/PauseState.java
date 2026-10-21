package com.shootemup.state;

import com.shootemup.core.Game;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Écran de pause, affiché par-dessus la partie en cours.
 * <p>
 * État du pattern <b>State</b> : tant qu'il est actif, la partie n'est plus
 * mise à jour, donc le jeu est figé. Échap rend la main à l'état précédent,
 * qui reprend exactement là où il s'était arrêté. T ouvre l'écran des touches :
 * les touches changées sont gardées en mémoire et servent dès la reprise.
 */
public class PauseState implements GameState {

    /** Le jeu, utilisé pour revenir à la partie. */
    private final Game game;

    /** L'écran à reprendre à la fin de la pause. */
    private final GameState previousState;

    /**
     * Crée l'écran de pause.
     *
     * @param game          le jeu auquel appartient cet écran
     * @param previousState l'écran mis en pause, à reprendre ensuite
     */
    public PauseState(Game game, GameState previousState) {
        this.game = game;
        this.previousState = previousState;
    }

    /** Aucune préparation nécessaire. */
    @Override
    public void enter() {
    }

    /**
     * Ne fait rien : c'est ce qui fige la partie pendant la pause.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void update(double dt) {
    }

    /**
     * Dessine la partie figée, un voile sombre par-dessus, puis le texte « PAUSE »
     * et les touches possibles.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        previousState.render(gc);

        gc.setFill(Color.rgb(0, 0, 0, 0.6));
        gc.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(48));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("PAUSE", Game.WIDTH / 2.0, Game.HEIGHT / 2.0);

        gc.setFont(Font.font(20));
        gc.fillText("ECHAP : REPRENDRE   -   T : TOUCHES", Game.WIDTH / 2.0, Game.HEIGHT / 2.0 + 60);
    }

    /**
     * Reprend la partie quand le joueur appuie sur Échap, ouvre l'écran des touches sur T.
     *
     * @param code la touche enfoncée
     */
    @Override
    public void keyPressed(KeyCode code) {
        if (code == KeyCode.ESCAPE) {
            game.setState(previousState);
        } else if (code == KeyCode.T) {
            game.setState(new ControlsState(game, this));
        }
    }

    /**
     * Ne fait rien pendant la pause.
     *
     * @param code la touche relâchée
     */
    @Override
    public void keyReleased(KeyCode code) {
    }
}