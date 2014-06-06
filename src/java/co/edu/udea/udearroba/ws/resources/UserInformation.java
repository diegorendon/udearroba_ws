package co.edu.udea.udearroba.ws.resources;

import co.edu.udea.udearroba.bl.services.AuthenticationManager;
import co.edu.udea.udearroba.dto.User;
import co.edu.udea.udearroba.i18n.Texts;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service for retrieving users information.
 *
 * @author Diego Rendón
 */
@Path("/userinformation")
public class UserInformation {

    /**
     * Web service for retrieving user information from UdeA Portal's databases
     * given the username and password.
     *
     * @param username The username.
     * @param password The encrypted user password using AES128.
     *
     * @return String The JSON data string with the user information.
     */
    @GET
    @Path("{username}/{password}")
    @Produces("application/json" + ";charset=utf-8")
    public String getUserInformation(@PathParam("username") String username, @PathParam("password") String password) {
        password = password.replace("|", "/");      // Workaround to allow slashes in the URI for the encrypted password parameter.
        AuthenticationManager authManager = new AuthenticationManager();
        User user = authManager.getUserInformation(username, password);
        JSONObject JSONResponse = new JSONObject();
        try {
            Map<String, String> userInfoMap = new HashMap<String, String>();
            if (user != null) {
                userInfoMap.put("idnumber", user.getIdNumber());
                userInfoMap.put("username", user.getUserName());
                userInfoMap.put("firstname", user.getFirstName());
                userInfoMap.put("lastname", user.getLastName());
                userInfoMap.put("email", user.getEmail());
                userInfoMap.put("phone1", user.getPhone1());
                userInfoMap.put("phone2", user.getPhone2());
                userInfoMap.put("city", user.getCity());
                userInfoMap.put("country", user.getCountry());
                JSONResponse.put("response", userInfoMap);
            } else {
                userInfoMap.clear();
                JSONResponse.put("response", userInfoMap);
            }
        } catch (JSONException ex) {
            Logger.getLogger(UserInformation.class.getName()).log(Level.SEVERE, Texts.getText("JSONResponseUserInfoLogMessage"), ex);
        }
        return JSONResponse.toString();
    }

    /**
     * Web service for retrieving user information from UdeA Portal's databases
     * given the identification.
     *
     * @param identification The user identification.
     *
     * @return String The JSON data string with the user information.
     */
    @GET
    @Path("{identification}")
    @Produces("application/json" + ";charset=utf-8")
    public String getUserName(@PathParam("identification") String identification) {
        AuthenticationManager authManager = new AuthenticationManager();
        String username = authManager.getUserName(identification);
        JSONObject JSONResponse = new JSONObject();
        try {
            JSONResponse.put("response", username);
        } catch (JSONException ex) {
            Logger.getLogger(UserInformation.class.getName()).log(Level.SEVERE, Texts.getText("JSONResponseUserNameLogMessage"), ex);
        }
        return JSONResponse.toString();
    }
}
