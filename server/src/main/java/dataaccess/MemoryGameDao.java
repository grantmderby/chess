package dataaccess;

import java.util.*;

import chess.ChessGame;
import exception.*;
import model.*;

public class MemoryGameDao implements GameDao
{
    private Map<Integer, GameData> games;
    private int nextGameID;

    public MemoryGameDao()
    {
        this.games = new LinkedHashMap<>();
        this.nextGameID=1;
    }


    @Override
    public int createGame(String gameName) throws DataAccessException
    {
        var game = new GameData(nextGameID++,null,null,gameName, new ChessGame());
        games.put(game.gameId(),game);
        return game.gameId();
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException
    {
        games.put(game.gameId(),game);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException, BadRequestException
    {
        if(games.isEmpty())
        {
            throw new BadRequestException("Game does not exist");
        }

        var game = games.get(gameID);
        if(game==null)
        {
            throw new BadRequestException("Game does not exist");
        }
        return games.get(gameID);
    }

    @Override
    public List<GameData> listGames() throws DataAccessException
    {
        return new ArrayList<>(games.values());
    }

    @Override
    public void clearGames() throws DataAccessException
    {
        this.nextGameID = 1;
        games.clear();
    }
}
