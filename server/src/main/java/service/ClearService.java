package service;

import dataaccess.*;
import exception.DataAccessException;

public class ClearService
{
    UserDao userDao;
    AuthDao authDao;
    GameDao gameDao;

    public ClearService(UserDao userDao, AuthDao authDao, GameDao gameDao)
    {
        this.userDao=userDao;
        this.authDao=authDao;
        this.gameDao=gameDao;
    }

    public void clearDatabase() throws DataAccessException
    {
        userDao.clearUsers();
        authDao.clearAuths();
        gameDao.clearGames();
    }
}
