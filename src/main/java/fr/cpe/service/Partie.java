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

    // ── Getters / Setters ────────────────────────────────────────────────────

    public Dresseur getDresseur1() { return dresseur1; }
    public Partie setDresseur1(Dresseur d) { this.dresseur1 = d; return this; }

    public Dresseur getDresseur2() { return dresseur2; }
    public Partie setDresseur2(Dresseur d) { this.dresseur2 = d; return this; }

    public Stade getStade() { return stade; }
    public Partie setStade(Stade s) { this.stade = s; return this; }

    public Dresseur getCurrentAttacker() { return currentAttacker; }
    public Partie setCurrentAttacker(Dresseur d) { this.currentAttacker = d; return this; }

    // ── Pokémon actifs ───────────────────────────────────────────────────────

    public Pokemon getActivePokemonOf(Dresseur dresseur) {
        if (dresseur == dresseur1) return activePokemonDresseur1;
        if (dresseur == dresseur2) return activePokemonDresseur2;
        throw new IllegalArgumentException("Dresseur inconnu");
    }

    public Partie setActivePokemonOf(Dresseur dresseur, Pokemon pokemon) {
        if (dresseur == dresseur1) activePokemonDresseur1 = pokemon;
        else if (dresseur == dresseur2) activePokemonDresseur2 = pokemon;
        else throw new IllegalArgumentException("Dresseur inconnu");
        return this;
    }

    // ── Lookup ───────────────────────────────────────────────────────────────

    public Dresseur getDresseurFromId(Integer id) {
        if (dresseur1.getIndex().equals(id)) return dresseur1;
        if (dresseur2.getIndex().equals(id)) return dresseur2;
        throw new IllegalArgumentException("Dresseur with id " + id + " not found");
    }

    public Dresseur getOpponent(Dresseur dresseur) {
        if (dresseur == dresseur1) return dresseur2;
        if (dresseur == dresseur2) return dresseur1;
        throw new IllegalArgumentException("Dresseur inconnu");
    }

    // ── Tour de jeu ──────────────────────────────────────────────────────────

    public Dresseur nextAttacker() {
        if (currentAttacker == null) return null;
        currentAttacker = (currentAttacker == dresseur1) ? dresseur2 : dresseur1;
        return currentAttacker;
    }

    // ── Actions ──────────────────────────────────────────────────────────────

    public void attack(Dresseur dresseur, Attaque attaque) {
        Dresseur cible = getOpponent(dresseur);
        Pokemon pokemonCible = getActivePokemonOf(cible);

        int defense = 0;
        if (pokemonCible.getStats() != null && pokemonCible.getStats().get(StatType.Def) != null) {
            defense = pokemonCible.getStats().get(StatType.Def);
        }
        int degats = Math.max(1, attaque.getPower() - defense / 3);
        pokemonCible.setHp(Math.max(0, pokemonCible.getHp() - degats));
    }

    /**
     * Change le Pokémon actif du dresseur.
     * Lève une exception si le Pokémon choisi est K.O.
     */
    public void changePokemon(Dresseur dresseur, Pokemon nouveauPokemon) {
        if (nouveauPokemon == null)
            throw new IllegalArgumentException("Le nouveau Pokémon ne peut pas être nul");
        if (nouveauPokemon.getHp() <= 0)
            throw new IllegalArgumentException("Impossible de choisir un Pokémon K.O. : " + nouveauPokemon.getNom());
        setActivePokemonOf(dresseur, nouveauPokemon);
    }

    public void useItem() {
        // TODO: logique d'utilisation d'objet
    }

    // ── Vérifications KO / fin de partie ────────────────────────────────────

    /** Renvoie true si le Pokémon a ses PV à 0. */
    public boolean isKO(Pokemon pokemon) {
        return pokemon != null && pokemon.getHp() <= 0;
    }

    /** Renvoie true si le dresseur a encore au moins un Pokémon vivant. */
    public boolean hasAlivePokemon(Dresseur dresseur) {
        if (dresseur == null || dresseur.getPokemon() == null) return false;
        return dresseur.getPokemon().values().stream().anyMatch(p -> p.getHp() > 0);
    }

    /**
     * Renvoie le vainqueur si l'un des dresseurs n'a plus de Pokémon,
     * ou null si la partie continue.
     */
    public Dresseur checkWinner() {
        if (!hasAlivePokemon(dresseur1)) return dresseur2;
        if (!hasAlivePokemon(dresseur2)) return dresseur1;
        return null;
    }

    public void quit() {
        throw new UnsupportedOperationException("Unimplemented method 'quit'");
    }
}
