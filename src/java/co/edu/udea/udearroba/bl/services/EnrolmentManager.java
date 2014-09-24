package co.edu.udea.udearroba.bl.services;

import co.edu.udea.exception.OrgSistemasSecurityException;
import co.edu.udea.udearroba.dao.impl.CourseDAO;
import co.edu.udea.udearroba.dto.AcademicInformation;
import co.edu.udea.udearroba.dto.MARESCourse;
import co.edu.udea.udearroba.dto.Metacourse;
import co.edu.udea.udearroba.exception.CourseDAOException;
import co.edu.udea.udearroba.i18n.Texts;
import co.edu.udea.wsClient.OrgSistemasWebServiceClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manage the enrolment process of the UdeA Portal's users.
 *
 * @author Diego Rendón
 */
public class EnrolmentManager {

    private boolean useTestingData;                                             // Switches between testing and production modes for the Web Services' calls. Set to true for development and false for production.
    private String TOKEN;                                                       // Token assigned to Ude@ in order to be able to use the REST Web services.
    private String PUBLIC_KEY;                                                  // Public key used by Ude@ in order to be able to use the REST Web services.
    private String SECRET_KEY;                                                  // The 128-bits secret key used by Ude@ to encrypt/decrypt the data.
    private String INITIALIZATION_VECTOR;                                       // The 16 bytes initialization vector used by Ude@ to encrypt/decrypt the data.
    private final String MARES_ACADEMIC_INFO_REST_CALL = "consultainformacionacademicamares"; // The Web service to call to get the academic information of an user in MARES.
    private final String MARES_ACADEMIC_INFO_PARAM1 = "cedula";                 // The Web service's param name used to get the user academic information.
    private final String MARES_COURSES_REST_CALL = "consultamateriasestudiantemares"; // The Web service to call to get the courses list of an user in MARES.
    private final String MARES_COURSES_PARAM1 = "cedula";                       // The Web service's first param name used to get the user's courses.
    private final String MARES_COURSES_PARAM2 = "programa";                     // The Web service's second param name used to get the user's courses.
    private final String MARES_COURSES_PARAM3 = "semestre";                     // The Web service's third param name used to get the user's courses.
    private final String RESOURCE_BUNDLE_PATH = "co.edu.udea.udearroba.properties.Application"; // Resource bundle with the application properties
    private final String LOG_IDENTIFICATION_TEXT = " | identification: ";       // The text to indicate the identification in the log messages.
    private final String LOG_USERNAME_TEXT = " | username: ";                   // The text to indicate the username in the log messages.
    private ResourceBundle resource;
    private CourseDAO courseDAO;
    private Validator validator;

    /**
     * Default constructor without parameters.
     */
    public EnrolmentManager() {
        try {
            this.resource = ResourceBundle.getBundle(RESOURCE_BUNDLE_PATH, new Locale("es"));
            this.useTestingData = Boolean.parseBoolean(resource.getString("useTestingData"));
            this.TOKEN = resource.getString("token");
            this.PUBLIC_KEY = resource.getString("publicKey");
            this.SECRET_KEY = resource.getString("secretKey");
            this.INITIALIZATION_VECTOR = resource.getString("initializationVector");
            courseDAO = new CourseDAO();
            validator = new Validator();
        } catch (CourseDAOException ex) {
            Logger.getLogger(EnrolmentManager.class.getName()).log(Level.SEVERE, Texts.getText("courseDAOLogMessage"), ex);
        }catch (MissingResourceException ex) {
            Logger.getLogger(EnrolmentManager.class.getName()).log(Level.SEVERE, Texts.getText("applicationPropertiesLogMessage"), ex);
        }
    }

