package fr.cpe.model;

import java.util.ArrayList;
import java.util.List;

public class Type {
    private int id;
    private String nom;
    private List<Type> faiblesses;
    private List<Type> avantages;


    public Type(int id, String nom) {
        this.id = id;
        this.nom = nom;
        this.faiblesses = new ArrayList<>();
        this.avantages = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Type> getFaiblesses() {
        return faiblesses;
    }

    public void setFaiblesses(List<Type> faiblesses) {
        this.faiblesses = faiblesses;
    }

    public List<Type> getAvantages() {
        return avantages;
    }

    public void setAvantages(List<Type> avantages) {
        this.avantages = avantages;
    }
}
