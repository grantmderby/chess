package dataaccess;
import exception.DataAccessException;
import model.*;
import java.util.HashMap;
import java.util.Map;

public class UserDao
{
    private Map<String,UserData> chessUsers;


    public UserDao()
    {
        this.chessUsers = new HashMap<>();
    }

    public void createUser(UserData user) throws DataAccessException
    {
        chessUsers.put(user.username(),user);
    }

    public UserData getUser(String username) throws DataAccessException
    {
        if(chessUsers.isEmpty())return null;
        return chessUsers.get(username);
    }

    public void clearUsers() throws DataAccessException
    {
        chessUsers.clear();
    }

}
