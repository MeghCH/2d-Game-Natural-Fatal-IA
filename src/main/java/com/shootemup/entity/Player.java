package com.shootemup.entity;

import com.shootemup.core.Game;
import com.shootemup.input.InputController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

/**
 * Personnage contrôlé par un joueur humain.
 * <p>
 * Chaque joueur possède son propre {@link InputController}, ce qui permet
 * de jouer à deux sur le même clavier. Le joueur se déplace dans les quatre
 * directions sans pouvoir sortir de l'écran, et cesse de bouger et de
 * s'afficher lorsqu'il n'a plus de points de vie.
 */
public class Player extends Character {

    /** Taille du joueur, en pixels. */
    public static final double SIZE = 40;

    /** Vitesse de déplacement, en pixels par seconde. */
    private static final double SPEED = 300;

    /** Points de vie de départ : 15 piqûres de moustique, 5 de guêpe ou 3 coups de corbeau. */
    private static final int MAX_HP = 30;

    /** Numéro du joueur : 1 ou 2. */
    private final int playerId;

    /** Touches de contrôle du joueur. */
    private final InputController controls;

    /** Score du joueur, augmenté à chaque ennemi abattu. */
    private int score = 0;

    /** Arme du joueur, qui gère la cadence de tir. */
    private final Weapon weapon = new Weapon();

    /**
     * Crée un joueur en pleine santé.
     *
     * @param playerId numéro du joueur, 1 ou 2, qui détermine sa couleur
     * @param x        position horizontale de départ
     * @param y        position verticale de départ
     * @param controls touches de contrôle du joueur
     */
    public Player(int playerId, double x, double y, InputController controls) {
        super(x, y, SIZE, SIZE, MAX_HP);
        this.playerId = playerId;
        this.controls = controls;
    }

    /**
     * Déplace le joueur selon ses touches enfoncées, sans le laisser sortir de l'écran.
     * Un joueur mort ne bouge plus.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void update(double dt) {
        if (!isAlive()) {
            return;
        }

        weapon.update(dt);

        if (controls.isUp()) {
            this.y -= SPEED * dt;
        }
        if (controls.isDown()) {
            this.y += SPEED * dt;
        }
        if (controls.isLeft()) {
            this.x -= SPEED * dt;
        }
        if (controls.isRight()) {
            this.x += SPEED * dt;
        }

        if (this.x < 0) {
            this.x = 0;
        } else if (this.x > Game.WIDTH - this.width) {
            this.x = Game.WIDTH - this.width;
        }

        if (this.y < 0) {
            this.y = 0;
        } else if (this.y > Game.HEIGHT - this.height) {
            this.y = Game.HEIGHT - this.height;
        }
    }

    /**
     * Dessine le joueur avec sa couleur : turquoise pour le joueur 1, corail pour le joueur 2.
     * Un joueur mort n'est pas dessiné.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        if (!isAlive()) {
            return;
        }

        Color playerColor = (this.playerId == 1) ? Color.web("#5DCAA5") : Color.web("#F0997B");
        gc.setFill(playerColor);
        gc.fillRect(this.x, this.y, this.width, this.height);
    }

    /**
     * @return le numéro du joueur, 1 ou 2
     */
    public int getPlayerId() {
        return this.playerId;
    }

    /**
     * @return les touches de contrôle du joueur
     */
    public InputController getControls() {
        return this.controls;
    }

    /**
     * @return le score actuel du joueur
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Ajoute des points au score du joueur.
     *
     * @param points nombre de points gagnés
     */
    public void addScore(int points) {
        score += points;
    }

    /**
     * Tire si la touche de tir est enfoncée et que l'arme est prête.
     * Un joueur mort ne tire pas.
     *
     * @return les balles tirées, ou une liste vide si le joueur ne tire pas
     */
    public List<Projectile> shoot() {
        if (!isAlive() || !controls.isShooting()) {
            return List.of();
        }
        return weapon.fire(this);
    }
}