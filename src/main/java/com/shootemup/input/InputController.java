package com.shootemup.input;

import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

/**
 * Touches de contrôle d'un joueur et état actuel de ces touches.
 * <p>
 * Chaque action (monter, descendre, tirer...) peut être associée à plusieurs
 * touches, ce qui permet par exemple d'accepter à la fois ZQSD (clavier AZERTY)
 * et WASD (clavier QWERTY). Chaque joueur possède son propre contrôleur, ce qui
 * permet de jouer à deux sur le même clavier. Les méthodes statiques
 * {@link #player1()} et {@link #player2()} fournissent les configurations
 * prêtes à l'emploi, à la manière d'une petite <b>Factory</b>.
 */
public class InputController {

    /** Touches pour monter. */
    private final Set<KeyCode> up;

    /** Touches pour descendre. */
    private final Set<KeyCode> down;

    /** Touches pour aller à gauche. */
    private final Set<KeyCode> left;

    /** Touches pour aller à droite. */
    private final Set<KeyCode> right;

    /** Touches pour tirer. */
    private final Set<KeyCode> shoot;

    /** Touches actuellement enfoncées. */
    private final Set<KeyCode> pressed = new HashSet<>();

    /**
     * Crée un contrôleur avec les touches données pour chaque action.
     *
     * @param up    touches pour monter
     * @param down  touches pour descendre
     * @param left  touches pour aller à gauche
     * @param right touches pour aller à droite
     * @param shoot touches pour tirer
     */
    public InputController(Set<KeyCode> up, Set<KeyCode> down, Set<KeyCode> left,
                           Set<KeyCode> right, Set<KeyCode> shoot) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.shoot = shoot;
    }

    /**
     * Fournit les contrôles du joueur 1.
     *
     * @return les contrôles du joueur 1 : ZQSD ou WASD pour se déplacer, Espace pour tirer
     */
    public static InputController player1() {
        return new InputController(
                Set.of(KeyCode.Z, KeyCode.W),
                Set.of(KeyCode.S),
                Set.of(KeyCode.Q, KeyCode.A),
                Set.of(KeyCode.D),
                Set.of(KeyCode.SPACE));
    }

    /**
     * Fournit les contrôles du joueur 2.
     *
     * @return les contrôles du joueur 2 : flèches pour se déplacer, Entrée pour tirer
     */
    public static InputController player2() {
        return new InputController(
                Set.of(KeyCode.UP),
                Set.of(KeyCode.DOWN),
                Set.of(KeyCode.LEFT),
                Set.of(KeyCode.RIGHT),
                Set.of(KeyCode.ENTER));
    }

    /**
     * Mémorise une touche enfoncée.
     *
     * @param code la touche enfoncée
     */
    public void keyPressed(KeyCode code) {
        pressed.add(code);
    }

    /**
     * Oublie une touche relâchée.
     *
     * @param code la touche relâchée
     */
    public void keyReleased(KeyCode code) {
        pressed.remove(code);
    }

    /**
     * Indique si le joueur monte.
     *
     * @return {@code true} si une touche pour monter est enfoncée
     */
    public boolean isUp() {
        return isAnyPressed(up);
    }

    /**
     * Indique si le joueur descend.
     *
     * @return {@code true} si une touche pour descendre est enfoncée
     */
    public boolean isDown() {
        return isAnyPressed(down);
    }

    /**
     * Indique si le joueur va à gauche.
     *
     * @return {@code true} si une touche pour aller à gauche est enfoncée
     */
    public boolean isLeft() {
        return isAnyPressed(left);
    }

    /**
     * Indique si le joueur va à droite.
     *
     * @return {@code true} si une touche pour aller à droite est enfoncée
     */
    public boolean isRight() {
        return isAnyPressed(right);
    }

    /**
     * Indique si le joueur tire.
     *
     * @return {@code true} si une touche de tir est enfoncée
     */
    public boolean isShooting() {
        return isAnyPressed(shoot);
    }

    /**
     * Oublie toutes les touches enfoncées, par exemple au retour de pause.
     */
    public void clear() {
        pressed.clear();
    }

    /**
     * Indique si au moins une des touches données est enfoncée.
     *
     * @param keys les touches associées à une action
     * @return {@code true} si l'une d'elles est enfoncée
     */
    private boolean isAnyPressed(Set<KeyCode> keys) {
        for (KeyCode key : keys) {
            if (pressed.contains(key)) {
                return true;
            }
        }
        return false;
    }
}