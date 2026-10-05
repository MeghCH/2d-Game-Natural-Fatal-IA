package com.shootemup.entity;
import javafx.scene.canvas.GraphicsContext;

/**
 * Représente un élément du jeu capable de se dessiner à l'écran.
 * <p>
 * Toute entité qui implémente cette interface (joueur, ennemi, projectile)
 * est dessinée à chaque image par la boucle de jeu, après sa mise à jour.
*/
public interface Renderable {
    /**
     * Dessine l'élément à sa position actuelle.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
    */
    void render(GraphicsContext gc);
}




