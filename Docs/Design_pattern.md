# Design Patterns - Shoot'em up

Ce document présente les patrons de conception utilisés dans le jeu. Pour chacun : le problème qu'il résout dans notre jeu, la solution choisie, les classes concernées et un extrait de code Java.

Les classes citées correspondent aux diagrammes du dossier `Diagrammes/`.

## Récapitulatif

| Pattern         | Famille      | Classes                     | Problème résolu                                          |
| --------------- | ------------ | --------------------------- | -------------------------------------------------------- |
| State           | Comportement | `GameState`, `BossPhase`    | changer d'écran et de phase de boss sans `if` géants     |
| Factory         | Création     | `EnemyFactory`, `EnemyType` | créer les ennemis à un seul endroit                      |
| Strategy        | Comportement | `MovementStrategy`          | varier les déplacements sans multiplier les sous-classes |
| Observer        | Comportement | `HealthObserver`, `Hud`     | mettre à jour l'affichage quand les PV changent          |
| Template Method | Comportement | `Level`                     | partager la logique commune des niveaux                  |
| Singleton       | Création     | `AssetManager`              | charger les images et sons une seule fois                |

---

## 1. State - les écrans du jeu et les phases du boss

### Problème

Le jeu passe par plusieurs écrans : menu, partie, pause, game over. Sans pattern, la boucle de jeu deviendrait une suite de `if (state == MENU) ... else if (state == PAUSE) ...` répétée dans `update`, `render` et la gestion des touches. Ajouter un écran obligerait à modifier tous ces blocs.

### Solution

Chaque écran est une classe qui implémente `GameState`. `Game` garde une référence vers l'état courant et lui délègue tout. Changer d'écran revient à changer d'objet.

```java
/**
 * Représente un écran du jeu (menu, partie, pause, game over).
 */
public interface GameState {

    /** Appelée une fois quand l'état devient actif. */
    void enter();

    /**
     * Met à jour la logique de l'écran.
     * @param dt temps écoulé depuis la dernière image, en secondes
     */
    void update(double dt);

    /**
     * Dessine l'écran.
     * @param gc contexte graphique JavaFX
     */
    void render(GraphicsContext gc);

    /**
     * Réagit à une touche pressée.
     * @param e l'événement clavier
     */
    void handleInput(KeyCode e);
}
```

```java
public class Game {

    private GameState currentState;

    /**
     * Remplace l'écran courant.
     * @param newState le nouvel écran à afficher
     */
    public void setState(GameState newState) {
        this.currentState = newState;
        newState.enter();
    }

    public void update(double dt) {
        currentState.update(dt);
    }

    public void render(GraphicsContext gc) {
        currentState.render(gc);
    }
}
```

```java
public class PauseState implements GameState {

    private final Game game;
    private final GameState previous;

    public PauseState(Game game, GameState previous) {
        this.game = game;
        this.previous = previous;
    }

    @Override
    public void handleInput(KeyCode e) {
        if (e.getCode() == KeyCode.ESCAPE) {
            game.setState(previous);   // retour à la partie
        }
    }

    // enter(), update() et render() : affichage du menu pause
}
```

### Deuxième utilisation : les phases de l'IA maléfique

Le boss change de comportement selon ses points de vie. Chaque phase est un état.

```java
public interface BossPhase {

    /**
     * Exécute le comportement de la phase.
     * @param boss le boss contrôlé
     * @param dt temps écoulé en secondes
     */
    void execute(EvilAI boss, double dt);

    /** @return la phase suivante, ou cette phase si c'est la dernière */
    BossPhase next();
}
```

```java
public class EvilAI extends Enemy {

    private BossPhase phase = new CalmPhase();

    @Override
    public void update(double dt) {
        phase.execute(this, dt);
        if (hp < maxHp / 3 || (hp < 2 * maxHp / 3 && phase instanceof CalmPhase)) {
            phase = phase.next();
        }
    }
}
```

Exemple de phases : `CalmPhase` (tir lent), `AngryPhase` (tir rapide), `BerserkPhase` (invoque des moustiques).

### Avantage

Ajouter un écran (options, sélection du nombre de joueurs) ou une phase de boss se fait en créant une classe, sans toucher au code existant : c'est le principe **ouvert/fermé**.

---

## 2. Factory - la création des ennemis

### Problème

