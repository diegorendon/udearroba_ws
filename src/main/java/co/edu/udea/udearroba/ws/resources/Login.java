package co.edu.udea.udearroba.ws.resources;

import co.edu.udea.udearroba.bl.services.AuthenticationManager;
import co.edu.udea.udearroba.bl.services.EnrolmentManager;
import co.edu.udea.udearroba.i18n.Texts;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.LoggerFactory;

/**
 * REST Web Service for users authentication.
 *
 * @author Diego Rendón
 */
@Path("/login")
public class Login {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Login.class);

    /**
     * Web service for authentication via UdeA Portal's databases.
     *
     * @param username The username.
     * @param password The encrypted user password using AES128.
     *
     * @return boolean True if the user's credentials were validated against the
     * UdeA Portal or false in other case.
     */
    @GET
    @Path("{username}/{password}")
    @Produces("application/json" + ";charset=utf-8")
    public String authenticateUser(@PathParam("username") String username, @PathParam("password") String password) {
        password = password.replace("|", "/");      // Workaround to allow slashes in the URI for the encrypted password parameter.
        AuthenticationManager authManager = new AuthenticationManager();
        JSONObject JSONResponse = new JSONObject();
        try {
            if (authManager.authenticateUser(username, password)) {
                JSONResponse.put("response", true);
            } else {
                JSONResponse.put("response", false);
            }
        } catch (JSONException ex) {
            logger.error("{} - {}", Texts.getText("JSONResponseAuthenticateUserLogMessage"), ex.getMessage());
        }
        return JSONResponse.toString();
    }

}
