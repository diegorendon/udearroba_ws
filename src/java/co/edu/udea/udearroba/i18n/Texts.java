package co.edu.udea.udearroba.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the resource bundle to externalize the texts of the application.
 *
 * @author Diego Rendón
 */
public class Texts {
    private static final String RESOURCE_BUNDLE = "co.edu.udea.udearroba.i18n.Texts";
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE, new Locale("es")); // Sets the default locale to spanish.
    
    public static String getText(String key) {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException ex) {
            Logger.getLogger(Texts.class.getName()).log(Level.WARNING, null, ex);
            return "Problem when loading the text for: [" + key + "]";
        }
    }
}