package fr.cpe.model;

public class Stade {
    private String index;
    private String nom;
    private Type type;

    public Stade(String index, String nom, Type type) {
        this.index = index;
        this.nom = nom;
        this.type = type;
    }

    public Stade() {
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
