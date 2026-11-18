package dataaccess.sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.GameDao;
import exception.DataAccessException;
import model.*;

public class MySQLGameDao implements GameDao
{

    AdditionalFunctions addFunc = new AdditionalFunctions();

    public MySQLGameDao()
    {
        try
        {

            String[] createStatements = {
                    """
            CREATE TABLE IF NOT EXISTS GameData (
                 gameID INT NOT NULL AUTO_INCREMENT,
                 PRIMARY KEY (gameID),
                 whiteUsername VARCHAR(100) DEFAULT NULL,
                 blackUsername VARCHAR(100) DEFAULT NULL,
                 gameName VARCHAR(100) NOT NULL,
                 gameData JSON NOT NULL
             );
            """
            };

            addFunc.configureDatabase(createStatements);
        } catch (DataAccessException ex)
        {
            throw new RuntimeException(String.format("Error: GameDataTable failed to be added, cannot continue. Error: %s",
                    ex.getMessage()));
        }
    }

    public int createGame(String gameName) throws DataAccessException
    {
        var statement = "INSERT INTO GameData (whiteUsername, blackUsername, gameName, gameData) VALUES (?,?,?,?)";
        var game = new GameData(-1, null, null, gameName, new ChessGame());
        var response = createGameHelper(statement, game);
        if (response == -1)
        {
            throw new DataAccessException("Create Game Failed Unexpectedly");
        }
        return response;
    }

    public void updateGame(GameData game) throws DataAccessException
    {
        var statement = "UPDATE GameData SET whiteUsername = ?, blackUsername = ?, gameData = ? WHERE gameID = ?";
        updateGameHelper(statement, game);
    }

    public GameData getGame(int gameID) throws DataAccessException
    {
        var statement = "SELECT * FROM gameData WHERE gameID = ?";
        ArrayList<GameData> game = getGameAndListHelper(statement, gameID);
        if (!game.isEmpty())
        {
            return game.getFirst();
        } else
        {
            return null;
        }
    }

    public List<GameData> listGames() throws DataAccessException
    {
        var statement = "SELECT * FROM gameData";
        return getGameAndListHelper(statement, -1);
    }

    public void clearGames() throws DataAccessException
    {
        String statement = "TRUNCATE TABLE GameData";
        addFunc.clearHelper(statement);
    }

    private int createGameHelper(String sqlStatement, GameData game) throws DataAccessException
    {
        try (Connection databaseConnection = DatabaseManager.getConnection())
        {
            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS))
            {
                preparedStatement.setString(1, game.whiteUsername());
                preparedStatement.setString(2, game.blackUsername());
                preparedStatement.setString(3, game.gameName());
                preparedStatement.setString(4, new Gson().toJson(game.game()));
                preparedStatement.executeUpdate();
                try (var keys = preparedStatement.getGeneratedKeys())
                {
                    if (keys.next())
                    {
                        return keys.getInt(1);
                    }
                }
            }
        } catch (SQLException sqlError)
        {
            String errorMessage = String.format("Unable to update database: %s, %s",
                    sqlStatement,
                    sqlError.getMessage());
            throw new DataAccessException(errorMessage);
        }
        return -1;
    }

    private void updateGameHelper(String sqlStatement, GameData game) throws DataAccessException
    {
        try (Connection databaseConnection = DatabaseManager.getConnection())
        {
            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(sqlStatement))
            {
                preparedStatement.setString(1, game.whiteUsername());
                preparedStatement.setString(2, game.blackUsername());
                preparedStatement.setString(3, new Gson().toJson(game.game()));
                preparedStatement.setInt(4, game.gameId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException sqlError)
        {
            String errorMessage = String.format("Unable to update database: %s, %s",
                    sqlStatement,
                    sqlError.getMessage());
            throw new DataAccessException(errorMessage);
        }
    }

    private ArrayList<GameData> getGameAndListHelper(String sqlStatement, int gameID) throws DataAccessException
    {
        var games = new ArrayList<GameData>();
        try (Connection databaseConnection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = databaseConnection.prepareStatement(sqlStatement))
        {
            if (gameID > 0)
            {
                preparedStatement.setInt(1, gameID);
            }
            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                while (resultSet.next())
                {
                    int gameIDFromDB = resultSet.getInt(1);
                    String wUserFromDB = resultSet.getString(2);
                    String bUserFromDB = resultSet.getString(3);
                    String gameName = resultSet.getString(4);
                    ChessGame chessGame = new Gson().fromJson(resultSet.getString(5), ChessGame.class);

                    games.add(new GameData(gameIDFromDB, wUserFromDB, bUserFromDB, gameName, chessGame));
                }
            }
        } catch (SQLException sqlError)
        {
            String errorMessage = String.format("Unable to update database: %s, %s",
                    sqlStatement,
                    sqlError.getMessage());
            throw new DataAccessException(errorMessage);
        }

        return games;
    }

}
