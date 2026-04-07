-- Insertion des types
INSERT INTO Type (name) VALUES ('Plante');
INSERT INTO Type (name) VALUES ('Feu');
INSERT INTO Type (name) VALUES ('Eau');
INSERT INTO Type (name) VALUES ('Vol');
INSERT INTO Type (name) VALUES ('Poison');
INSERT INTO Type (name) VALUES ('Insecte');
INSERT INTO Type (name) VALUES ('Electrique');
INSERT INTO Type (name) VALUES ('Normal');

-- Récupération des IDs des types insérés
WITH TypeIds AS (
    SELECT id, name FROM Type
)
SELECT id INTO @plante_id FROM TypeIds WHERE name = 'Plante';
SELECT id INTO @feu_id FROM TypeIds WHERE name = 'Feu';
SELECT id INTO @eau_id FROM TypeIds WHERE name = 'Eau';
SELECT id INTO @vol_id FROM TypeIds WHERE name = 'Vol';
SELECT id INTO @poison_id FROM TypeIds WHERE name = 'Poison';
SELECT id INTO @insecte_id FROM TypeIds WHERE name = 'Insecte';
SELECT id INTO @electrique_id FROM TypeIds WHERE name = 'Electrique';
SELECT id INTO @normal_id FROM TypeIds WHERE name = 'Normal';

-- Insertion des capacités (exemples, à adapter selon les capacités réelles des Pokémon)
INSERT INTO Abilite (name, description) VALUES ('Chlorophylle', 'Augmente la vitesse sous le soleil.');
INSERT INTO Abilite (name, description) VALUES ('Sécheresse', 'Augmente la puissance des attaques Feu.');
INSERT INTO Abilite (name, description) VALUES ('Torrent', 'Augmente la puissance des attaques Eau.');
INSERT INTO Abilite (name, description) VALUES ('Vol', 'Permet de voler.');
INSERT INTO Abilite (name, description) VALUES ('Corps Maudit', 'Augmente la puissance des attaques Spectre.');
INSERT INTO Abilite (name, description) VALUES ('Électrik', 'Augmente la puissance des attaques Électrique.');
INSERT INTO Abilite (name, description) VALUES ('Statik', 'Peut paralyser les adversaires.');
INSERT INTO Abilite (name, description) VALUES ('Synchrono', 'Peut transmettre des statuts aux adversaires.');

-- Récupération des IDs des capacités insérées
WITH AbiliteIds AS (
    SELECT id, name FROM Abilite
)
SELECT id INTO @chlorophylle_id FROM AbiliteIds WHERE name = 'Chlorophylle';
SELECT id INTO @secheresse_id FROM AbiliteIds WHERE name = 'Sécheresse';
SELECT id INTO @torrent_id FROM AbiliteIds WHERE name = 'Torrent';
SELECT id INTO @vol_id_abilite FROM AbiliteIds WHERE name = 'Vol';
SELECT id INTO @corps_maudit_id FROM AbiliteIds WHERE name = 'Corps Maudit';
SELECT id INTO @electrik_id FROM AbiliteIds WHERE name = 'Électrik';
SELECT id INTO @statik_id FROM AbiliteIds WHERE name = 'Statik';
SELECT id INTO @synchrono_id FROM AbiliteIds WHERE name = 'Synchrono';

