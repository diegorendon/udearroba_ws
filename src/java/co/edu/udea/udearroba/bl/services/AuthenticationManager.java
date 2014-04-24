package co.edu.udea.udearroba.bl.services;

import co.edu.udea.exception.OrgSistemasSecurityException;
import co.edu.udea.udearroba.dao.impl.UserDAO;
import co.edu.udea.udearroba.dto.AuthenticationInformation;
import co.edu.udea.udearroba.dto.MARESStudent;
import co.edu.udea.udearroba.dto.SIPEEmployee;
import co.edu.udea.udearroba.dto.User;
import co.edu.udea.udearroba.exception.UserDAOException;
import co.edu.udea.udearroba.i18n.Texts;
import co.edu.udea.udearroba.util.EncryptionUtil;
import co.edu.udea.wsClient.OrgSistemasWebServiceClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manage the authentication procces of the UdeA Portal's users.
 *
 * @author Diego Rendón
 */
public class AuthenticationManager {

    private boolean useTestingData;                                             // Switches between testing and production modes for the Web Services' calls. Set to true for development and false for production.
    private String TOKEN;                                                       // Token assigned to Ude@ in order to be able to use the REST Web services.
    private String PUBLIC_KEY;                                                  // Public key used by Ude@ in order to be able to use the REST Web services.
    private String SECRET_KEY;                                                  // The secret key used by Ude@ to encrypt/decrypt the data.
    private String INITIAL_VECTOR;                                              // The initial vector used by Ude@ to encrypt/decrypt the data.
    private final String MUA_AUTHENTICATION_REST_CALL = "validarusuariooidxcn"; // The Web service to call to get the user identification.
    private final String MUA_AUTHENTICATION_PARAM1 = "usuario";                 // The Web service's first param name used to authenticate the user.
    private final String MUA_AUTHENTICATION_PARAM2 = "clave";                   // The Web service's second param name used to authenticate the user.
    private final String MUA_USERNAME_REST_CALL = "buscarnombreusuariomua";     // The Web service to call to get the user information of a student in MARES.
    private final String MUA_USERNAME_PARAM1 = "cedula";                        // The Web service's first param name used to get the username.
    private final String MUA_USERNAME_PARAM2 = "tipoDocumento";                 // The Web service's second param name used to get the username.
    private final String MARES_USERINFO_REST_CALL = "consultapersonamares";     // The Web service to call to get the user information of a student in MARES.
    private final String MARES_USERINFO_PARAM1 = "cedula";                      // The Web service's param name used to get the user information.
    private final String SIPE_USERINFO_REST_CALL = "consultaempleadossipe";     // The Web service to call to get the user information of an employee in SIPE.
    private final String SIPE_USERINFO_PARAM1 = "cedula";                       // The Web service's param name used to get the user information.
    private final String RESOURCE_BUNDLE_PATH = "co.edu.udea.udearroba.properties.Application"; // Resource bundle with the application properties
    private ResourceBundle resource;
    private UserDAO userDAO;

