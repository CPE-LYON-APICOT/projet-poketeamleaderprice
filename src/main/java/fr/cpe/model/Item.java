package fr.cpe.model;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "itemType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = HealingItem.class, name = "healing"),
    @JsonSubTypes.Type(value = EffectItem.class, name = "effect")
})
public abstract class Item {
    private int id;
    private String nom;

    public Item(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Item() {}

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
}
