package fr.cpe.Controller;

import fr.cpe.model.Dresseur;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;


public class IndexController
{
    public Button startButton;
    public Button quitButton;
    public Label pressStartLabel;

    public void initialize() {
        //Animation de clignotement sur le label "Press Start"
        FadeTransition blink = new FadeTransition(Duration.millis(700), pressStartLabel);
        blink.setFromValue(1.0);
        blink.setToValue(0.2);
        blink.setCycleCount(Animation.INDEFINITE);
        blink.setAutoReverse(true);
        blink.play();
    }

    public void pressStartButton(ActionEvent event)
    {
        String fxmlPath = "/fr/cpe/views/ChooseTeam.fxml";
        String title = "Poke-Cheap - Choisissez vos Pokémon !";

        try{
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Parent root = loader.load();
            ChooseTeamController controller = loader.getController();
            controller.initialize(new Dresseur());
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
