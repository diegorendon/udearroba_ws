/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.udea.udearroba.exception;

/**
 *
 * @author Diego
 */
public class UserDAOException extends Exception {

    public static final String MISSING_RESOURCE_BUNDLE = "No se encuentra el recurso de propiedades para las consultas.";
    public static final String SQL_ERROR = "Ha ocurrido un error de SQL";

    public UserDAOException(String message) {
        super(message);
    }
    
}
