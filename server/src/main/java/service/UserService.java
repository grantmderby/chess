package service;
import dataaccess.*;
import exception.AlreadyTakenException;
import model.*;
import exception.DataAccessException;

import java.util.UUID;

public class UserService
{
    UserDao userDao;
    AuthDao authDao;

    public UserService(UserDao userDao, AuthDao authDao)
    {
        this.userDao=userDao;
        this.authDao=authDao;
    }

    public AuthData registerUser(UserData user) throws AlreadyTakenException
    {
        if(userDao.getUser(user.username())!=null)
        {
            throw new AlreadyTakenException("This username already in use");
        }
        else
        {
            userDao.createUser(user);
            return createAuth(user.username());
        }
    }

    private AuthData createAuth(String username)
    {
        String token = UUID.randomUUID().toString();
        var authData = new AuthData(username,token);
        authDao.createAuth(authData);
        return authData;
    }



    public void clearUserData() throws DataAccessException
    {
        userDao.clearUsers();
    }
}
