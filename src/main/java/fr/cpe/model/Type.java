package fr.cpe.model;

import java.util.List;

public class Type {
    private String index;
    private String nom;
    private List<Type> faiblesses;
    private List<Type> avantages;

    public Type() {
    }

    public Type(String index, String nom, List<Type> faiblesses, List<Type> avantages) {
        this.index = index;
        this.nom = nom;
        this.faiblesses = faiblesses;
        this.avantages = avantages;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
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
