package fr.cpe.Controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.util.Duration;


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
        String fxmlPath = "@../views/";
        String title = "Poke-Cheap - Choisissez vos Pokémon !";

        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
