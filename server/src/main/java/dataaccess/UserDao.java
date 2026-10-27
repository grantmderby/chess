package dataaccess;

import java.util.HashMap;
import java.util.Map;

import exception.DataAccessException;
import model.*;

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
