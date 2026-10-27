package dataaccess;

import java.util.Map;
import java.util.HashMap;

import exception.DataAccessException;
import model.*;


public class AuthDao
{
    private final Map<String, AuthData> authMap;

    public AuthDao()
    {
        this.authMap = new HashMap<>();
    }

    public AuthData getAuth(String authToken)
    {
        if(authMap.isEmpty())return null;
        return authMap.get(authToken);
    }

    public boolean verifyAuth(String authToken) throws DataAccessException
    {
        return authMap.containsKey(authToken);
    }

    public void createAuth(AuthData auth) throws DataAccessException
    {
        authMap.put(auth.authToken(),auth);
    }

    public void deleteAuth(String authToken) throws DataAccessException
    {
        authMap.remove(authToken);
    }

    public void clearAuths() throws DataAccessException
    {
        authMap.clear();
    }
}
