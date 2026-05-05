package fr.cpe.Controller;

import fr.cpe.App;
import fr.cpe.model.Attaque;
import fr.cpe.model.Dresseur;
import fr.cpe.model.Pokemon;
import fr.cpe.service.Partie;
import fr.cpe.service.PartieService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;

public class BattleController {

    public Label enemyHpLabel;
    public ImageView battleBackgroundView;
    @FXML
    private Label enemyNameLabel;
    @FXML
    private Label enemyLevelLabel;
    @FXML
    private ProgressBar enemyHpBar;
    @FXML
    private ImageView enemySpriteView;

    @FXML
    private Label playerNameLabel;
    @FXML
    private Label playerLevelLabel;
    @FXML
    private ProgressBar playerHpBar;
    @FXML
    private Label playerHpValueLabel;
    @FXML
    private ImageView playerSpriteView;

    @FXML
    private GridPane actionPane;
    @FXML
    private GridPane movesPane;
    @FXML
    private Button attackButton;
    @FXML
    private Button bagButton;
    @FXML
    private Button pokemonButton;
    @FXML
    private Button runButton;
    @FXML
    private Button moveButton1;
    @FXML
    private Button moveButton2;
    @FXML
    private Button moveButton3;
    @FXML
    private Button moveButton4;
    @FXML
    private Label battleMessageLabel;

    private Partie partie;
    private PartieService partieService;
    private Dresseur currentAttacker;

    @FXML
    private void initialize() {
        attackButton.setOnAction(event -> showMoveSelection());
        bagButton.setOnAction(event -> battleMessageLabel.setText("SAC non implémenté pour le moment."));
        pokemonButton.setOnAction(event -> battleMessageLabel.setText("Changer de Pokémon n'est pas encore disponible."));
        runButton.setOnAction(event -> battleMessageLabel.setText("Fuite impossible en combat de démonstration."));

        moveButton1.setOnAction(event -> chooseAttack(0));
        moveButton2.setOnAction(event -> chooseAttack(1));
        moveButton3.setOnAction(event -> chooseAttack(2));
        moveButton4.setOnAction(event -> chooseAttack(3));

        hideMovesPane();
    }

    public void initialize(Partie partie) {
        this.partie = partie;
        this.partieService = App.injector != null ? App.injector.getInstance(PartieService.class) : null;
        this.currentAttacker = partie != null ? partie.getDresseur1() : null;
        if (this.partie != null && this.currentAttacker != null) {
            this.partie.setCurrentAttacker(this.currentAttacker);
        }
        updateBattleUI();
    }

    private void showMoveSelection() {
        if (partie == null || currentAttacker == null) {
            battleMessageLabel.setText("Aucun combat actif.");
            return;
        }

        Pokemon attackerPokemon = partie.getActivePokemonOf(currentAttacker);
        if (attackerPokemon == null || attackerPokemon.getLesattaquesprises() == null) {
            battleMessageLabel.setText("Aucun Pokémon actif pour attaquer.");
            return;
        }

        Attaque[] moves = attackerPokemon.getLesattaquesprises();
        if (moves.length == 0) {
            battleMessageLabel.setText("Aucune attaque disponible.");
            return;
        }

        updateMoveButtons(moves);
        actionPane.setVisible(false);
        actionPane.setManaged(false);
        movesPane.setVisible(true);
        movesPane.setManaged(true);
        battleMessageLabel.setText("Choisis une attaque pour " + attackerPokemon.getNom() + ".");
    }

    private void updateMoveButtons(Attaque[] moves) {
        moveButton1.setText(moves.length > 0 ? moves[0].getName() : "---");
        moveButton2.setText(moves.length > 1 ? moves[1].getName() : "---");
        moveButton3.setText(moves.length > 2 ? moves[2].getName() : "---");
        moveButton4.setText(moves.length > 3 ? moves[3].getName() : "---");
    }

