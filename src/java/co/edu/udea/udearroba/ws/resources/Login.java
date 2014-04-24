package co.edu.udea.udearroba.ws.resources;

import co.edu.udea.udearroba.bl.services.AuthenticationManager;
import co.edu.udea.udearroba.i18n.Texts;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service for Moodle's users authentication.
 *
 * @author Diego Rendón
 */
@Path("/login")
public class Login {

    /**
     * Web service for authentication via UdeA Portal's databases.
     *
     * @param username The username.
     * @param password The password hash.
     *
     * @return boolean True if the user's credentials were validated against the
     * UdeA Portal or false in other case.
     */
    @GET
    @Path("{username}/{password}")
    @Produces("application/json" + ";charset=utf-8")
    public String authenticateUser(@PathParam("username") String username, @PathParam("password") String password) {
        AuthenticationManager authManager = new AuthenticationManager();
        JSONObject JSONResponse = new JSONObject();
        try {
            if (authManager.authenticateUser(username, password)) {
                JSONResponse.put("response", true);
            } else {
                JSONResponse.put("response", false);
            }
        } catch (JSONException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, Texts.getText("JSONResponseAuthenticateUserLogMessage"), ex);
        }
        return JSONResponse.toString();
    }

}
