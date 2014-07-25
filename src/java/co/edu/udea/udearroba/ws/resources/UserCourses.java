/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.udea.udearroba.ws.resources;

import co.edu.udea.udearroba.bl.services.EnrolmentManager;
import co.edu.udea.udearroba.dto.MARESCourse;
import co.edu.udea.udearroba.dto.Metacourse;
import co.edu.udea.udearroba.i18n.Texts;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service for user's courses information.
 *
 * @author Diego Rendón
 */
@Path("/courses")
public class UserCourses {
    
    /**
     * Web service for retrieving user's courses list from UdeA Portal's databases
     * given the identification.
     *
     * @param identification The user identification.
     *
     * @return String The JSON data string with the user's courses list.
     */
    @GET
    @Path("{identification}")
    @Produces("application/json" + ";charset=utf-8")
    public String getCourses(@PathParam("identification") String identification) {
        EnrolmentManager enrolManager = new EnrolmentManager();
        List<MARESCourse> courseList = enrolManager.getCourses(identification);      
        JSONObject JSONResponse = new JSONObject();
        try {
            JSONArray JSONCourses = new JSONArray();
            if (courseList != null) {
                for (int i = 0; i < courseList.size(); i++) {
                    Map<String, String> courseInfoMap = new HashMap<String, String>();
                    courseInfoMap.put("semestre", courseList.get(i).getSemestre());
                    courseInfoMap.put("materia", courseList.get(i).getMateria());
                    courseInfoMap.put("codigo", courseList.get(i).getCodigo());
                    courseInfoMap.put("nombre", courseList.get(i).getNombre());
                    courseInfoMap.put("grupo", courseList.get(i).getGrupo());
                    courseInfoMap.put("creditos", courseList.get(i).getCreditos());
                    courseInfoMap.put("tipoHomologacion", courseList.get(i).getTipoHomologacion());
                    courseInfoMap.put("nota", courseList.get(i).getNota());
                    // Add the Moodle idNumber of the correspondent course or metacourse.
                    String code = courseList.get(i).getCodigo();
                    String courseKey = code.substring(0, 3)+"-"+code.substring(3, 6)+"-"+courseList.get(i).getGrupo();
                    Metacourse metacurso = enrolManager.getMetacourse(courseKey);
                    if(metacurso != null){
                        courseInfoMap.put("codigoMoodle", metacurso.getKey());
                    }else{
                        courseInfoMap.put("codigoMoodle", courseKey);
                    }
                    JSONCourses.put(courseInfoMap);
                }
            } 
            JSONResponse.put("response", JSONCourses);
        } catch (JSONException ex) {
            Logger.getLogger(UserInformation.class.getName()).log(Level.SEVERE, Texts.getText("JSONResponseUserInfoLogMessage"), ex);
        }
        return JSONResponse.toString();
    }
}
