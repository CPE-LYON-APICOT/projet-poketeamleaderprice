package fr.cpe.model;

import java.util.List;

public class PartieService {
    private String index;
    private List<Dresseur> dresseurs;
    private Stade stade;

    public PartieService(String index, List<Dresseur> dresseurs, Stade stade) {
        this.index = index;
        this.dresseurs = dresseurs;
        this.stade = stade;
    }

    public PartieService() {
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public List<Dresseur> getDresseurs() {
        return dresseurs;
    }

    public void addDresseur(Dresseur dresseur)
    {
        if (this.dresseurs.size() > 3) {
            throw new IllegalStateException("Une partie ne peut avoir que 2 dresseurs ");
        } else {
            this.dresseurs.add(dresseur);
        }
    }

    public void setDresseurs(List<Dresseur> dresseurs) {
        this.dresseurs = dresseurs;
    }

    public Stade getStade() {
        return stade;
    }

    public void setStade(Stade stade) {
        this.stade = stade;
    }
}
