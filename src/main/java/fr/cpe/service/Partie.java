package fr.cpe.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import fr.cpe.model.Attaque;
import fr.cpe.model.Dresseur;
import fr.cpe.model.Pokemon;
import fr.cpe.model.Stade;
import fr.cpe.model.StatType;

@Singleton
public class Partie {

    private Dresseur dresseur1;
    private Dresseur dresseur2;
    private Stade stade;
    private Pokemon activePokemonDresseur1;
    private Pokemon activePokemonDresseur2;
    private Dresseur currentAttacker;

    @Inject
    public Partie() {
    }

    public Dresseur getDresseur1() {
        return dresseur1;
    }

    public Partie setDresseur1(Dresseur dresseur1) {
        this.dresseur1 = dresseur1;
        return this;
    }

    public Dresseur getDresseur2() {
        return dresseur2;
    }

    public Partie setDresseur2(Dresseur dresseur2) {
        this.dresseur2 = dresseur2;
        return this;
    }

    public Dresseur getDresseurFromId(Integer id) {
        if (dresseur1.getIndex().equals(id)) {
            return dresseur1;
        } else if (dresseur2.getIndex().equals(id)) {
            return dresseur2;
        } else {
            throw new IllegalArgumentException("Dresseur with id " + id + " not found");
        }
    }

    public Pokemon getActivePokemonOf(Dresseur dresseur) {
        if (dresseur == dresseur1) {
            return activePokemonDresseur1;
        } else if (dresseur == dresseur2) {
            return activePokemonDresseur2;
        } else {
            throw new IllegalArgumentException("Dresseur inconnu");
        }
    }

    public Partie setActivePokemonOf(Dresseur dresseur, Pokemon pokemon) {
        if (dresseur == dresseur1) {
            this.activePokemonDresseur1 = pokemon;
        } else if (dresseur == dresseur2) {
            this.activePokemonDresseur2 = pokemon;
        } else {
            throw new IllegalArgumentException("Dresseur inconnu");
        }
        return this;
    }

    public Stade getStade() {
        return stade;
    }

    public Partie setStade(Stade stade) {
        this.stade = stade;
        return this;
    }

    public void attack(Dresseur dresseur, Attaque attaque) {
        Dresseur cible = getOpponent(dresseur);
        if (cible == null) {
            throw new IllegalArgumentException("Aucun adversaire trouvé pour " + dresseur.getNom());
        }

        Pokemon pokemonCible = getActivePokemonOf(cible);
        if (pokemonCible == null) {
            throw new IllegalStateException("Aucun Pokémon actif pour l'adversaire");
        }

        int defense = 0;
        if (pokemonCible.getStats() != null && pokemonCible.getStats().get(StatType.Def) != null) {
            defense = pokemonCible.getStats().get(StatType.Def);
        }

        int degats = attaque.getPower() - defense;
        if (degats < 0) {
            degats = 0;
        }

        pokemonCible.setHp(Math.max(0, pokemonCible.getHp() - degats));
    }

    public Dresseur getOpponent(Dresseur dresseur) {
        if (dresseur == dresseur1) {
            return dresseur2;
        }
        if (dresseur == dresseur2) {
            return dresseur1;
        }
        throw new IllegalArgumentException("Dresseur inconnu");
    }

    public Dresseur getCurrentAttacker() {
        return currentAttacker;
    }

    public Partie setCurrentAttacker(Dresseur currentAttacker) {
        this.currentAttacker = currentAttacker;
        return this;
    }

    public Dresseur nextAttacker() {
        if (currentAttacker == null) {
            return null;
        }
        if (currentAttacker == dresseur1) {
            currentAttacker = dresseur2;
        } else {
            currentAttacker = dresseur1;
        }
        return currentAttacker;
    }

    public void changePokemon(Dresseur dresseur, Pokemon nouveauPokemon) {
        if (dresseur == dresseur1) {
            this.activePokemonDresseur1 = nouveauPokemon;
        } else if (dresseur == dresseur2) {
            this.activePokemonDresseur2 = nouveauPokemon;
        } else {
            throw new IllegalArgumentException("Dresseur inconnu");
        }
    }

    public void useItem() {
        // Logic to use an item during the battle
    }

    public Dresseur checkWinner() {
        if (activePokemonDresseur1.getHp() <= 0) {
            return dresseur2;
        } else if (activePokemonDresseur2.getHp() <= 0) {
            return dresseur1;
        }
        return null;
    }

    public void quit() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'quit'");
    }

}
