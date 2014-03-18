package co.edu.udea.udearroba.bl.services;

import co.edu.udea.UtilidadesAutenticacion_wsdl.UtilidadesAutenticacionPortTypeProxy;
import co.edu.udea.exception.OrgSistemasSecurityException;
import co.edu.udea.wsClient.OrgSistemasWebServiceClient;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manage the authentication procces of the UdeA Portal's users.
 *
 * @author Diego
 */
public class AuthenticationManager {

    private static final String TOKEN = "5facbdd992ecd3e667df2b544e22a80a8274fd59"; //TODO: update this token with the appropriate information
    private static final String CLAVE = "251860937072417361372773";                 //TODO: update this token with the appropriate information
    private OrgSistemasWebServiceClient RESTWebServiceClient;           // Use the OrgSistemasSecurity.jar
    private UtilidadesAutenticacionPortTypeProxy SOAPWebServiceClient;  // Use the AutenticacionUdeA.jar

    public AuthenticationManager() {
        try {
            this.RESTWebServiceClient = new OrgSistemasWebServiceClient();
            this.SOAPWebServiceClient = new UtilidadesAutenticacionPortTypeProxy();
        } catch (OrgSistemasSecurityException ex) {
            //TODO: manage this exception and log it
        }
    }

    public boolean authenticateUser(String username, String password) {
        boolean userValidated = false;
        String response;
        //            return username.equals("usuario.udearroba");
        try { // First: validate user against MARES.
            this.SOAPWebServiceClient.autenticaLogin(username, password);           //TODO: This is for testing only, delete it.
            response = this.SOAPWebServiceClient.autenticaLogin(username, password); //TODO: Use a REST Web Sservice instead of a SOAP one.
            return (response != null && !response.contains("ERROR"));
        } 
        catch (RemoteException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!userValidated) { // Second: if validation against MARES fails then validate user against SIPE.
            response = null;
            return (response != null && !response.contains("ERROR"));
        } else if (!userValidated) { // Third; if validation against MARES and SIPE fails then validate user against SERVA.
            response = null;
            return (response != null && !response.contains("ERROR"));
        } else {
            return false;
        }
    }
}