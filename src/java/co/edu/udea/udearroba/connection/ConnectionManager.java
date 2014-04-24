package co.edu.udea.udearroba.connection;

import co.edu.udea.udearroba.i18n.Texts;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Manage the connection to the databases.
 *
 * @author Diego Rendón
 */
public class ConnectionManager {

    /**
     * Constant index for the SERVA database connection.
     */
    public static final int SERVA = 1;

    /**
     * Creates and return the connection to a database.
     *
     * @param database the integer index that indicates to which database will
     * establish the connection.
     *
     * @return a Connection object to the selected database.
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws Exception
     */
    public static Connection getConnection(int database) throws ClassNotFoundException, SQLException, Exception {
        String RESOURCE_BUNDLE_PATH;
        try {

            switch (database) {
                case SERVA:
                    RESOURCE_BUNDLE_PATH = "co.edu.udea.udearroba.properties.SERVADataBase";
                    break;
                default:
                    RESOURCE_BUNDLE_PATH = "";
            }
            ResourceBundle resource = ResourceBundle.getBundle(RESOURCE_BUNDLE_PATH, new Locale("es"));
            Class.forName(resource.getString("dataBaseDriver"));
            Connection connection = DriverManager.getConnection(resource.getString("dataBaseURL"), resource.getString("dataBaseUser"), resource.getString("dataBasePassword"));
            return connection;
        } catch (ClassNotFoundException ex) {
            throw new ClassNotFoundException(Texts.getText("missingDriver"));
        } catch (SQLException ex) {
            throw new SQLException(Texts.getText("SQLExceptionMessage") + " " + ex.getMessage());
        } catch (MissingResourceException ex) {
            throw new Exception(Texts.getText("missingResourceBundle"));
        }
    }
}
