package fr.cpe.model;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public class Pokemon {
    private int Num_Poke;
    private String nom;
    private List<Type> types;
    private Attaque[] lesattaquesdisponibles;
    private Attaque[] lesattaquesprises;
    private Integer hp;
    private Map<StatType, Integer> stats;
    private BufferedImage image_face;
    private BufferedImage image_dos;
    private BufferedImage sprite;
    private char description;
    private Abilite ability;

    public Pokemon(int num_Poke, String nom, List<Type> types, Attaque[] lesattaquesdisponibles, Attaque[] lesattaquesprises, int hp, Map<StatType, Integer> stats, String image_face, String image_dos, String sprite, String description, Abilite ability) {
        Num_Poke = num_Poke;
        this.nom = nom;
        this.types = types;
        this.lesattaquesdisponibles = lesattaquesdisponibles;
        this.lesattaquesprises = lesattaquesprises;
        this.hp = hp;
        this.stats = stats;
        this.image_face = image_face;
        this.image_dos = image_dos;
        this.sprite = sprite;
        this.description = description;
        this.ability = ability;
    }

    public Pokemon() {
    }

    public Integer getNum_Poke() {
        return Num_Poke;
    }

    public void setNum_Poke(Integer num_Poke) {
        Num_Poke = num_Poke;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public Attaque[] getLesattaquesdisponibles() {
        return lesattaquesdisponibles;
    }

    public void setLesattaquesdisponibles(Attaque[] lesattaquesdisponibles) {
        this.lesattaquesdisponibles = lesattaquesdisponibles;
    }

    public Attaque[] getLesattaquesprises() {
        return lesattaquesprises;
    }

    public void setLesattaquesprises(Attaque[] lesattaquesprises) {
        this.lesattaquesprises = lesattaquesprises;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public  Map<StatType, Integer> getStats() {
        return stats;
    }

    public void setStats(Map<StatType, Integer> stats) {
        this.stats = stats;
    }

    public BufferedImage getImage_face() {
        return image_face;
    }

    public void setImage_face(BufferedImage image_face) {
        this.image_face = image_face;
    }

    public BufferedImage getImage_dos() {
        return image_dos;
    }

    public void setImage_dos(BufferedImage image_dos) {
        this.image_dos = image_dos;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public char getDescription() {
        return description;
    }

    public void setDescription(char description) {
        this.description = description;
    }

    public Abilite getAbility() {
        return ability;
    }

    public void setAbility(Abilite ability) {
        this.ability = ability;
    }
}
