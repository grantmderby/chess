package dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.BadRequestException;
import exception.DataAccessException;
import model.*;

public class MySQLGameDao implements GameDao
{

    private int nextGameID;

    public MySQLGameDao()
    {
        try
        {
            configureDatabase();
        }catch (DataAccessException ex)
        {
            throw new RuntimeException(String.format("Error: UserTable failed to be added, cannot continue. Error: %s",ex.getMessage()));
        }
        this.nextGameID=1;
    }

    public int createGame(String gameName) throws DataAccessException
    {
        var statement = "INSERT INTO GameData (gameID, whiteUsername, blackUsername, gameName, gameData) VALUES (?,?,?,?,?)";
        var game = new GameData(nextGameID, null, null, gameName, new ChessGame());
        createAndUpdateGameHelper(statement, game);
        return nextGameID++;
    }

    public void updateGame(GameData game) throws DataAccessException
    {
        var statement = "INSERT INTO GameData (gameID, whiteUsername, blackUsername, gameName, gameData) VALUES (?,?,?,?,?)";
        createAndUpdateGameHelper(statement, game);
    }

    public GameData getGame(int gameID) throws DataAccessException, BadRequestException
    {
        var statement = "SELECT * FROM gameData WHERE gameID = ?";
        ArrayList<GameData> game = getGameAndListHelper(statement,gameID);
        if(!game.isEmpty())
        {
            return game.getFirst();
        }
        else
        {
            return null;
        }
    }

    public List<GameData> listGames() throws DataAccessException
    {
        var statement = "SELECT * FROM gameData";
        return getGameAndListHelper(statement,-1);
    }

    public void clearGames() throws DataAccessException
    {
        String statement = "TRUNCATE TABLE GameData";
        clearGamesHelper(statement);
    }

    private ArrayList<GameData> getGameAndListHelper(String sqlStatement, int gameID) throws DataAccessException
    {
        var games = new ArrayList<GameData>();
        try(Connection databaseConnection = DatabaseManager.getConnection();
            PreparedStatement preparedStatement = databaseConnection.prepareStatement(sqlStatement))
        {
            if(gameID>0)
            {
                preparedStatement.setInt(1,gameID);
            }
            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                while(resultSet.next())
                {
                    int gameIDFromDB = resultSet.getInt(1);
                    String wUserFromDB = resultSet.getString(2);
                    String bUserFromDB = resultSet.getString(3);
                    String gameName= resultSet.getString(4);
                    ChessGame chessGame = new Gson().fromJson(resultSet.getString(5),ChessGame.class);

                    games.add(new GameData(gameIDFromDB,wUserFromDB,bUserFromDB,gameName,chessGame));
                }
            }
        } catch(SQLException sqlError)
        {
            String errorMessage = String.format("Unable to update database: %s, %s",sqlStatement,sqlError.getMessage());
            throw new DataAccessException(errorMessage);
        }

        return games;
    }

    private void createAndUpdateGameHelper(String sqlStatement, GameData game) throws DataAccessException
    {
        try(Connection databaseConnection = DatabaseManager.getConnection())
        {
            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(sqlStatement))
            {
                preparedStatement.setInt(1,game.gameId());
                preparedStatement.setString(2,game.whiteUsername());
                preparedStatement.setString(3,game.blackUsername());
                preparedStatement.setString(4,game.gameName());
                preparedStatement.setString(5,new Gson().toJson(game.game()));
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException sqlError)
        {
            String errorMessage = String.format("Unable to update database: %s, %s",sqlStatement,sqlError.getMessage());
            throw new DataAccessException(errorMessage);
        }
    }

    private void clearGamesHelper(String sqlStatement) throws DataAccessException
    {
        try(Connection databaseConnection = DatabaseManager.getConnection())
        {
            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(sqlStatement))
            {
                preparedStatement.executeUpdate();
            }
        } catch(SQLException | DataAccessException sqlError)
        {
            String errorMessage = String.format("Unable to update database: %s, %s",sqlStatement,sqlError.getMessage());
            throw new DataAccessException(errorMessage);
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS GameData (
                 gameID INT PRIMARY KEY,
                 whiteUsername VARCHAR(100) DEFAULT NULL,
                 blackUsername VARCHAR(100) DEFAULT NULL,
                 gameName VARCHAR(100) NOT NULL,
                 gameData JSON NOT NULL
             );
            """
    };

    private void configureDatabase() throws DataAccessException
    {
        try (Connection conn = DatabaseManager.getConnection())
        {
            for (String statement : createStatements)
            {
                try (var preparedStatement = conn.prepareStatement(statement))
                {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException sqlError)
        {
            throw new DataAccessException(String.format("Unable to configure database: %s", sqlError.getMessage()));
        }
    }
}
