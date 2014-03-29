package co.edu.udea.udearroba.ws.resources;

import co.edu.udea.udearroba.bl.services.AuthenticationManager;
import co.edu.udea.udearroba.dto.User;
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
 * REST Web Service for retrieving Moodle's users information.
 *
 * @author Diego
 */
@Path("/userinfo")
public class UserInfo {
    
    /**
     * Web service for retrieving user information from UdeA Portal's databases.
     *
     * @param username The username.
     * @param password The user password hash.
     * @return String The JSON data string with the user information.
     */
    @GET
    @Path("{username}/{password}")
    @Produces("application/json" + ";charset=utf-8")
    public String getUserInformation(@PathParam("username") String username, @PathParam("password") String password) {
        AuthenticationManager authManager = new AuthenticationManager();
        User user = authManager.getUserInformation(username, password);       // Uses the REST Web service.
        JSONObject JSONResponse = new JSONObject();
        try {
            if (user != null) {
                Map <String, String> userInfoMap = new HashMap<String, String>();
                userInfoMap.put("idnumber", user.getIdnumber());
                userInfoMap.put("username", user.getUsername());
                userInfoMap.put("firstname", user.getFirstname());
                userInfoMap.put("lastname", user.getLastname());
                userInfoMap.put("email", user.getEmail());
                userInfoMap.put("phone1", user.getPhone1());
                userInfoMap.put("phone2", user.getPhone2());
                userInfoMap.put("city", user.getCity());
                userInfoMap.put("country", user.getCountry());
                JSONResponse.put("response", userInfoMap);
            } else {
                JSONResponse.put("response", false);
            }
        } catch (JSONException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return JSONResponse.toString();
    }
}