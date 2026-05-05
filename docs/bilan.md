# Fiche rendu projet

> Ce document est un bilan destiné au client. Présentez ce qui a été livré, ce qui fonctionne, et tournez habilement ce qui manque. Pas de jargon technique — on parle de fonctionnalités et de valeur perçue.

## Rappel du projet

L'objectif du projet était de reproduire un système de combat pokemon, notamment les actions pour attaquer, utiliser un item, changer de pokemon et fuir. Chaque joueur choisit sa liste de pokemons et d'items puis chacun se connecte au Bus Azure, l'un hébergeant la partie, l'autre la rejoignant.

## Ce qui a été livré

### Fonctionnalité 1 — *Choisir ses pokemons*
Avant de commencer un combat, chaque joueur doit sélectionner ses pokemons via une liste. Celle-ci permet de voir les détails du pokemon, notamment sa petite description.

![Choisir ses pokemons](images/choosePokemons.png)

### Fonctionnalité 2 — *Choisir les attaques pour chaque pokemon*
Pour chaque pokemon, le joueur doit 

![Choisir les attaques du pokemon](images/attacks.png)

### Fonctionnalité 3 — *Choisir son nom de dresseur*
![Choisir les attaques du pokemon](images/choisirNom.png)

### Fonctionnalité 4 — *Choisir ses items*
![Choisir les attaques du pokemon](images/chooseItems.png)

### Fonctionnalité 5 — *Retour en arrière*
![Choisir les attaques du pokemon](images/retour.png)

### Fonctionnalité 6 — *Lancement d'un combat*
![Choisir les attaques du pokemon](images/InterfaceCombat.png)

### Fonctionnalité 7 — *Lancer une attaque*
![Choisir les attaques du pokemon](images/launchAttack.png)

### Fonctionnalité 8 — *Système de tour par tour*
![Choisir les attaques du pokemon](images/tourpartour.png)

## Ce qui n'a pas été livré (et pourquoi)

Principalemnent, c'est le debogage du bus azure qui nous aura posé problème. Notamment en termes de temps perdu. Les autres fonctionnalités n'ont pas été réalisé parce qu'il fallait que le bus fonctionne pour développer et tester les autres éléments.

### Système en ligne via le Bus Azure

Le système de Bus Azure a été capricieux tout au long du projet. La fonctionnalité était complexe à intégrer, et demandait beaucoup plus de debogage que prévu. Nous avons donc dû changer de cap vers la fin du projet pour une version hors ligne, avec une seule interface pour les deux joueurs.

A noter cependant que le système hors ligne utilise la même structure de code que la version avec le bus. De plus, nous avons laisser l'ensemble des éléments permettant de réaliser la version en ligne. Ainsi, le développement de cette fonctionnalité à l'avenir est garanti et prendra seulement un peu plus de temps, et pourra être développée comme un ajout (possibilité de faire hors-ligne ou en ligne).

### Les actions "Use Item" et "Fuite"

Ces trois actions n'ont pas été finies, et ne sont donc pas disponibles pour le moment. Cependant, toute la structure pour les intégrer est en place, et leur développement n'est qu'une question de temps.


### Ajout de musique / bruitage

Nous ne nous sommes pas concentré particulièrement sur cette fonctionnalité, qui relève plus de l'estétique que d'une fonctionnalité technique.

La structure du code permet facilement de rajouter les éléments nécessaires à l'activation du son pour un partie, où encore des bruitages.

## Perspectives

<!-- Quelles évolutions proposez-vous pour la suite ? -->

