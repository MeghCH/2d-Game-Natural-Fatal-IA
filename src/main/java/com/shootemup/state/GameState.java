package com.shootemup.state;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

/**
 * Représente un écran du jeu : partie en cours, pause, menu ou game over.
 * <p>
 * Implémente le pattern <b>State</b> : la classe {@code Game} garde une référence
 * vers l'état courant et lui délègue la logique, l'affichage et les entrées.
 * Changer d'écran revient à changer d'objet, sans aucun {@code if} sur le type
 * d'écran dans la boucle de jeu.
 */
public interface GameState {

    /**
     * Appelée une fois, au moment où cet état devient l'état courant.
     * Sert à préparer ou réinitialiser l'écran.
     */
    void enter();

    /**
     * Met à jour la logique de l'écran.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    void update(double dt);

    /**
     * Dessine l'écran.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    void render(GraphicsContext gc);

    /**
     * Réagit à l'appui sur une touche.
     *
     * @param code la touche enfoncée
     */
    void keyPressed(KeyCode code);

    /**
     * Réagit au relâchement d'une touche.
     *
     * @param code la touche relâchée
     */
    void keyReleased(KeyCode code);
}