package co.edu.udea.udearroba.bl.services;

import co.edu.udea.exception.OrgSistemasSecurityException;
import co.edu.udea.udearroba.dao.impl.UserDAO;
import co.edu.udea.udearroba.dto.AuthenticationInformation;
import co.edu.udea.udearroba.dto.MARESStudent;
import co.edu.udea.udearroba.dto.SIPEEmployee;
import co.edu.udea.udearroba.dto.User;
import co.edu.udea.udearroba.exception.UserDAOException;
import co.edu.udea.wsClient.OrgSistemasWebServiceClient;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manage the authentication procces of the UdeA Portal's users.
 *
 * @author Diego Rendón
 */
public class AuthenticationManager {

    private final String TOKEN = "167c82ec048434e9ef8e99e373ac0c6a2f21ad16";    // Token assigned to Ude@ in order to be able to use the REST Web services.
    private final String PUBLIC_KEY = "235589583811087512133117";               // Public key used by Ude@ in order to be able to use the REST Web services.
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
    private UserDAO userDAO;
    
    boolean dabug = false;  //TODO: delete and change for production correct value

    /**
     * Constructor.
     *
     * Initializes the Web Service client.
     */
    public AuthenticationManager() {
        try {
            userDAO = new UserDAO();
        } catch (UserDAOException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, "Creación del DAO de usuarios", ex);
        }
    }

    /**
     * Authenticate the user against the UdeA Portal's databases.
     *
     * @param username The username.
     * @param password The password hash.
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
            RESTWebServiceClient = new OrgSistemasWebServiceClient(PUBLIC_KEY, dabug);
        } catch (OrgSistemasSecurityException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean userExist;
        String username = null;
        try {
            username = RESTWebServiceClient.obtenerString(MUA_USERNAME_REST_CALL, TOKEN);
        } catch (OrgSistemasSecurityException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        userExist = this.validateUsername(username);
        return userExist;
    }

    /**
     * Return the user information from UdeA Portal's databases.
     *
     * @param username The username.
     * @param password The user password hash.
     *
     * @return User an User DTO with the user information.
     */
    public User getUserInformation(String username, String password) {
        OrgSistemasWebServiceClient RESTWebServiceClient = null;
        try {
            RESTWebServiceClient = new OrgSistemasWebServiceClient(PUBLIC_KEY, dabug);
        } catch (OrgSistemasSecurityException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, "getUserInformation creación del wsClient con public_key y false.", ex);
        }
        User user = null;
        boolean isValidIdentification;
        String identification = this.getIdentification(username, password);
        isValidIdentification = this.validateIdentification(identification);
        if (isValidIdentification && RESTWebServiceClient != null) {
            // First: try to get info from MARES.
            RESTWebServiceClient.addParam(MARES_USERINFO_PARAM1, identification);
            List<MARESStudent> studentsList = new ArrayList<MARESStudent>();
            try {
                studentsList = RESTWebServiceClient.obtenerBean(MARES_USERINFO_REST_CALL, TOKEN, MARESStudent.class);
            } catch (OrgSistemasSecurityException ex) {
                Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, "getUserInformation llamado al WS para obtener la información de usuario.", ex);
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
                    RESTWebServiceClient = new OrgSistemasWebServiceClient(PUBLIC_KEY, dabug);
                } catch (OrgSistemasSecurityException ex) {
                    Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, "getUserInformation segunda creación del wsClient.", ex);
                }
                List<SIPEEmployee> employeesList = new ArrayList<SIPEEmployee>();
                if (RESTWebServiceClient != null) {
                    RESTWebServiceClient.addParam(SIPE_USERINFO_PARAM1, identification);
                    try {
                        employeesList = RESTWebServiceClient.obtenerBean(SIPE_USERINFO_REST_CALL, TOKEN, SIPEEmployee.class);
                    } catch (OrgSistemasSecurityException ex) {
                        Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, "getUserInformation llamado al WS para obtener la información del empleado en SIPE.", ex);
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
            RESTWebServiceClient = new OrgSistemasWebServiceClient(PUBLIC_KEY, dabug);
        } catch (OrgSistemasSecurityException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, "getUserName Creación del ws Client con public_key y false ", ex);
        }
        boolean isValidIdentification, isValidUsername = false;
        String username = "ERROR";
        isValidIdentification = this.validateIdentification(identification);
        if (isValidIdentification && RESTWebServiceClient != null) {
            RESTWebServiceClient.addParam(MUA_USERNAME_PARAM1, identification);
            RESTWebServiceClient.addParam(MUA_USERNAME_PARAM2, "CC");
            try {
                username = RESTWebServiceClient.obtenerString(MUA_USERNAME_REST_CALL, TOKEN);
            } catch (OrgSistemasSecurityException ex) {
                Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, "getUserName llamado al WS para obtener el nombre de usuario en MUA.", ex);
            }
            isValidUsername = this.validateUsername(username);
        }
        return isValidUsername ? username : null;
    }

    /**
     * Returns the identification of a user from UdeA Portal's databases.
     *
     * @param username The username.
     * @param password The user password hash.
     *
     * @return String the user identification number or NULL.
     */
    public String getIdentification(String username, String password) {
        OrgSistemasWebServiceClient RESTWebServiceClient = null;
        try {
            RESTWebServiceClient = new OrgSistemasWebServiceClient(PUBLIC_KEY, dabug);
        } catch (OrgSistemasSecurityException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, "getIdentification Creación del wsClient con public_key y false", ex);
        }
        String identification = "ERROR";
        boolean isValidIdentification = false;
        // First: try to get info from MARES OR SIPE.
        if (!isValidIdentification && RESTWebServiceClient != null) {
            RESTWebServiceClient.addParam(MUA_AUTHENTICATION_PARAM1, username);
            RESTWebServiceClient.addParam(MUA_AUTHENTICATION_PARAM2, password);
            try {
                identification = RESTWebServiceClient.obtenerString(MUA_AUTHENTICATION_REST_CALL, TOKEN);
            } catch (Exception ex) {
                Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, "getIdentification excepción general del llamado del WS para obtener la identificación en MUA.", ex);
            } catch (OrgSistemasSecurityException ex) {
                Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, "getIdentification excepción de Organización y sistemas del llamado del WS para obtener la identificación en MUA.", ex);
            }
            isValidIdentification = validateIdentification(identification);
        }
        // Second: try to get info from SERVA if the past try had failed.
        if (!isValidIdentification) {
            try {
                AuthenticationInformation authenticationInfo = this.userDAO.getAuthenticationInfoFromSERVA(username, password);
                identification = authenticationInfo.getIdentification();
            } catch (Exception ex) {
                Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            isValidIdentification = validateIdentification(identification);
        }
        return isValidIdentification ? identification : null;
    }

    /**
     * Create or update the user authentication information in SERVA.
     *
     * @param username The username.
     * @param password The user password hash.
     *
     * @return User an User DTO with the user information.
     */
    private boolean creteOrUpdateAuthenticationInfo(AuthenticationInformation authInfo) {
        try {
            if (this.userDAO.getAuthenticationInfoFromSERVA(authInfo.getIdentification()) != null) {
                return this.userDAO.update(authInfo);
            } else {
                return this.userDAO.insert(authInfo);
            }
        } catch (Exception ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, "Creación o actualización de registro en SERVA.", ex);
            return false;
        }
    }

    /*
     * Validates if an identification returned for a REST Web Service call is correct.
     *
     * @param identification The user identification to test.
     *
     * @return bolean True if the user identification is correct and False in other case.
     */
    private boolean validateIdentification(String identification) {
        boolean isValidIdentification = false;
        if (identification != null && !identification.contains("ERROR")) {
            try {
                Long.parseLong(identification);                     // TODO: delete this if the identification contains alphanumeric characters instead of only numbers.
                isValidIdentification = true;
            } catch (NumberFormatException exception) {
                isValidIdentification = false;
            }
        }
        return isValidIdentification;
    }

    /*
     * Validates if an username returned by a REST Web Service call is correct.
     *
     * @param username The username to test.
     *
     * @return bolean True if the username is correct and False in other case.
     */
    private boolean validateUsername(String username) {
        boolean isValidIdentification = false;
        if (username != null && !username.contains("ERROR")) {
            isValidIdentification = true;
        }
        return isValidIdentification;
    }
}
