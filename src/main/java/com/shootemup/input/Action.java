package com.shootemup.input;

/**
 * Les actions qu'un joueur peut faire au clavier.
 * <p>
 * Chaque action est associée à une ou plusieurs touches dans un
 * {@link InputController}, et peut être réassignée depuis l'écran des touches.
 */
public enum Action {

    /** Monter. */
    UP("MONTER"),

    /** Descendre. */
    DOWN("DESCENDRE"),

    /** Aller à gauche. */
    LEFT("GAUCHE"),

    /** Aller à droite. */
    RIGHT("DROITE"),

    /** Tirer. */
    SHOOT("TIRER");

    /** Nom de l'action affiché à l'écran. */
    private final String label;

    /**
     * Crée une action.
     *
     * @param label nom de l'action affiché à l'écran
     */
    Action(String label) {
        this.label = label;
    }

    /**
     * Indique le nom de l'action affiché à l'écran.
     *
     * @return le nom de l'action, en majuscules
     */
    public String getLabel() {
        return label;
    }
}
