package co.edu.udea.udearroba.dto;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * The Data Transfer Object for user's entities.
 * 
 * @author Diego Rendón
 */
public class User {

    private String idNumber;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone1;
    private String phone2;
    private String city;
    private String country;

    public String getJSONString() {
        JSONObject JSONResponse = new JSONObject();
        try {
            JSONResponse.put("idnumber", this.getIdNumber());
            JSONResponse.put("username", this.getUserName());
            JSONResponse.put("firstname", this.getFirstName());
            JSONResponse.put("lastname", this.getLastName());
            JSONResponse.put("email", this.getEmail());
            JSONResponse.put("phone1", this.getPhone1());
            JSONResponse.put("phone2", this.getPhone2());
            JSONResponse.put("city", this.getCity());
            JSONResponse.put("country", this.getCountry());
        } catch (JSONException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return JSONResponse.toString();
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