    /**
     * Returns the user's academic information from UdeA Portal's databases.
     *
     * @param identification The user identification.
     *
     * @return AcademicInformation An AcademicInformation DTO with the user's
     * academic information or NULL.
     */
    public List<AcademicInformation> getAcademicInformation(String identification) {
        OrgSistemasWebServiceClient RESTWebServiceClient = null;
        try {
            RESTWebServiceClient = new OrgSistemasWebServiceClient(useTestingData);
        } catch (OrgSistemasSecurityException ex) {
            Logger.getLogger(EnrolmentManager.class.getName()).log(Level.SEVERE, Texts.getText("RESTWebServiceClientLogMessage") + LOG_IDENTIFICATION_TEXT + identification, ex);
        }
        boolean isValidIdentification;
        isValidIdentification = this.validator.validateIdentification(identification);
        List<AcademicInformation> academicInformationList = null;
        if (isValidIdentification && RESTWebServiceClient != null) {
            RESTWebServiceClient.addParam(MARES_ACADEMIC_INFO_PARAM1, identification.trim());
            academicInformationList = new ArrayList<AcademicInformation>();
            try {
                academicInformationList = RESTWebServiceClient.obtenerBean(MARES_ACADEMIC_INFO_REST_CALL, TOKEN, AcademicInformation.class);
            } catch (OrgSistemasSecurityException ex) {
                Logger.getLogger(EnrolmentManager.class.getName()).log(Level.SEVERE, Texts.getText("academicinformationRESTCallLogMessage") + LOG_IDENTIFICATION_TEXT + identification, ex);
            }        
        }
        return academicInformationList;
    }

    /**
     * Returns the user's courses list from UdeA Portal's databases.
     *
     * @param identification The user identification.
     *
     * @return List<MARESCourse> A list of MARESCourse DTOs with the user's information or NULL.
     */
    public List<MARESCourse> getCourses(String identification) {
        OrgSistemasWebServiceClient RESTWebServiceClient = null;
        try {
            RESTWebServiceClient = new OrgSistemasWebServiceClient(useTestingData);
        } catch (OrgSistemasSecurityException ex) {
            Logger.getLogger(EnrolmentManager.class.getName()).log(Level.SEVERE, Texts.getText("RESTWebServiceClientLogMessage") + LOG_IDENTIFICATION_TEXT + identification, ex);
        }
        List<AcademicInformation> academicInformationList = this.getAcademicInformation(identification);
        boolean isValidIdentification;
        isValidIdentification = this.validator.validateIdentification(identification);
        AcademicInformation currentSemesterInformation = null;
        int lastSemesterIndex = 0; // The academic information of the last semester is retrieved at index 0 of the list. // TODO: validate if this is always the case
        if (!academicInformationList.isEmpty() && academicInformationList.get(lastSemesterIndex) != null){
            currentSemesterInformation = academicInformationList.get(lastSemesterIndex);
        }
        List<MARESCourse> courseList = null;
        if (isValidIdentification && RESTWebServiceClient != null && currentSemesterInformation != null) {
            RESTWebServiceClient.addParam(MARES_COURSES_PARAM1, identification);
            RESTWebServiceClient.addParam(MARES_COURSES_PARAM2, currentSemesterInformation.getPrograma());
            RESTWebServiceClient.addParam(MARES_COURSES_PARAM3, currentSemesterInformation.getSemestre());
            courseList = new ArrayList<MARESCourse>();
            try {
                courseList = RESTWebServiceClient.obtenerBean(MARES_COURSES_REST_CALL, TOKEN, MARESCourse.class);
            } catch (OrgSistemasSecurityException ex) {
                Logger.getLogger(EnrolmentManager.class.getName()).log(Level.SEVERE, Texts.getText("coursesRESTCallLogMessage") + LOG_IDENTIFICATION_TEXT + identification, ex);
            }            
        }
        return courseList;
    }
    
    /**
     * Returns the metacourse associated with a course.
     *
     * @param coursekey The key of the course to check if has associated metacourses.
     *
     * @return Metacourse a metacourse DTO associated with the course 
     * identified by coursekey if exist or NULL.
     */
    public Metacourse getMetacourse(String coursekey) {
        try {
            return this.courseDAO.getMetacourseFromSERVA(coursekey);
        } catch (Exception ex) {
            Logger.getLogger(EnrolmentManager.class.getName()).log(Level.SEVERE, Texts.getText("SERVAMetacourseInfoLogMessage"), ex);
        }
        return null;
    }
}
