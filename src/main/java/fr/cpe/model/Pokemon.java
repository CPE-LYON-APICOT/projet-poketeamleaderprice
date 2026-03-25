package fr.cpe.model;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public class Pokemon {
    private Integer Num_Poke;
    private String nom;
    private List<Type> types;
    private Attaque[] lesattaquesdisponibles;
    private Attaque[] lesattaquesprises;
    private Integer hp;
    private PokeStat attack;
    private PokeStat defense;
    private PokeStat spAttack;
    private PokeStat spDefense;
    private PokeStat speed;
    private BufferedImage image;
    private BufferedImage sprite;
    private char description;
    private Abilite ability;
}
