package fr.cpe.model;

import java.util.Map;

public class EffectItem extends Item {
    private Map<StatType, Integer> affectedStat;

    public EffectItem(Map<StatType, Integer> affectedStat) {
        this.affectedStat = affectedStat;
    }

    public EffectItem() {
    }

    public Map<StatType, Integer> getAffectedStat() {
        return affectedStat;
    }

    public void setAffectedStat(Map<StatType, Integer> affectedStat) {
        this.affectedStat = affectedStat;
    }
}
