# Design Patterns - Shoot'em up

Ce document présente les patrons de conception utilisés dans le jeu. Pour chacun : le problème qu'il résout dans notre jeu, la solution choisie, les classes concernées et un extrait de code Java.

Les classes citées correspondent aux diagrammes du dossier `Diagrammes/`. Les extraits sont tirés du code du projet ; certaines lignes sont remplacées par `// ...` pour garder l'essentiel.

## Récapitulatif

| Pattern         | Famille      | Classes                               | Problème résolu                                          |
| --------------- | ------------ | ------------------------------------- | -------------------------------------------------------- |
| State           | Comportement | `GameState`, `BossPhase`              | changer d'écran et de phase de boss sans `if` géants     |
| Factory         | Création     | `EnemyFactory`, `EnemyType`           | créer les ennemis à un seul endroit                      |
| Strategy        | Comportement | `MovementStrategy`                    | varier les déplacements sans multiplier les sous-classes |
| Observer        | Comportement | `HealthObserver`, `PlayerStatusPanel` | mettre à jour l'affichage quand les PV changent          |
| Template Method | Comportement | `Level`                               | partager la logique commune des niveaux                  |
| Singleton       | Création     | `AssetManager`                        | charger chaque image une seule fois                      |

---

## 1. State - les écrans du jeu et les phases du boss

### Problème

Le jeu passe par plusieurs écrans : menu, partie, pause, game over. Sans pattern, la boucle de jeu deviendrait une suite de `if (state == MENU) ... else if (state == PAUSE) ...` répétée dans `update`, `render` et la gestion des touches. Ajouter un écran obligerait à modifier tous ces blocs.

### Solution

Chaque écran est une classe qui implémente `GameState` : `MenuState`, `ControlsState`, `PlayingState`, `PauseState`, `GameOverState` et `VictoryState`. `Game` garde une référence vers l'état courant et lui délègue tout. Changer d'écran revient à changer d'objet.

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
     * Réagit à l'appui sur une touche.
     * @param code la touche enfoncée
     */
    void keyPressed(KeyCode code);

    /**
     * Réagit au relâchement d'une touche.
     * @param code la touche relâchée
     */
    void keyReleased(KeyCode code);
}
```

```java
public class Game {

    private GameState currentState;

    public static final int WIDTH = 960;
    public static final int HEIGHT = 540;

    /**
     * Remplace l'écran courant.
     * @param newState le nouvel écran à afficher
     */
    public void setState(GameState newState) {
        this.currentState = Objects.requireNonNull(newState, "L'état ne peut pas être null.");
        newState.enter();
    }

    public GameState getCurrentState() {
        return this.currentState;
    }

    public void update(double dt) {
        state().update(dt);
    }

    public void render(GraphicsContext gc) {
        state().render(gc);
    }

    // keyPressed() et keyReleased() délèguent de la même façon

    /** Renvoie l'état courant, ou lève une erreur claire s'il n'y en a pas. */
    private GameState state() {
        if (currentState == null) {
            throw new IllegalStateException(
                    "Aucun état défini : appelez setState() avant de lancer le jeu.");
        }
        return currentState;
    }
}
```

```java
public class PauseState implements GameState {

    private final Game game;
    private final GameState previousState;

    public PauseState(Game game, GameState previousState) {
        this.game = game;
        this.previousState = previousState;
    }

    @Override
    public void render(GraphicsContext gc) {
        previousState.render(gc);   // la partie reste visible derrière
        // ... voile sombre et texte « PAUSE »
    }

    @Override
    public void keyPressed(KeyCode code) {
        if (code == KeyCode.ESCAPE) {
            game.setState(previousState);   // retour à la partie
        } else if (code == KeyCode.T) {
            game.setState(new ControlsState(game, this));   // configuration des touches
        }
    }

