package com.shootemup.input;

import javafx.scene.input.KeyCode;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Touches de contrôle d'un joueur et état actuel de ces touches.
 * <p>
 * Chaque {@link Action} (monter, descendre, tirer...) peut être associée à
 * plusieurs touches, ce qui permet par exemple d'accepter à la fois ZQSD
 * (clavier AZERTY) et WASD (clavier QWERTY). Les touches peuvent être changées
 * en cours de route avec {@link #setKeys(Action, Set)}. Chaque joueur possède
 * son propre contrôleur, ce qui permet de jouer à deux sur le même clavier.
 * Les méthodes statiques {@link #player1()} et {@link #player2()} fournissent
 * les configurations prêtes à l'emploi, à la manière d'une petite <b>Factory</b>.
 */
public class InputController {

    /** Touches associées à chaque action, dans l'ordre où elles ont été données. */
    private final Map<Action, Set<KeyCode>> bindings = new EnumMap<>(Action.class);

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
        setKeys(Action.UP, up);
        setKeys(Action.DOWN, down);
        setKeys(Action.LEFT, left);
        setKeys(Action.RIGHT, right);
        setKeys(Action.SHOOT, shoot);
    }

    /**
     * Fournit les contrôles du joueur 1.
     *
     * @return les contrôles du joueur 1 : ZQSD ou WASD pour se déplacer, Espace pour tirer
     */
    public static InputController player1() {
        return new InputController(
                keys(KeyCode.Z, KeyCode.W),
                keys(KeyCode.S),
                keys(KeyCode.Q, KeyCode.A),
                keys(KeyCode.D),
                keys(KeyCode.SPACE));
    }

    /**
     * Fournit les contrôles du joueur 2.
     *
     * @return les contrôles du joueur 2 : flèches pour se déplacer, Entrée pour tirer
     */
    public static InputController player2() {
        return new InputController(
                keys(KeyCode.UP),
                keys(KeyCode.DOWN),
                keys(KeyCode.LEFT),
                keys(KeyCode.RIGHT),
                keys(KeyCode.ENTER));
    }

    /**
     * Regroupe des touches en gardant leur ordre, pour les afficher dans cet ordre.
     *
     * @param codes les touches
     * @return les touches, dans l'ordre donné
     */
    private static Set<KeyCode> keys(KeyCode... codes) {
        return new LinkedHashSet<>(List.of(codes));
    }

    /**
     * Indique les touches associées à une action.
     *
     * @param action l'action
     * @return les touches de cette action, en lecture seule
     */
    public Set<KeyCode> getKeys(Action action) {
        return Collections.unmodifiableSet(bindings.get(action));
    }

    /**
     * Remplace les touches associées à une action.
     *
     * @param action l'action à modifier
     * @param keys   les nouvelles touches de cette action
     */
    public void setKeys(Action action, Set<KeyCode> keys) {
        bindings.put(action, new LinkedHashSet<>(keys));
    }

    /**
     * Retire une touche de l'action qui l'utilise, s'il y en a une.
     *
     * @param code la touche à retirer
     * @return l'action qui utilisait cette touche, ou {@code null} si aucune
     */
    public Action removeKey(KeyCode code) {
        for (Map.Entry<Action, Set<KeyCode>> binding : bindings.entrySet()) {
            if (binding.getValue().remove(code)) {
                return binding.getKey();
            }
        }
        return null;
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
     * Indique si le joueur fait une action.
     *
     * @param action l'action à tester
     * @return {@code true} si une touche de cette action est enfoncée
     */
    public boolean isPressed(Action action) {
        return isAnyPressed(bindings.get(action));
    }

    /**
     * Indique si le joueur monte.
     *
     * @return {@code true} si une touche pour monter est enfoncée
     */
    public boolean isUp() {
        return isPressed(Action.UP);
    }

    /**
     * Indique si le joueur descend.
     *
     * @return {@code true} si une touche pour descendre est enfoncée
     */
    public boolean isDown() {
        return isPressed(Action.DOWN);
    }

    /**
     * Indique si le joueur va à gauche.
     *
     * @return {@code true} si une touche pour aller à gauche est enfoncée
     */
    public boolean isLeft() {
        return isPressed(Action.LEFT);
    }

    /**
     * Indique si le joueur va à droite.
     *
     * @return {@code true} si une touche pour aller à droite est enfoncée
     */
    public boolean isRight() {
        return isPressed(Action.RIGHT);
    }

    /**
     * Indique si le joueur tire.
     *
     * @return {@code true} si une touche de tir est enfoncée
     */
    public boolean isShooting() {
        return isPressed(Action.SHOOT);
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
