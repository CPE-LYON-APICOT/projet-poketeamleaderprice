package fr.cpe.model;

public class Attaque {
    private int id;
    private String name;
    private int power;
    private int accuracy;
    private int pp;
    private Type type;

    public Attaque() {
    }

    public Attaque(int id, String name, int power, int accuracy, int pp, Type type) {
        this.id = id;
        this.name = name;
        this.power = power;
        this.accuracy = accuracy;
        this.pp = pp;
        this.type = type;
    }

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

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
