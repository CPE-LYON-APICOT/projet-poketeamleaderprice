package fr.cpe.util;

import com.google.inject.Injector;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

/**
 * Utility class to load FXML files with Guice injection support.
 */
public class GuiceFXMLLoader {

    private static Injector injector;

    public static void setInjector(Injector inj) {
        injector = inj;
    }

    public static Parent load(String resource) throws IOException {
        URL fxmlUrl = GuiceFXMLLoader.class.getResource(resource);
        FXMLLoader loader = new FXMLLoader(fxmlUrl);

        if (injector != null) {
            loader.setControllerFactory(param -> injector.getInstance(param));
        }

        return loader.load();
    }

    public static <T> T load(String resource, Class<T> controllerClass) throws IOException {
        URL fxmlUrl = GuiceFXMLLoader.class.getResource(resource);
        FXMLLoader loader = new FXMLLoader(fxmlUrl);

        if (injector != null) {
            loader.setControllerFactory(param -> injector.getInstance(param));
        }

        loader.load();
        return loader.getController();
    }
}

