# Gameplay — Natural Fatal IA

> Définition du gameplay. Règles chiffrées et niveaux : voir `GDD.md`.
> Idée générale du jeu : voir `Concept.md`.

## 1. Perspective et vue

- Vue du dessus, en 2D.
- L'écran est fixe : les joueurs restent visibles en bas, les ennemis arrivent par le haut
  et descendent vers eux.
- Pas de vue de côté, pas de scrolling horizontal.
- Le décor défile vers le bas pour donner l'impression d'avancer, dans un style arcade
  des années 80, volontairement simple pour rester lisible.
- Les informations importantes sont toujours visibles : PV des deux joueurs en bas,
  PV du boss en haut pendant le combat final.

## 2. Contrôles, entrées utilisateur et gestion du temps

- Le jeu se joue à 1 ou 2 joueurs sur le même clavier.
  - Joueur 1 : se déplace avec ZQSD (ou WASD), tire avec Espace.
  - Joueur 2 : se déplace avec les flèches, tire avec Entrée.
  - Échap : pause. Entrée / Espace : valider, recommencer.
- Le déplacement se fait dans les 4 directions. On ne peut pas sortir de l'écran.
- Le temps est en continu : les ennemis descendent même si on ne bouge pas.
  Il n'y a pas de tour par tour.
- Être touché fait perdre des PV immédiatement. Il n'y a pas d'invincibilité longue :
  il faut esquiver en permanence.

## 3. Comportement des personnages, progression, inventaire et physique

- Chaque joueur commence avec 30 PV. Pas d'armes différentes : une seule arme de base.
- Pas d'inventaire à gérer. Le seul élément à ramasser est le bonus de soin qui tombe
  du haut de l'écran.
- Progression en 4 niveaux, du plus facile au plus dur :
  1. Moustiques (2 dégâts) : faciles à éliminer, niveau d'introduction.
  2. Guêpes (6 dégâts) : plus dangereuses.
  3. Corbeaux (10 dégâts) : très dangereux au contact.
  4. IA Fatale (boss final) : gros combat, plusieurs comportements.
- Détruire des ennemis fait gagner du score et de l'XP.
- Il n'y a pas de physique compliquée : pas de saut, pas de gravité, pas de rebonds.
  Les déplacements et les tirs vont en ligne simple : les joueurs tirent vers le haut,
  les ennemis descendent vers le bas.

## 4. Interactions avec l'environnement et IA des ennemis

- La seule interaction avec l'environnement, c'est :
  - esquiver les ennemis qui descendent,
  - détruire les ennemis avec les tirs,
  - attraper les bonus de soin sans se faire toucher.
- Les ennemis apparaissent petit à petit par le haut, à des moments aléatoires.
- Chaque ennemi a sa propre manière de se déplacer :
  - Moustiques : en zigzag, comme un vrai insecte.
  - Guêpes : en rond, après être arrivées par le haut.
  - Corbeaux : tout droit vers le bas, il faut sortir de leur couloir.
  - Boss IA Fatale : change de comportement selon ses PV restants
    (balayage, puis zigzag, puis poursuite des joueurs).
- Le boss est le seul ennemi avec une barre de vie affichée.

## 5. Points à repréciser

1. Combien de tirs pour tuer chaque ennemi ? Combien de dégâts fait l'arme des joueurs ?
2. Le boss tire-t-il des projectiles ou fait-il seulement des dégâts au contact ?
3. À quoi sert l'XP : score seul, soin, arme plus forte ?
4. Bonus de soin : combien de PV rendus, combien de temps à l'écran, fréquence ?
5. Si un seul des deux joueurs meurt, l'autre continue-t-il seul ?
6. Fin de niveau : au temps écoulé ou quand tous les ennemis sont tués ?
