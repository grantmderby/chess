package service;
import dataaccess.*;
import exception.AlreadyTakenException;
import exception.UnauthorizedException;
import model.*;
import exception.DataAccessException;

import java.util.Objects;
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

    public AuthData registerUser(UserData user) throws AlreadyTakenException, DataAccessException
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

    public AuthData loginUser(UserData user) throws UnauthorizedException, DataAccessException
    {
        if(userDao.getUser(user.username())==null
                || !Objects.equals(userDao.getUser(user.username()).password(), user.password()))
        {
            throw new UnauthorizedException("Username or Password is incorrect");
        }
        else
        {
            return createAuth(user.username());
        }
    }

    public void logoutUser(String authToken) throws UnauthorizedException, DataAccessException
    {
        if(!authDao.verifyAuth(authToken))
        {
            throw new UnauthorizedException("Unauthorized to perform this action");
        }

        else
        {
            authDao.deleteAuth(authToken);
        }
    }

    private AuthData createAuth(String username) throws DataAccessException
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
