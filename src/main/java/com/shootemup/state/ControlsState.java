package com.shootemup.state;

import com.shootemup.core.Game;
import com.shootemup.input.Action;
import com.shootemup.input.InputController;
import com.shootemup.ui.ArcadeTheme;
import com.shootemup.ui.Starfield;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Écran des touches : chaque joueur peut changer la touche de chacune de ses actions.
 * <p>
 * Les flèches choisissent la ligne, Entrée la modifie : la touche appuyée
 * ensuite devient la nouvelle touche de l'action. Si cette touche servait déjà
 * à une autre action, de n'importe quel joueur, elle lui est retirée ; si cette
 * action n'a alors plus de touche, elle récupère les anciennes touches de
 * l'action modifiée (les deux actions échangent leurs touches). Échap annule
 * la modification en cours, ou revient à l'écran précédent (menu ou pause).
 * Échap ne peut pas être choisie, car elle sert à mettre en pause.
 * <p>
 * Les touches sont modifiées directement dans les contrôleurs gardés par
 * {@link Game} : ouvert depuis la pause, l'écran change donc les touches de la
 * partie en cours, qui les utilise dès la reprise.
 */
public class ControlsState implements GameState {

    /** Le jeu, qui garde les touches des joueurs. */
    private final Game game;

    /** L'écran à retrouver en quittant celui-ci : le menu ou la pause. */
    private final GameState previousState;

    /** Le ciel étoilé qui défile derrière l'écran, comme au menu. */
    private final Starfield background = new Starfield(Game.WIDTH, Game.HEIGHT);

    /** Les actions, dans l'ordre des lignes à l'écran. */
    private static final Action[] ACTIONS = Action.values();

    /** Couleur de chaque joueur, la même que dans le menu. */
    private static final Color[] PLAYER_COLORS = {
            ArcadeTheme.PLAYER_ONE,
            ArcadeTheme.PLAYER_TWO
    };

    /** Ordonnée de la première ligne d'actions, en pixels. */
    private static final double FIRST_ROW_Y = 176;

    /** Écart vertical entre deux lignes d'actions, en pixels. */
    private static final double ROW_SPACING = 42;

    /** Largeur d'une colonne de joueur, en pixels. */
    private static final double COLUMN_WIDTH = 360;

    /** Ordonnée du haut du panneau d'un joueur, en pixels. */
    private static final double COLUMN_TOP = 112;

    /** Hauteur du panneau d'un joueur, en pixels. */
    private static final double COLUMN_HEIGHT = 286;

    /** Index du joueur dont une ligne est sélectionnée. */
    private int selectedPlayer = 0;

    /** Index de l'action sélectionnée, dans {@link #ACTIONS}. */
    private int selectedAction = 0;

    /** Vrai quand on attend la nouvelle touche de l'action sélectionnée. */
    private boolean waitingForKey = false;

    /** Temps écoulé pour le clignotement, en secondes. */
    private double elapsed = 0;

    /**
     * Crée l'écran des touches.
     *
     * @param game          le jeu auquel appartient cet écran
     * @param previousState l'écran à retrouver avec Échap : le menu ou la pause
     */
    public ControlsState(Game game, GameState previousState) {
        this.game = game;
        this.previousState = previousState;
    }

    /**
     * Sélectionne la première ligne et annule une éventuelle modification en cours.
     */
    @Override
    public void enter() {
        selectedPlayer = 0;
        selectedAction = 0;
        waitingForKey = false;
        elapsed = 0;
    }

