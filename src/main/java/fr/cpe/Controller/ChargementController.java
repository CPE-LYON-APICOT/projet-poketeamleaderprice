package fr.cpe.Controller;

import fr.cpe.model.Dresseur;
import fr.cpe.service.Partie;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class ChargementController {

    private static final long POLLING_INTERVAL_MS = 1000; // Check every second
    private static final int MAX_WAIT_TIME_MS = 60000; // 60 seconds timeout

    private Timer pollingTimer;
    private long startTime;
    private Dresseur currentDresseur;

    public Label statusLabel;

    private static final Logger LOGGER = Logger.getLogger(ChargementController.class.getName());

    public void initialize(Dresseur dresseur) {
        this.currentDresseur = dresseur;
        this.startTime = System.currentTimeMillis();
        
        startPolling();
    }

    private void startPolling() {
        pollingTimer = new Timer(true);
        pollingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> checkGameState());
            }
        }, 0, POLLING_INTERVAL_MS);
    }

    private void checkGameState() {
        // Debug: print current Partie state so we can see why we remain on the loading screen
        try {
            Partie p = Partie.getInstance();
            String d1 = (p.getDresseur1() == null) ? "null" : (p.getDresseur1().getIndex() + "(" + p.getDresseur1().getNom() + ")");
            String d2 = (p.getDresseur2() == null) ? "null" : (p.getDresseur2().getIndex() + "(" + p.getDresseur2().getNom() + ")");
            LOGGER.info(() -> "ChargementController.checkGameState - dresseur1=" + d1 + " dresseur2=" + d2);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error while logging Partie state", e);
        }
        // Check for timeout
        if (System.currentTimeMillis() - startTime > MAX_WAIT_TIME_MS) {
            pollingTimer.cancel();
            showTimeoutMessage();
            return;
        }

        // Check if both players are connected
        Partie partie = Partie.getInstance();
        
        if (isGameReady()) {
            pollingTimer.cancel();
            navigateToBattle();
        } else {
            updateStatusMessage(partie);
        }
    }

    private boolean isGameReady() {
        Partie partie = Partie.getInstance();
        
        // Check if dresseur1 is set (host)
        boolean hasDresseur1 = partie.getDresseur1() != null;
        // Check if dresseur2 is set (opponent joined)
        boolean hasDresseur2 = partie.getDresseur2() != null;
        
        return hasDresseur1 && hasDresseur2;
    }

    private void updateStatusMessage(Partie partie) {
        if (statusLabel != null) {
            boolean isHost = currentDresseur != null && partie.getDresseur1() != null 
                && currentDresseur.getIndex() != null 
                && currentDresseur.getIndex().equals(partie.getDresseur1().getIndex());
            
            if (isHost) {
                statusLabel.setText("En attente d'un adversaire...");
            } else {
                statusLabel.setText("Connexion au serveur...");
            }
        }
    }

    private void showTimeoutMessage() {
        if (statusLabel != null) {
            statusLabel.setText("Temps d'attente dépassé. Veuillez réessayer.");
        }
        
        // Optionally navigate back after a delay
        new Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    // Could navigate back to ChooseItems or Index
                });
            }
        }, 3000);
    }

    private void navigateToBattle() {
        try {
            String fxmlPath = "/fr/cpe/views/Battle.fxml";
            String title = "Poke-Cheap - Combat!";

            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Parent root = loader.load();
            BattleController controller = loader.getController();
            controller.initialize(Partie.getInstance());

            // Get the current stage
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cleanup() {
        if (pollingTimer != null) {
            pollingTimer.cancel();
        }
    }
}
