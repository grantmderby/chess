package dataaccess;

import exception.DataAccessException;
import model.UserData;

public interface UserDao
{
    void createUser(UserData user) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void clearUsers() throws DataAccessException;
}
