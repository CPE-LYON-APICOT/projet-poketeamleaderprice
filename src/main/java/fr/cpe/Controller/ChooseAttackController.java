package fr.cpe.Controller;

import fr.cpe.model.Attaque;
import fr.cpe.model.Pokemon;
import fr.cpe.model.Type;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChooseAttackController {

    public Button addAttackButton;
    public Button removeAttackButton;
    public Button clearButton;
    public Button cancelButton;
    @FXML private Label pokemonNameLabel;
	@FXML private Label pokemonTypeLabel;
	@FXML private Label pokemonDescriptionLabel;
	@FXML private Label attackNameValueLabel;
	@FXML private Label attackTypeValueLabel;
	@FXML private Label attackPowerValueLabel;
	@FXML private Label attackAccuracyValueLabel;
	@FXML private Label attackPpValueLabel;
	@FXML private Label selectionCountLabel;
	@FXML private Label statusLabel;
	@FXML private ListView<Attaque> availableAttacksListView;
	@FXML private ListView<Attaque> selectedAttacksListView;
	@FXML private Button validateButton;

	private final ObservableList<Attaque> availableAttacks = FXCollections.observableArrayList();
	private final ObservableList<Attaque> selectedAttacks = FXCollections.observableArrayList();

	private Pokemon pokemon;
	private boolean validated;

	@FXML
	private void initialize() {
		availableAttacksListView.setItems(availableAttacks);
		selectedAttacksListView.setItems(selectedAttacks);

		availableAttacksListView.setCellFactory(list -> new ListCell<>() {
			@Override
			protected void updateItem(Attaque item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty || item == null ? null : formatAttackSummary(item));
			}
		});

		selectedAttacksListView.setCellFactory(list -> new ListCell<>() {
			@Override
			protected void updateItem(Attaque item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty || item == null ? null : (getIndex() + 1) + ". " + formatAttackSummary(item));
			}
		});

		availableAttacksListView.getSelectionModel().selectedItemProperty().addListener((obs, oldAttack, newAttack) ->
			updateAttackDetails(newAttack)
		);

		selectedAttacksListView.getSelectionModel().selectedItemProperty().addListener((obs, oldAttack, newAttack) -> {
			if (newAttack != null) {
				updateAttackDetails(newAttack);
			}
		});

		updateAttackDetails(null);
		updateSelectionState();
		statusLabel.setText("Sélectionne 4 attaques pour valider.");
	}

	public void setPokemon(Pokemon pokemon) {
		this.pokemon = pokemon;
		refreshPokemonData();
	}

	public boolean isValidated() {
		return validated;
	}


	private void refreshPokemonData() {
		validated = false;
		selectedAttacks.clear();
		availableAttacks.clear();

		if (pokemon == null) {
			pokemonNameLabel.setText("Choisis tes 4 attaques");
			pokemonTypeLabel.setText("Sélectionne les attaques du pokémon actif");
			pokemonDescriptionLabel.setText("Ajoute les attaques une par une pour composer ton set de combat.");
			statusLabel.setText("Aucun pokémon sélectionné.");
			updateSelectionState();
			updateAttackDetails(null);
			return;
		}

		pokemonNameLabel.setText(pokemon.getNom());
		pokemonTypeLabel.setText(formatTypes(pokemon.getTypes()));
		pokemonDescriptionLabel.setText(
			pokemon.getDescription() == null || pokemon.getDescription().isBlank()
				? "Sélectionne 4 attaques pour composer ton set de combat."
				: pokemon.getDescription()
		);

		if (pokemon.getLesattaquesdisponibles() != null) {
			availableAttacks.addAll(Arrays.asList(pokemon.getLesattaquesdisponibles()));
		}

		if (pokemon.getLesattaquesprises() != null) {
			selectedAttacks.addAll(Arrays.asList(pokemon.getLesattaquesprises()));
		}

		if (availableAttacks.isEmpty()) {
			statusLabel.setText("Aucune attaque disponible pour ce pokémon.");
		} else {
			statusLabel.setText("Sélectionne 4 attaques pour valider.");
		}

		availableAttacksListView.getSelectionModel().clearSelection();
		selectedAttacksListView.getSelectionModel().clearSelection();
		updateSelectionState();
		updateAttackDetails(null);
	}

	@FXML
	private void handleAddAttack() {
		Attaque selectedAttack = availableAttacksListView.getSelectionModel().getSelectedItem();
		if (selectedAttack == null) {
			statusLabel.setText("Choisis une attaque dans la liste de gauche.");
			return;
		}

		if (selectedAttacks.contains(selectedAttack)) {
			statusLabel.setText("Cette attaque est déjà sélectionnée.");
			return;
		}

		if (selectedAttacks.size() >= 4) {
			statusLabel.setText("Tu ne peux choisir que 4 attaques maximum.");
			return;
		}

		selectedAttacks.add(selectedAttack);
		selectedAttacksListView.getSelectionModel().select(selectedAttack);
		statusLabel.setText(selectedAttack.getName() + " a été ajoutée.");
		updateSelectionState();
	}

	@FXML
	private void handleRemoveAttack() {
		Attaque selectedAttack = selectedAttacksListView.getSelectionModel().getSelectedItem();
		if (selectedAttack == null) {
			statusLabel.setText("Choisis une attaque à retirer dans la liste de droite.");
			return;
		}

		selectedAttacks.remove(selectedAttack);
		statusLabel.setText(selectedAttack.getName() + " a été retirée.");
		updateSelectionState();
		updateAttackDetails(selectedAttacksListView.getSelectionModel().getSelectedItem());
	}

	@FXML
	private void handleClearSelection() {
		selectedAttacks.clear();
		selectedAttacksListView.getSelectionModel().clearSelection();
		statusLabel.setText("Sélection réinitialisée.");
		updateSelectionState();
		updateAttackDetails(null);
	}

	@FXML
	private void handleValidate() {
		if (selectedAttacks.size() != 4) {
			statusLabel.setText("Tu dois choisir exactement 4 attaques.");
			return;
		}

		if (pokemon != null) {
			pokemon.setLesattaquesprises(selectedAttacks.toArray(new Attaque[0]));
		}

		validated = true;
		closeWindow();
	}

	@FXML
	private void handleClose() {
		validated = false;
		closeWindow();
	}

	private void updateSelectionState() {
		selectionCountLabel.setText(selectedAttacks.size() + " / 4");
		validateButton.setDisable(selectedAttacks.size() != 4);
	}

	private void updateAttackDetails(Attaque attack) {
		if (attack == null) {
			attackNameValueLabel.setText("-");
			attackTypeValueLabel.setText("-");
			attackPowerValueLabel.setText("-");
			attackAccuracyValueLabel.setText("-");
			attackPpValueLabel.setText("-");
			return;
		}

		attackNameValueLabel.setText(attack.getName());
		attackTypeValueLabel.setText(attack.getType() == null ? "Inconnu" : attack.getType().getNom());
		attackPowerValueLabel.setText(String.valueOf(attack.getPower()));
		attackAccuracyValueLabel.setText(String.valueOf(attack.getAccuracy()));
		attackPpValueLabel.setText(String.valueOf(attack.getPp()));
	}

	private String formatAttackSummary(Attaque attack) {
		return attack.getName() + " • " + typeName(attack) + " • Puissance " + attack.getPower();
	}

	private String typeName(Attaque attack) {
		return attack.getType() == null ? "Inconnu" : attack.getType().getNom();
	}

	private String formatTypes(List<Type> types) {
		if (types == null || types.isEmpty()) {
			return "Type : inconnu";
		}

		return "Type : " + types.stream()
			.map(Type::getNom)
			.collect(Collectors.joining(", "));
	}

	private void closeWindow() {
		Stage stage = (Stage) validateButton.getScene().getWindow();
		stage.close();
	}
}

