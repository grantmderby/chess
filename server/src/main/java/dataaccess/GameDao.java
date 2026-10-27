package dataaccess;
import exception.DataAccessException;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class GameDao
{
    private Map<Integer, GameData> games;

    public GameDao()
    {
        this.games = new HashMap<>();
    }


    public void createGame(GameData game)
    {
        games.put(game.gameId(),game);
    }

    public GameData getGame(int gameID)
    {
        if(games.isEmpty())return null;
        return games.get(gameID);
    }

    public List<GameData> listGames()
    {
        return new ArrayList<>(games.values());
    }

    public void deleteGame(GameData game)
    {
        games.remove(game.gameId());
    }

    public void deleteGame(int gameID)
    {
        games.remove(gameID);
    }

    public void clearGames() throws DataAccessException
    {
        games.clear();
    }
}
