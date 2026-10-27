package dataaccess;
import chess.ChessGame;
import exception.DataAccessException;
import model.*;

import java.util.*;

public class GameDao
{
    private Map<Integer, GameData> games;
    private int nextGameID;

    public GameDao()
    {
        this.games = new LinkedHashMap<>();
        this.nextGameID=1;
    }


    public int createGame(String gameName) throws DataAccessException
    {
        var game = new GameData(nextGameID++,"","",gameName, new ChessGame());
        games.put(game.gameId(),game);
        return game.gameId();
    }

    public GameData getGame(int gameID) throws DataAccessException
    {
        if(games.isEmpty())return null;
        return games.get(gameID);
    }

    public List<GameData> listGames() throws DataAccessException
    {
        return new ArrayList<>(games.values());
    }

    public void clearGames() throws DataAccessException
    {
        this.nextGameID = 1;
        games.clear();
    }
}
