package fr.cpe.Controller;

import fr.cpe.model.Dresseur;
import fr.cpe.model.Pokemon;
import fr.cpe.model.StatType;
import fr.cpe.service.Partie;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BattleController {

    @FXML
    private Label enemyNameLabel;
    @FXML
    private Label enemyLevelLabel;
    @FXML
    private Label enemyHpLabel;
    @FXML
    private ImageView enemySpriteImageView;
    
    @FXML
    private Label playerNameLabel;
    @FXML
    private Label playerLevelLabel;
    @FXML
    private Label playerHpLabel;
    @FXML
    private ImageView playerSpriteImageView;
    
    @FXML
    private VBox enemyInfoBox;
    @FXML
    private VBox playerInfoBox;
    @FXML
    private HBox actionButtonsBox;

    private Partie partie;

    public void initialize(Partie partie) {
        this.partie = partie;
        updateBattleUI();
    }

    private void updateBattleUI() {
        if (partie == null) {
            return;
        }

        Dresseur dresseur1 = partie.getDresseur1();
        Dresseur dresseur2 = partie.getDresseur2();

        // Update enemy (dresseur2) info
        if (dresseur2 != null) {
            Pokemon enemyPokemon = partie.getActivePokemonOf(dresseur2);
            if (enemyPokemon != null) {
                enemyNameLabel.setText(enemyPokemon.getNom());
                enemyLevelLabel.setText(" Lv.50");
                Integer maxHp = enemyPokemon.getStats() != null ? enemyPokemon.getStats().get(StatType.Atk) : enemyPokemon.getHp();
                enemyHpLabel.setText(enemyPokemon.getHp() + " / " + maxHp);
            }
        }

        // Update player (dresseur1) info
        if (dresseur1 != null) {
            Pokemon playerPokemon = partie.getActivePokemonOf(dresseur1);
            if (playerPokemon != null) {
                playerNameLabel.setText(playerPokemon.getNom());
                playerLevelLabel.setText(" Lv.50");
                Integer maxHp = playerPokemon.getStats() != null ? playerPokemon.getStats().get(StatType.Atk) : playerPokemon.getHp();
                playerHpLabel.setText(playerPokemon.getHp() + " / " + maxHp);
            }
        }
    }
}
