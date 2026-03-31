# Conception technique

> Ce document décrit l'architecture technique de votre projet. Vous êtes dans le rôle du lead-dev / architecte. C'est un document technique destiné à des développeurs.

## Vue d'ensemble

<!-- Décrivez les grandes briques de votre application et comment elles communiquent. Un schéma d'architecture est bienvenu. -->

## Design Patterns

### DP 1 — *Singleton*

**Feature associée** : Connexion BDD

**Justification** : On ne doit avoir qu'une seule connexion à la fois à la BDD. On ne peut pas se permettre de récréer une connexion à chaque fois qu'on en a besoin. Si une connexion est déjà ouverte, on doit pouvoir l'utiliser.

**Intégration** : La classe permettant la connexion à la base de données intègrera le pattern singleton.

### DP 2 — *Observer*

**Feature associée** : Connexion à internet pour les parties + les évenements claviers/sonores

**Justification** : Tous ces éléments étant asynchrone, il faut qu'un élément du code attende qu'un événement se produise pour activer certains comportements. Un observeur nous permettra d'attendre "en arrière plan" (le code ne s'arrête pas pendant l'attente) qu'un évenement se produise pour activer du son / des images / la réception d'une connexion

**Intégration** : 

### DP 3 — *Mediator*

**Feature associée** : Lancement et gestion d'une Partie

**Justification** : Une partie gère les interactions entre les objets (dresseurs, objets, pokemon, terrain). Au niveau du code, il serait étrange qu'une attaque d'un pokemon permette à celui-ci de directement prendre la barre de vie de son adversaire et de lui enlever des points... Il serait préférable de gérer les interactions au sein d'une classe spécifique, pour alléger le nombre de méthodes spécifiques aux combat dans les objets.

**Intégration** : une classe Combat, intégrant une liste de dresseurs, de pokemon, d'objets et un terrain.

### DP 4 — *Command*

**Feature associée** : 

**Justification** : 

**Intégration** : 

## Diagrammes UML

### Diagramme 1 — *Type classe*

```plantuml
@startuml
skinparam groupInheritance 4
top to bottom direction

class Partie {
  - index : String
  - dresseurs : List<Dresseur>
  - stade : Stade
}

class Stade {
  - index : String
  - nom : String
  - type : Type
}

class Dresseur {
  - index : Integer
  - nom : String
  - pokemon : Map<Pokemon, Integer>
  - items : List<Item>
}

abstract class Item {
  - index : String
  - nom : String
}

class HealingItem extends Item {
  - hpHeal : Integer
}

enum StatType {
  Atk
  AtkSpe
  Def
  DefSpe
  Spd
}

class EffectItem extends Item {
  - affectedStat : Map<StatType, Integer>
}

class Pokemon {
  - Num_Poke : Integer
  - nom : String
  - types : List<Type>
  - lesattaquesdisponibles : Attaque[]
  - lesattaquesprises : Attaque[]
  - hp : Integer
  - stats : Map<StatType, Integer>
  - spriteFace : BufferedImage
  - spriteDos : BufferedImage
  - spriteMini : BufferedImage
  - description : char
  - ability : Abilite
}

class Abilite {
  - index : String
  - nom : String
}

class Type {
  - index : String
  - nom : String
  - faiblesse : List<Type>
  - avantages : List<Type>
}

class Attaque {
  - index : String
  - nom : String
  - degat : String
}

class SingletonConnectionDB {
    -instance
    +getInstance()
}

class NetworkService {
    +connect(gameToken: string)
    +searchForGame()
    +listenForNewPlayers()
}

Partie --o Dresseur : has
Partie --o Stade : has
Dresseur --o Pokemon : has
Dresseur --o Item : has
Pokemon --o Type : has
Pokemon --o Attaque : has
Pokemon --o Abilite : has
Stade --o Type : has

@enduml

```
![Diagramme_Class_Pokemon.png](images/Diagramme_Class_Pokemon.png)

### Diagramme 2 — *Use Case*
![Diagramme UseCase](images/UseCase.drawio.png)

