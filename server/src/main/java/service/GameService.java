package service;
import dataaccess.*;
import exception.UnauthorizedException;
import model.*;
import exception.DataAccessException;

import java.util.ArrayList;
import java.util.List;



public class GameService
{
    public record GameInfo(int gameID, String whiteUsername, String blackUsername, String gameName){}

    GameDao gameDao;
    AuthDao authDao;

    public GameService(AuthDao authDao, GameDao gameDao)
    {

        this.gameDao=gameDao;
        this.authDao=authDao;
    }

    public List<GameInfo> listGames(String authToken) throws UnauthorizedException, DataAccessException
    {
        if(!authDao.verifyAuth(authToken))
        {
            throw new UnauthorizedException("Unauthorized to perform this action");
        }


        List<GameInfo> gameInfo = new ArrayList<>();
        var gameList = gameDao.listGames();

        for(GameData g : gameList)
        {
            gameInfo.add(new GameInfo(g.gameId(),g.whiteUsername(),g.blackUsername(),g.gameName()));
        }

        return gameInfo;
    }

    public int createGame(String authToken, String gameName) throws UnauthorizedException, DataAccessException
    {
        if(!authDao.verifyAuth(authToken))
        {
            throw new UnauthorizedException("Unauthorized to perform this action");
        }

        return gameDao.createGame(gameName);
    }

    public void clearGameData() throws DataAccessException
    {
        gameDao.clearGames();
    }
}
