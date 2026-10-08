package com.shootemup.enemy;

/**
 * Fabrique centralisée des ennemis.
 * <p>
 * Les niveaux ne connaissent que {@link EnemyType} : pour ajouter un ennemi,
 * on ajoute une valeur à l'enum et une ligne dans la fabrique.
 */
public class EnemyFactory {

    /**
     * Crée une fabrique d'ennemis.
     */
    public EnemyFactory() {
    }

    /**
     * Crée un ennemi prêt à jouer.
     *
     * @param type le type d'ennemi
     * @param x position horizontale de départ
     * @param y position verticale de départ
     * @return l'ennemi créé
     */
    public Enemy create(EnemyType type, double x, double y) {
        return switch (type) {
            case MOSQUITO -> new Mosquito(x, y);
            case WASP -> new Wasp(x, y);
            case CROW -> new Crow(x, y);
            case EVIL_AI -> new EvilAI(x, y);
        };
    }
}
