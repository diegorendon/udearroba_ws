/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.udea.udearroba.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author Diego
 */
public class ConnectionManager {
    public static final int MARES = 1;
    public static final int SERVA = 2;

    public static Connection getConnection(int database) throws ClassNotFoundException, SQLException, Exception {
        try {
            String bundle;
            switch (database) {
                case MARES:
                    bundle = "co.edu.udea.udearroba.properties.DBMares";
                    break;
                case SERVA:
                    bundle = "co.edu.udea.udearroba.properties.DBServa";
                    break;
                default:
                    bundle = "";
            }
            ResourceBundle rb = ResourceBundle.getBundle(bundle);
            Class.forName(rb.getString("driverDB"));
            Connection connection = DriverManager.getConnection(rb.getString("urlDB"), rb.getString("user"), rb.getString("passDB"));
            return connection;
        } catch (ClassNotFoundException ex) {
            throw new ClassNotFoundException("No se encuentra el driver de conexión.");
        } catch (SQLException ex) {
            throw new SQLException("Ha ocurrido un error de SQL.");
        } catch (MissingResourceException ex) {
            throw new Exception("No se encuentra el recurso de propiedades para las consultas.");
        }
    }
}