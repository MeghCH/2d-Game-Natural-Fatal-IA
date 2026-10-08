# Concept — Natural Fatal IA (B-OOP-301)

> Shoot'em up 2D coopératif (1 à 2 joueurs), vue verticale / du dessus, style arcade années 80.
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

Deux joueurs en bas de l'écran remontent une zone hostile à scrolling vertical.
Ils détruisent des vagues d'ennemis venus du haut, esquivent les contacts, attrapent
des bonus de soin et affrontent un boss final : l'IA Fatale.

Boucle en 30 secondes : se déplacer -> tirer -> esquiver -> détruire -> marquer -> survivre.

Pourquoi c'est adapté au module OOP :
- Hiérarchie d'entités naturelle : `Entity -> Character -> Player / Enemy`, `Projectile`, `Bonus`.
- Patterns visibles : State (écrans + phases du boss), Factory (spawn), Strategy (déplacements),
  Observer (HUD), Template Method (`Level`), Singleton (`AssetManager`).
- Pas de physique complexe ni d'inventaire : le budget va dans la game-loop, les collisions,
  les vagues et le boss.
- Démo jouable simple : 4 niveaux de difficulté croissante + boss, win / lose clair.

## 3. Choix figés (alignés sur le GDD et le code)

### 3.1 Vue et joueurs

- 2D, vue verticale / du dessus. Les ennemis viennent du haut et descendent vers les joueurs.
- Fenêtre fixe **960 x 540** (`Game.WIDTH / HEIGHT`).
- **1 à 2 joueurs** en simultané sur le même clavier, en bas de l'écran.
- Style : shoot'em up arcade des années 80, volontairement simple (rectangles / pixel art, pas d'effets complexes).

### 3.2 Stack

- Java 21 + Maven + JavaFX 21 + JUnit 5 + JaCoCo (voir `Stack.md`).
- Boucle via `AnimationTimer` (~60 images/s), dessin sur `Canvas` (`Main`, `Game`).
- Logique testable sans fenêtre : le modèle ne dépend pas de JavaFX.

### 3.3 Règles reprises du GDD

- Chaque joueur : **30 PV max**, une arme de base, pas d'armes à débloquer.
- Ennemis et dégâts au contact : moustiques (2, zigzag), guêpes (6, rond),
  corbeaux (10, ligne droite), IA Fatale (boss, comportement selon ses PV).
- Apparition progressive et aléatoire par le haut.
- Bonus de soin qui tombent du haut, à attraper sans se faire toucher.
- Tuer des ennemis fait gagner score + XP.

### 3.4 Contrôles (implémentés dans `InputController`)

- Joueur 1 : ZQSD / WASD + Espace pour tirer.
- Joueur 2 : flèches + Entrée pour tirer.
- Échap : pause. Chaque joueur a son propre `InputController`, ce qui permet le 2 joueurs sur un clavier.

### 3.5 Périmètre MVP (démo jouable)

Inclus :
- [x] 2 joueurs déplaçables, bornés à l'écran (vitesse 300 px/s), 30 PV.
- [x] États : Playing / Pause. HUD : PV + score par joueur (`PlayerStatusPanel`, Observer).
- [ ] 4 niveaux : moustiques -> guêpes -> corbeaux -> boss IA Fatale.
- [ ] Projectiles joueurs, collisions, cleanup hors écran.
- [ ] Bonus de soin tombant du haut.
- [ ] États : Menu / GameOver / Victory + barre de PV du boss.
- [ ] Score + XP.

Exclus du MVP :
- Scrolling horizontal, multijoueur réseau, boutique, particules complexes, audio, sauvegarde fichier.

> L'ancien brouillon (1 vaisseau, 3 PV, Drone / Weaver / Tank, Swing, 800x600) est abandonné :
> il contredisait le GDD et le code. Ne pas le réintroduire.

## 4. Architecture OOP (résumé, détail dans `Design_pattern.md`)

- Encapsulation : PV, position, score privés, mutations via `takeDamage()`, `heal()`, `addScore()`.
- Héritage : `Entity` -> `Character` -> `Player` / ennemis.
- Polymorphisme : `update(dt)`, `render(gc)` redéfinis par sous-classe.
- Abstraction : interfaces `Updatable`, `Renderable`, `Damageable`, `HealthObserver`.

Packages réels (`com.shootemup`) :
```
core   -> Game (contexte State), GameLoop
entity -> Entity, Character, Player, Projectile, Weapon, Renderable, Updatable, Damageable, HealthObserver
state  -> GameState, PlayingState, PauseState (+ Menu, GameOver à faire)
ui     -> PlayerStatusPanel (Observer)
input  -> InputController (factory player1 / player2)
```

## 5. Points à repréciser

1. Dégâts de l'arme des joueurs et PV des ennemis (combien de tirs par moustique / guêpe / corbeau ?).
2. Dégâts et PV du boss, détail de ses attaques (contact seul ou tirs ?).
3. Règles XP : à quoi sert l'XP (score seul, soin, amélioration ?).
4. Bonus : taux d'apparition, valeur de soin, durée / cumul.
5. Victoire / défaite en coop : que se passe-t-il si un seul joueur meurt ? Fin de niveau au temps ou au quota de kills ?
6. Menu / GameOver / Victory : contenu exact et touches.
7. Vitesse, cadence de tir, cooldown : valeurs chiffrées à figer dans le GDD.
