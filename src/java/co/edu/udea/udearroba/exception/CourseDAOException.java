package co.edu.udea.udearroba.exception;

import co.edu.udea.udearroba.i18n.Texts;

/**
 * Exception thrown from errors in the course DAO.
 * 
 * @author Diego Rendón
 */
public class CourseDAOException extends Exception {
    
    public static final String MISSING_RESOURCE_BUNDLE = Texts.getText("SQLPropertiesLogMessage");
    public static final String SQL_ERROR = Texts.getText("SQLExecutionLogMessage");

    public CourseDAOException(String message) {
        super(message);
    }
}
