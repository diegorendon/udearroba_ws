package co.edu.udea.udearroba.dao;

import co.edu.udea.udearroba.dto.AuthenticationInformation;

/**
 * Interface that establishes the methods needed in a DAO for user's entities.
 *
 * @author Diego
 */
public interface UserDAOInterface {

    /**
     * Inserts a record with the user's authentication information in SERVA.
     *
     * @param authenticationInfo An AuthenticationInformation instance that
     * contains the username, password and identification to be insert in a new
     * record.
     *
     * @return true or false indicating if the insertion operation was
     * successful or had failed respectively.
     * @throws co.edu.udea.udearroba.exception.UserDAOException
     * @throws Exception
     */
    public boolean insert(AuthenticationInformation authenticationInfo) throws Exception;

    /**
     * Updates a record with the user's authentication information in SERVA.
     *
     * @param authenticationInfo An AuthenticationInformation instance that
     * contains the username, password and identification to be updated in the
     * existing record.
     *
     * @return true or false indicating if the update operation was successful
     * or had failed respectively.
     * @throws co.edu.udea.udearroba.exception.UserDAOException
     * @throws Exception
     */
    public boolean update(AuthenticationInformation authenticationInfo) throws Exception;

    /**
     * Retrieves the authentication information asociated with the given
     * identification.
     *
     * @param identification The user identification.
     *
     * @return AuthenticationInformation object that contains the authentication
     * information asociated with the given identification.
     * @throws co.edu.udea.udearroba.exception.UserDAOException
     * @throws Exception
     */
    public AuthenticationInformation getAuthenticationInfoFromSERVA(String identification) throws Exception;

    /**
     * Retrieves the authentication information asociated with the given
     * username and password.
     *
     * @param username The username.
     * @param password The password hash.
     *
     * @return AuthenticationInformation object that contains the authentication
     * information asociated with the given username and password.
     * @throws co.edu.udea.udearroba.exception.UserDAOException
     * @throws Exception
     */
    public AuthenticationInformation getAuthenticationInfoFromSERVA(String username, String password) throws Exception;
}