Les vagues doivent créer des moustiques, des guêpes, des corbeaux et le boss. Si chaque niveau appelait directement `new Mosquito(...)`, `new Wasp(...)`, la configuration des ennemis (mouvement, position) serait dupliquée partout.

### Solution

Une seule classe sait construire chaque ennemi à partir de son type.

```java
/** Types d'ennemis disponibles dans le jeu. */
public enum EnemyType {
    MOSQUITO, WASP, CROW, EVIL_AI
}
```

```java
/**
 * Fabrique centralisée des ennemis.
 */
public class EnemyFactory {

    /**
     * Crée un ennemi prêt à jouer.
     * @param type le type d'ennemi
     * @param x position horizontale de départ
     * @param y position verticale de départ
     * @return l'ennemi créé
     */
    public Enemy create(EnemyType type, double x, double y) {
        return switch (type) {
            case MOSQUITO -> new Mosquito(x, y, new ZigzagMovement());
            case WASP     -> new Wasp(x, y, new StraightMovement());
            case CROW     -> new Crow(x, y, new DiveMovement());
            case EVIL_AI  -> new EvilAI(x, y);
        };
    }
}
```

### Avantage

Les niveaux ne connaissent que `EnemyType`. Pour ajouter un ennemi, on ajoute une valeur à l'enum et une ligne dans la fabrique. Le compilateur signale le `switch` incomplet si on oublie.

---

## 3. Strategy - les déplacements des ennemis

### Problème

Le moustique vole en zigzag, la guêpe fonce en ligne droite, le corbeau pique sur le joueur. Créer des sous-classes comme `ZigzagMosquito` ou `DivingWasp` ferait exploser le nombre de classes.

### Solution

Le déplacement est sorti de l'ennemi et confié à un objet interchangeable.

```java
/**
 * Algorithme de déplacement d'une entité.
 */
public interface MovementStrategy {

    /**
     * Déplace l'entité.
     * @param e l'entité à déplacer
     * @param dt temps écoulé en secondes
     */
    void move(Entity e, double dt);
}
```

```java
public class ZigzagMovement implements MovementStrategy {

    private static final double SPEED = 120;
    private static final double AMPLITUDE = 80;
    private static final double FREQUENCY = 0.05;

    @Override
    public void move(Entity e, double dt) {
        e.setX(e.getX() - SPEED * dt);
        e.setY(e.getY() + Math.cos(e.getX() * FREQUENCY) * AMPLITUDE * dt);
    }
}
```

```java
public abstract class Enemy extends Character {

    protected MovementStrategy movement;

    /**
     * Change le déplacement de l'ennemi en cours de partie.
     * @param movement le nouveau déplacement
     */
    public void setMovement(MovementStrategy movement) {
        this.movement = movement;
    }

    @Override
    public void update(double dt) {
        movement.move(this, dt);
    }
}
```

### Avantage

Les déplacements se combinent librement avec les ennemis et peuvent changer pendant la partie (une guêpe blessée qui passe en zigzag). Comme `ZigzagMovement` ne garde aucun état propre, une même instance peut être partagée par plusieurs ennemis.

---

## 4. Observer - l'affichage des points de vie

### Problème

Quand un joueur perd des PV, plusieurs éléments doivent réagir : la barre de vie du HUD, un son, peut-être un clignotement. Si `Player` appelait directement `hud.updateBar()`, la logique du jeu dépendrait de l'interface graphique.

### Solution

Les personnages tiennent une liste d'observateurs qu'ils préviennent à chaque changement, sans savoir qui ils sont.

```java
/**
 * Objet à prévenir quand les PV d'un personnage changent.
 */
public interface HealthObserver {

    /**
     * @param c le personnage dont les PV ont changé
     */
    void onHealthChanged(Character c);
}
```

```java
public abstract class Character extends Entity implements Damageable {

    protected int hp;
    protected int maxHp;
    private final List<HealthObserver> observers = new ArrayList<>();

    public void addObserver(HealthObserver o) {
        observers.add(o);
    }

    @Override
    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
        notifyObservers();
    }

    protected void notifyObservers() {
        for (HealthObserver o : observers) {
            o.onHealthChanged(this);
        }
    }
}
```

```java
public class Hud implements HealthObserver {

    @Override
    public void onHealthChanged(Character c) {
        // met à jour la barre de vie du joueur concerné
    }
}
```

### Avantage