    /**
     * Fait défiler le fond et avancer le clignotement.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void update(double dt) {
        elapsed += dt;
        background.update(dt);
    }

    /**
     * Dessine l'écran de configuration : titre néon, une colonne encadrée
     * par joueur, lignes d'actions sélectionnables et aide en bas de l'écran.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        double centerX = Game.WIDTH / 2.0;

        gc.setFill(ArcadeTheme.VOID);
        gc.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        background.render(gc);
        ArcadeTheme.horizonGrid(gc, Game.WIDTH, Game.HEIGHT,
                Game.HEIGHT * 0.7, ArcadeTheme.CYAN, elapsed * 0.55);
        ArcadeTheme.vignette(gc, Game.WIDTH, Game.HEIGHT);

        ArcadeTheme.centered(gc, "NATURAL FATAL SOFT // SYSTEM CONFIG",
                centerX, 30, 11, ArcadeTheme.MUTED, null);
        ArcadeTheme.centered(gc, "TOUCHES", centerX + 3, 67,
                38, ArcadeTheme.MAGENTA, null);
        ArcadeTheme.glowCentered(gc, "TOUCHES", centerX, 64,
                38, ArcadeTheme.WHITE, ArcadeTheme.MAGENTA);
        ArcadeTheme.centered(gc, "CONFIGURATION DES JOUEURS",
                centerX, 94, 12, ArcadeTheme.CYAN, ArcadeTheme.VOID);

        List<InputController> controls = game.getPlayerControls();
        for (int player = 0; player < controls.size(); player++) {
            drawColumn(gc, player, controls.get(player));
        }

        if (waitingForKey) {
            ArcadeTheme.centered(gc, "APPUYEZ SUR LA NOUVELLE TOUCHE",
                    centerX, 430, 16, ArcadeTheme.AMBER, ArcadeTheme.MAGENTA);
            ArcadeTheme.centered(gc, "ECHAP // ANNULER",
                    centerX, 456, 11, ArcadeTheme.MUTED, ArcadeTheme.VOID);
        } else {
            ArcadeTheme.centered(gc,
                    "FLECHES // CHOISIR     ENTREE // MODIFIER     ECHAP // RETOUR",
                    centerX, 430, 13, ArcadeTheme.CYAN, ArcadeTheme.VOID);
            ArcadeTheme.centered(gc,
                    "UNE TOUCHE DEJA UTILISEE EST RETIREE DE SON ANCIENNE ACTION",
                    centerX, 456, 10, ArcadeTheme.MUTED, ArcadeTheme.VOID);
        }

        ArcadeTheme.centered(gc, "F11 // FULLSCREEN",
                centerX, Game.HEIGHT - 20, 9, ArcadeTheme.MUTED, null);

        ArcadeTheme.scanlines(gc, Game.WIDTH, Game.HEIGHT);
        ArcadeTheme.crtFrame(gc, Game.WIDTH, Game.HEIGHT);
    }

    /**
     * Dessine le panneau d'un joueur : son nom, puis une ligne par action
     * avec ses touches. La ligne sélectionnée est encadrée de la couleur du joueur.
     *
     * @param gc       contexte graphique du canvas sur lequel dessiner
     * @param player   index du joueur
     * @param controls touches du joueur
     */
    private void drawColumn(GraphicsContext gc, int player, InputController controls) {
        double centerX = Game.WIDTH * (2 * player + 1) / 4.0;
        double left = centerX - COLUMN_WIDTH / 2;
        double right = centerX + COLUMN_WIDTH / 2;
        Color color = PLAYER_COLORS[player % PLAYER_COLORS.length];

        ArcadeTheme.panel(gc, left - 16, COLUMN_TOP,
                COLUMN_WIDTH + 32, COLUMN_HEIGHT, color);
        ArcadeTheme.centered(gc, (player + 1) + "P", centerX,
                COLUMN_TOP + 30, 18, color, ArcadeTheme.VOID);

        gc.setStroke(ArcadeTheme.withAlpha(color, 0.35));
        gc.setLineWidth(1);
        gc.strokeLine(left, COLUMN_TOP + 48, right, COLUMN_TOP + 48);

        for (int i = 0; i < ACTIONS.length; i++) {
            double y = FIRST_ROW_Y + i * ROW_SPACING;
            boolean selected = player == selectedPlayer && i == selectedAction;

            if (selected) {
                gc.setFill(ArcadeTheme.withAlpha(color, 0.13));
                gc.fillRect(left, y - 17, COLUMN_WIDTH, 34);
                gc.setStroke(color);
                gc.setLineWidth(1.5);
                gc.strokeRect(left + 0.5, y - 16.5, COLUMN_WIDTH - 1, 33);
                ArcadeTheme.text(gc, ">", left + 10, y, 13, color, ArcadeTheme.VOID);
            }

            ArcadeTheme.text(gc, ACTIONS[i].getLabel(), left + 26, y,
                    13, selected ? ArcadeTheme.WHITE : ArcadeTheme.MUTED,
                    ArcadeTheme.VOID);

            if (selected && waitingForKey) {
                if (elapsed % 1.0 < 0.6) {
                    ArcadeTheme.rightText(gc, "?", right - 14, y, 16,
                            color, ArcadeTheme.VOID);
                }
            } else {
                ArcadeTheme.rightText(gc, describe(controls.getKeys(ACTIONS[i])),
                        right - 14, y, 13,
                        selected ? color : ArcadeTheme.WHITE, ArcadeTheme.VOID);
            }
        }
    }

