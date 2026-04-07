package fr.cpe.Controller;

import fr.cpe.dao.PokemonDAO;
import fr.cpe.model.Dresseur;
import fr.cpe.model.Pokemon;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ChooseTeamController {
    public Button hostGameButton;
    public Button joinGameButton;
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
    public Button ItemsButton;
    public Button LeftButton;
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
        teamSlotButtons.addAll(Arrays.asList(teamSlot1, teamSlot2, teamSlot3, teamSlot4, teamSlot5, teamSlot6));
        for (Button slot : teamSlotButtons) {
            slot.setStyle(EMPTY_SLOT_STYLE);
            slot.setText("+");
            slot.setOnAction(e -> addPokemonToTeam(slot));
        }

        List<Pokemon> pokemons = new PokemonDAO().getAll();
        pokemonListView.setItems(FXCollections.observableArrayList(pokemons));
        pokemonListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Pokemon pokemon, boolean empty) {
                super.updateItem(pokemon, empty);
                if (empty || pokemon == null) {
                    setText(null);
                } else {
                    setText(pokemon.getNom());
                }
            }
        });

        pokemonListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedPokemon = newSelection;
            if (newSelection != null) {
                pokemonDescriptionArea.setText(newSelection.getDescription());
            } else {
                pokemonDescriptionArea.clear();
            }
        });
    }

    private Object addPokemonToTeam(Button slot) {
        if (selectedPokemon == null) {
            return null;
        }

        if (slot.getText().equals("+")) {
            slot.setText(selectedPokemon.getNom());
            slot.setStyle(FILLED_SLOT_STYLE);
        } else {
            slot.setText("+");
            slot.setStyle(EMPTY_SLOT_STYLE);
        }
        return null;
    }

    public void changeDresseurNom(ActionEvent actionEvent) {
        String newnom = playerNameField.getText();
        dresseur.setNom(newnom);
    }

    public void pressNextButton(ActionEvent event)
    {
        String fxmlPath = "/fr/cpe/views/ChooseItems.fxml";
        String title = "Poke-Cheap - Choisissez vos Items !";

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pressLeftButton(ActionEvent event)
    {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
