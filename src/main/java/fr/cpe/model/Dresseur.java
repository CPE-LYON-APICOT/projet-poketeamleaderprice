package fr.cpe.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dresseur {
    private Integer index;
    private String nom;
    private Map<Integer,Pokemon> pokemon;
    private Map<Integer, Item> items;

    public Dresseur(Integer index, String nom, Map<Integer, Pokemon> pokemon, Map<Integer, Item> items) {
        this.index = index;
        this.nom = nom;
        this.pokemon = pokemon;
        this.items = items;
    }

    public Dresseur() {
        this.pokemon = new HashMap<>();
        this.items = new HashMap<>();
    }

    public Pokemon getPokemonTeam(int index_List) {
        return this.pokemon.get(index_List);
    }

    @com.fasterxml.jackson.annotation.JsonSetter("pokemon")
    public void setPokemonFromJson(Map<String, Object> raw) {
        this.pokemon = new HashMap<>();
        if (raw == null) return;
        com.fasterxml.jackson.databind.ObjectMapper mapper = 
            new com.fasterxml.jackson.databind.ObjectMapper();
        for (Map.Entry<String, Object> entry : raw.entrySet()) {
            try {
                int key = Integer.parseInt(entry.getKey());
                if (entry.getValue() != null) {
                    Pokemon p = mapper.convertValue(entry.getValue(), Pokemon.class);
                    this.pokemon.put(key, p);
                }
            } catch (Exception e) {
                System.err.println("Erreur conversion pokemon clé " + entry.getKey() + ": " + e.getMessage());
            }
        }
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Map<Integer, Pokemon> getPokemon() {
        return pokemon;
    }

    public void addPokemon(Integer slot, Pokemon pokemon) {
        if (this.pokemon.size() > 6) {
            throw new IllegalStateException("Un dresseur ne peut pas avoir plus de 6 Pokémon.");
        } else {
            this.pokemon.put(slot, pokemon);
        }
    }

    public void setPokemon(Map<Integer, Pokemon> pokemon) {
        this.pokemon = pokemon;
    }

    public Map<Integer, Item> getItems() {
        return items;
    }

    public void setItems(Map<Integer, Item> items) {
        this.items = items;
    }

    public void addItem(Integer nb,Item item)
    {
        this.items.put(nb, item);
    }
}