    // enter(), update() et keyReleased() ne font rien : le jeu est figé
}
```

L'écran des touches (`ControlsState`) est lui aussi un état : il reçoit l'écran
précédent (menu ou pause) pour y revenir avec Échap. Les contrôleurs de touches
sont gardés par `Game`, pas par la partie : les changements faits depuis la pause
sont donc immédiatement utilisés à la reprise, et conservés d'une partie à l'autre.

Les transitions entre écrans :

| Depuis    | Touche / événement                         | Vers      |
| --------- | ------------------------------------------ | --------- |
| Menu      | Entrée                                     | Partie    |
| Menu      | T                                          | Touches   |
| Touches   | Échap                                      | écran précédent (Menu ou Pause) |
| Partie    | Échap                                      | Pause     |
| Pause     | Échap                                      | Partie    |
| Pause     | T                                          | Touches   |
| Partie    | les deux joueurs sont morts                        | Game Over |
| Partie    | boss vaincu (progression finie)                    | Victoire  |
| Game Over | Entrée                                     | Partie    |
| Game Over | Échap                                      | Menu      |
| Victoire  | Entrée                                     | Partie    |
| Victoire  | Échap                                      | Menu      |

### Deuxième utilisation : les phases de l'IA maléfique

Le boss change de comportement selon ses points de vie. Chaque phase est un état. Le balayage horizontal, commun à deux phases, est une méthode `default` de l'interface.

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

    /** Balayage horizontal : le boss rebondit sur les bords de l'écran. */
    default void sweep(EvilAI boss, double dt, double speed) {
        double maxX = Game.WIDTH - boss.getWidth();
        double nextX = boss.getX() + speed * dt * boss.getDirection();
        if (nextX < 0) {
            nextX = 0;
            boss.reverseDirection();
        } else if (nextX > maxX) {
            nextX = maxX;
            boss.reverseDirection();
        }
        boss.setX(nextX);
    }
}
```

```java
public class EvilAI extends Enemy {

    private BossPhase phase = new CalmPhase();
    private int direction = 1;
    private int thresholdsCrossed = 0;

    @Override
    public void update(double dt) {
        if (!isAlive()) {
            return;
        }
        phase.execute(this, dt);
        int reached = 0;
        if (hp < maxHp / 3) {
            reached = 2;
        } else if (hp < 2 * maxHp / 3) {
            reached = 1;
        }
        while (thresholdsCrossed < reached) {   // un gros coup peut franchir les deux seuils
            changePhase();
            thresholdsCrossed++;
        }
    }

    public void changePhase() {
        phase = phase.next();
    }
}
```

Les trois phases :

| Phase           | Quand           | Comportement                                                  |
| --------------- | --------------- | ------------------------------------------------------------- |
| `CalmPhase`     | au départ       | balayage horizontal lent (60 px/s)                            |
| `AngryPhase`    | PV sous les 2/3 | balayage horizontal rapide (140 px/s)                         |
| `BersherkPhase` | PV sous le 1/3  | balayage horizontal rapide (220 px/s) avec va-et-vient vertical (130 px/s) |

`BersherkPhase.next()` renvoie la phase elle-même : c'est la dernière.

### Avantage

Ajouter un écran (options, sélection du nombre de joueurs) ou une phase de boss se fait en créant une classe, sans toucher au code existant : c'est le principe **ouvert/fermé**.

---

## 2. Factory - la création des ennemis

### Problème

Les vagues doivent créer des moustiques, des guêpes, des corbeaux et le boss. Si chaque niveau appelait directement `new Mosquito(...)`, `new Wasp(...)`, la création des ennemis serait dispersée dans tout le code.

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
            case MOSQUITO -> new Mosquito(x, y);
            case WASP     -> new Wasp(x, y);
            case CROW     -> new Crow(x, y);
            case EVIL_AI  -> new EvilAI(x, y);
        };
    }
}
```

Chaque ennemi fixe lui-même ses caractéristiques et son déplacement dans son constructeur :

```java
public class Mosquito extends Enemy {

    public static final double SIZE = 32;
    private static final int MAX_HP = 2;
    private static final int DAMAGE = 2;
    private static final int SCORE = 100;

