package co.edu.udea.udearroba.bl.services;

import co.edu.udea.UtilidadesAutenticacion_wsdl.UtilidadesAutenticacionPortTypeProxy;
import co.edu.udea.exception.OrgSistemasSecurityException;
import co.edu.udea.udearroba.dto.EstudianteMares;
import co.edu.udea.udearroba.dto.User;
import co.edu.udea.wsClient.OrgSistemasWebServiceClient;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manage the authentication procces of the UdeA Portal's users.
 *
 * @author Diego
 */
public class AuthenticationManager {

//    private static UtilidadesAutenticacionPortTypeProxy util = new UtilidadesAutenticacionPortTypeProxy();
    private static final String TOKEN = "5facbdd992ecd3e667df2b544e22a80a8274fd59"; // TODO: update this token with the appropriate information.
    private static final String CLAVE = "251860937072417361372773";                 // TODO: update this token with the appropriate information.
    private static final String USERINFO_REST_CALL = "consultapersonamares";        // The Web service to call in order to get the user information.
    private OrgSistemasWebServiceClient RESTWebServiceClient;                       // Use the OrgSistemasSecurity.jar.
    private UtilidadesAutenticacionPortTypeProxy SOAPWebServiceClient;              // Use the AutenticacionUdeA.jar.

    /**
     * Constructor.
     *
     * Initializes the Web Service client.
     */
    public AuthenticationManager() {
        try {
            this.RESTWebServiceClient = new OrgSistemasWebServiceClient();
            this.SOAPWebServiceClient = new UtilidadesAutenticacionPortTypeProxy();
        } catch (OrgSistemasSecurityException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Manage the user authentication process.
     *
     * @param username The username.
     * @param password The password hash.
     *
     * @return boolean True if the user's credentials were validated against the
     * UdeA Portal or false in other case.
     */
    public boolean authenticateUser(String username, String password) {
        password = password.replace("+", " ");
        boolean userValidated = false;
        String response = "ERROR";
        // First: try to validate user against MARES.
        try {
            response = this.SOAPWebServiceClient.autenticaLogin(username, password); //TODO: Use a REST Web Sservice instead of a SOAP one.
        } catch (Exception ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        userValidated = response != null && !response.contains("ERROR");
        // Second: try to validate user against SERVA.
        if (!userValidated) {
            try {
//                response = userDAO.validateserva();
            } catch (Exception ex) {
                Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            userValidated = response != null && !response.contains("ERROR");
        }
        // Third: try to validate user against SIPE.
        // TODO: implement SIPE validation.
        return userValidated;
    }

    /**
     * Retrieves the user information from UdeA Portal's databases.
     *
     * @param username The username.
     * @param password The user password hash.
     *
     * @return User an User DTO object with the user information.
     */
    public User getUserInformation(String username, String password) {
        password = password.replace("+", " ");
        boolean isValidIdentification = false;
        String identification = "ERROR";
        // First: try to get info from MARES.
        try {
            identification = this.SOAPWebServiceClient.autenticaLogin(username, password); //TODO: Use a REST Web Sservice instead of a SOAP one.
        } catch (Exception ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        isValidIdentification = identification != null && !identification.contains("ERROR");
        // Second: try to get info from SERVA.
        // TODO: implement call to SERVA.
        if (!isValidIdentification) {
            try {
//                response = userDAO.validateserva();
            } catch (Exception ex) {
                Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            isValidIdentification = identification != null && !identification.contains("ERROR");
        }
        // Third: try to get info from SIPE.
        // TODO: implement call to SIPE.
        
        User user = null;
        if (isValidIdentification) {
            this.RESTWebServiceClient.addParam("cedula", identification);
            List<EstudianteMares> userList = new ArrayList<EstudianteMares>();
            try {
                userList = this.RESTWebServiceClient.obtenerBean(USERINFO_REST_CALL, TOKEN, EstudianteMares.class);
            } catch (OrgSistemasSecurityException ex) {
                Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!userList.isEmpty() && userList.get(userList.size()-1) != null) {
                user = new User();
                user.setIdnumber(identification);
                user.setUsername(username);
                user.setFirstname(userList.get(0).getNombre());
                user.setLastname(userList.get(0).getApellidos());
                user.setEmail(userList.get(0).getEmail());
                user.setPhone1(userList.get(0).getTelefono());
                user.setPhone2(userList.get(0).getCelular());
                user.setCity(userList.get(0).getNombreMunicipioResidencia());
                user.setCountry(userList.get(0).getNombrePaisResidencia());
            }
        }
        return user;
    }
}
