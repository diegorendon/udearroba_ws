package co.edu.udea.udearroba.bl.services;

/**
 * Perform common business logic validations.
 *
 * @author Diego Rendón
 */
public class Validator {

    /*
     * Validates if an identification number is correct.
     *
     * @param identification The user identification number to test.
     *
     * @return boolean True if the user identification is correct and False in other case.
     */
    protected boolean validateIdentification(String identification) {
        boolean isValidIdentification = false;
        if (identification != null && !identification.contains("ERROR")) {
            identification = identification.trim();
            try {
                Long.parseLong(identification);
                isValidIdentification = true;
            } catch (NumberFormatException exception) {
                isValidIdentification = false;
            }
        }
        return isValidIdentification;
    }

    /*
     * Validates if an username is correct.
     *
     * @param username The username to test.
     *
     * @return boolean True if the username is correct and False in other case.
     */
    protected boolean validateUsername(String username) {
        boolean isValidUsername = false;
        if (username != null && !username.trim().contains("ERROR")) {
            isValidUsername = true;
        }
        return isValidUsername;
    }
    
}
