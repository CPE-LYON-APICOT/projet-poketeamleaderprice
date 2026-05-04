package fr.cpe.Controller;

import fr.cpe.App;
import fr.cpe.dao.EffectItemDAO;
import fr.cpe.dao.HealingItemDAO;
import fr.cpe.dao.StadeDAO;
import fr.cpe.model.*;
import fr.cpe.service.ConnectionService;
import fr.cpe.service.Partie;
import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChooseItemsController {
    public Button backToTeamButton;
    public ListView<String> itemListView;
    public TextArea itemDescriptionArea;
    public Button hostGameButton;
    public Button joinGameButton;
    public Button LeftButton;

    private Dresseur dresseur;

    @Inject
    private ConnectionService connectionService;

    @Inject
    private Partie partie;

    public void initialize(Dresseur dresseur){
        this.dresseur = dresseur;

        EffectItemDAO effectItemDAO = new EffectItemDAO();
        HealingItemDAO healingItemDAO = new HealingItemDAO();
        List<String> lesitems = new ArrayList<>();
        lesitems.addAll(effectItemDAO.getAllNom());
        lesitems.addAll(healingItemDAO.getAllNom());

        itemListView.getItems().setAll(lesitems);

        if (hostGameButton != null) {
            hostGameButton.setText("Valider Joueur 1");
        }
        if (joinGameButton != null) {
            joinGameButton.setText("Valider Joueur 2");
        }
    }

    public void addItemtoBag(Button slot)
    {

    }



    public void setBackToTeamButton(ActionEvent event)
    {
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

    private void ensureConnectionService() {
        if (this.connectionService == null && App.injector != null) {
            this.connectionService = App.injector.getInstance(ConnectionService.class);
        }
        if (this.connectionService == null) {
            throw new IllegalStateException("ConnectionService non initialise. Verifie le chargement FXML avec Guice.");
        }
    }
}
