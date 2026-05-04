package fr.cpe.Controller;

import com.google.inject.Inject;

import fr.cpe.dao.AttaqueDAO;
import fr.cpe.model.Dresseur;
import fr.cpe.model.Pokemon;
import fr.cpe.model.StatType;
import fr.cpe.service.Partie;
import fr.cpe.service.PartieService;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BattleController {

    public ImageView battleBackgroundView;
    public ImageView enemySpriteView;
    public ProgressBar enemyHpBar;
    public Label enemyHpValueLabel;
    public ImageView playerSpriteView;
    public Label playerHpValueLabel;
    public ProgressBar playerHpBar;
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

    @Inject
    private PartieService partieService;

    public void initialize(Partie partie) {
        this.partie = partie;
        updateBattleUI();

        Dresseur hostdresseur = partie.getDresseur1();
        Dresseur joindresseur = partie.getDresseur2();
        Dresseur player;
        Dresseur opponent;

        //Initialisé les HP des premiers pokemons des dresseurs
        // Player
        String HPPokemon = player.getPokemonTeam(0).getHp() + " / " + player.getPokemonTeam(0).getHp();
        this.playerHpValueLabel.setText(HPPokemon);



    }

    private void updatePlayerHpBar(int damage) {
        // int actualHP = this.playerHpLabel.
    }

    private void updateOppHpBar() {

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

    public void pressAttackButton(ActionEvent event) {
        Dotenv dotenv = Dotenv.load();
        String playerId = dotenv.get("PLAYER_ID");
        if (playerId == null || playerId.isEmpty()) {
            System.err.println("PLAYER_ID not found in .env, using default value 1");
            playerId = "1";
        }

        try {
            this.partieService.handleAttack(
                this.partie.getDresseurFromId(Integer.parseInt(playerId)),
                new AttaqueDAO().get(1).orElseThrow(() -> new IllegalArgumentException("Invalid attack ID"))
            );
        } catch (NumberFormatException e) {
            System.err.println("PLAYER_ID invalid format: " + e.getMessage());
        }
    }
}
