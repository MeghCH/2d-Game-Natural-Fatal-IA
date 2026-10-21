# Concept — Natural Fatal IA (B-OOP-301)

> Shoot'em up 2D coopératif à 2 joueurs, vue verticale / du dessus, style arcade années 80.
> Détail des règles et niveaux : voir `GDD.md`.
> Stack réelle du projet : Java 21 + Maven + JavaFX + JUnit 5 (voir `Stack.md`).

## 1. Contexte et objectif

Créer un shoot'em up 2D en Java en appliquant les principes OOP : encapsulation, héritage, polymorphisme, abstraction.

Attendus Epitech :
- GUI pour afficher le jeu + infos joueurs (PV, score).
- Diagrammes UML avec design patterns explicites.
- Couverture de tests.
- Documentation complète (Javadoc).
- Review : présentation + conception + démo jouable.

## 2. Pitch

**Titre : Natural Fatal IA.**

Deux joueurs en bas de l'écran repoussent les vagues d'ennemis venues du haut.
Ils les détruisent, esquivent les contacts, attrapent des potions de soin
et affrontent un boss final : l'IA Fatale.

Boucle en 30 secondes : se déplacer -> tirer -> esquiver -> détruire -> marquer -> survivre.

Pourquoi c'est adapté au module OOP :
- Hiérarchie d'entités naturelle : `Entity -> Character -> Player / Enemy`, `Projectile`, `Potion`.
- Patterns visibles : State (écrans + phases du boss), Factory (spawn), Strategy (déplacements),
  Observer (HUD), Template Method (`Level`), Singleton (`AssetManager`).
- Pas de physique complexe ni d'inventaire : le budget va dans la game-loop, les collisions,
  les vagues et le boss.
- Démo jouable simple : 4 niveaux de difficulté croissante, le dernier contre le boss, et des écrans de fin (victoire / game over) avec le score.

## 3. Choix figés (alignés sur le GDD et le code)

### 3.1 Vue et joueurs

- 2D, vue verticale / du dessus. Les ennemis viennent du haut et descendent vers les joueurs.
- Fenêtre fixe **960 x 540** (`Game.WIDTH / HEIGHT`).
- **2 joueurs** en simultané sur le même clavier, en bas de l'écran. On peut jouer seul : le second joueur reste alors immobile.
- Style : shoot'em up arcade des années 80, volontairement simple (sprites animés pour les joueurs, les tirs, les ennemis, le boss et les potions ; ciel étoilé procédural uniquement sur le menu, pas d'effets complexes).

### 3.2 Stack

- Java 21 + Maven + JavaFX 21 + JUnit 5 + JaCoCo (voir `Stack.md`).
- Boucle via `AnimationTimer` (~60 images/s), dessin sur `Canvas` (`Main`, `Game`).
- Logique testable sans fenêtre : JavaFX n'apparaît que dans les méthodes `render` et la gestion des touches.

### 3.3 Règles reprises du GDD

- Chaque joueur : **30 PV max**, une arme de base (1 dégât, un tir toutes les 0,2 s), pas d'armes à débloquer.
- Ennemis et dégâts au contact : moustiques (2, zigzag), guêpes (6, ligne droite),
  corbeaux (10, descente lente puis piqué), IA Fatale (boss, 10, comportement selon ses PV).
- Apparition par vagues depuis le haut : la vague suivante arrive quand la précédente est éliminée.
- Potions de soin (10 PV) qui tombent du haut toutes les 6 à 12 s, à attraper sans se faire toucher.
- Tuer un ennemi rapporte du score au joueur qui l'a abattu. Pas d'XP pour l'instant.

### 3.4 Contrôles (implémentés dans `InputController`)

- Joueur 1 : ZQSD / WASD + Espace pour tirer.
- Joueur 2 : flèches + Entrée pour tirer.
- Échap : pause ; en pause, T pour changer les touches, gardées pour la reprise. Chaque joueur a son propre `InputController`, ce qui permet le 2 joueurs sur un clavier.
- Menu : Entrée pour jouer, T pour changer les touches. Écran de fin : Entrée pour rejouer, Échap pour revenir au menu.
- Écran des touches (`ControlsState`) : chaque action (`Action`) de chaque joueur peut être réassignée.