-- Insertion des Pokémon de Bublizarre à Raichu
INSERT INTO Pokemon (name, hp, attack, special_attack, defense, defense_special, speed, imageFacePath, imageDosPath, imageSpritePath, description, abilityId) VALUES
('Bublizarre', 80, 82, 110, 100, 100, 40, 'path/to/bublizarre_face.png', 'path/to/bublizarre_dos.png', 'path/to/bublizarre_sprite.png', 'Description de Bublizarre', (SELECT id FROM Abilite WHERE name = 'Chlorophylle')),
('Abo', 45, 90, 60, 65, 30, 65, 'path/to/abo_face.png', 'path/to/abo_dos.png', 'path/to/abo_sprite.png', 'Description de Abo', (SELECT id FROM Abilite WHERE name = 'Synchrono')),
('Raflepia', 70, 110, 65, 80, 80, 95, 'path/to/raflepia_face.png', 'path/to/raflepia_dos.png', 'path/to/raflepia_sprite.png', 'Description de Raflepia', (SELECT id FROM Abilite WHERE name = 'Synchrono')),
('Arbok', 60, 95, 65, 69, 80, 80, 'path/to/arbok_face.png', 'path/to/arbok_dos.png', 'path/to/arbok_sprite.png', 'Description de Arbok', (SELECT id FROM Abilite WHERE name = 'Synchrono')),
('Piafabec', 35, 55, 40, 40, 55, 90, 'path/to/piafabec_face.png', 'path/to/piafabec_dos.png', 'path/to/piafabec_sprite.png', 'Description de Piafabec', (SELECT id FROM Abilite WHERE name = 'Synchrono')),
('Rapasdepic', 30, 45, 35, 30, 45, 70, 'path/to/rapasdepic_face.png', 'path/to/rapasdepic_dos.png', 'path/to/rapasdepic_sprite.png', 'Description de Rapasdepic', (SELECT id FROM Abilite WHERE name = 'Synchrono')),
('Aerodactyl', 80, 105, 65, 65, 75, 130, 'path/to/aerodactyl_face.png', 'path/to/aerodactyl_dos.png', 'path/to/aerodactyl_sprite.png', (SELECT id FROM Abilite WHERE name = 'Vol')),
('Rattatac', 35, 55, 25, 35, 25, 70, 'path/to/rattatac_face.png', 'path/to/rattatac_dos.png', 'path/to/rattatac_sprite.png', 'Description de Rattatac', (SELECT id FROM Abilite WHERE name = 'Synchrono')),
('Rattata', 30, 56, 25, 35, 25, 72, 'path/to/rattata_face.png', 'path/to/rattata_dos.png', 'path/to/rattata_sprite.png', 'Description de Rattata', (SELECT id FROM Abilite WHERE name = 'Synchrono')),
('Pikachu', 35, 55, 50, 40, 50, 90, 'path/to/pikachu_face.png', 'path/to/pikachu_dos.png', 'path/to/pikachu_sprite.png', 'Description de Pikachu', (SELECT id FROM Abilite WHERE name = 'Statik')),
('Raichu', 60, 90, 110, 55, 85, 110, 'path/to/raichu_face.png', 'path/to/raichu_dos.png', 'path/to/raichu_sprite.png', 'Description de Raichu', (SELECT id FROM Abilite WHERE name = 'Statik'));

-- Association des types aux Pokémon
INSERT INTO Pokemon_Type (pokemon_id, type_id) VALUES
((SELECT id FROM Pokemon WHERE name = 'Bublizarre'), (SELECT id FROM Type WHERE name = 'Plante')),
((SELECT id FROM Pokemon WHERE name = 'Abo'), (SELECT id FROM Type WHERE name = 'Normal')),
((SELECT id FROM Pokemon WHERE name = 'Raflepia'), (SELECT id FROM Type WHERE name = 'Normal')),
((SELECT id FROM Pokemon WHERE name = 'Arbok'), (SELECT id FROM Type WHERE name = 'Poison')),
((SELECT id FROM Pokemon WHERE name = 'Piafabec'), (SELECT id FROM Type WHERE name = 'Normal')),
((SELECT id FROM Pokemon WHERE name = 'Rapasdepic'), (SELECT id FROM Type WHERE name = 'Normal')),
((SELECT id FROM Pokemon WHERE name = 'Aerodactyl'), (SELECT id FROM Type WHERE name = 'Vol')),
((SELECT id FROM Pokemon WHERE name = 'Aerodactyl'), (SELECT id FROM Type WHERE name = 'Roche')),
((SELECT id FROM Pokemon WHERE name = 'Rattatac'), (SELECT id FROM Type WHERE name = 'Normal')),
((SELECT id FROM Pokemon WHERE name = 'Rattata'), (SELECT id FROM Type WHERE name = 'Normal')),
((SELECT id FROM Pokemon WHERE name = 'Pikachu'), (SELECT id FROM Type WHERE name = 'Electrique')),
((SELECT id FROM Pokemon WHERE name = 'Raichu'), (SELECT id FROM Type WHERE name = 'Electrique'));