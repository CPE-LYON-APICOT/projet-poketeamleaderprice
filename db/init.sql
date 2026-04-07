-- Création des tables (si elles n'existent pas déjà)
CREATE TABLE IF NOT EXISTS Type (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Stade (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    type_id INTEGER,
    FOREIGN KEY (type_id) REFERENCES Type(id)
);

CREATE TABLE IF NOT EXISTS Abilite (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS Pokemon (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    hp INTEGER NOT NULL,
    attack INTEGER NOT NULL,
    special_attack INTEGER NOT NULL,
    defense INTEGER NOT NULL,
    defense_special INTEGER NOT NULL,
    speed INTEGER NOT NULL,
    imageFacePath TEXT,
    imageDosPath TEXT,
    imageSpritePath TEXT,
    description TEXT,
    abilityId INTEGER,
    FOREIGN KEY (abilityId) REFERENCES Abilite(id)
);

CREATE TABLE IF NOT EXISTS Pokemon_Type (
    pokemon_id INTEGER,
    type_id INTEGER,
    PRIMARY KEY (pokemon_id, type_id),
    FOREIGN KEY (pokemon_id) REFERENCES Pokemon(id),
    FOREIGN KEY (type_id) REFERENCES Type(id)
);

CREATE TABLE IF NOT EXISTS Type_Type_Avantages (
    type_id INTEGER,
    avantage_type_id INTEGER,
    PRIMARY KEY (type_id, avantage_type_id),
    FOREIGN KEY (type_id) REFERENCES Type(id),
    FOREIGN KEY (avantage_type_id) REFERENCES Type(id)
);

CREATE TABLE IF NOT EXISTS Type_Type_Faiblesses (
    type_id INTEGER,
    faiblesse_type_id INTEGER,
    PRIMARY KEY (type_id, faiblesse_type_id),
    FOREIGN KEY (type_id) REFERENCES Type(id),
    FOREIGN KEY (faiblesse_type_id) REFERENCES Type(id)
);

CREATE TABLE IF NOT EXISTS Attaque (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    power INTEGER NOT NULL,
    accuracy INTEGER NOT NULL,
    pp INTEGER NOT NULL,
    type_id INTEGER,
    FOREIGN KEY (type_id) REFERENCES Type(id)
);

CREATE TABLE IF NOT EXISTS HealingItem (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    hp_heal INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS EffectItem (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    attack INTEGER NOT NULL,
    special_attack INTEGER NOT NULL,
    defense INTEGER NOT NULL,
    defense_special INTEGER NOT NULL,
    speed INTEGER NOT NULL
);