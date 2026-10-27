package service;
import dataaccess.*;
import exception.BadRequestException;
import exception.UnauthorizedException;
import model.*;
import exception.DataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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

    public void joinGame(String authToken, String playerColor, int gameID) throws DataAccessException, UnauthorizedException, BadRequestException
    {
        if(!authDao.verifyAuth(authToken))
        {
            throw new UnauthorizedException("Unauthorized to perform this action");
        }

        var game = gameDao.getGame(gameID);

        if(Objects.equals(playerColor, "WHITE"))
        {
            if(Objects.equals(game.whiteUsername(),""))
            {
                var newGame = new GameData(game.gameId(),authDao.getAuth(authToken).username(),game.blackUsername(),game.gameName(),game.game());
                gameDao.updateGame(newGame);
            }
            else throw new UnauthorizedException("This player slot is already taken");
        }

        else if(Objects.equals(playerColor, "BLACK"))
        {
            if(Objects.equals(game.blackUsername(),""))
            {
                var newGame = new GameData(game.gameId(),game.whiteUsername(),authDao.getAuth(authToken).username(),game.gameName(),game.game());
                gameDao.updateGame(newGame);
            }
            else throw new UnauthorizedException("This player slot is already taken");
        }
        else throw new BadRequestException("Did not provide a valid player color");

    }

    public void clearGameData() throws DataAccessException
    {
        gameDao.clearGames();
    }
}
