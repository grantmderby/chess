package service;

import java.util.Objects;
import java.util.UUID;

import org.eclipse.jetty.server.Authentication;
import org.mindrot.jbcrypt.BCrypt;

import dataaccess.*;
import model.*;
import exception.*;

public class UserService
{
    UserDao userDao;
    AuthDao authDao;

    public UserService(UserDao userDao, AuthDao authDao)
    {
        this.userDao = userDao;
        this.authDao = authDao;
    }

    public AuthData registerUser(UserData user) throws AlreadyTakenException, DataAccessException, BadRequestException
    {
        if(user.username()==null||user.password()==null||user.email()==null)
        {
            throw new BadRequestException("The username, password, and/or email was not included");
        }
        if(userDao.getUser(user.username())!=null)
        {
            throw new AlreadyTakenException("This username already in use");
        }
        else
        {
            String hashedPassword = BCrypt.hashpw(user.password(),BCrypt.gensalt());
            user = new UserData(user.username(),hashedPassword,user.email());
            userDao.createUser(user);
            return createAuth(user.username());
        }
    }

    public AuthData loginUser(UserData user) throws UnauthorizedException, DataAccessException, BadRequestException
    {
        if(user.username()==null||user.password()==null)
        {
            throw new BadRequestException("The username and/or password was not included");
        }
        if(userDao.getUser(user.username())==null
                || !BCrypt.checkpw(user.password(),userDao.getUser(user.username()).password()))
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
        verifyAuth(authToken);
        authDao.deleteAuth(authToken);
    }

    private AuthData createAuth(String username) throws DataAccessException
    {
        String token = UUID.randomUUID().toString();
        var authData = new AuthData(username,token);
        authDao.createAuth(authData);
        return authData;
    }

    private void verifyAuth(String authToken) throws DataAccessException, UnauthorizedException
    {
        if (authDao.getAuth(authToken)==null)
        {
            throw new UnauthorizedException("Unauthorized to perform this action");
        }
    }

}
