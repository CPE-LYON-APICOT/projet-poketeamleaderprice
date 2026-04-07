package fr.cpe.model;

public class Stade {
    private int id;
    private String nom;
    private Type type;

    public Stade(int id, String nom, Type type) {
        this.id = id;
        this.nom = nom;
        this.type = type;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
