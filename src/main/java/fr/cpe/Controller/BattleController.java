package fr.cpe.Controller;

import fr.cpe.App;
import fr.cpe.model.Attaque;
import fr.cpe.model.Dresseur;
import fr.cpe.model.Pokemon;
import fr.cpe.service.Partie;
import fr.cpe.service.PartieService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class BattleController {

    // ── Labels / barres ennemies ──────────────────────────────────────────────
    public Label enemyHpLabel;
    public ImageView battleBackgroundView;
    @FXML private Label enemyNameLabel;
    @FXML private Label enemyLevelLabel;
    @FXML private ProgressBar enemyHpBar;
    @FXML private Label enemyHpValueLabel;
    @FXML private ImageView enemySpriteView;

    // ── Labels / barres joueur ────────────────────────────────────────────────
    @FXML private Label playerNameLabel;
    @FXML private Label playerLevelLabel;
    @FXML private ProgressBar playerHpBar;
    @FXML private Label playerHpValueLabel;
    @FXML private ImageView playerSpriteView;

    // ── Panneaux d'actions ────────────────────────────────────────────────────
    @FXML private GridPane actionPane;
    @FXML private GridPane movesPane;
    @FXML private GridPane pokemonSwitchPane;   // ← nouveau panneau FXML

    // ── Boutons d'action ──────────────────────────────────────────────────────
    @FXML private Button attackButton;
    @FXML private Button bagButton;
    @FXML private Button pokemonButton;
    @FXML private Button runButton;

    // ── Boutons d'attaque ─────────────────────────────────────────────────────
    @FXML private Button moveButton1;
    @FXML private Button moveButton2;
    @FXML private Button moveButton3;
    @FXML private Button moveButton4;

    // ── Boutons de sélection de Pokémon (6 slots) ─────────────────────────────
    @FXML private Button switchButton1;
    @FXML private Button switchButton2;
    @FXML private Button switchButton3;
    @FXML private Button switchButton4;
    @FXML private Button switchButton5;
    @FXML private Button switchButton6;
    @FXML private Button cancelSwitchButton;

    // ── Message ───────────────────────────────────────────────────────────────
    @FXML private Label battleMessageLabel;

    // ── État interne ──────────────────────────────────────────────────────────
    private Partie partie;
    private PartieService partieService;
    private Dresseur currentAttacker;

    /** Dresseur qui DOIT changer de Pokémon (null si pas de changement forcé). */
    private Dresseur forcedSwitchDresseur = null;

    // ─────────────────────────────────────────────────────────────────────────
    // Initialisation FXML
    // ─────────────────────────────────────────────────────────────────────────

    @FXML
    private void initialize() {
        attackButton.setOnAction(e -> showMoveSelection());
        bagButton   .setOnAction(e -> battleMessageLabel.setText("SAC non implémenté pour le moment."));
        pokemonButton.setOnAction(e -> showPokemonSwitchPanel(currentAttacker, false));
        runButton   .setOnAction(e -> battleMessageLabel.setText("Fuite impossible en combat de démonstration."));

        moveButton1.setOnAction(e -> chooseAttack(0));
        moveButton2.setOnAction(e -> chooseAttack(1));
        moveButton3.setOnAction(e -> chooseAttack(2));
        moveButton4.setOnAction(e -> chooseAttack(3));

        // Boutons de switch liés aux slots 0-5
        List<Button> switchBtns = getSwitchButtons();
        for (int i = 0; i < switchBtns.size(); i++) {
            final int slot = i;
            switchBtns.get(i).setOnAction(e -> confirmPokemonSwitch(slot));
        }
        if (cancelSwitchButton != null) {
            cancelSwitchButton.setOnAction(e -> cancelSwitch());
        }

        hidePokemonSwitchPane();
        hideMovesPane();
    }

    /** Appelée par le code qui charge la vue, pour brancher la Partie. */
    public void initialize(Partie partie) {
        this.partie = partie;
        this.partieService = App.injector != null ? App.injector.getInstance(PartieService.class) : null;
        this.currentAttacker = partie != null ? partie.getDresseur1() : null;
        if (this.partie != null && this.currentAttacker != null) {
            this.partie.setCurrentAttacker(this.currentAttacker);
        }
        updateBattleUI();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Sélection d'attaque
    // ─────────────────────────────────────────────────────────────────────────

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
        showOnlyPane(movesPane);
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
            showOnlyPane(actionPane);
            return;
        }
        Pokemon attackerPokemon = partie.getActivePokemonOf(currentAttacker);
        if (attackerPokemon == null
                || attackerPokemon.getLesattaquesprises() == null
                || index >= attackerPokemon.getLesattaquesprises().length) {
            battleMessageLabel.setText("Choix d'attaque invalide.");
            showOnlyPane(actionPane);
            return;
        }

        Attaque attaque = attackerPokemon.getLesattaquesprises()[index];
        Dresseur attaquant = currentAttacker;           // capture avant switchTurn()
        partieService.handleAttack(attaquant, attaque);

        String msg = attaquant.getNom() + " utilise " + attaque.getName() + " !";

        Platform.runLater(() -> {
            battleMessageLabel.setText(msg);
            updateBattleUI();
            showOnlyPane(actionPane);

            // ── Vérification KO ──────────────────────────────────────────────
            Dresseur adversaire = partie.getOpponent(attaquant);
            Pokemon  pokemonAdv = partie.getActivePokemonOf(adversaire);

            if (partie.isKO(pokemonAdv)) {
                handleKO(adversaire, pokemonAdv);
            } else {
                switchTurn();
            }
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Gestion du KO
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Appelée quand {@code pokemonKO} (appartenant à {@code dresseurKO}) vient d'être mis K.O.
     */
    private void handleKO(Dresseur dresseurKO, Pokemon pokemonKO) {
        String koMsg = pokemonKO.getNom() + " est K.O. !";

        // Vérifie s'il reste des Pokémon vivants
        if (!partie.hasAlivePokemon(dresseurKO)) {
            // Défaite totale
            Dresseur vainqueur = partie.getOpponent(dresseurKO);
            battleMessageLabel.setText(koMsg + "\n" + vainqueur.getNom() + " a gagné !");
            actionPane.setDisable(true);
        } else {
            // Le dresseur doit changer obligatoirement
            battleMessageLabel.setText(koMsg + "\n" + dresseurKO.getNom() + ", choisis un nouveau Pokémon !");
            showPokemonSwitchPanel(dresseurKO, true /* changement forcé */);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Panneau de sélection de Pokémon
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Affiche le panneau de sélection de Pokémon pour {@code dresseur}.
     *
     * @param dresseur      le dresseur qui change de Pokémon
     * @param forceSwitch   true = KO, le joueur ne peut pas annuler
     */
    private void showPokemonSwitchPanel(Dresseur dresseur, boolean forceSwitch) {
        if (dresseur == null || partie == null) return;

        forcedSwitchDresseur = forceSwitch ? dresseur : null;

        Pokemon actif = partie.getActivePokemonOf(dresseur);
        List<Button> btns = getSwitchButtons();

        // Récupère les Pokémon de l'équipe (slots 0-5)
        for (int i = 0; i < btns.size(); i++) {
            Button btn = btns.get(i);
            Pokemon p = dresseur.getPokemon().get(i);

            if (p == null) {
                btn.setText("---");
                btn.setDisable(true);
            } else {
                int hpMax = p.getHpMax() != null ? p.getHpMax() : p.getHp();
                btn.setText(p.getNom() + "\nPV " + p.getHp() + "/" + hpMax);
                boolean estActif = (p == actif);
                boolean estKO    = p.getHp() <= 0;
                btn.setDisable(estActif || estKO);   // ne peut pas re-choisir l'actif ni un KO
            }
        }

        // Bouton Annuler : masqué si changement forcé
        if (cancelSwitchButton != null) {
            cancelSwitchButton.setVisible(!forceSwitch);
            cancelSwitchButton.setManaged(!forceSwitch);
        }

        showOnlyPane(pokemonSwitchPane);
        battleMessageLabel.setText(dresseur.getNom() + ", choisis ton Pokémon !");
    }

    /** Confirmé : le joueur clique sur un slot de Pokémon. */
    private void confirmPokemonSwitch(int slot) {
        Dresseur dresseur = (forcedSwitchDresseur != null) ? forcedSwitchDresseur : currentAttacker;
        if (dresseur == null || partie == null || partieService == null) return;

        Pokemon choix = dresseur.getPokemon().get(slot);
        if (choix == null || choix.getHp() <= 0) {
            battleMessageLabel.setText("Ce Pokémon ne peut pas combattre !");
            return;
        }

        partieService.handleChangePokemon(dresseur, choix);

        boolean etaitForce = (forcedSwitchDresseur != null);
        forcedSwitchDresseur = null;

        Platform.runLater(() -> {
            updateBattleUI();
            showOnlyPane(actionPane);
            battleMessageLabel.setText(dresseur.getNom() + " envoie " + choix.getNom() + " !");

            // Si c'était un changement forcé (KO), c'est à l'adversaire d'attaquer
            if (etaitForce) {
                switchTurn();
            }
        });
    }

    /** Annule le panneau de sélection (uniquement si non forcé). */
    private void cancelSwitch() {
        if (forcedSwitchDresseur != null) return; // impossible d'annuler si forcé
        showOnlyPane(actionPane);
        battleMessageLabel.setText("Au tour de " + currentAttacker.getNom() + ". Que veux-tu faire ?");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Gestion des tours
    // ─────────────────────────────────────────────────────────────────────────

    private void switchTurn() {
        if (partie == null) return;
        Dresseur next = partie.nextAttacker();
        if (next != null) {
            currentAttacker = next;
            Platform.runLater(() ->
                battleMessageLabel.setText("Au tour de " + currentAttacker.getNom() + ". Choisis une action.")
            );
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers d'affichage
    // ─────────────────────────────────────────────────────────────────────────

    /** Affiche uniquement le panneau demandé, masque les deux autres. */
    private void showOnlyPane(GridPane visible) {
        setPaneVisible(actionPane,       visible == actionPane);
        setPaneVisible(movesPane,        visible == movesPane);
        setPaneVisible(pokemonSwitchPane, visible == pokemonSwitchPane);
    }

    private void setPaneVisible(GridPane pane, boolean show) {
        if (pane == null) return;
        pane.setVisible(show);
        pane.setManaged(show);
    }

    private void hideMovesPane() {
        setPaneVisible(movesPane, false);
        setPaneVisible(actionPane, true);
    }

    private void hidePokemonSwitchPane() {
        setPaneVisible(pokemonSwitchPane, false);
    }

    private List<Button> getSwitchButtons() {
        List<Button> list = new ArrayList<>();
        list.add(switchButton1);
        list.add(switchButton2);
        list.add(switchButton3);
        list.add(switchButton4);
        list.add(switchButton5);
        list.add(switchButton6);
        return list;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Mise à jour de l'interface
    // ─────────────────────────────────────────────────────────────────────────

    private String resolveSpritePath(String rawPath) {
        String normalized = rawPath.startsWith("/") ? rawPath : "/fr/cpe/" + rawPath;
        if (getClass().getResource(normalized) != null) return normalized;
        if (normalized.endsWith(".png")) {
            String back = normalized.substring(0, normalized.length() - 4) + "_back.png";
            if (getClass().getResource(back) != null) return back;
            String mini = normalized.substring(0, normalized.length() - 4) + "_mini.png";
            if (getClass().getResource(mini) != null) return mini;
        }
        return normalized;
    }

    private void updateBattleUI() {
        if (partie == null) return;

        Dresseur d2 = partie.getDresseur2();
        if (d2 != null) {
            Pokemon enemy = partie.getActivePokemonOf(d2);
            if (enemy != null) {
                loadSprite(enemySpriteView, enemy.getImage_face(), 200, 200);
                enemyNameLabel.setText(enemy.getNom());
                enemyLevelLabel.setText(" Lv.50");
                int hpMax = enemy.getHpMax() != null ? enemy.getHpMax() : enemy.getHp();
                enemyHpValueLabel.setText(enemy.getHp() + " / " + hpMax);
                enemyHpBar.setProgress((double) enemy.getHp() / Math.max(1, hpMax));
            }
        }

        Dresseur d1 = partie.getDresseur1();
        if (d1 != null) {
            Pokemon player = partie.getActivePokemonOf(d1);
            if (player != null) {
                loadSprite(playerSpriteView, player.getImage_dos(), 150, 150);
                playerNameLabel.setText(player.getNom());
                playerLevelLabel.setText(" Lv.50");
                int hpMax = player.getHpMax() != null ? player.getHpMax() : player.getHp();
                playerHpValueLabel.setText(player.getHp() + " / " + hpMax);
                playerHpBar.setProgress((double) player.getHp() / Math.max(1, hpMax));
            }
        }
    }

    private void loadSprite(ImageView view, String path, double w, double h) {
        if (path == null || path.isBlank()) return;
        String resolved = resolveSpritePath(path);
        var url = getClass().getResource(resolved);
        if (url != null) {
            view.setImage(new Image(url.toExternalForm()));
            view.setFitWidth(w);
            view.setFitHeight(h);
            view.setPreserveRatio(true);
        } else {
            System.err.println("Sprite resource not found: " + resolved);
        }
    }
}
