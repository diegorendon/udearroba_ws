package co.edu.udea.udearroba.dao.impl;

import co.edu.udea.udearroba.connection.ConnectionManager;
import co.edu.udea.udearroba.dao.CourseDAOInterface;
import co.edu.udea.udearroba.dto.Course;
import co.edu.udea.udearroba.dto.Metacourse;
import co.edu.udea.udearroba.exception.CourseDAOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * The Data Access Object for course's entities.
 *
 * @author Diego Rendón
 */
public class CourseDAO implements CourseDAOInterface {
    private final String SERVA_QUERY_COURSE_BY_KEY = "getCourseByKey";              // Name of the string of SQL query for a course by key from SERVA.
    private final String SERVA_QUERY_METACOURSE_BY_COURSE_KEY = "getMetacourseByCourseKey";// Name of the string of SQL query for a metacourse by course key from SERVA.
    private final String RESOURCE_BUNDLE_PATH = "co.edu.udea.udearroba.properties.CourseDAOQueries";
    private ResourceBundle resource;
    
    /**
     * Dafault CourseDAO constructor without parameters.
     *
     * @throws CourseDAOException
     */
    public CourseDAO() throws CourseDAOException {
        try {
            this.resource = ResourceBundle.getBundle(RESOURCE_BUNDLE_PATH);
        } catch (MissingResourceException exception) {
            throw new CourseDAOException(CourseDAOException.MISSING_RESOURCE_BUNDLE);
        }
    }

    /**
     * @see CourseDAOInterface#getCourseFromSERVA(java.lang.String)
     *
     * @param key The key that identifies the course.
     *
     * @return Course object that contains the course information asociated with
     * the given key.
     * @throws co.edu.udea.udearroba.exception.CourseDAOException
     * @throws Exception
     */
    public Course getCourseFromSERVA(String key) throws Exception {
        Connection connection = ConnectionManager.getConnection(ConnectionManager.SERVA);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(resource.getString(SERVA_QUERY_COURSE_BY_KEY));
            preparedStatement.setString(1, key);
            resultSet = preparedStatement.executeQuery();
            Course course = new Course();
            if (resultSet.next()) {
                course.setId(resultSet.getString(1));
                course.setKey(resultSet.getString(2));
                course.setMetacourseId(resultSet.getString(3));
            } else {
                course = null;
            }
            return course;
        } catch (SQLException exception) {
            throw new CourseDAOException(CourseDAOException.SQL_ERROR + ":" + exception.getMessage());
        } catch (Exception exception) {
            throw exception;
        } finally {
            preparedStatement.close();
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * @see CourseDAOInterface#getMetacourseFromSERVA(java.lang.String) 
     *
     * @param courseKey The key of the course for search the correspondent metacourse.
     *
     * @return Metacourse object that contains the metacourse information
     * asociated with the given course key or NULL if the course does not belong
     * to a metacourse.
     * @throws co.edu.udea.udearroba.exception.CourseDAOException
     * @throws Exception
     */
    public Metacourse getMetacourseFromSERVA(String courseKey) throws Exception {
        Connection connection = ConnectionManager.getConnection(ConnectionManager.SERVA);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(resource.getString(SERVA_QUERY_METACOURSE_BY_COURSE_KEY));
            preparedStatement.setString(1, courseKey);
            resultSet = preparedStatement.executeQuery();
            Metacourse metacourse = new Metacourse();
            if (resultSet.next()) {
                metacourse.setId(resultSet.getString(1));
                metacourse.setKey(resultSet.getString(2));
                metacourse.setName(resultSet.getString(3));
                metacourse.setDescription(resultSet.getString(4));
            } else {
                metacourse = null;
            }
            return metacourse;
        } catch (SQLException exception) {
            throw new CourseDAOException(CourseDAOException.SQL_ERROR + ":" + exception.getMessage());
        } catch (Exception exception) {
            throw exception;
        } finally {
            preparedStatement.close();
            if (connection != null) {
                connection.close();
            }
        }
    }
    
}
