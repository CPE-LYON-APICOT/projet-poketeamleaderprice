package fr.cpe.model;

public abstract class Item {
    private String index;
    private String nom;

    public Item(String index, String nom) {
        this.index = index;
        this.nom = nom;
    }

    public Item() {
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
