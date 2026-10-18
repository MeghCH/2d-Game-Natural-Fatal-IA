# Gameplay — Natural Fatal IA

> Définition du gameplay. Règles chiffrées et niveaux : voir `GDD.md`.
> Idée générale du jeu : voir `Concept.md`.

## 1. Perspective et vue

- Vue du dessus, en 2D.
- L'écran est fixe (960 x 540) : les joueurs restent visibles en bas, les ennemis arrivent par le haut
  et descendent vers eux.
- Pas de vue de côté, pas de scrolling horizontal.
- Le fond est uni, dans un style arcade des années 80 volontairement simple pour rester lisible.
  Le ciel étoilé qui défile (`Starfield`) n'est affiché que sur le menu ; en partie,
  seul le fond uni du niveau est dessiné pour garder les vagues lisibles.
- Les informations importantes sont toujours visibles en haut de l'écran : PV et score de chaque joueur
  (J1 à gauche, J2 à droite) et numéro du niveau au centre. Pendant le combat final, la barre de PV
  du boss (`BossHealthBar`) est affichée en haut au centre, avec ses PV restants.

## 2. Contrôles, entrées utilisateur et gestion du temps

- Le jeu lance toujours 2 joueurs sur le même clavier. On peut jouer seul : le second joueur reste alors immobile.
  - Joueur 1 : se déplace avec ZQSD (ou WASD), tire avec Espace.
  - Joueur 2 : se déplace avec les flèches, tire avec Entrée.
  - Menu : Entrée pour jouer.
  - Échap : pause, puis Échap à nouveau pour reprendre.
  - Écran de fin : Entrée pour rejouer, Échap pour revenir au menu.
- Écran de victoire : « VICTOIRE » quand le boss est vaincu, avec le score total.
- Le déplacement se fait dans les 4 directions, à 300 px/s. On ne peut pas sortir de l'écran.
- Le tir est automatique tant que la touche est enfoncée, à raison d'un tir toutes les 0,2 s.
- Le temps est en continu : les ennemis descendent même si on ne bouge pas.
  Il n'y a pas de tour par tour.
- Être touché fait perdre des PV immédiatement et fait clignoter la barre de vie. Il n'y a pas
  d'invincibilité après un coup : il faut esquiver en permanence.

## 3. Comportement des personnages, progression, inventaire et physique

- Chaque joueur commence avec 30 PV. Pas d'armes différentes : une seule arme de base, qui inflige 1 dégât par tir.
- Pas d'inventaire à gérer. Le seul élément à ramasser est la potion de soin (10 PV), qui tombe
  du haut de l'écran toutes les 6 à 12 secondes.
- Progression en 4 niveaux, du plus facile au plus dur :
  1. Moustiques (2 PV, 2 dégâts) : faciles à éliminer, niveau d'introduction.
  2. Guêpes (3 PV, 6 dégâts) : plus dangereuses.
  3. Corbeaux (5 PV, 10 dégâts) : rapides et très dangereux au contact.
  4. IA Fatale (100 PV, 10 dégâts, boss final) : gros combat, trois comportements successifs.
- Détruire un ennemi rapporte du score au joueur qui l'a abattu (100, 200, 300 ou 1000 points).
  Il n'y a pas d'XP pour l'instant.
- Il n'y a pas de physique compliquée : pas de saut, pas de gravité, pas de rebonds.
  Les déplacements et les tirs vont en ligne simple : les joueurs tirent vers le haut,
  les ennemis descendent vers le bas.

## 4. Interactions avec l'environnement et IA des ennemis

- La seule interaction avec l'environnement, c'est :
  - esquiver les ennemis qui descendent,
  - détruire les ennemis avec les tirs,
  - attraper les potions de soin sans se faire toucher.
- Les ennemis arrivent par vagues depuis le haut, répartis sur la largeur de l'écran.
  La vague suivante arrive quand la précédente est éliminée ou sortie par le bas.
- Un ennemi qui touche un joueur lui inflige ses dégâts puis disparaît, sauf le boss qui reste en jeu.
- Chaque ennemi a sa propre manière de se déplacer :
  - Moustiques : en zigzag en descendant, comme un vrai insecte.
  - Guêpes : tout droit vers le bas (la trajectoire en rond prévue n'est pas encore implémentée).
  - Corbeaux : descente lente puis piqué rapide vers le bas, il faut sortir de leur couloir.
  - Boss IA Fatale : change de comportement selon ses PV restants
    (balayage horizontal lent, puis balayage rapide, puis balayage rapide avec va-et-vient vertical).

## 5. Points tranchés par le code

1. Arme des joueurs : 1 dégât par tir. Il faut donc 2 tirs pour un moustique, 3 pour une guêpe, 5 pour un corbeau et 100 pour le boss.
2. Le boss ne tire pas : il fait seulement des dégâts au contact (10).
3. Potion de soin : 10 PV, une seule fois, toutes les 6 à 12 secondes ; elle disparaît si elle sort par le bas.
4. Si un seul des deux joueurs meurt, l'autre continue seul. La partie s'arrête quand les deux sont morts.
5. Fin de niveau : quand toutes les vagues ont été éliminées ou sont sorties de l'écran.

## 6. Points encore ouverts

1. À quoi servirait l'XP : score seul, soin, arme plus forte ?
2. Le boss inflige ses dégâts à chaque image tant qu'un joueur le touche : faut-il une courte invincibilité après un coup ?

La partie est perdue (« GAME OVER ») quand les deux joueurs sont morts, gagnée (« VICTOIRE ») quand le boss est vaincu.
