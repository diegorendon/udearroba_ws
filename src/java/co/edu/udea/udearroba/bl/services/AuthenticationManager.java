package co.edu.udea.udearroba.bl.services;

import co.edu.udea.UtilidadesAutenticacion_wsdl.UtilidadesAutenticacionPortTypeProxy;
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

    private final String TOKEN = "5facbdd992ecd3e667df2b544e22a80a8274fd59";    // TODO: update this token with the appropriate information.
    private final String CLAVE = "251860937072417361372773";                    // TODO: update this token with the appropriate information.
    private final String MARES_USERINFO_REST_CALL = "consultapersonamares";     // The Web service to call in order to get the user information.
    private final String MARES_USERINFO_PARAM1 = "cedula";                      // The Web service's param name used to get the user information.
    private final String SIPE_USERINFO_REST_CALL = "consultapersonamares";      // The Web service to call in order to get the user information.
    private final String SIPE_USERINFO_PARAM1 = "cedula";                       // The Web service's param name used to get the user information.
    private OrgSistemasWebServiceClient RESTWebServiceClient;                   // Use the OrgSistemasSecurity.jar.
    private UtilidadesAutenticacionPortTypeProxy SOAPWebServiceClient;          // Use the AutenticacionUdeA.jar.
    private UserDAO userDAO;

    /**
     * Constructor.
     *
     * Initializes the Web Service client.
     */
    public AuthenticationManager() {
        try {
            userDAO = new UserDAO();
            this.RESTWebServiceClient = new OrgSistemasWebServiceClient();
            this.SOAPWebServiceClient = new UtilidadesAutenticacionPortTypeProxy();
        } catch (OrgSistemasSecurityException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserDAOException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
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
        BasicUserInformation basicUserInfo = this.getBasicUserInfo(username, password);
        userValidated = this.validateIdentification(basicUserInfo.getIdentification());
        return userValidated;
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
        User user = null;
        BasicUserInformation basicUserInfo = this.getBasicUserInfo(username, password);
        if (basicUserInfo != null) {
            switch (basicUserInfo.getRole()) {
                case STUDENT: {
                    this.RESTWebServiceClient.addParam(MARES_USERINFO_PARAM1, basicUserInfo.getIdentification());
                    List<MARESStudent> userList = new ArrayList<MARESStudent>();
                    try {
                        userList = this.RESTWebServiceClient.obtenerBean(MARES_USERINFO_REST_CALL, TOKEN, MARESStudent.class);
                    } catch (OrgSistemasSecurityException ex) {
                        Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    int lastRecordIndex = userList.size() - 1;
                    if (!userList.isEmpty() && userList.get(lastRecordIndex) != null) {
                        MARESStudent student = (MARESStudent) userList.get(lastRecordIndex); // Take the last record returned
                        user = new User();
                        user.setIdNumber(basicUserInfo.getIdentification());
                        user.setUserName(username);
                        user.setFirstName(student.getNombre());
                        user.setLastName(student.getApellidos());
                        user.setEmail(student.getEmail());
                        user.setPhone1(student.getTelefono());
                        user.setPhone2(student.getCelular());
                        user.setCity(student.getNombreMunicipioResidencia());
                        user.setCountry(student.getNombrePaisResidencia());
                    }
                    if (user != null) {
                        AuthenticationInformation authenticationInfo = new AuthenticationInformation(user.getUserName(), password, user.getIdNumber());
                        this.creteOrUpdateAuthenticationInfoInSERVA(authenticationInfo);
                    }
                    break;
                }
                case EMPLOYEE: {
                    this.RESTWebServiceClient.addParam(SIPE_USERINFO_PARAM1, basicUserInfo.getIdentification());
                    List<SIPEEmployee> userList = new ArrayList<SIPEEmployee>();
                    try {
                        userList = this.RESTWebServiceClient.obtenerBean(SIPE_USERINFO_REST_CALL, TOKEN, SIPEEmployee.class);
                    } catch (OrgSistemasSecurityException ex) {
                        Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    int lastRecordIndex = userList.size() - 1;
                    if (!userList.isEmpty() && userList.get(lastRecordIndex) != null) {
                        SIPEEmployee employee = (SIPEEmployee) userList.get(lastRecordIndex); // Take the last record returned
                        user = new User();
                        user.setIdNumber(basicUserInfo.getIdentification());
                        user.setUserName(username);
                        user.setFirstName(employee.getNombre());
                        user.setLastName(employee.getApellidos());
                        user.setEmail(employee.getEmail());
                        user.setPhone1(employee.getTelefono());
                        user.setPhone2(employee.getCelular());
                        user.setCity(employee.getNombreMunicipioResidencia());
                        user.setCountry(employee.getNombrePaisResidencia());
                    }
                    break;
                }
            }
        }
        return user;
    }

    /**
     * Returns the identification and role information of a user from UdeA
     * Portal's databases.
     *
     * @param username The username.
     * @param password The user password hash.
     *
     * @return BasicUserInformation an object (of an inner class) with the
     * user's identification and role information.
     */
    private BasicUserInformation getBasicUserInfo(String username, String password) {
        String identification = "ERROR";
        BasicUserInformation basicUserInfo = null;
        boolean isValidIdentification = false;
        // First: try to get info from MARES.
        if (!isValidIdentification) {
            try {
                identification = this.SOAPWebServiceClient.autenticaLogin(username, password); //TODO: Use a REST Web Sservice instead of a SOAP one.
            } catch (Exception ex) {
                Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            isValidIdentification = validateIdentification(identification);
            if (isValidIdentification) {
                basicUserInfo = new BasicUserInformation(identification);
                basicUserInfo.setRole(UserRole.STUDENT);
            }
        }
        // Second: try to get info from SERVA.
        if (!isValidIdentification) {
            try {
                AuthenticationInformation authenticationInfo = this.userDAO.getAuthenticationInfoFromSERVA(username, password);
                identification = authenticationInfo.getIdentification();
            } catch (Exception ex) {
                Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            isValidIdentification = validateIdentification(identification);
            if (isValidIdentification) {
                basicUserInfo = new BasicUserInformation(identification);
                basicUserInfo.setRole(UserRole.STUDENT);
            }
        }
        // Third: try to get info from SIPE if the two past tries had failed.
        if (!isValidIdentification) {
            try {
                // TODO: implement call to SIPE.
//                identification = this.RESTWebServiceClient.autenticaLogin(username, password); //TODO: Use a REST Web Sservice instead of a SOAP one.
            } catch (Exception ex) {
                Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            isValidIdentification = validateIdentification(identification);
            if (isValidIdentification) {
                basicUserInfo = new BasicUserInformation(identification);
                basicUserInfo.setRole(UserRole.EMPLOYEE);
            }
        }
        return isValidIdentification ? basicUserInfo : null;
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

    /**
     * Create or update the user authentication information in SERVA.
     *
     * @param username The username.
     * @param password The user password hash.
     *
     * @return User an User DTO with the user information.
     */
    private boolean creteOrUpdateAuthenticationInfoInSERVA(AuthenticationInformation authInfo) {
        try {
            if (this.userDAO.getAuthenticationInfoFromSERVA(authInfo.getIdentification()) != null) {
                return this.userDAO.update(authInfo);
            } else {
                return this.userDAO.insert(authInfo);
            }
        } catch (Exception ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /**
     * Manage the authentication procces of the UdeA Portal's users.
     *
     * @author Diego Rendón
     */
    private class BasicUserInformation {

        private String identification;
        private UserRole role;

        /**
         * Constructor with the identification parameter.
         *
         * @param identification String with the identification name of the
         * user.
         *
         * Instantiates the user's basic information with the identification.
         */
        public BasicUserInformation(String identification) {
            this.identification = identification;
        }

        /**
         * Constructor with the identification and role parameters.
         *
         * @param identification String with the identification name of the
         * user.
         * @param role UserRole enumeration value that indicates the role of the user.
         *
         * Instantiates the user's basic information with the identification and
         * role.
         */
        public BasicUserInformation(String identification, UserRole role) {
            this.identification = identification;
            this.role = role;
        }

        public String getIdentification() {
            return identification;
        }

        public void setIdentification(String identification) {
            this.identification = identification;
        }

        public UserRole getRole() {
            return role;
        }

        public void setRole(UserRole role) {
            this.role = role;
        }
    }

    /**
     * Enumeration of the possible user roles.
     */
    private enum UserRole {

        EMPLOYEE(3, "Empleado"), STUDENT(5, "Estudiante");       // TODO: move strings to a buundle.
        private int code;
        private String label;

        private UserRole(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

    }
}
