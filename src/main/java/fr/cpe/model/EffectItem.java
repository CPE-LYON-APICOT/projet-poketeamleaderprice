package fr.cpe.model;

import java.util.Map;

public class EffectItem extends Item {
    private Map<StatType, Integer> affectedStat;

    public EffectItem() {
        super();
    }

    public EffectItem(int id, String name, Map<StatType, Integer> affectedStat) {
        super(id, name);
        this.affectedStat = affectedStat;
    }
    
    public Map<StatType, Integer> getAffectedStat() {
        return affectedStat;
    }

    public void setAffectedStat(Map<StatType, Integer> affectedStat) {
        this.affectedStat = affectedStat;
    }
}
