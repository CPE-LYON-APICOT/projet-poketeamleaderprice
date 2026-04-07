package fr.cpe.Controller;

import fr.cpe.db.PokemonDAO;
import fr.cpe.model.Pokemon;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private Pokemon selectedPokemon;

    private static final String EMPTY_SLOT_STYLE = "-fx-font-size: 34px; -fx-font-weight: bold; -fx-text-fill: #2a75bb; -fx-background-color: linear-gradient(to bottom, #ffffff 0%, #d8ecff 100%); -fx-border-color: #2a75bb; -fx-border-width: 4; -fx-background-radius: 18; -fx-border-radius: 18;";
    private static final String FILLED_SLOT_STYLE = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: linear-gradient(to bottom, #3b82f6 0%, #1d4ed8 100%); -fx-border-color: #facc15; -fx-border-width: 4; -fx-background-radius: 18; -fx-border-radius: 18;";

    public void initialize() {
        teamSlotButtons.addAll(Arrays.asList(teamSlot1, teamSlot2, teamSlot3, teamSlot4, teamSlot5, teamSlot6));

        for (int i = 0; i < teamSlotButtons.size(); i++) {
            final int index = i;
            Button slot = teamSlotButtons.get(i);
            slot.setText("+");
            slot.setStyle(EMPTY_SLOT_STYLE);
            slot.setOnAction(event -> assignSelectedPokemonToSlot(index));
        }

        pokemonListView.setItems(FXCollections.observableArrayList(new PokemonDAO().getAll()));
        pokemonListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Pokemon item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNom());
            }
        });

        pokemonListView.getSelectionModel().selectedItemProperty().addListener((obs, oldPokemon, newPokemon) -> {
            selectedPokemon = newPokemon;
            if (newPokemon == null) {
                pokemonDescriptionArea.clear();
            } else {
                pokemonDescriptionArea.setText(buildPokemonDescription(newPokemon));
            }
        });

        if (!pokemonListView.getItems().isEmpty()) {
            pokemonListView.getSelectionModel().selectFirst();
        }
    }

    private void assignSelectedPokemonToSlot(int slotIndex) {
        if (selectedPokemon == null) {
            pokemonDescriptionArea.setText("Sélectionne d'abord un Pokémon dans la liste.");
            return;
        }

        Button slot = teamSlotButtons.get(slotIndex);
        slot.setText(selectedPokemon.getNom());
        slot.setStyle(FILLED_SLOT_STYLE);
    }

    private String buildPokemonDescription(Pokemon pokemon) {
        return pokemon.getNom()
                + "\nHP : " + pokemon.getHp()
                + "\n"
                + (pokemon.getDescription() == null ? "" : pokemon.getDescription());
    }
}
