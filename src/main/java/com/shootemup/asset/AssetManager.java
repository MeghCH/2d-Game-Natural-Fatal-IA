package com.shootemup.asset;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Point d'accès unique aux images du jeu, chargées une seule fois.
 * <p>
 * Implémente le pattern <b>Singleton</b> : il n'existe qu'un seul gestionnaire
 * dans tout le jeu, obtenu avec {@link #getInstance()}. Charger une image depuis
 * le disque est lent ; le gestionnaire garde donc chaque image en mémoire après
 * son premier chargement, et la redonne directement les fois suivantes.
 * Plusieurs gestionnaires feraient charger les mêmes images plusieurs fois,
 * d'où l'intérêt d'un exemplaire unique.
 * <p>
 * Les images sont cherchées dans {@code src/main/resources}, avec un chemin
 * commençant par {@code /}, par exemple {@code "/sprites/player1.png"}.
 */
public final class AssetManager {

    /** L'unique exemplaire du gestionnaire, créé au premier appel de {@link #getInstance()}. */
    private static AssetManager instance;

    /** Images déjà chargées, associées à leur chemin. */
    private final Map<String, Image> images = new HashMap<>();

    /**
     * Constructeur privé : aucune autre classe ne peut créer de gestionnaire.
     * Il faut passer par {@link #getInstance()}.
     */
    private AssetManager() {
    }

    /**
     * Renvoie l'unique gestionnaire d'images, en le créant au premier appel.
     *
     * @return le gestionnaire d'images
     */
    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    /**
     * Renvoie l'image située au chemin donné, en la chargeant au premier appel.
     * <p>
     * Si l'image est introuvable, renvoie {@code null} : l'appelant peut alors
     * dessiner une forme simple à la place, sans faire planter le jeu.
     *
     * @param path chemin de l'image dans les ressources, par exemple {@code "/sprites/player1.png"}
     * @return l'image chargée, ou {@code null} si elle est introuvable
     */
    public Image getImage(String path) {
        if (images.containsKey(path)) {
            return images.get(path);
        }

        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            return null;
        }

        Image image = new Image(stream);
        images.put(path, image);
        return image;
    }
}