package dataaccess;

import exception.BadRequestException;
import exception.DataAccessException;
import model.GameData;

import java.util.List;

public interface GameDao
{
    int createGame(String gameName) throws DataAccessException;

    void updateGame(GameData game) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException, BadRequestException;

    List<GameData> listGames() throws DataAccessException;

    void clearGames() throws DataAccessException;
}
