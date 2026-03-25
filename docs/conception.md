# Conception technique

> Ce document décrit l'architecture technique de votre projet. Vous êtes dans le rôle du lead-dev / architecte. C'est un document technique destiné à des développeurs.

## Vue d'ensemble

<!-- Décrivez les grandes briques de votre application et comment elles communiquent. Un schéma d'architecture est bienvenu. -->

## Design Patterns

### DP 1 — *Nom du pattern*

**Feature associée** : 
l
**Justification** : 
<!-- Pourquoi ce pattern ? Pourquoi pas un autre ? -->

**Intégration** : 
<!-- Comment s'intègre-t-il dans l'architecture ? -->

### DP 2 — *Nom du pattern*

**Feature associée** : 

**Justification** : 

**Intégration** : 

### DP 3 — *Nom du pattern*

**Feature associée** : 

**Justification** : 

**Intégration** : 

### DP 4 — *Nom du pattern*

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
  - sac : Sac
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

