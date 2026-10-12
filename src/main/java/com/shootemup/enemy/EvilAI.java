package com.shootemup.enemy;

import com.shootemup.asset.Animation;
import com.shootemup.asset.AssetManager;
import com.shootemup.enemy.boss.BossPhase;
import com.shootemup.enemy.boss.CalmPhase;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Boss final du niveau 4 : l'IA maléfique.
 * <p>
 * Change de phase quand ses points de vie passent sous les deux tiers
 * puis sous le tiers de son maximum. Un boss mort ne bouge plus.
 */
public class EvilAI extends Enemy {

    /** Taille du boss, en pixels. */
    public static final double SIZE = 80;

    /** Points de vie de départ, valeur provisoire à figer dans le GDD. */
    private static final int MAX_HP = 100;

    /** Points de dégâts infligés au joueur touché, valeur provisoire. */
    private static final int DAMAGE = 10;

    /** Points de score donnés quand le boss est détruit. */
    private static final int SCORE = 1000;

    /** Phase de comportement actuelle du boss. */
    private BossPhase phase = new CalmPhase();

    /** Animation de la phase calme : ligne 0 de la planche 128x128, 2 images de 64x64. */
    private static final Animation CALM = new Animation(64, 64, 0, 2, 4);

    /** Animation des phases énervées : ligne 1 de la planche, 2 images de 64x64. */
    private static final Animation ENRAGED = new Animation(64, 64, 1, 2, 6);

    /** Sens du balayage horizontal : 1 vers la droite, -1 vers la gauche. */
    private int direction = 1;

    /** Nombre de seuils de points de vie déjà franchis : 0, 1 ou 2. */
    private int thresholdsCrossed = 0;

    /**
     * Crée le boss en pleine santé, dans sa première phase.
     *
     * @param x position horizontale de départ
     * @param y position verticale de départ
     */
    public EvilAI(double x, double y) {
        super(x, y, SIZE, SIZE, MAX_HP, DAMAGE, SCORE,
                (e, dt) -> {
                });
    }

    /**
     * Exécute la phase actuelle puis change de phase quand les points de vie
     * franchissent un nouveau seuil. Chaque seuil ne fait changer de phase
     * qu'une seule fois. Un boss mort ne bouge plus.
     *
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    @Override
    public void update(double dt) {
        if (!isAlive()) {
            return;
        }
        animationTime += dt;
        phase.execute(this, dt);

        int reached = 0;
        if (hp < maxHp / 3) {
            reached = 2;
        } else if (hp < 2 * maxHp / 3) {
            reached = 1;
        }
        while (thresholdsCrossed < reached) {
            changePhase();
            thresholdsCrossed++;
        }
    }

    /**
     * Passe à la phase suivante. Appelée quand les points de vie passent
     * sous un seuil, sans suite de {@code if} dans la boucle de jeu.
     */
    public void changePhase() {
        phase = phase.next();
    }

    /**
     * Indique la phase actuelle du boss.
     *
     * @return la phase actuelle du boss
     */
    public BossPhase getPhase() {
        return phase;
    }

    /**
     * Indique le sens du balayage horizontal.
     *
     * @return 1 vers la droite, -1 vers la gauche
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Inverse le sens du balayage, quand le boss touche un bord de l'écran.
     */
    public void reverseDirection() {
        direction = -direction;
    }

    /**
     * Dessine le boss avec son sprite : ligne 0 au repos, ligne 1 quand il
     * est énervé. Si la planche est introuvable, dessine un carré rose à la
     * place. Un boss mort n'est pas dessiné.
     *
     * @param gc contexte graphique du canvas sur lequel dessiner
     */
    @Override
    public void render(GraphicsContext gc) {
        if (!isAlive()) {
            return;
        }
        Image sheet = AssetManager.getInstance().getImage("/sprites/boss.png");
        if (sheet == null) {
            gc.setFill(Color.web("#E24B7A"));
            gc.fillRect(x, y, width, height);
            return;
        }
        Animation animation = (phase instanceof CalmPhase) ? CALM : ENRAGED;
        animation.draw(gc, sheet, animationTime, x, y, width, height);
    }
}
