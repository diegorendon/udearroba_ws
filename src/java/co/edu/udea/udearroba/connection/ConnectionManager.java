package co.edu.udea.udearroba.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Manage the connection to the databases.
 *
 * @author Diego
 */
public class ConnectionManager {

    /**
     * Constant index for the SERVA database connection.
     */
    public static final int SERVA = 1;
//    public static final int SIPE = 2;
//    public static final int MARES = 3;

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
        try {
            String bundle;
            switch (database) {
                case SERVA:
                    bundle = "co.edu.udea.udearroba.properties.SERVADataBase";
                    break;
//                case SIPE:
//                    bundle = "co.edu.udea.udearroba.properties.SIPEDataBase";
//                    break;
//                case MARES:
//                    bundle = "co.edu.udea.udearroba.properties.MARESDataBase";
//                    break;
                default:
                    bundle = "";
            }
            ResourceBundle rb = ResourceBundle.getBundle(bundle);
            Class.forName(rb.getString("dataBaseDriver"));
            Connection connection = DriverManager.getConnection(rb.getString("dataBaseURL"), rb.getString("dataBaseUser"), rb.getString("dataBasePassword"));
            return connection;
        } catch (ClassNotFoundException ex) {
            throw new ClassNotFoundException("No se encuentra el driver de conexión."); // FIXME: move strings to a buundle;
        } catch (SQLException ex) {
            throw new SQLException("Ha ocurrido un error de SQL: " + ex.getMessage());     // FIXME: move strings to a buundle;
        } catch (MissingResourceException ex) {
            throw new Exception("No se encuentra el recurso de propiedades para las consultas.");   // FIXME: move strings to a buundle;
        }
    }
}
