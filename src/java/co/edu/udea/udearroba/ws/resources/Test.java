/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.udea.udearroba.ws.resources;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Diego
 */
@Path("/test")
public class Test {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Test
     */
    public Test() {
    }

    /**
     * Retrieves representation of an instance of co.edu.udea.udearroba.ws.resources.Test
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        JSONObject user = new JSONObject();
        try {
            user.put("identification", "1018130535");
            user.put("first_name", "Juan");
            user.put("last_name", "Pérez");
            user.put("email", "estudiante@udea.edu.co");
            user.put("role", 5);
            user.put("status", "MATRIC");
        } catch (JSONException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user.toString();
    }
}