    private void chooseAttack(int index) {
        if (partie == null || currentAttacker == null || partieService == null) {
            battleMessageLabel.setText("Impossible d'exécuter l'attaque.");
            hideMovesPane();
            return;
        }

        Pokemon attackerPokemon = partie.getActivePokemonOf(currentAttacker);
        if (attackerPokemon == null || attackerPokemon.getLesattaquesprises() == null || index >= attackerPokemon.getLesattaquesprises().length) {
            battleMessageLabel.setText("Choix d'attaque invalide.");
            hideMovesPane();
            return;
        }

        Attaque attaque = attackerPokemon.getLesattaquesprises()[index];
        partieService.handleAttack(currentAttacker, attaque);
        battleMessageLabel.setText(currentAttacker.getNom() + " utilise " + attaque.getName() + " !");
        updateBattleUI();
        hideMovesPane();
        switchTurn();
    }

    private void switchTurn() {
        if (partie == null) {
            return;
        }

        Dresseur next = partie.nextAttacker();
        if (next != null) {
            currentAttacker = next;
            battleMessageLabel.setText("Au tour de " + currentAttacker.getNom() + ". Choisis une action.");
        }
    }

    private void hideMovesPane() {
        movesPane.setVisible(false);
        movesPane.setManaged(false);
        actionPane.setVisible(true);
        actionPane.setManaged(true);
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

    private void updateBattleUI() {
        if (partie == null) return;

        Dresseur dresseur1 = partie.getDresseur1();
        Dresseur dresseur2 = partie.getDresseur2();

        if (dresseur2 != null) {
            Pokemon enemyPokemon = partie.getActivePokemonOf(dresseur2);
            if (enemyPokemon != null) {
                String resolvedPath = resolveSpritePath(enemyPokemon.getImage_face());
                var spriteUrl = getClass().getResource(resolvedPath);
                if (spriteUrl != null) {
                    Image spriteImage = new Image(spriteUrl.toExternalForm());
                    enemySpriteView.setImage(spriteImage);
                    enemySpriteView.setFitWidth(200);
                    enemySpriteView.setFitHeight(200);
                    enemySpriteView.setPreserveRatio(true);
                } else {
                    // Do not return early: log missing resource and continue updating other UI elements
                    System.err.println("Enemy sprite resource not found: " + resolvedPath);
                }

                enemyNameLabel.setText(enemyPokemon.getNom());
                enemyLevelLabel.setText(" Lv.50");
                int hpMax = enemyPokemon.getHpMax() != null ? enemyPokemon.getHpMax() : enemyPokemon.getHp();
                enemyHpBar.setProgress((double) enemyPokemon.getHp() / hpMax);
            }
        }

        if (dresseur1 != null) {
            Pokemon playerPokemon = partie.getActivePokemonOf(dresseur1);
            if (playerPokemon != null) {
                String resolvedPath = resolveSpritePath(playerPokemon.getImage_dos());
                var spriteUrl = getClass().getResource(resolvedPath);
                if (spriteUrl != null) {
                    Image spriteImage = new Image(spriteUrl.toExternalForm());
                    playerSpriteView.setImage(spriteImage);
                    playerSpriteView.setFitWidth(180);
                    playerSpriteView.setFitHeight(180);
                    playerSpriteView.setPreserveRatio(true);
                } else {
                    // Do not return early: log missing resource and continue updating other UI elements
                    System.err.println("Player sprite resource not found: " + resolvedPath);
                }

                playerNameLabel.setText(playerPokemon.getNom());

                playerLevelLabel.setText(" Lv.50");
                int hpMax = playerPokemon.getHpMax() != null ? playerPokemon.getHpMax() : playerPokemon.getHp();
                playerHpValueLabel.setText(playerPokemon.getHp() + " / " + hpMax);
                playerHpBar.setProgress((double) playerPokemon.getHp() / hpMax);
            }
        }
    }
}
