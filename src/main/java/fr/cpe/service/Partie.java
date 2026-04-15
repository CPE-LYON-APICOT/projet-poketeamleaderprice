package fr.cpe.service;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    private Partie(Dresseur dresseur1, Dresseur dresseur2, Stade stade) {
        this.dresseur1 = dresseur1;
        this.dresseur2 = dresseur2;
        this.stade = stade;
        this.activePokemonDresseur1 = dresseur1.getPokemon().get(0);
        this.activePokemonDresseur2 = dresseur2.getPokemon().get(0);
        this.commandServices = new ArrayList<>();
        this.commandServices.add(new ConnectionService(new CommandExecutor(), new MessageStore()));
        this.commandServices.add(new PartieService(new CommandExecutor(), new MessageStore()));
    }

    public static Partie createPartie(Dresseur dresseur1, List<Pokemon> pokemonsDresseur1, Dresseur dresseur2, List<Pokemon> pokemonsDresseur2, Stade stade) {
        instance = new Partie(dresseur1, dresseur2, stade);
        return instance;
    }

    public static Partie getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Partie instance has not been created yet.");
        }
        return instance;
    }


    public Dresseur getDresseur1() {
        return dresseur1;
    }

    public Dresseur getDresseur2() {
        return dresseur2;
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

    public Stade getStade() {
        return stade;
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
