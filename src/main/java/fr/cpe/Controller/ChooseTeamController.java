package fr.cpe.Controller;

import fr.cpe.dao.PokemonDAO;
import fr.cpe.model.Dresseur;
import fr.cpe.model.Pokemon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class ChooseTeamController {
    public Button ItemsButton;
    public Button LeftButton;
    public TextField playerNameField;
    public Button teamSlot1;
    public Button teamSlot2;
    public Button teamSlot3;
    public Button teamSlot4;
    public Button teamSlot5;
    public Button teamSlot6;

    public ListView<Pokemon> pokemonListView;
    public TextArea pokemonDescriptionArea;

    private final List<Button> teamSlotButtons = new ArrayList<>();
    private Pokemon selectedPokemon;

    private Dresseur dresseur;

    private static final String EMPTY_SLOT_STYLE = "-fx-font-size: 34px; -fx-font-weight: bold; -fx-text-fill: #2a75bb; -fx-background-color: linear-gradient(to bottom, #ffffff 0%, #d8ecff 100%); -fx-border-color: #2a75bb; -fx-border-width: 4; -fx-background-radius: 18; -fx-border-radius: 18;";
    private static final String FILLED_SLOT_STYLE = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: linear-gradient(to bottom, #3b82f6 0%, #1d4ed8 100%); -fx-border-color: #facc15; -fx-border-width: 4; -fx-background-radius: 18; -fx-border-radius: 18;";


    public void setDresseur(Dresseur dresseur)
    {
        this.dresseur =  dresseur;
        initialize();
    }

    public void initialize() {
        try {


            PokemonDAO pokemonDAO = new PokemonDAO();
            ObservableList<Pokemon> items = FXCollections.observableArrayList(pokemonDAO.getAll());
            pokemonListView.setItems(items);
            pokemonListView.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(Pokemon p, boolean empty) {
                    super.updateItem(p, empty);
                    setText(empty || p == null ? null : p.getNom());
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void changeDresseurNom(ActionEvent actionEvent) {
        String newnom = playerNameField.getText();
        dresseur.setNom(newnom);

    }

}
