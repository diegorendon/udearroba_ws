/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.udea.udearroba.exception;

import co.edu.udea.udearroba.i18n.Texts;

/**
 * Exception thrown from errors in the user DAO.
 * 
 * @author Diego Rendón
 */
public class UserDAOException extends Exception {

    public static final String MISSING_RESOURCE_BUNDLE = Texts.getText("SQLPropertiesLogMessage");
    public static final String SQL_ERROR = Texts.getText("SQLExecutionLogMessage");

    public UserDAOException(String message) {
        super(message);
    }
    
}