    /**
     * Default constructor without parameters.
     */
    public AuthenticationManager() {
        try {
            this.resource = ResourceBundle.getBundle(RESOURCE_BUNDLE_PATH, new Locale("es"));
            this.useTestingData = Boolean.parseBoolean(resource.getString("useTestingData"));
            this.TOKEN = resource.getString("token");
            this.PUBLIC_KEY = resource.getString("publicKey");
            this.SECRET_KEY = resource.getString("secretKey");
            this.INITIAL_VECTOR = (this.SECRET_KEY.length() >= 16) ? this.SECRET_KEY.substring(0, 16) : "167c82ec048434e9";
            userDAO = new UserDAO();
        } catch (UserDAOException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, Texts.getText("userDAOLogMessage"), ex);
        } catch (MissingResourceException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, Texts.getText("applicationPropertiesLogMessage"), ex);
        }
    }

    /**
     * Authenticate the user against the UdeA Portal's databases.
     *
     * @param username The username.
     * @param password The encrypted user password.
     *
     * @return boolean True if the user's credentials were validated against the
     * UdeA Portal or false in other case.
     */
    public boolean authenticateUser(String username, String password) {
        boolean userValidated;
        String identification = this.getIdentification(username, password);
        userValidated = this.validateIdentification(identification);
        return userValidated;
    }

    /**
     * Checks if a user exist in the UdeA Portal's databases.
     *
     * @param identification The user identification.
     *
     * @return boolean True if the user has credentials in the UdeA Portal's
     * databases or false in other case.
     */
    public boolean checkUserExistence(String identification) {
        OrgSistemasWebServiceClient RESTWebServiceClient = null;
        try {
            RESTWebServiceClient = new OrgSistemasWebServiceClient(useTestingData);
        } catch (OrgSistemasSecurityException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, Texts.getText("RESTWebServiceClientLogMessage"), ex);
        }
        boolean userExist;
        String username = null;
        try {
            username = RESTWebServiceClient.obtenerString(MUA_USERNAME_REST_CALL, TOKEN);
        } catch (OrgSistemasSecurityException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, Texts.getText("usernameRESTCallLogMessage"), ex);
        }
        userExist = this.validateUsername(username);
        return userExist;
    }

    /**
     * Return the user information from UdeA Portal's databases.
     *
     * @param username The username.
     * @param password The encrypted user password.
     *
     * @return User An User DTO with the user information.
     */
    public User getUserInformation(String username, String password) {
        OrgSistemasWebServiceClient RESTWebServiceClient = null;
        try {
            RESTWebServiceClient = new OrgSistemasWebServiceClient(useTestingData);
        } catch (OrgSistemasSecurityException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, Texts.getText("RESTWebServiceClientLogMessage"), ex);
        }
        User user = null;
        boolean isValidIdentification;
        String identification = this.getIdentification(username, password).trim();
        isValidIdentification = this.validateIdentification(identification);
        if (isValidIdentification && RESTWebServiceClient != null) {
            // First: try to get info from MARES.
            RESTWebServiceClient.addParam(MARES_USERINFO_PARAM1, identification);
            List<MARESStudent> studentsList = new ArrayList<MARESStudent>();
            try {
                studentsList = RESTWebServiceClient.obtenerBean(MARES_USERINFO_REST_CALL, TOKEN, MARESStudent.class);
            } catch (OrgSistemasSecurityException ex) {
                Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, Texts.getText("MARESUserInfoRESTCallLogMessage"), ex);
            }
            int lastRecordIndex = studentsList.size() - 1;
            if (!studentsList.isEmpty() && studentsList.get(lastRecordIndex) != null) {
                MARESStudent student = (MARESStudent) studentsList.get(lastRecordIndex); // Take the last record returned
                user = new User();
                user.setIdNumber(identification);
                user.setUserName(username);
                user.setFirstName(student.getNombre());
                user.setLastName(student.getApellidos());
                user.setEmail(student.getEmail());
                user.setPhone1(student.getTelefono());
                user.setPhone2(student.getCelular());
                user.setCity(student.getNombreMunicipioResidencia());
                user.setCountry(student.getNombrePaisResidencia());
            }
            // Second: try to get info from SIPE
            if (user == null) {
                try {
                    RESTWebServiceClient = new OrgSistemasWebServiceClient(useTestingData);
                } catch (OrgSistemasSecurityException ex) {
                    Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, Texts.getText("RESTWebServiceClientLogMessage"), ex);
                }
                List<SIPEEmployee> employeesList = new ArrayList<SIPEEmployee>();
                if (RESTWebServiceClient != null) {
                    RESTWebServiceClient.addParam(SIPE_USERINFO_PARAM1, identification);
                    try {
                        employeesList = RESTWebServiceClient.obtenerBean(SIPE_USERINFO_REST_CALL, TOKEN, SIPEEmployee.class);
                    } catch (OrgSistemasSecurityException ex) {
                        Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, Texts.getText("SIPEUserInfoRESTCallLogMessage"), ex);
                    }
                }
                lastRecordIndex = employeesList.size() - 1;
                if (!employeesList.isEmpty() && employeesList.get(lastRecordIndex) != null) {
                    SIPEEmployee employee = (SIPEEmployee) employeesList.get(lastRecordIndex); // Take the last record returned
                    user = new User();
                    user.setIdNumber(identification);
                    user.setUserName(username);
                    user.setFirstName(employee.getNombre());
                    user.setLastName(employee.getPrimerApellido() + " " + employee.getSegundoApellido());
                    user.setEmail(employee.getEmail());
                    user.setPhone1(employee.getTelefono());
                    user.setPhone2(employee.getCelular());
                    user.setCity(null);
                    user.setCountry(null);
                }
            }
            if (user != null) {
                AuthenticationInformation authenticationInfo = new AuthenticationInformation(user.getUserName(), password, user.getIdNumber());
                this.creteOrUpdateAuthenticationInfo(authenticationInfo);
            }
        }
        return user;
    }

    /**
     * Returns the username of a user from UdeA Portal's databases.
     *
     * @param identification The user identification.
     *
     * @return String The username associated with the user's identification or
     * NULL.
     */
    public String getUserName(String identification) {
        OrgSistemasWebServiceClient RESTWebServiceClient = null;
        try {
            RESTWebServiceClient = new OrgSistemasWebServiceClient(useTestingData);
        } catch (OrgSistemasSecurityException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, Texts.getText("RESTWebServiceClientLogMessage"), ex);
        }
        boolean isValidIdentification, isValidUsername = false;
        String username = "ERROR";
        isValidIdentification = this.validateIdentification(identification);
        if (isValidIdentification && RESTWebServiceClient != null) {
            RESTWebServiceClient.addParam(MUA_USERNAME_PARAM1, identification);
            RESTWebServiceClient.addParam(MUA_USERNAME_PARAM2, "-");    // This parameter is set to "-" in order to ignore it.
            try {
                username = RESTWebServiceClient.obtenerString(MUA_USERNAME_REST_CALL, TOKEN);
            } catch (OrgSistemasSecurityException ex) {
                Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, Texts.getText("usernameRESTCallLogMessage"), ex);
            }
            isValidUsername = this.validateUsername(username);
        }
        return isValidUsername ? username : null;
    }

    /**
     * Returns the identification of a user from UdeA Portal's databases.
     *
     * @param username The username.
     * @param password The encrypted user password.
     *
     * @return String The user identification number or NULL.
     */
    public String getIdentification(String username, String password) {
        OrgSistemasWebServiceClient RESTWebServiceClient = null;
        try {
            RESTWebServiceClient = new OrgSistemasWebServiceClient(PUBLIC_KEY, useTestingData);
        } catch (OrgSistemasSecurityException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, Texts.getText("RESTWebServiceClientLogMessage"), ex);
        }
        String identification = "ERROR";
        boolean isValidIdentification = false;
        // First: try to get info from MARES OR SIPE.
        if (!isValidIdentification && RESTWebServiceClient != null) {
            RESTWebServiceClient.addParam(MUA_AUTHENTICATION_PARAM1, username);
            RESTWebServiceClient.addParam(MUA_AUTHENTICATION_PARAM2, EncryptionUtil.decrypt(password, INITIAL_VECTOR, SECRET_KEY));
            try {
                identification = RESTWebServiceClient.obtenerString(MUA_AUTHENTICATION_REST_CALL, TOKEN).trim();
            } catch (OrgSistemasSecurityException ex) {
                Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, Texts.getText("identificationRESTCallLogMessage"), ex);
            }
            isValidIdentification = validateIdentification(identification);
        }
        // Second: try to get info from SERVA if the past try had failed.
        if (!isValidIdentification) {
            try {
                AuthenticationInformation authenticationInfo = this.userDAO.getAuthenticationInfoFromSERVA(username, password);
                if (authenticationInfo != null) {
                    identification = authenticationInfo.getIdentification().trim();
                }
            } catch (Exception ex) {
                Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, Texts.getText("SERVAAuthenticationInfoLogMessage"), ex);
            }
            isValidIdentification = validateIdentification(identification);
        }
        return isValidIdentification ? identification : null;
    }

    /**
     * Creates or updates the user authentication information in SERVA.
     *
     * @param authInfo An AuthenticationInformation DTO with the user
     * authentication information to be stored.
     *
     * @return User an User .
     * @return boolean True if the create or update operation was successfully
     * performed in the database and False in other case.
     */
    private boolean creteOrUpdateAuthenticationInfo(AuthenticationInformation authInfo) {
        try {
            if (this.userDAO.getAuthenticationInfoFromSERVA(authInfo.getIdentification()) != null) {
                return this.userDAO.update(authInfo);
            } else {
                return this.userDAO.insert(authInfo);
            }
        } catch (Exception ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, Texts.getText("SERVACreateOrUpdateAauthenticationInfoLogMessage"), ex);
            return false;
        }
    }

    /*
     * Validates if an identification number is correct.
     *
     * @param identification The user identification number to test.
     *
     * @return boolean True if the user identification is correct and False in other case.
     */
    private boolean validateIdentification(String identification) {
        boolean isValidIdentification = false;
        identification = identification.trim();
        if (identification != null && !identification.contains("ERROR")) {
            try { // TODO: delete this if the identification contains alphanumeric characters instead of only numbers digitss.
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
    private boolean validateUsername(String username) {
        boolean isValidUsername = false;
        username = username.trim();
        if (username != null && !username.contains("ERROR")) {
            isValidUsername = true;
        }
        return isValidUsername;
    }
}
