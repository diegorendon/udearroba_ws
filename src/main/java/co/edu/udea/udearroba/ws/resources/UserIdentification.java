package co.edu.udea.udearroba.ws.resources;

import co.edu.udea.udearroba.bl.services.AuthenticationManager;
import co.edu.udea.udearroba.i18n.Texts;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.LoggerFactory;

/**
 * REST Web Service for retrieving users identification numbers.
 *
 * @author Diego Rendón
 */
@Path("/useridentification")
public class UserIdentification {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserIdentification.class);

    /**
     * Web service for retrieving user identification from UdeA Portal's databases.
     *
     * @param username The username.
     * @param password The encrypted user password using AES128.
     *
     * @return String The JSON data string with the user identification.
     */
    @GET
    @Path("{username}/{password}")
    @Produces("application/json" + ";charset=utf-8")
    public String getIdentification(@PathParam("username") String username, @PathParam("password") String password) {
        password = password.replace("|", "/");      // Workaround to allow slashes in the URI for the encrypted password parameter.
        AuthenticationManager authManager = new AuthenticationManager();
        String identification = authManager.getIdentification(username, password);
        JSONObject JSONResponse = new JSONObject();
        try {
            JSONResponse.put("response", identification);
        } catch (JSONException ex) {
            logger.error("{} - {}", Texts.getText("JSONResponseUserNameLogMessage"), ex.getMessage());
        }
        return JSONResponse.toString();
    }
}
