package fr.cpe.Controller;

import fr.cpe.dao.PokemonDAO;
import fr.cpe.model.Dresseur;
import fr.cpe.model.Pokemon;
import fr.cpe.model.Type;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChooseTeamController {
    public TextField playerNameField;
    public Button teamSlot1;
    public Button teamSlot2;
    public Button teamSlot3;
    public Button teamSlot4;
    public Button teamSlot5;
    public Button teamSlot6;

    public TableView<Pokemon> pokemonTableView;
    public TableColumn<Pokemon, String> nomColumn;
    public TableColumn<Pokemon, String> typeColumn;
    public TableColumn<Pokemon, String> spriteColumn;
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
        //Boutons de sélections de Team
        teamSlotButtons.addAll(Arrays.asList(teamSlot1, teamSlot2, teamSlot3, teamSlot4, teamSlot5, teamSlot6));
        for (Button slot : teamSlotButtons) {
            slot.setStyle(EMPTY_SLOT_STYLE);
            slot.setText("+");
            slot.setGraphic(null);
            slot.setOnAction(e -> {
                try {
                    ChooseAttack(slot);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

        //Configuration des colonnes du Sprite
        spriteColumn.setCellValueFactory(new PropertyValueFactory<>("sprite"));
        spriteColumn.setCellFactory(col -> new TableCell<>() {
            protected void updateItem(String path, boolean empty) {
                super.updateItem(path, empty);
                if (empty || path == null || path.isBlank()) {
                    setGraphic(null);
                    return;
                }

                String resolvedPath = resolveSpritePath(path);
                var spriteUrl = getClass().getResource(resolvedPath);
                if (spriteUrl == null) {
                    setGraphic(null);
                    return;
                }

                ImageView imageView = new ImageView(new Image(spriteUrl.toExternalForm()));
                imageView.setFitWidth(48);
                imageView.setFitHeight(48);
                imageView.setPreserveRatio(true);
                setGraphic(imageView);
            }
        });

        // Configuration des colonnes du TableView
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));

        // Colonnes avec Types
        typeColumn.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getTypes() == null || cellData.getValue().getTypes().isEmpty() ? "N/A"
                : cellData.getValue().getTypes().stream().map(Type::getNom).collect(Collectors.joining(", "))
            )
        );

        // Chargement des pokémons et ajout à la TableView
        List<Pokemon> pokemons = new PokemonDAO().getAll();
        pokemonTableView.setItems(FXCollections.observableArrayList(pokemons));

        // Listener pour la sélection d'une ligne
        pokemonTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedPokemon = newSelection;
            if (newSelection != null) {
                pokemonDescriptionArea.setText(newSelection.getDescription());
            } else {
                pokemonDescriptionArea.clear();
            }
        });
    }

    private String resolveSpritePath(String rawPath) {
        String normalized = rawPath.startsWith("/") ? rawPath : "/fr/cpe/" + rawPath;

        if (getClass().getResource(normalized) != null) {
            return normalized;
        }

        if (normalized.endsWith(".png") && !normalized.endsWith("_mini.png")) {
            String withMiniSuffix = normalized.substring(0, normalized.length() - 4) + "_mini.png";
            if (getClass().getResource(withMiniSuffix) != null) {
                return withMiniSuffix;
            }
        }

        return normalized;
    }

    private void ChooseAttack(Button slot) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fr/cpe/views/ChooseAttack.fxml"));
        Parent root1 = fxmlLoader.load();

        ChooseAttackController controller = fxmlLoader.getController();
        controller.setPokemon(selectedPokemon);

        Stage stage = new Stage();
        stage.initOwner(((Node) slot).getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root1));
        stage.setTitle("Poke-Cheap - Choisis tes attaques");
        stage.showAndWait();

        if (controller.isValidated()) {
            addPokemonToTeam(slot);
        }
    }
    private void addPokemonToTeam(Button slot) throws IOException {
        if (selectedPokemon == null) {
            return;
        }

        boolean isEmptySlot = slot.getGraphic() == null && "+".equals(slot.getText());

        if (isEmptySlot) {
            String resolvedPath = resolveSpritePath(selectedPokemon.getSprite());
            var spriteUrl = getClass().getResource(resolvedPath);
            if (spriteUrl == null) {
                return;
            }

            ImageView spriteView = new ImageView(new Image(spriteUrl.toExternalForm()));
            spriteView.setFitWidth(60);
            spriteView.setFitHeight(60);
            spriteView.setPreserveRatio(true);

            slot.setGraphic(spriteView);
            slot.setText("");
            slot.setStyle(FILLED_SLOT_STYLE);
        } else {
            slot.setGraphic(null);
            slot.setText("+");
            slot.setStyle(EMPTY_SLOT_STYLE);
        }
    }

    public void changeDresseurNom(ActionEvent actionEvent) {
        String newnom = playerNameField.getText();

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
