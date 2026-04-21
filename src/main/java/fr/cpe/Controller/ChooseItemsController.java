package fr.cpe.Controller;

import fr.cpe.model.Dresseur;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ChooseItemsController {
    public Button backToTeamButton;
    public ListView itemListView;
    public TextArea itemDescriptionArea;
    public Button hostGameButton;
    public Button joinGameButton;


    private Dresseur dresseur;

    public void initialize(Dresseur dresseur){
        this.dresseur = dresseur;

    }
}
