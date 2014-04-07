package co.edu.udea.udearroba.dto;

/**
 * The Data Transfer Object for the authentication information entities.
 * 
 * @author Diego
 */
public class AuthenticationInformation {
    private String username;
    private String password;
    private String identification;

    public AuthenticationInformation() {
    }
    
    public AuthenticationInformation(String userName, String password, String idNumber) {
        this.username = userName;
        this.password = password;
        this.identification = idNumber;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
}
