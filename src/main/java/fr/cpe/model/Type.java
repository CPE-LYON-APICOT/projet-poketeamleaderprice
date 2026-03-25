package fr.cpe.model;

import java.util.List;

public class Type {
    private String index;
    private String nom;
    private List<Type> faiblesse;
    private List<Type> avantages;

    public Type() {
    }

    public Type(String index, String nom, List<Type> faiblesse, List<Type> avantages) {
        this.index = index;
        this.nom = nom;
        this.faiblesse = faiblesse;
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

    public List<Type> getFaiblesse() {
        return faiblesse;
    }

    public void setFaiblesse(List<Type> faiblesse) {
        this.faiblesse = faiblesse;
    }

    public List<Type> getAvantages() {
        return avantages;
    }

    public void setAvantages(List<Type> avantages) {
        this.avantages = avantages;
    }
}