### 3.5 Périmètre MVP (démo jouable)

Inclus :
- [x] 2 joueurs déplaçables, bornés à l'écran (vitesse 300 px/s), 30 PV.
- [x] États : Menu / Playing / Pause / GameOver / Victory. HUD : PV + score par joueur (`PlayerStatusPanel`, Observer).
- [x] 4 niveaux : moustiques -> guêpes -> corbeaux -> boss IA Fatale.
- [x] Projectiles joueurs, collisions, cleanup hors écran.
- [x] Potions de soin tombant du haut.
- [x] Sprites animés pour les joueurs, les tirs et les potions (`AssetManager`, `Animation`).
- [x] Score par joueur.
- [x] Barre de PV du boss (`BossHealthBar`).
- [x] Écran de victoire distinct (`VictoryState`).
- [ ] XP.

Exclus du MVP :
- Scrolling horizontal, multijoueur réseau, boutique, particules complexes, audio, sauvegarde fichier.

> L'ancien brouillon (1 vaisseau, 3 PV, Drone / Weaver / Tank, Swing, 800x600) est abandonné :
> il contredisait le GDD et le code. Ne pas le réintroduire.

## 4. Architecture OOP (résumé, détail dans `Design_pattern.md`)

- Encapsulation : PV, position, score privés, mutations via `takeDamage()`, `heal()`, `addScore()`.
- Héritage : `Entity` -> `Character` -> `Player` / ennemis.
- Polymorphisme : `update(dt)`, `render(gc)` redéfinis par sous-classe.
- Abstraction : interfaces `Updatable`, `Renderable`, `Damageable`, `HealthObserver`, `MovementStrategy`, `BossPhase`, `GameState`.

Packages réels (`com.shootemup`) :
```
core     -> Game (contexte State), GameLoop
entity   -> Entity, Character, Player, Projectile, Weapon, Potion, PotionSpawner,
            Renderable, Updatable, Damageable, HealthObserver
enemy    -> Enemy, Mosquito, Wasp, Crow, EvilAI, EnemyType, EnemyFactory
enemy.boss -> BossPhase, CalmPhase, AngryPhase, BersherkPhase
movement -> MovementStrategy, StraightMovement, ZigzagMovement, DiveMovement
level    -> Level, MosquitoLevel, WaspLevel, CrowLevel, BossLevel, Wave, LevelManager, TutorialManager
state    -> GameState, MenuState, ControlsState, PlayingState, PauseState, GameOverState, VictoryState
ui       -> PlayerStatusPanel (Observer), BossHealthBar (barre de vie du boss), Starfield (décor du menu uniquement)
input    -> InputController (factory player1 / player2, touches modifiables), Action
asset    -> AssetManager (Singleton), Animation
```

## 5. Points tranchés par le code

1. Arme des joueurs : 1 dégât par tir, un tir toutes les 0,2 s. PV : moustique 2, guêpe 3, corbeau 5, boss 100.
2. Boss : 10 dégâts au contact, pas de tirs, trois phases selon ses PV (balayage lent, balayage rapide, balayage rapide avec va-et-vient vertical), avec barre de PV affichée.
3. Potions : 10 PV, une seule fois, toutes les 6 à 12 s, perdues si elles sortent par le bas.
4. Coop : si un joueur meurt, l'autre continue. La partie s'arrête quand les deux sont morts ou que le boss est vaincu.
5. Fin de niveau : quand toutes les vagues sont éliminées ou sorties de l'écran.
6. Menu et GameOver : implémentés (Entrée pour jouer / rejouer, Échap pour revenir au menu).
7. Vitesses : joueur 300 px/s, balle 600 px/s, moustiques et guêpes 100 px/s, corbeaux 70 px/s puis 425 px/s en piqué, boss 60 / 140 / 220 px/s selon la phase.

## 6. Points à repréciser

1. Règles XP : à quoi sert l'XP (score seul, soin, amélioration ?).
2. Dégâts du boss appliqués à chaque image tant qu'un joueur le touche : ajouter une courte invincibilité ?
