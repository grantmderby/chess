package dataaccess;
import exception.DataAccessException;
import model.*;
import java.util.Map;
import java.util.HashMap;


public class AuthDao
{
    private Map<String, AuthData> authMap;

    public AuthDao()
    {
        this.authMap = new HashMap<>();
    }

    public AuthData getAuth(String authToken)
    {
        if(authMap.isEmpty())return null;
        return authMap.get(authToken);
    }

    public void createAuth(AuthData auth)
    {
        authMap.put(auth.authToken(),auth);
    }

    public void deleteAuth(AuthData auth)
    {
        authMap.remove(auth.authToken());
    }

    public void deleteAuth(String authToken)
    {
        authMap.remove(authToken);
    }

    public void clearAuths() throws DataAccessException
    {
        authMap.clear();
    }
}
