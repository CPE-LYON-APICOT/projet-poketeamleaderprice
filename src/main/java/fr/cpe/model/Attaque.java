package fr.cpe.model;

public class Attaque {
    private String index;
    private String nom;
    private String degat;

    public Attaque(String index, String nom, String degat) {
        this.index = index;
        this.nom = nom;
        this.degat = degat;
    }

    public Attaque() {
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

    public String getDegat() {
        return degat;
    }

    public void setDegat(String degat) {
        this.degat = degat;
    }
}
