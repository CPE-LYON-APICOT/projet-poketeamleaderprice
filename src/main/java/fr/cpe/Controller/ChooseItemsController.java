package fr.cpe.Controller;

import fr.cpe.commands.HostGameCommand;
import fr.cpe.dao.EffectItemDAO;
import fr.cpe.dao.HealingItemDAO;
import fr.cpe.dao.StadeDAO;
import fr.cpe.model.Dresseur;
import fr.cpe.model.HealingItem;
import fr.cpe.model.Item;
import fr.cpe.service.CommandExecutor;
import fr.cpe.service.CommandService;
import fr.cpe.service.ConnectionService;
import fr.cpe.service.MessageStore;
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
    public ListView itemListView;
    public TextArea itemDescriptionArea;
    public Button hostGameButton;
    public Button joinGameButton;
    public Button LeftButton;


    private Dresseur dresseur;

    public void initialize(Dresseur dresseur){
        this.dresseur = dresseur;

        EffectItemDAO effectItemDAO = new EffectItemDAO();
        HealingItemDAO healingItemDAO = new HealingItemDAO();
        List<String> lesitems = new ArrayList<>();
        lesitems.addAll(effectItemDAO.getAllNom());
        lesitems.addAll(healingItemDAO.getAllNom());

        itemListView.getItems().setAll(lesitems);

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

    public void pressHostGameButton() {
        try {
            ConnectionService cs = new ConnectionService(new CommandExecutor(), new MessageStore());
            cs.executeCommand(new HostGameCommand(cs.getMessageStore(), this.dresseur, new StadeDAO().get(1).orElseThrow()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
