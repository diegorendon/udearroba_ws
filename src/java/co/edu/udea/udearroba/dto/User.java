package co.edu.udea.udearroba.dto;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Diego
 */
public class User {

    private String idnumber;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phone1;
    private String phone2;
    private String city;
    private String country;
//    private String status;

    public String getJSONString() {
        JSONObject JSONResponse = new JSONObject();
        try {
            JSONResponse.put("idnumber", this.getIdnumber());
            JSONResponse.put("username", this.getUsername());
            JSONResponse.put("firstname", this.getFirstname());
            JSONResponse.put("lastname", this.getLastname());
            JSONResponse.put("email", this.getEmail());
            JSONResponse.put("phone1", this.getPhone1());
            JSONResponse.put("phone2", this.getPhone2());
            JSONResponse.put("city", this.getCity());
            JSONResponse.put("country", this.getCountry());
//                  JSONResponse.put("user", this);
        } catch (JSONException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return JSONResponse.toString();
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
}
