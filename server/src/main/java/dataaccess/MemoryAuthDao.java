package dataaccess;

import java.util.Map;
import java.util.HashMap;

import exception.DataAccessException;
import model.*;


public class MemoryAuthDao implements AuthDao
{
    private final Map<String, AuthData> authMap;

    public MemoryAuthDao()
    {
        this.authMap = new HashMap<>();
    }

    @Override
    public AuthData getAuth(String authToken)
    {
        if (authMap.isEmpty())
        {
            return null;
        }
        return authMap.get(authToken);
    }

    @Override
    public boolean verifyAuth(String authToken) throws DataAccessException
    {
        return !authMap.containsKey(authToken);
    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException
    {
        authMap.put(auth.authToken(), auth);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException
    {
        authMap.remove(authToken);
    }

    @Override
    public void clearAuths() throws DataAccessException
    {
        authMap.clear();
    }
}
