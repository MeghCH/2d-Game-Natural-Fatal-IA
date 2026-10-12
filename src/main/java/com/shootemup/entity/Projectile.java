package com.shootemup.entity;

import com.shootemup.core.Game;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import com.shootemup.asset.Animation;
import com.shootemup.asset.AssetManager;
import javafx.scene.image.Image;

/**
 * Balle tirée par un personnage, qui avance en ligne droite dans une direction donnée.
 * <p>
 * La direction est définie par une vitesse horizontale et une vitesse verticale,
 * ce qui permet de tirer vers le haut, en diagonale ou vers le bas avec la même classe.
 * Attention, à l'écran l'axe vertical est inversé : une vitesse verticale négative
 * fait monter la balle.
 */
public class Projectile extends Entity {

    /** Diamètre de la balle, en pixels. */
    public static final double SIZE = 16;

    /** Vitesse horizontale, en pixels par seconde : positive vers la droite. */
    private final double vx;

    /** Vitesse verticale, en pixels par seconde : négative vers le haut. */
    private final double vy;

    /** Points de dégâts infligés à la cible touchée. */
    private final int damage;

    /** Le personnage qui a tiré cette balle, pour lui attribuer les points. */
    private final Character owner;

    /** {@code true} tant que la balle est en jeu. */
    private boolean active;

        /** Animation en vol des balles du joueur 1 : ligne 0 de la planche. */
    private static final Animation P1_FLIGHT = new Animation(8, 8, 0, 2, 12);

    /** Animation en vol des balles du joueur 2 : ligne 1 de la planche. */
    private static final Animation P2_FLIGHT = new Animation(8, 8, 1, 2, 12);

    /** Le chronomètre. */
    private double animationTime = 0;

    /**
     * Crée une balle en jeu.
     *
     * @param x      position horizontale de départ
     * @param y      position verticale de départ
     * @param vx     vitesse horizontale, en pixels par seconde
     * @param vy     vitesse verticale, en pixels par seconde (négative vers le haut)
     * @param damage points de dégâts infligés à la cible
     * @param owner  le personnage qui tire
     */
    public Projectile(double x, double y, double vx, double vy, int damage, Character owner) {
        super(x, y, SIZE, SIZE);
        this.vx = vx;
        this.vy = vy;
        this.damage = damage;
        this.owner = owner;
        this.active = true;
    }

    /**
     * Fait avancer la balle selon sa vitesse.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void update(double dt) {
        x += vx * dt;
        y += vy * dt;
        animationTime += dt;
    }

    /**
     * Dessine la balle avec son animation, selon le joueur qui l'a tirée.
     * Si la planche est introuvable, dessine un petit rond jaune à la place.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        Image sheet = AssetManager.getInstance().getImage("/sprites/projectile.png");

        if (sheet == null) {
            gc.setFill(Color.YELLOW);
            gc.fillOval(x, y, width, height);
            return;
        }
        Animation animation = (owner instanceof Player player && player.getPlayerId() == 2)
                ? P2_FLIGHT
                : P1_FLIGHT;
        animation.draw(gc, sheet, animationTime, x, y, width, height);
    }

    /**
     * Indique si la balle est sortie de l'écran.
     *
     * @return {@code true} si la balle est entièrement sortie de l'écran, par n'importe quel bord
     */
    public boolean isOutOfScreen() {
        return x + width < 0 || x > Game.WIDTH || y + height < 0 || y > Game.HEIGHT;
    }

    /**
     * Retire la balle du jeu, par exemple quand elle touche une cible.
     */
    public void destroy() {
        active = false;
    }

    /**
     * Indique si la balle est encore en jeu.
     *
     * @return {@code true} si la balle est encore en jeu
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Indique les points de dégâts infligés par la balle.
     *
     * @return les points de dégâts infligés par la balle
     */
    public int getDamage() {
        return this.damage;
    }

    /**
     * Indique le personnage qui a tiré la balle.
     *
     * @return le personnage qui a tiré la balle
     */
    public Character getOwner() {
        return this.owner;
    }
}