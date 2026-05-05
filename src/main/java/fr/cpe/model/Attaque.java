package fr.cpe.model;

public class Attaque {
    private int id;
    private String name;
    private int power;
    private int accuracy;
    private int pp;
    private Type type;

    public Attaque(int id, String name, int power, int accuracy, int pp, Type type) {
        this.id = id;
        this.name = name;
        this.power = power;
        this.accuracy = accuracy;
        this.pp = pp;
        this.type = type;
    }

    public Attaque() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPower() {
        return power;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getPp() {
        return pp;
    }

    public Type getType() {
        return type;
    }
}
