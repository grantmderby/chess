package dataaccess;

import java.util.HashMap;
import java.util.Map;

import exception.DataAccessException;
import model.*;

public class MemoryUserDao implements UserDao
{
    private Map<String, UserData> chessUsers;


    public MemoryUserDao()
    {
        this.chessUsers = new HashMap<>();
    }

    @Override
    public void createUser(UserData user) throws DataAccessException
    {
        chessUsers.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException
    {
        if (chessUsers.isEmpty())
        {
            return null;
        }
        return chessUsers.get(username);
    }

    @Override
    public void clearUsers() throws DataAccessException
    {
        chessUsers.clear();
    }

}