    public Mosquito(double x, double y) {
        super(x, y, SIZE, SIZE, MAX_HP, DAMAGE, SCORE, new ZigzagMovement());
    }
}
```

| Ennemi     | PV  | Dégâts au contact | Score | Déplacement                     |
| ---------- | --- | ----------------- | ----- | ------------------------------- |
| `Mosquito` | 2   | 2                 | 100   | `ZigzagMovement`                |
| `Wasp`     | 3   | 6                 | 200   | `StraightMovement`              |
| `Crow`     | 5   | 10                | 300   | `DiveMovement`                  |
| `EvilAI`   | 100 | 10                | 1000  | aucun : ses phases le déplacent |

`Wave` est le seul client de la fabrique : elle crée ses ennemis puis les répartit sur la largeur de l'écran.

### Avantage

Les niveaux ne connaissent que `EnemyType`. Pour ajouter un ennemi, on ajoute une valeur à l'enum et une ligne dans la fabrique. Le compilateur signale le `switch` incomplet si on oublie.

---

## 3. Strategy - les déplacements des ennemis

### Problème

Le moustique descend en zigzag, la guêpe descend en ligne droite, le corbeau pique rapidement vers le bas. Créer des sous-classes comme `ZigzagMosquito` ou `DivingWasp` ferait exploser le nombre de classes.

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

    private static final double SPEED = 100;
    private static final double AMPLITUDE = 80;
    private static final double FREQUENCY = 0.05;

    @Override
    public void move(Entity e, double dt) {
        e.setY(e.getY() + SPEED * dt);
        e.setX(e.getX() + Math.cos(e.getY() * FREQUENCY) * AMPLITUDE * dt);
    }
}
```

`StraightMovement` descend tout droit à 100 px/s et `DiveMovement` descend lentement à 70 px/s pendant 0,8 s puis pique à 425 px/s, comme un corbeau qui fond sur les joueurs.

```java
public abstract class Enemy extends Character {

    protected MovementStrategy movement;

    /**
     * Change le déplacement de l'ennemi en cours de partie.
     * @param movement le nouveau déplacement
     */
    public void setMovement(MovementStrategy movement) {
        this.movement = Objects.requireNonNull(movement, "Le déplacement ne peut pas être null.");
    }

    @Override
    public void update(double dt) {
        if (!isAlive()) {
            return;
        }
        movement.move(this, dt);
    }
}
```

`MovementStrategy` n'a qu'une méthode : le boss reçoit donc simplement un déplacement vide, `(e, dt) -> { }`, puisque ce sont ses phases qui le font bouger.

### Avantage

Les déplacements se combinent librement avec les ennemis et peuvent changer pendant la partie (une guêpe blessée qui passerait en zigzag). Comme les déplacements ne gardent aucun état propre, une même instance peut être partagée par plusieurs ennemis.

---

## 4. Observer - l'affichage des points de vie

### Problème

Quand un joueur perd ou regagne des PV, plusieurs éléments doivent réagir : la barre de vie du `PlayerStatusPanel`, son clignotement, peut-être un son. Si `Player` appelait directement `PlayerStatusPanel.updateBar()`, la logique du jeu dépendrait de l'interface graphique.

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

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
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
public class PlayerStatusPanel implements HealthObserver, Updatable, Renderable {

    private static final double FLASH_DURATION = 0.3;

    private final List<Player> players = new ArrayList<>();
    private final Map<Character, Double> flashTimers = new HashMap<>();
    private final Map<Character, Integer> lastKnownHp = new HashMap<>();

    public void track(Player player) {
        players.add(player);
        lastKnownHp.put(player, player.getHp());
        player.addObserver(this);
    }

    @Override
    public void onHealthChanged(Character c) {
        int previousHp = lastKnownHp.getOrDefault(c, c.getHp());
        if (c.getHp() < previousHp) {
            flashTimers.put(c, FLASH_DURATION); // clignote seulement si les PV baissent
        }
        lastKnownHp.put(c, c.getHp());
    }

    public boolean isFlashing(Character c) {
        return flashTimers.containsKey(c);
    }

    // update() fait avancer les clignotements, render() dessine les barres
}
```

Quand un joueur ramasse une potion, `heal()` prévient aussi le panneau : la barre se remplit, sans clignoter.

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

    protected final Queue<Wave> waves = new ArrayDeque<>();
    protected final List<Enemy> activeEnemies = new ArrayList<>();
    private final EnemyFactory factory;

    protected Level(EnemyFactory factory) {
        this.factory = Objects.requireNonNull(factory, "La fabrique ne peut pas être null.");
    }

    /** Prépare le niveau. Appelée au lancement. */
    public final void start() {
        waves.clear();
        activeEnemies.clear();
        buildWaves();
    }

    /** Méthode modèle : l'ordre des étapes est fixe. */
    public final void update(double dt) {
        if (activeEnemies.isEmpty() && !waves.isEmpty()) {
            activeEnemies.addAll(waves.poll().spawn(factory));
        }
        for (Enemy enemy : activeEnemies) {
            enemy.update(dt);
        }
        activeEnemies.removeIf(enemy -> !enemy.isAlive() || enemy.getY() > Game.HEIGHT);
    }

    /** Étape propre à chaque niveau : définir ses vagues. */
    protected abstract void buildWaves();

    public boolean isCompleted() {
        return waves.isEmpty() && activeEnemies.isEmpty();
    }
}
```

