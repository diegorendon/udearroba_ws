package co.edu.udea.udearroba.dto;

/**
 * The Data Transfer Object for the courses entities.
 *
 * @author Diego Rendón
 */
public class Course {

    private String id;
    private String key;
    private String metacourseId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMetacourseId() {
        return metacourseId;
    }

    public void setMetacourseId(String metacourseId) {
        this.metacourseId = metacourseId;
    }

}
