package com.shootemup.entity;

/**
 * Représente tout objet du jeu visible à l'écran.
 * <p>
 * Une entité possède une position et une taille, et sait se mettre à jour
 * et se dessiner. Les classes filles (joueur, ennemis, projectiles)
 * définissent leur propre comportement dans {@link #update(double)}
 * et leur propre apparence dans {@link #render(javafx.scene.canvas.GraphicsContext)}.
 */
public abstract class Entity implements Updatable, Renderable {
    /** Position horizontale du coin supérieur gauche, en pixels. */
    protected double x;
    /** Position verticale du coin supérieur gauche, en pixels. */
    protected double y;
    /** Largeur de l'entité, en pixels. */
    protected double width;
    /** Hauteur de l'entité, en pixels. */
    protected double height;

    /**
     * Crée une entité à la position et à la taille données.
     *
     * @param x      position horizontale du coin supérieur gauche
     * @param y      position verticale du coin supérieur gauche
     * @param width  largeur de l'entité
     * @param height hauteur de l'entité
     */
    protected Entity(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * @return la position horizontale du coin supérieur gauche
     */
    public double getX() {
        return this.x;
    }

    /**
     * @return la position verticale du coin supérieur gauche
     */
    public double getY() {
        return this.y;
    }

    /**
     * @return la largeur de l'entité
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * @return la hauteur de l'entité
     */
    public double getHeight() {
        return this.height;
    }

    /**
     * Modifie la position horizontale de l'entité.
     *
     * @param x nouvelle position horizontale du coin supérieur gauche
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Modifie la position verticale de l'entité.
     *
     * @param y nouvelle position verticale du coin supérieur gauche
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Indique si cette entité touche une autre entité.
     * <p>
     * Les deux entités sont assimilées à des rectangles alignés sur les axes
     * (détection AABB) : elles se touchent si leurs rectangles se chevauchent.
     *
     * @param other l'autre entité à tester
     * @return {@code true} si les deux entités se chevauchent, {@code false} sinon
     */
    public boolean collidesWith(Entity other) {
        return (this.x < other.x + other.width) &&
           (this.x + this.width > other.x) &&
           (this.y < other.y + other.height) &&
           (this.y + this.height > other.y);
    }
}