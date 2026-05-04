package fr.cpe.model;

public class HealingItem extends Item {
    private Integer hpHeal;

    public HealingItem() {
        super();
    }

    public HealingItem(int id, String name, Integer hpHeal) {
        super(id, name);
        this.hpHeal = hpHeal;
    }

    public Integer getHpHeal() {
        return hpHeal;
    }

    public void setHpHeal(Integer hpHeal) {
        this.hpHeal = hpHeal;
    }
}
