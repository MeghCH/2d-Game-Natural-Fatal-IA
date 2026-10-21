package com.shootemup.ui;

import com.shootemup.core.Game;
import com.shootemup.entity.Character;
import com.shootemup.entity.HealthObserver;
import com.shootemup.entity.Player;
import com.shootemup.entity.Renderable;
import com.shootemup.entity.Updatable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tableau de bord du jeu (HUD) : barre de vie, points de vie et score de chaque joueur.
 * <p>
 * Implémente le pattern <b>Observer</b> : le panneau s'abonne aux joueurs qu'il
 * suit et est prévenu chaque fois que l'un d'eux perd des points de vie. Il fait
 * alors clignoter sa barre en rouge pendant un court instant. Sans cette
 * notification, le panneau ne pourrait pas savoir à quel moment un coup a été reçu.
 */
public class PlayerStatusPanel implements HealthObserver, Updatable, Renderable {

    /** Durée du clignotement rouge après un coup, en secondes. */
    private static final double FLASH_DURATION = 0.3;

    /** Largeur d'une barre de vie, en pixels. */
    private static final double BAR_WIDTH = 200;

    /** Hauteur d'une barre de vie, en pixels. */
    private static final double BAR_HEIGHT = 16;

    /** Espace entre les barres et le bord de l'écran, en pixels. */
    private static final double MARGIN = 20;

    /** Joueurs affichés par le panneau. */
    private final List<Player> players = new ArrayList<>();

    /** Personnages en train de clignoter, associés au temps de clignotement restant. */
    private final Map<Character, Double> flashTimers = new HashMap<>();

    /** Dernière valeur de points de vie connue pour chaque personnage suivi. */
    private final Map<Character, Integer> lastKnownHp = new HashMap<>();

    /**
     * Crée un panneau vide, sans joueur suivi.
     */
    public PlayerStatusPanel() {
    }

    /**
     * Affiche un joueur dans le panneau et s'abonne à ses changements de points de vie.
     *
     * @param player le joueur à suivre
     */
    public void track(Player player) {
        players.add(player);
        lastKnownHp.put(player, player.getHp());
        player.addObserver(this);
    }

    /**
     * Fait clignoter la barre du personnage seulement s'il a perdu des points de vie,
     * pas lorsqu'il est soigné.
     *
     * @param c le personnage dont les points de vie ont changé
     */
    @Override
    public void onHealthChanged(Character c) {
        int previousHp = lastKnownHp.getOrDefault(c, c.getHp());
        if (c.getHp() < previousHp) {
            flashTimers.put(c, FLASH_DURATION);
        }
        lastKnownHp.put(c, c.getHp());
    }

    /**
     * Fait avancer les clignotements en cours et retire ceux qui sont terminés.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void update(double dt) {
        flashTimers.replaceAll((character, time) -> time - dt);
        flashTimers.values().removeIf(time -> time <= 0);
    }

    /**
     * Indique si la barre d'un personnage est en train de clignoter.
     *
     * @param c le personnage à vérifier
     * @return {@code true} si le personnage a été touché il y a peu, {@code false} sinon
     */
    public boolean isFlashing(Character c) {
        return flashTimers.containsKey(c);
    }

    /**
     * Dessine, pour chaque joueur suivi, sa barre de vie, ses points de vie et son score.
     * Le joueur 1 est affiché en haut à gauche, le joueur 2 en haut à droite.
     * La barre devient rouge pendant un court instant quand le joueur est touché.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        for (Player player : players) {
            double barX = (player.getPlayerId() == 1)
                    ? MARGIN
                    : Game.WIDTH - MARGIN - BAR_WIDTH;
            double barY = 32;

            Color playerColor = (player.getPlayerId() == 1)
                    ? ArcadeTheme.PLAYER_ONE
                    : ArcadeTheme.PLAYER_TWO;

            ArcadeTheme.panel(gc, barX - 8, 12, BAR_WIDTH + 16, 64,
                    playerColor);

            ArcadeTheme.text(gc, player.getPlayerId() + "P", barX, 26,
                    11, playerColor, ArcadeTheme.VOID);

            double ratio = player.getMaxHp() == 0
                    ? 0
                    : (double) player.getHp() / player.getMaxHp();
            ArcadeTheme.segmentedBar(gc, barX, barY, BAR_WIDTH, BAR_HEIGHT,
                    ratio,
                    isFlashing(player) ? ArcadeTheme.RED : playerColor);

            String text = String.format("HP %02d/%02d   SCORE %06d",
                    player.getHp(), player.getMaxHp(), player.getScore());
            ArcadeTheme.text(gc, text, barX, 64, 11, ArcadeTheme.WHITE,
                    ArcadeTheme.VOID);
        }
    }
}