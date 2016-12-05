package co.edu.udea.udearroba.ws.resources;

import co.edu.udea.udearroba.bl.services.EnrolmentManager;
import co.edu.udea.udearroba.dto.AcademicInformation;
import co.edu.udea.udearroba.i18n.Texts;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.LoggerFactory;

/**
 * REST Web Service for user's academic information.
 *
 * @author Diego Rendón
 */
@Path("/useracademicinformation")
public class UserAcademicInformation {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserAcademicInformation.class);

    /**
     * Web service for retrieving user's academic information from UdeA Portal's
     * databases given the identification.
     *
     * @param identification The user identification.
     *
     * @return String The JSON data string with the user's academic information.
     */
    @GET
    @Path("{identification}")
    @Produces("application/json" + ";charset=utf-8")
    public String getAcademicInformation(@PathParam("identification") String identification) {
        EnrolmentManager enrolManager = new EnrolmentManager();
        List<AcademicInformation> academicInformationList = enrolManager.getAcademicInformation(identification);
        JSONObject JSONResponse = new JSONObject();
        try {
            JSONArray JSONSemesters = new JSONArray();
            if (academicInformationList != null) {
                for (int i = 0; i < academicInformationList.size(); i++) {
                    Map<String, String> semesterInfoMap = new HashMap<String, String>();
                    semesterInfoMap.put("semestre", academicInformationList.get(i).getSemestre());
                    semesterInfoMap.put("programa", academicInformationList.get(i).getPrograma());
                    semesterInfoMap.put("inicioSemestre", academicInformationList.get(i).getInicioSemestre());
                    semesterInfoMap.put("finSemestre", academicInformationList.get(i).getFinSemestre());
                    semesterInfoMap.put("promedioSemestre", academicInformationList.get(i).getPromedioSemestre());
                    semesterInfoMap.put("promedioPrograma", academicInformationList.get(i).getPromedioPrograma());
                    semesterInfoMap.put("promedioUniversidad", academicInformationList.get(i).getPromedioUniversidad());
                    semesterInfoMap.put("tercioProgramaNivel", academicInformationList.get(i).getTercioProgramaNivel());
                    semesterInfoMap.put("creditosAcumulados", academicInformationList.get(i).getCreditosAcumulados());
                    semesterInfoMap.put("creditosfaltantes", academicInformationList.get(i).getCreditosfaltantes());
                    semesterInfoMap.put("numeroAprobadas", academicInformationList.get(i).getNumeroAprobadas());
                    semesterInfoMap.put("numeroPerdidas", academicInformationList.get(i).getNumeroPerdidas());
                    semesterInfoMap.put("numeroCanceladas", academicInformationList.get(i).getNumeroCanceladas());
                    semesterInfoMap.put("tipoHomologacion", academicInformationList.get(i).getTipoHomologacion());
                    semesterInfoMap.put("nombrePrograma", academicInformationList.get(i).getNombrePrograma());
                    JSONSemesters.put(semesterInfoMap);
                }
            }
            JSONResponse.put("response", JSONSemesters);
        } catch (JSONException ex) {
            logger.error("{} - {}", Texts.getText("JSONResponseUserInfoLogMessage"), ex.getMessage());
        }
        return JSONResponse.toString();
    }
}
