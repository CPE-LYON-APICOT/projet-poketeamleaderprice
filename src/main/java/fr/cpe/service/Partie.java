package fr.cpe.service;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private List<CommandService> commandServices;
    private static Partie instance;

    @Inject
    private Partie() {
        this.commandServices = new ArrayList<>();
        instance = this;
    }

    public static Partie getInstance() {
        if (instance == null) {
            instance = new Partie();
            return instance;
        }
        return instance;
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
        int degats = attaque.getPower() - this.getActivePokemonOf(dresseur).getStats().get(StatType.Def);
        if (degats < 0) {
            degats = 0;
        }
        this.getActivePokemonOf(dresseur).setHp(this.getActivePokemonOf(dresseur).getHp() - degats);
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
