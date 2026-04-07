package fr.cpe.model;

public class Abilite {
    private String index;
    private String nom;

    public Abilite(String index, String nom) {
        this.index = index;
        this.nom = nom;
    }

    public Abilite() {
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
}
