package dataaccess;

import chess.ChessGame;
import dataaccess.sql.DatabaseManager;
import dataaccess.sql.MySQLGameDao;
import exception.BadRequestException;
import exception.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLGameDaoTest
{
    GameDao SQLGameDao;

    @BeforeEach
    void setUp()
    {
        try
        {
            DatabaseManager.createDatabase();
            this.SQLGameDao = new MySQLGameDao();
        }catch (DataAccessException e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    @AfterEach
    void tearDown() throws DataAccessException
    {
        SQLGameDao.clearGames();
    }

    @Test
    void createGame()
    {
        assertDoesNotThrow(()->SQLGameDao.createGame("test"));
    }

    @Test
    void getGame() throws DataAccessException, BadRequestException
    {
        int gameIdTest = 1;
        String gameName = "Game 1";
        ChessGame chessGame = new ChessGame();

        GameData data = new GameData (gameIdTest,null,null,gameName,chessGame);

        int gameID = SQLGameDao.createGame(gameName);
        assertDoesNotThrow(()->SQLGameDao.getGame(gameID));
        GameData game = SQLGameDao.getGame(gameID);
        assertNotNull(game);
        assert(Objects.equals(game,data));
        assertEquals(gameName, game.gameName());
        System.out.printf("Game %s was successfully added and returned from db",gameName);
    }

    @Test
    void updateGame() throws DataAccessException, BadRequestException
    {
        String gameName = "Test";
        String blackUsername = "bob";
        var gameID = SQLGameDao.createGame(gameName);

        GameData data = new GameData(gameID,null,blackUsername,gameName,new ChessGame());
        assertDoesNotThrow(()->SQLGameDao.updateGame(data));
        assertEquals(blackUsername, SQLGameDao.getGame(gameID).blackUsername());
    }

    @Test
    void listGames() throws DataAccessException
    {
        SQLGameDao.createGame("Test1");
        SQLGameDao.createGame("Test2");
        SQLGameDao.createGame("Test3");


        var gameList = SQLGameDao.listGames();
        assertNotNull(gameList);
        assertEquals("Test1", gameList.getFirst().gameName());
        assertEquals("Test2", gameList.get(1).gameName());
        assertEquals("Test3", gameList.getLast().gameName());
    }




    @Test
    void clearGame()
    {
        assertDoesNotThrow(()-> SQLGameDao.clearGames());
    }
}
