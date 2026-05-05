package fr.cpe.Controller;

import com.google.inject.Inject;
import fr.cpe.App;
import fr.cpe.dao.EffectItemDAO;
import fr.cpe.dao.HealingItemDAO;
import fr.cpe.dao.StadeDAO;
import fr.cpe.model.Dresseur;
import fr.cpe.model.EffectItem;
import fr.cpe.model.HealingItem;
import fr.cpe.model.Item;
import fr.cpe.model.StatType;
import fr.cpe.model.Stade;
import fr.cpe.service.ConnectionService;
import fr.cpe.service.Partie;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class ChooseItemsController {
    public Button backToTeamButton;
    public ListView<String> itemListView;
    public TextArea itemDescriptionArea;
    public VBox itemDetailsBox;
    public Label selectedItemNameLabel;
    public TextField quantityField;
    public Button decreaseQuantityButton;
    public Button increaseQuantityButton;
    public Button addItemButton;
    public Button hostGameButton;
    public Button joinGameButton;
    public Button LeftButton;

    private Dresseur dresseur;
    private final Map<String, Item> availableItemsByName = new HashMap<>();
    private Item selectedItem;
    private int selectedQuantity = 1;

    @Inject
    private ConnectionService connectionService;

    @Inject
    private Partie partie;

    public void initialize(Dresseur dresseur) {
        this.dresseur = dresseur;

        EffectItemDAO effectItemDAO = new EffectItemDAO();
        HealingItemDAO healingItemDAO = new HealingItemDAO();

        List<String> items = new ArrayList<>();
        availableItemsByName.clear();

        for (EffectItem item : effectItemDAO.getAll()) {
            availableItemsByName.put(item.getNom(), item);
            items.add(item.getNom());
        }

        for (HealingItem item : healingItemDAO.getAll()) {
            availableItemsByName.put(item.getNom(), item);
            items.add(item.getNom());
        }

        itemListView.getItems().setAll(items);
        itemDescriptionArea.setText("Clique sur un item pour afficher ses détails.");
        selectedItemNameLabel.setText("Sélectionne un item");
        setQuantity(1);
        hideItemDetails();
        updateQuantityControls();

        // Autorise uniquement les chiffres dans le champ quantité.
        UnaryOperator<TextFormatter.Change> digitsOnlyFilter = change -> {
            String next = change.getControlNewText();
            return next.matches("\\d*") ? change : null;
        };

        quantityField.setTextFormatter(new TextFormatter<>(digitsOnlyFilter));
        quantityField.setOnAction(event -> commitQuantityFromField());
        quantityField.focusedProperty().addListener((obs, oldFocused, newFocused) -> {
            if (!newFocused) {
                commitQuantityFromField();
            }
        });

        itemListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                clearSelection();
            } else {
                selectItem(newSelection);
            }
        });
    }

    public void decreaseQuantity(ActionEvent event) {
        setQuantity(selectedQuantity - 1);
    }

    public void increaseQuantity(ActionEvent event) {
        setQuantity(selectedQuantity + 1);
    }

    public void pressAddItemButton(ActionEvent event) {
        if (selectedItem == null) {
            itemDescriptionArea.setText("Sélectionne un item avant de l'ajouter au sac.");
            return;
        }

        if (dresseur.getItems() == null) {
            dresseur.setItems(new HashMap<>());
        }

        int currentTotal = dresseur.getTotalItemQuantity();
        if (currentTotal + selectedQuantity > 20) {
            itemDescriptionArea.setText("Le sac du dresseur ne peut pas dépasser 20 objets. Total actuel : "
                    + currentTotal + ", tentative d'ajout : " + selectedQuantity + ".");
            return;
        }

        Integer existingQuantityKey = null;
        for (Map.Entry<Integer, Item> entry : dresseur.getItems().entrySet()) {
            if (entry.getValue() != null && entry.getValue().getId() == selectedItem.getId()) {
                existingQuantityKey = entry.getKey();
                break;
            }
        }

        int totalQuantity = selectedQuantity;
        if (existingQuantityKey != null) {
            totalQuantity += existingQuantityKey;
            dresseur.getItems().remove(existingQuantityKey);
        }

        dresseur.addItem(totalQuantity, selectedItem);

        itemDescriptionArea.setText(buildItemDescription(selectedItem)
                + "\n\n" + selectedQuantity + " x " + selectedItem.getNom() + " ajouté(s) au sac.");
    }

    private void selectItem(String itemName) {
        selectedItem = availableItemsByName.get(itemName);
        selectedQuantity = 1;
        selectedItemNameLabel.setText(itemName);
        quantityField.setText("1");
        itemDescriptionArea.setText(buildItemDescription(selectedItem));
        showItemDetails();
        updateQuantityControls();
    }

    private void clearSelection() {
        selectedItem = null;
        selectedQuantity = 1;
        selectedItemNameLabel.setText("Sélectionne un item");
        quantityField.setText("1");
        itemDescriptionArea.setText("Clique sur un item pour afficher ses détails.");
        hideItemDetails();
        updateQuantityControls();
    }

    private void setQuantity(int quantity) {
        selectedQuantity = Math.max(1, quantity);
        quantityField.setText(String.valueOf(selectedQuantity));
        updateQuantityControls();
    }

    private void commitQuantityFromField() {
        if (selectedItem == null) {
            quantityField.setText("1");
            return;
        }

        String text = quantityField.getText();
        if (text == null || text.isBlank()) {
            setQuantity(1);
            return;
        }

        try {
            setQuantity(Integer.parseInt(text));
        } catch (NumberFormatException e) {
            setQuantity(selectedQuantity);
        }
    }

    private void updateQuantityControls() {
        if (decreaseQuantityButton != null) {
            decreaseQuantityButton.setDisable(selectedItem == null || selectedQuantity <= 1);
        }
        if (increaseQuantityButton != null) {
            increaseQuantityButton.setDisable(selectedItem == null);
        }
        if (addItemButton != null) {
            addItemButton.setDisable(selectedItem == null);
        }
        if (quantityField != null) {
            quantityField.setDisable(selectedItem == null);
        }
    }

    private void showItemDetails() {
        if (itemDetailsBox != null) {
            itemDetailsBox.setVisible(true);
            itemDetailsBox.setManaged(true);
        }
    }

    private void hideItemDetails() {
        if (itemDetailsBox != null) {
            itemDetailsBox.setVisible(false);
            itemDetailsBox.setManaged(false);
        }
    }

    private String buildItemDescription(Item item) {
        if (item == null) {
            return "Aucune description disponible.";
        }

        StringBuilder description = new StringBuilder();
        description.append("Nom : ").append(item.getNom()).append('\n');
        description.append("ID : ").append(item.getId()).append('\n');

        if (item instanceof HealingItem healingItem) {
            description.append("Type : Objet de soin\n");
            description.append("Soigne : ").append(healingItem.getHpHeal()).append(" PV");
        } else if (item instanceof EffectItem effectItem) {
            description.append("Type : Objet d'effet\n");
            description.append("Bonus : ");

            if (effectItem.getAffectedStat() == null || effectItem.getAffectedStat().isEmpty()) {
                description.append("aucun");
            } else {
                List<String> bonuses = new ArrayList<>();
                for (Map.Entry<StatType, Integer> entry : effectItem.getAffectedStat().entrySet()) {
                    bonuses.add(entry.getKey() + " " + (entry.getValue() >= 0 ? "+" : "") + entry.getValue());
                }
                description.append(String.join(", ", bonuses));
            }
        }

        return description.toString();
    }

    public void setBackToTeamButton(ActionEvent event) {
        String fxmlPath = "/fr/cpe/views/ChooseTeam.fxml";
        String title = "Poke-Cheap - Choisissez vos Pokémon !";

        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Parent root = loader.load();
            ChooseTeamController controller = loader.getController();
            controller.initialize(this.dresseur);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pressLeftButton(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pressHostGameButton(ActionEvent event) {
        try {
            ensureConnectionService();
            Stade stade = new StadeDAO().get(1).orElseThrow();

            if (this.partie.getDresseur1() == null) {
                this.dresseur.setIndex(0);
                connectionService.hostGame(this.dresseur, stade);
                navigateToChooseTeamForSecondPlayer(event);
            } else {
                this.dresseur.setIndex(1);
                connectionService.connect(this.dresseur);
                navigateToBattle(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pressJoinGameButton(ActionEvent event) {
        try {
            ensureConnectionService();

            if (this.partie.getDresseur1() == null) {
                // Joueur 2 cliqué avant joueur 1 : erreur, on l'ignore ou on affiche un message
                System.out.println("Le joueur 1 n'a pas encore rejoint.");
                return;
            }

            this.dresseur.setIndex(1);
            connectionService.connect(this.dresseur);
            navigateToBattle(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToChooseTeamForSecondPlayer(ActionEvent event) {
        try {
            String fxmlPath = "/fr/cpe/views/ChooseTeam.fxml";
            String title = "Poke-Cheap - Choisissez l'équipe du Joueur 2 !";

            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Parent root = loader.load();
            ChooseTeamController controller = loader.getController();
            Dresseur secondDresseur = new Dresseur();
            secondDresseur.setNom("Joueur 2");
            controller.initialize(secondDresseur);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToBattle(ActionEvent event) {
        try {
            String fxmlPath = "/fr/cpe/views/Battle.fxml";
            String title = "Poke-Cheap - Combat!";

            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Parent root = loader.load();
            BattleController controller = loader.getController();
            controller.initialize(this.partie);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/** Pour une possible connexion
    public void pressHostGameButton(ActionEvent event) {
        try {
            ensureConnectionService();
            Stade stade = new StadeDAO().get(1).orElseThrow();

            connectionService.hostGame(this.dresseur, stade);
            this.dresseur.setIndex(0);

            navigateToChargement(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pressJoinGameButton(ActionEvent event) {
        try {
            ensureConnectionService();
            connectionService.connect(this.dresseur);
            this.dresseur.setIndex(1);

            navigateToChargement(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToChargement(ActionEvent event) {
        try {
            String fxmlPath = "/fr/cpe/views/Chargement.fxml";
            String title = "Poke-Cheap - Chargement...";

            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Parent root = loader.load();
            ChargementController controller = loader.getController();
            controller.initialize(this.dresseur);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 */
    private void ensureConnectionService() {
        if (this.connectionService == null && App.injector != null) {
            this.connectionService = App.injector.getInstance(ConnectionService.class);
        }
        if (this.connectionService == null) {
            throw new IllegalStateException("ConnectionService non initialise. Verifie le chargement FXML avec Guice.");
        }
    }

}