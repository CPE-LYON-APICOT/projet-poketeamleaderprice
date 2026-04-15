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
import fr.cpe.service.Partie;
import fr.cpe.service.PartieService;

import java.util.logging.Logger;

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

    private static final String DEFAULT_HUB = "game";
    //private static final Logger LOGGER = Logger.getLogger(AppModule.class.getName());

    @Override
    protected void configure() {
        bind(PartieService.class).to(Partie.class).in(Singleton.class);
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
     * Provides the remote PartieService proxy used to send method calls over Web PubSub.
     *
     * @param publisher the WebPubSubServiceClient to use for publishing
     * @return a proxy implementation of PartieService
     */
    @Provides
    @Singleton
    @com.google.inject.name.Named("remotePartieService")
    public PartieService provideRemotePartieService(WebPubSubServiceClient publisher) {
        return BusProxy.create(PartieService.class, publisher);
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
        // Warning: Using test configuration - should set AZURE_WEBPUBSUB_CONNECTION_STRING in production
        //LOGGER.warning("AZURE_WEBPUBSUB_CONNECTION_STRING not set. Using test configuration that will not work in production.");
        return "Endpoint=https://projet-poo-pokemon.webpubsub.azure.com;AccessKey=7etYXQBwM6MemPU4tSnfoAxV4ZYYnxu8MCY2s8SSGv3ZEZA9XdnGJQQJ99CCAC5T7U2XJ3w3AAAAAWPSl8V6;Version=1.0;";
    }
}