La logique du jeu ne dépend plus de l'affichage. On peut ajouter un `SoundManager` observateur sans modifier `Character`, et tester `takeDamage` sans interface graphique.

---

## 5. Template Method - le déroulement des niveaux

### Problème

Les quatre niveaux fonctionnent pareil : faire apparaître des vagues, mettre à jour les ennemis, retirer les morts, détecter la fin. Seul le contenu des vagues change. Copier cette logique dans chaque niveau créerait du code dupliqué.

### Solution

La classe abstraite `Level` fixe le déroulement ; chaque niveau ne redéfinit que l'étape qui varie.

```java
/**
 * Niveau du jeu. Le déroulement est commun, seules les vagues changent.
 */
public abstract class Level {

    protected final Queue<Wave> waves = new LinkedList<>();
    protected final List<Enemy> activeEnemies = new ArrayList<>();
    private final EnemyFactory factory;

    protected Level(EnemyFactory factory) {
        this.factory = factory;
    }

    /** Prépare le niveau. Appelée au lancement. */
    public final void start() {
        waves.clear();
        activeEnemies.clear();
        buildWaves();
    }

    /** Méthode modèle : l'ordre des étapes est fixe. */
    public final void update(double dt) {
        spawnNextWaveIfReady();
        activeEnemies.forEach(e -> e.update(dt));
        activeEnemies.removeIf(e -> !e.isAlive());
    }

    /** Étape propre à chaque niveau : définir ses vagues. */
    protected abstract void buildWaves();

    public boolean isCompleted() {
        return waves.isEmpty() && activeEnemies.isEmpty();
    }

    private void spawnNextWaveIfReady() {
        if (activeEnemies.isEmpty() && !waves.isEmpty()) {
            activeEnemies.addAll(waves.poll().spawn(factory));
        }
    }
}
```

```java
public class TutorialLevel extends Level {

    public TutorialLevel(EnemyFactory factory) {
        super(factory);
    }

    @Override
    protected void buildWaves() {
        waves.add(new Wave(EnemyType.MOSQUITO, 3));
        waves.add(new Wave(EnemyType.MOSQUITO, 6));
        waves.add(new Wave(EnemyType.MOSQUITO, 10));
    }
}
```

### Avantage

`update` est `final` : aucun niveau ne peut casser le déroulement. Créer un niveau se résume à décrire ses vagues.

---

## 6. Singleton - le chargement des ressources

### Problème

Les images et sons doivent être chargés une seule fois. Si chaque moustique chargeait son image, le jeu ralentirait et consommerait beaucoup de mémoire.

### Solution

Une classe unique, accessible partout, garde les ressources en cache.

```java
/**
 * Point d'accès unique aux images du jeu, chargées une seule fois.
 */
public final class AssetManager {

    private static AssetManager instance;
    private final Map<String, Image> images = new HashMap<>();

    private AssetManager() { }

    /** @return l'unique instance du gestionnaire */
    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    /**
     * Renvoie une image, en la chargeant au premier appel.
     * @param path chemin de la ressource, par exemple "/sprites/mosquito.png"
     * @return l'image chargée
     */
    public Image getImage(String path) {
        return images.computeIfAbsent(path,
                p -> new Image(getClass().getResourceAsStream(p)));
    }
}
```

### Limites assumées

Le Singleton crée un état global, ce qui rend les tests plus difficiles. Nous le limitons donc aux ressources (images, sons), qui sont réellement uniques, et ne l'utilisons pas pour la logique du jeu (joueurs, niveaux), qui passe par des objets transmis explicitement.

---

## Ajout au diagramme de structure

Pour que le Singleton apparaisse dans les diagrammes, ajouter dans `Uml_structure_jeu.puml` :

```plantuml
class AssetManager <<Singleton>> {
  - {static} instance : AssetManager
  - images : Map<String, Image>
  - AssetManager()
  + {static} getInstance() : AssetManager
  + getImage(path : String) : Image
}

PlayingState ..> AssetManager : <<use>>
```

## Patterns et tests

Ces patterns facilitent aussi la couverture de tests demandée par le sujet :

- `EnemyFactory` se teste en vérifiant que chaque type renvoie le bon ennemi avec les bons dégâts (2, 6, 10) ;
- `MovementStrategy` se teste en vérifiant la position d'une entité après un `move` ;
- l'Observer se teste avec un faux observateur qui compte les notifications ;
- `Level.isCompleted()` se teste sans interface graphique.
