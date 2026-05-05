import json
import requests
from bs4 import BeautifulSoup

DB_FILE = "db.json"
BASE_URL = "https://pokemondb.net/pokedex/"

# Charger la BDD
with open(DB_FILE, "r", encoding="utf-8") as f:
    db = json.load(f)

attaques = db["attaques"]
types = db["types"]
pokemons = db["pokemons"]

# Helpers
def get_attack_by_name(name):
    for atk in attaques:
        if atk["name"].lower() == name.lower():
            return atk
    return None

def get_type_id(type_name):
    for t in types:
        if t["nom"].lower() == type_name.lower():
            return t["id"]
    # créer type si inexistant
    new_id = max(t["id"] for t in types) + 1
    types.append({
        "id": new_id,
        "nom": type_name,
        "faiblesses": [],
        "avantages": []
    })
    return new_id

def create_attack(name, power, accuracy, pp, type_name):
    new_id = max(a["id"] for a in attaques) + 1
    type_id = get_type_id(type_name)

    new_attack = {
        "id": new_id,
        "name": name,
        "power": power,
        "accuracy": accuracy,
        "pp": pp,
        "type_id": type_id
    }

    attaques.append(new_attack)
    return new_attack


def scrape_moves(pokemon_name):
    url = BASE_URL + pokemon_name.lower()
    print(f"Scraping {url}")

    try:
        res = requests.get(url, timeout=10)
        res.raise_for_status()
    except requests.RequestException as e:
        print(f"  ❌ Erreur réseau : {e}")
        return []

    soup = BeautifulSoup(res.text, "html.parser")
    moves = []

    # Chercher tous les tableaux
    tables = soup.find_all("table")

    for table in tables:
        # Chercher le h2 ou h3 précédent pour savoir si c'est "level up"
        header = None
        for prev_sibling in table.find_all_previous(["h2", "h3"], limit=5):
            if prev_sibling:
                header = prev_sibling
                break

        if not header or "level up" not in header.text.lower():
            continue

        # Extraire les lignes (ignorer l'en-tête)
        rows = table.find_all("tr")[1:]

        for row in rows:
            cols = row.find_all("td")
            if len(cols) < 4:
                continue

            try:
                # Structure : Lvl | Move | Type | Power | Acc | PP
                # Essayer col[1] pour le nom (après Lvl)
                name = cols[1].text.strip() if len(cols) > 1 else ""
                type_name = cols[2].text.strip() if len(cols) > 2 else "Normal"
                power_text = cols[3].text.strip() if len(cols) > 3 else "0"
                accuracy_text = cols[4].text.strip() if len(cols) > 4 else "100"
                pp_text = cols[5].text.strip() if len(cols) > 5 else "0"

                if not name:
                    continue

                # nettoyage
                power = int(power_text) if power_text.isdigit() else 0
                accuracy = int(accuracy_text) if accuracy_text.isdigit() else 100
                pp = int(pp_text) if pp_text.isdigit() else 0

                moves.append({
                    "name": name,
                    "type": type_name,
                    "power": power,
                    "accuracy": accuracy,
                    "pp": pp
                })
            except (IndexError, ValueError) as e:
                # Ignorer les lignes mal formées
                continue

    if not moves:
        print(f"  ⚠️  Aucune attaque trouvée pour {pokemon_name}")

    return moves


def update_pokemon(pokemon):
    moves = scrape_moves(pokemon["name"])

    for move in moves:
        existing = get_attack_by_name(move["name"])

        if existing:
            atk_id = existing["id"]
        else:
            new_atk = create_attack(
                move["name"],
                move["power"],
                move["accuracy"],
                move["pp"],
                move["type"]
            )
            atk_id = new_atk["id"]

        if atk_id not in pokemon["availableAttacks"]:
            pokemon["availableAttacks"].append(atk_id)


# 🚀 Exemple : Charmeleon + Charizard
for p in pokemons:
    update_pokemon(p)

# Sauvegarde
with open(DB_FILE, "w", encoding="utf-8") as f:
    json.dump(db, f, indent=2, ensure_ascii=False)

print("✅ DB mise à jour !")