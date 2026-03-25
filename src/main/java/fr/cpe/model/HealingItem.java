package fr.cpe.model;

public class HealingItem extends Item {
    private Integer hpHeal;

    public HealingItem(Integer hpHeal) {
        this.hpHeal = hpHeal;
    }

    public HealingItem() {
    }

    public Integer getHpHeal() {
        return hpHeal;
    }

    public void setHpHeal(Integer hpHeal) {
        this.hpHeal = hpHeal;
    }
}
