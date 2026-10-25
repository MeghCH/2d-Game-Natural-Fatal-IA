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
- Résolution logique fixe **960 x 540** (`Game.WIDTH / HEIGHT`), mise à l'échelle en plein écran.
- **2 joueurs** en simultané sur le même clavier, en bas de l'écran. On peut jouer seul : le second joueur reste alors immobile.
- Style : shoot'em up arcade des années 80-90 (sprites animés pour les joueurs, les tirs, les ennemis, le boss et les potions). L'interface reprend les codes des bornes d'arcade : typographie monospace, panneaux à double bordure, cadre CRT, scanlines, vignettage et décors procéduraux animés sur le menu, les écrans de fin, l'écran des touches et les niveaux. La démarche artistique a été complétée par une musique de fond bouclée en continu, pour renforcer l'ambiance de borne d'arcade sans gêner le gameplay.
- Affichage : jeu en plein écran au lancement, mis à l'échelle en conservant le ratio 16:9 ; `F11` bascule entre plein écran et fenêtre.

### 3.2 Stack

- Java 21 + Maven + JavaFX 21 + JUnit 5 + JaCoCo (voir `Stack.md`).
- Audio via Java Sound, intégré au JDK (`javax.sound.sampled.Clip`), sans dépendance supplémentaire.
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
- Écran des touches (`ControlsState`) : chaque action (`Action`) de chaque joueur peut être réassignée. Les flèches choisissent le joueur et la ligne, Entrée attend la nouvelle touche, Échap annule ou revient à l'écran précédent. Une touche déjà utilisée est retirée de son ancienne action, et Échap ne peut pas être choisie.
- `F11` bascule entre plein écran et fenêtré, sans interférer avec la pause.

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
- [x] Écran de configuration des touches (`ControlsState`), accessible depuis le menu et la pause.
- [x] Direction artistique arcade 80-90 : thème partagé (`ArcadeTheme`), décors animés (`LevelBackdrop`), HUD segmenté et cadre CRT.
- [x] Musique de fond en boucle continue (`MusicPlayer`, piste WAV de 30,4 s) pour compléter l'ambiance.
- [x] Plein écran au lancement et bascule `F11`.
- [ ] XP.

Exclus du MVP :
- Scrolling horizontal, multijoueur réseau, boutique, particules complexes, effets sonores ponctuels (tirs, impacts), sauvegarde fichier.

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
ui       -> ArcadeTheme (palette et primitives arcade), LevelBackdrop (décor animé des niveaux),
            PlayerStatusPanel (Observer), BossHealthBar (barre de vie du boss),
            Starfield (ciel étoilé procédural du menu et des écrans de configuration)
input    -> InputController (factory player1 / player2, touches modifiables), Action
asset    -> AssetManager (Singleton), Animation
audio    -> MusicPlayer (musique de fond en boucle via Java Sound)
```

## 5. Points tranchés par le code

1. Arme des joueurs : 1 dégât par tir, un tir toutes les 0,2 s. PV : moustique 2, guêpe 3, corbeau 5, boss 100.
2. Boss : 10 dégâts au contact, pas de tirs, trois phases selon ses PV (balayage lent, balayage rapide, balayage rapide avec va-et-vient vertical), avec barre de PV affichée.
3. Potions : 10 PV, une seule fois, toutes les 6 à 12 s, perdues si elles sortent par le bas.
4. Coop : si un joueur meurt, l'autre continue. La partie s'arrête quand les deux sont morts ou que le boss est vaincu.
5. Fin de niveau : quand toutes les vagues sont éliminées ou sorties de l'écran.
6. Menu, GameOver, Victoire et écran des touches : implémentés (Entrée pour jouer / rejouer, Échap pour revenir au menu, T pour configurer les touches).
7. Vitesses : joueur 300 px/s, balle 600 px/s, moustiques et guêpes 100 px/s, corbeaux 70 px/s puis 425 px/s en piqué, boss 60 / 140 / 220 px/s selon la phase.
8. Audio : une seule piste de fond, `freesound_community-idea-34284.wav` (30,4 s), jouée en boucle continue à -8 dB. Le jeu reste silencieux mais jouable si la ressource ou le périphérique audio manque.

## 6. Points tranchés (anciennes questions ouvertes)

1. XP : tranché — il n'y a pas d'XP dans le MVP (score seul, voir §5 point 6 et les exclusions du MVP).
2. Invincibilité après un coup : tranché — aucune invincibilité ; voir `Gameplay.md` §2 et la remarque dans la doc des patterns.
