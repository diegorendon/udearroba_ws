package co.edu.udea.udearroba.dao.impl;

import co.edu.udea.udearroba.connection.ConnectionManager;
import co.edu.udea.udearroba.dao.UserDAOInterface;
import co.edu.udea.udearroba.dto.AuthenticationInformation;
import co.edu.udea.udearroba.exception.UserDAOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * The Data Access Object for user's entities.
 *
 * @author Diego
 */
public class UserDAO implements UserDAOInterface {

    private final String SERVA_QUERY_INSERT = "insert";                                                 // Name of the string of SQL insert query for SERVA
    private final String SERVA_QUERY_UPDATE = "update";                                                 // Name of the string of SQL update query for SERVA
    private final String SERVA_QUERY_AUTH_INFO_BY_IDENTIFICATION = "getAuthInfoByIdentification";       // Name of the string of SQL query by identification for SERVA
    private final String SERVA_QUERY_AUTH_INFO_BY_USERNAME_PASSWORD = "getAuthInfoByUsernamePassword";  // Name of the string of SQL query by username and password for SERVA
    private final String RESOURCE_BUNDLE_PATH = "co.edu.udea.udearroba.properties.UserDAOQueries";
    protected ResourceBundle resource;

    /**
     * Dafault UserDAO constructor without parameters.
     *
     * @throws UserDAOException
     */
    public UserDAO() throws UserDAOException {
        try {
            resource = ResourceBundle.getBundle(RESOURCE_BUNDLE_PATH);
        } catch (MissingResourceException exception) {
            throw new UserDAOException(UserDAOException.MISSING_RESOURCE_BUNDLE);
        }
    }

    /**
     * @see
     * UserDAOInterface#insert(co.edu.udea.udearroba.dto.AuthenticationInformation)
     *
     * @param authenticationInfo An AuthenticationInformation object that
     * contains the username, password and identification to be insert in a new
     * record.
     *
     * @return true or false indicating if the insertion operation was
     * successful or had failed respectively.
     * @throws co.edu.udea.udearroba.exception.UserDAOException
     * @throws Exception
     */
    public boolean insert(AuthenticationInformation authenticationInfo) throws UserDAOException, Exception {
        Connection connection = ConnectionManager.getConnection(ConnectionManager.SERVA);
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(resource.getString(SERVA_QUERY_INSERT));
            preparedStatement.setString(1, authenticationInfo.getUsername());
            preparedStatement.setString(2, authenticationInfo.getPassword());
            preparedStatement.setString(3, authenticationInfo.getIdentification());
            return (preparedStatement.executeUpdate() > 0);
        } catch (SQLException e) {
            throw new UserDAOException(UserDAOException.SQL_ERROR + ":" + e.getMessage());
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
     * @see
     * UserDAOInterface#update(co.edu.udea.udearroba.dto.AuthenticationInformation)
     *
     * @param authenticationInfo An AuthenticationInformation object that
     * contains the username, password and identification to be updated in the
     * existing record.
     *
     * @return true or false indicating if the update operation was successful
     * or had failed respectively.
     * @throws co.edu.udea.udearroba.exception.UserDAOException
     * @throws Exception
     */
    public boolean update(AuthenticationInformation authenticationInfo) throws UserDAOException, Exception {
        Connection connection = ConnectionManager.getConnection(ConnectionManager.SERVA);
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(resource.getString(SERVA_QUERY_UPDATE));
            preparedStatement.setString(1, authenticationInfo.getPassword());
            preparedStatement.setString(2, authenticationInfo.getIdentification());
            preparedStatement.setString(3, authenticationInfo.getUsername());
            return (preparedStatement.executeUpdate() > 0);
        } catch (SQLException exception) {
            throw new UserDAOException(UserDAOException.SQL_ERROR + ":" + exception.getMessage());
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
     * @see UserDAOInterface#getAuthenticationInfoFromSERVA(java.lang.String)
     *
     * @param identification The user identification.
     *
     * @return AuthenticationInformation object that contains the authentication
     * information asociated with the given identification.
     * @throws co.edu.udea.udearroba.exception.UserDAOException
     * @throws Exception
     */
    public AuthenticationInformation getAuthenticationInfoFromSERVA(String identification) throws Exception {
        Connection connection = ConnectionManager.getConnection(ConnectionManager.SERVA);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(resource.getString(SERVA_QUERY_AUTH_INFO_BY_IDENTIFICATION));
            preparedStatement.setString(1, identification);
            resultSet = preparedStatement.executeQuery();
            AuthenticationInformation authInfo = new AuthenticationInformation();
            if (resultSet.next()) {
                authInfo.setUsername(resultSet.getString(1));
                authInfo.setPassword(resultSet.getString(2));
                authInfo.setIdentification(resultSet.getString(3));
            } else {
                authInfo = null;
            }
            return authInfo;
        } catch (SQLException exception) {
            throw new UserDAOException(UserDAOException.SQL_ERROR + ":" + exception.getMessage());
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
     * @see UserDAOInterface#getAuthenticationInfoFromSERVA(java.lang.String,
     * java.lang.String)
     *
     * @param username The username.
     * @param password The password hash.
     *
     * @return AuthenticationInformation object that contains the authentication
     * information asociated with the given username and password.
     * @throws co.edu.udea.udearroba.exception.UserDAOException
     * @throws Exception
     */
    public AuthenticationInformation getAuthenticationInfoFromSERVA(String username, String password) throws Exception {
        Connection connection = ConnectionManager.getConnection(ConnectionManager.SERVA);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(resource.getString(SERVA_QUERY_AUTH_INFO_BY_USERNAME_PASSWORD));
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            AuthenticationInformation authInfo = new AuthenticationInformation();
            if (resultSet.next()) {
                authInfo.setUsername(resultSet.getString(1));
                authInfo.setPassword(resultSet.getString(2));
                authInfo.setIdentification(resultSet.getString(3));
            } else {
                authInfo = null;
            }
            return authInfo;
        } catch (SQLException exception) {
            throw new UserDAOException(UserDAOException.SQL_ERROR + ":" + exception.getMessage());
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