```java
public class MosquitoLevel extends Level {

    public MosquitoLevel(EnemyFactory factory) {
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

`WaspLevel` (3, 5 et 8 guêpes), `CrowLevel` (3, 5 et 8 corbeaux) et `BossLevel` (1 boss) suivent le même modèle. `TutorialManager.createProgression()` les assemble dans un `LevelManager`, qui passe au niveau suivant quand le courant est terminé.

### Avantage

`start` et `update` sont `final` : aucun niveau ne peut casser le déroulement. Créer un niveau se résume à décrire ses vagues.

---

## 6. Singleton - le chargement des images

### Problème

Les images doivent être chargées une seule fois. Les joueurs, les projectiles et les potions demandent leur feuille de sprites à chaque affichage : sans cache, le jeu relirait le fichier 60 fois par seconde pour chaque objet.

### Solution

Une classe unique, accessible partout, garde les images en cache.

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
     * @param path chemin de la ressource, par exemple "/sprites/player1.png"
     * @return l'image chargée, ou null si le fichier est introuvable
     */
    public Image getImage(String path) {
        if (images.containsKey(path)) {
            return images.get(path);
        }
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            return null;   // l'appelant dessine alors un rectangle de couleur
        }
        Image image = new Image(stream);
        images.put(path, image);
        return image;
    }
}
```

Chaque feuille de sprites est ensuite découpée par une `Animation` (rangée, nombre d'images, vitesse) : `IDLE` et `ATTACK` pour le joueur, `P1_FLIGHT` et `P2_FLIGHT` pour les tirs, `FALLING` et `COLLECTED` pour les potions.

### Limites assumées

Le Singleton crée un état global, ce qui rend les tests plus difficiles. Nous le limitons donc aux images, qui sont réellement uniques, et ne l'utilisons pas pour la logique du jeu (joueurs, niveaux), qui passe par des objets transmis explicitement. Quand une image manque, `getImage` renvoie `null` et chaque objet se dessine avec un simple rectangle de couleur.

---

## Dans les diagrammes

Chaque pattern est visible dans le dossier `Diagrammes/` grâce à un stéréotype : `<<State>>` sur `GameState` et `BossPhase`, `<<Factory>>` sur `EnemyFactory`, `<<Strategy>>` sur `MovementStrategy`, `<<Observer>>` sur `HealthObserver`, `<<Template Method>>` sur `Level` et `<<Singleton>>` sur `AssetManager`. L'`AssetManager` figure dans `Uml_structure_jeu.puml`, utilisé par `Player`, `Projectile` et `Potion`. La `BossHealthBar` (barre de vie du boss, simple affichage relu à chaque image) figure dans `Uml_structure_jeu.puml`, possédée par `PlayingState`.

## Patterns et tests

Ces patterns facilitent aussi la couverture de tests demandée par le sujet :

- `EnemyFactoryTest` vérifie que chaque type renvoie le bon ennemi avec les bons dégâts (2, 6, 10) ;
- `MovementTest` vérifie la position d'une entité après un `move` ;
- `CharacterTest` teste l'Observer avec un faux observateur qui compte les notifications, et `PlayerStatusPanelTest` vérifie qu'un soin ne fait pas clignoter la barre ;
- `EvilAITest` vérifie le passage d'une phase à l'autre, y compris quand un seul coup franchit les deux seuils ;
- `StateTransitionTest` vérifie les allers-retours entre la partie, la pause et l'écran des touches ;
- `ControlsStateTest` vérifie la sélection, la réassignation et l'échange des touches, ainsi que l'impossibilité de choisir Échap ;
- `InputControllerTest` vérifie les touches par défaut, la détection des appuis et la modification des liaisons ;
- `LevelTest` vérifie l'enchaînement des vagues, `Level.isCompleted()` et les couleurs/noms de secteur sans interface graphique.
