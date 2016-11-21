package co.edu.udea.udearroba.dao;

import co.edu.udea.udearroba.dto.Course;
import co.edu.udea.udearroba.dto.Metacourse;

/**
 * Interface that establishes the methods needed in a DAO for courses entities.
 *
 * @author Diego Rendón
 */
public interface CourseDAOInterface {

    /**
     * Retrieves the course information asociated with the given key.
     *
     * @param key The key that identifies the course.
     *
     * @return Course object that contains the course information asociated with
     * the given key.
     * @throws co.edu.udea.udearroba.exception.CourseDAOException
     * @throws Exception
     */
    public Course getCourseFromSERVA(String key) throws Exception;

    /**
     * Retrieves the metacourse information asociated with the given key.
     *
     * @param courseKey The key of the course for search the correspondent metacourse.
     *
     * @return Metacourse object that contains the metacourse information
     * asociated with the given course key or NULL if the course does not belong
     * to a metacourse.
     * @throws co.edu.udea.udearroba.exception.CourseDAOException
     * @throws Exception
     */
    public Metacourse getMetacourseFromSERVA(String courseKey) throws Exception;

}
