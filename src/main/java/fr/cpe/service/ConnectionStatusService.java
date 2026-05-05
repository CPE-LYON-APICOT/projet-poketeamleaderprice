package fr.cpe.service;

import com.google.inject.Inject;
//import fr.cpe.bus.OnlineInitializer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
/*
public class ConnectionStatusService {

    private final OnlineInitializer onlineInitializer;
    private Circle statusCircle;
    private Text statusLabel;

    @Inject
    public ConnectionStatusService(OnlineInitializer onlineInitializer) {
        this.onlineInitializer = onlineInitializer;
    }

    public void init(Pane gamePane) {
        statusCircle = new Circle(15);
        statusCircle.setLayoutX(720);
        statusCircle.setLayoutY(40);
        statusCircle.setStroke(Color.WHITE);
        statusCircle.setStrokeWidth(2);

        statusLabel = new Text(745, 45, "Bus déconnecté");
        statusLabel.setFill(Color.web("#e0e0e0"));

        update();
        gamePane.getChildren().addAll(statusCircle, statusLabel);
    }

    public void update() {
        if (statusCircle == null || statusLabel == null) {
            return;
        }
        boolean connected = onlineInitializer.isConnected();
        statusCircle.setFill(connected ? Color.LIMEGREEN : Color.CRIMSON);
        statusLabel.setText(connected ? "Bus connecté" : "Bus déconnecté");
    }
}
*/