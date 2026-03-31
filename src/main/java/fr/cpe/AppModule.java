package fr.cpe;

// ╔══════════════════════════════════════════════════════════════════════════════╗
// ║                                                                            ║
// ║   ✏️  FICHIER MODIFIABLE — C'est ici que vous configurez Guice             ║
// ║                                                                            ║
// ║   Quand vous créez une interface + une implémentation, déclarez            ║
// ║   le binding ici pour que Guice sache quoi injecter.                       ║
// ║                                                                            ║
// ╚══════════════════════════════════════════════════════════════════════════════╝

import com.azure.messaging.webpubsub.WebPubSubServiceClient;
import com.azure.messaging.webpubsub.WebPubSubServiceClientBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import fr.cpe.bus.BusProxy;
import fr.cpe.service.HelloService;

/**
 * Module Guice — c'est ici que vous déclarez vos bindings (interface → implémentation).
 *
 * <h2>Quand ajouter un binding ?</h2>
 * <p>Dès que vous utilisez une <strong>interface</strong> comme type de dépendance.
 * Guice ne peut pas deviner quelle implémentation choisir tout seul.</p>
 *
 * <h2>Exemple concret :</h2>
 * <pre>
 *   // Vous avez créé :
 *   //   - interface CollisionStrategy { ... }
 *   //   - class SimpleCollision implements CollisionStrategy { ... }
 *   //
 *   // Dans configure(), ajoutez :
 *   bind(CollisionStrategy.class).to(SimpleCollision.class);
 *
 *   // Maintenant, partout où Guice voit @Inject CollisionStrategy,
 *   // il fournira une instance de SimpleCollision.
 * </pre>
 *
 * <h2>Classes concrètes :</h2>
 * <p>Si votre dépendance est une classe concrète (pas une interface),
 * Guice sait l'instancier tout seul — pas besoin de binding.</p>
 */
public class AppModule extends AbstractModule {

    private static final String DEFAULT_HUB = "game-events";

    @Override
    protected void configure() {
        // Pas de binding pour l'instant : Guice sait instancier les classes concrètes
        // tout seul (GameEngine, GameService) grâce à @Inject.
        //
        // Quand vous introduirez des interfaces, ajoutez vos bindings ici.
    }

    /**
     * Provides the WebPubSubServiceClient as a singleton.
     *
     * @return the configured WebPubSubServiceClient
     */
    @Provides
    @Singleton
    public WebPubSubServiceClient provideWebPubSubServiceClient() {
        String connectionString = getConnectionString();
        return new WebPubSubServiceClientBuilder()
                .connectionString(connectionString)
                .hub(DEFAULT_HUB)
                .buildClient();
    }

    /**
     * Provides the HelloService as a BusProxy that sends calls over Web PubSub.
     *
     * @param publisher the WebPubSubServiceClient to use for publishing
     * @return a proxy implementation of HelloService
     */
    @Provides
    @Singleton
    public HelloService provideHelloService(WebPubSubServiceClient publisher) {
        return BusProxy.create(HelloService.class, publisher);
    }

    /**
     * Gets the Azure Web PubSub connection string from environment or default.
     *
     * @return the connection string
     */
    public static String getConnectionString() {
        String envValue = System.getenv("AZURE_WEBPUBSUB_CONNECTION_STRING");
        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }
        // Default for local testing - should be replaced with actual connection string
        return "Endpoint=https://localhost;AccessKey=test;Version=1.0;";
    }
}
