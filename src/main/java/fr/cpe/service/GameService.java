package fr.cpe.service;

// ╔══════════════════════════════════════════════════════════════════════════════╗
// ║                                                                            ║
// ║   ✏️  FICHIER MODIFIABLE — C'est le cœur de votre projet                   ║
// ║                                                                            ║
// ║   Le code actuel est un EXEMPLE (une balle qui rebondit).                  ║
// ║   Remplacez-le entièrement par votre propre logique de jeu.                ║
// ║                                                                            ║
// ║   Gardez juste la structure init() / update() car GameEngine              ║
// ║   les appelle automatiquement.                                             ║
// ║                                                                            ║
// ╚══════════════════════════════════════════════════════════════════════════════╝
import fr.cpe.bus.*;
import com.google.inject.Inject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.beans.binding.Bindings;

import java.io.IOException;

/**
 * Service de jeu — gère l'état du jeu et ses éléments visuels.
 *
 * <h2>C'est ici que vous codez votre jeu !</h2>
 *
 * <p>Ce fichier est un <strong>exemple</strong> : une balle qui rebondit.
 * Remplacez tout par votre propre logique.</p>
 *
 * <h2>Méthodes importantes :</h2>
 * <ul>
 *   <li>{@code init(gamePane)} — appelé une fois au démarrage, créez vos Nodes ici</li>
 *   <li>{@code update(width, height)} — appelé ~60x/sec, mettez à jour la logique et les positions ici</li>
 * </ul>
 *
 * <h2>Rendu (Scene Graph) :</h2>
 * <p>Pas besoin de méthode render() ! Vous créez des Nodes JavaFX (Circle, Rectangle,
 * Text, ImageView…) dans {@code init()}, vous les ajoutez au {@code gamePane},
 * et JavaFX les affiche automatiquement. Dans {@code update()}, vous mettez à jour
 * leurs positions.</p>
 *
 * <h2>Clics souris :</h2>
 * <p>Chaque Node gère ses propres clics :</p>
 * <pre>
 *   monCercle.setOnMouseClicked(e -&gt; {
 *       // ce cercle a été cliqué !
 *   });
 * </pre>
 *
 * <h2>Comment ajouter des dépendances :</h2>
 * <p>Ajoutez-les en paramètre du constructeur avec {@code @Inject} :</p>
 * <pre>
 *   @Inject
 *   public GameService(BallService ball, MonAutreService autre) {
 *       this.ball = ball;
 *       this.autre = autre;
 *   }
 * </pre>
 * <p>Guice les injectera automatiquement.</p>
 */
public class GameService {

    private static final double WINDOW_WIDTH = 1134.0;
    private static final double WINDOW_HEIGHT = 917.0;

    @Inject
    public GameService(OnlineInitializer onlineInitializer, PartieService PartieService) {
        onlineInitializer.start();
    }

    /**
     * Initialise les éléments visuels du jeu (appelé une fois au démarrage).
     */
    public void init(Pane gamePane)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/cpe/views/index.fxml"));
            Parent root = loader.load();
            gamePane.getChildren().setAll(root);

            if (root instanceof Region region) {
                region.prefWidthProperty().bind(gamePane.widthProperty());
                region.prefHeightProperty().bind(gamePane.heightProperty());
            } else {
                root.layoutXProperty().bind(Bindings.createDoubleBinding(
                        () -> (gamePane.getWidth() - root.getLayoutBounds().getWidth()) / 2.0,
                        gamePane.widthProperty(), root.layoutBoundsProperty()
                ));
                root.layoutYProperty().bind(Bindings.createDoubleBinding(
                        () -> (gamePane.getHeight() - root.getLayoutBounds().getHeight()) / 2.0,
                        gamePane.heightProperty(), root.layoutBoundsProperty()
                ));
            }

            if (gamePane.getScene() != null && gamePane.getScene().getWindow() instanceof Stage stage) {
                stage.setWidth(WINDOW_WIDTH);
                stage.setHeight(WINDOW_HEIGHT);
                stage.centerOnScreen();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Met à jour l'état du jeu (appelé à chaque frame).
     */
    public void update(double width, double height) {

    }
}
