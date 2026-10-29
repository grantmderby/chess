package dataaccess;

import exception.DataAccessException;
import model.AuthData;

public interface AuthDao
{
    AuthData getAuth(String authToken) throws DataAccessException;

    boolean verifyAuth(String authToken) throws DataAccessException;

    void createAuth(AuthData auth) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;

    void clearAuths() throws DataAccessException;
}