    /**
     * En attente d'une touche : l'assigne à l'action sélectionnée, ou annule sur Échap.
     * Sinon : les flèches choisissent la ligne, Entrée la modifie, Échap revient à l'écran précédent.
     *
     * @param code la touche enfoncée
     */
    @Override
    public void keyPressed(KeyCode code) {
        if (waitingForKey) {
            if (code == KeyCode.UNDEFINED) {
                return;
            }
            if (code != KeyCode.ESCAPE) {
                assign(code);
            }
            waitingForKey = false;
            return;
        }

        int playerCount = game.getPlayerControls().size();
        switch (code) {
            case UP -> selectedAction = (selectedAction + ACTIONS.length - 1) % ACTIONS.length;
            case DOWN -> selectedAction = (selectedAction + 1) % ACTIONS.length;
            case LEFT -> selectedPlayer = (selectedPlayer + playerCount - 1) % playerCount;
            case RIGHT -> selectedPlayer = (selectedPlayer + 1) % playerCount;
            case ENTER -> waitingForKey = true;
            case ESCAPE -> game.setState(previousState);
            default -> {
            }
        }
    }

    /**
     * Ne fait rien sur l'écran des touches.
     *
     * @param code la touche relâchée
     */
    @Override
    public void keyReleased(KeyCode code) {
    }

    /**
     * Fait de la touche donnée la seule touche de l'action sélectionnée.
     * <p>
     * La touche est d'abord retirée de l'action qui l'utilisait, chez n'importe
     * quel joueur, pour qu'une touche ne déclenche jamais deux actions. Si cette
     * action se retrouve sans touche, elle reçoit les anciennes touches de
     * l'action sélectionnée.
     *
     * @param code la nouvelle touche
     */
    private void assign(KeyCode code) {
        InputController target = game.getPlayerControls().get(selectedPlayer);
        Action action = ACTIONS[selectedAction];
        Set<KeyCode> previous = new LinkedHashSet<>(target.getKeys(action));

        for (InputController controls : game.getPlayerControls()) {
            Action taken = controls.removeKey(code);
            if (taken != null && controls.getKeys(taken).isEmpty()) {
                controls.setKeys(taken, previous);
            }
        }
        target.setKeys(action, Set.of(code));
    }

    /**
     * Décrit les touches d'une action, par exemple « Z / W ».
     *
     * @param keys les touches à décrire
     * @return les noms des touches séparés par « / », ou « --- » s'il n'y en a aucune
     */
    static String describe(Set<KeyCode> keys) {
        if (keys.isEmpty()) {
            return "---";
        }
        List<String> names = new ArrayList<>();
        for (KeyCode key : keys) {
            names.add(keyName(key));
        }
        return String.join(" / ", names);
    }

    /**
     * Donne le nom affiché d'une touche : en français pour les plus courantes,
     * une flèche pour les touches fléchées, le nom JavaFX en majuscules sinon.
     *
     * @param key la touche
     * @return le nom de la touche, en majuscules
     */
    static String keyName(KeyCode key) {
        return switch (key) {
            case SPACE -> "ESPACE";
            case ENTER -> "ENTREE";
            case SHIFT -> "MAJ";
            case UP -> "↑";
            case DOWN -> "↓";
            case LEFT -> "←";
            case RIGHT -> "→";
            default -> key.getName().toUpperCase();
        };
    }
}
