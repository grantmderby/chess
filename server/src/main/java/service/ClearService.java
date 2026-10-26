package service;
import dataaccess.*;

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

    public void clearUserData()
    {
        userDao.clearUsers();
        authDao.clearAuths();
        gameDao.clearGames();
    }
}
