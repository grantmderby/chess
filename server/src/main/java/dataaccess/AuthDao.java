package dataaccess;
import model.*;
import java.util.ArrayList;
import java.util.Objects;

public class AuthDao
{
    private ArrayList<AuthData> authList;

    public AuthDao()
    {
        this.authList = new ArrayList<>();
    }

    public AuthData getAuth(String authToken)
    {
        if(authList.isEmpty())return null;
        for(AuthData auth : authList)
        {
            if(Objects.equals(authToken, auth.authToken()))
            {
                return auth;
            }
        }
        return null;
    }

    public void createAuth(String authToken, String username) throws DataAccessException //raise exception user with username already exists
    {
        if(getAuth(authToken)==null)
        {
            authList.add(new AuthData(authToken, username));
        }
        else throw new DataAccessException("authToken already taken");
    }
}
