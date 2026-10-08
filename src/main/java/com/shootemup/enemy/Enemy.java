package com.shootemup.enemy;

import com.shootemup.entity.Character;
import com.shootemup.entity.Player;
import com.shootemup.movement.MovementStrategy;
import java.util.Objects;

/**
 * Ennemi du jeu : un personnage qui descend vers les joueurs.
 * <p>
 * Le déplacement est confié à une {@link MovementStrategy} interchangeable,
 * ce qui permet de changer le comportement en cours de partie sans créer
 * de sous-classe. Un ennemi mort ne bouge plus.
 */
public abstract class Enemy extends Character {

    /** Déplacement actuel de l'ennemi. */
    protected MovementStrategy movement;

    /** Points de dégâts infligés au joueur touché. */
    protected final int contactDamage;

    /** Points de score donnés au joueur qui détruit cet ennemi. */
    protected final int scoreValue;

    /**
     * Crée un ennemi en pleine santé.
     *
     * @param x position horizontale de départ
     * @param y position verticale de départ
     * @param width largeur de l'ennemi
     * @param height hauteur de l'ennemi
     * @param maxHp points de vie maximum, aussi les points de vie de départ
     * @param contactDamage points de dégâts infligés au joueur touché
     * @param scoreValue points de score donnés quand l'ennemi est détruit
     * @param movement déplacement initial de l'ennemi
     */
    protected Enemy(double x, double y, double width, double height, int maxHp,
                    int contactDamage, int scoreValue, MovementStrategy movement) {
        super(x, y, width, height, maxHp);
        this.contactDamage = contactDamage;
        this.scoreValue = scoreValue;
        this.movement = Objects.requireNonNull(movement, "Le déplacement ne peut pas être null.");
    }

    /**
     * Déplace l'ennemi selon son déplacement actuel. Un ennemi mort ne bouge plus.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void update(double dt) {
        if (!isAlive()) {
            return;
        }
        movement.move(this, dt);
    }

    /**
     * Inflige ses dégâts au joueur touché.
     *
     * @param target le joueur touché
     */
    public void attack(Player target) {
        target.takeDamage(contactDamage);
    }

    /**
     * Indique les points de dégâts infligés au joueur touché.
     *
     * @return les points de dégâts infligés au joueur touché
     */
    public int getContactDamage() {
        return contactDamage;
    }

    /**
     * Indique les points de score donnés quand l'ennemi est détruit.
     *
     * @return les points de score donnés quand l'ennemi est détruit
     */
    public int getScoreValue() {
        return scoreValue;
    }

    /**
     * Change le déplacement de l'ennemi en cours de partie.
     *
     * @param movement le nouveau déplacement
     */
    public void setMovement(MovementStrategy movement) {
        this.movement = Objects.requireNonNull(movement, "Le déplacement ne peut pas être null.");
    }

    /**
     * Indique le déplacement actuel de l'ennemi.
     *
     * @return le déplacement actuel de l'ennemi
     */
    public MovementStrategy getMovement() {
        return movement;
    }
}
