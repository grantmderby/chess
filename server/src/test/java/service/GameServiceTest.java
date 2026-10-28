package service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.*;
import exception.*;
import dataaccess.*;
import response.GameInfo;


class GameServiceTest
{
    private ClearService clearService;
    private GameService gameService;
    private UserService userService;
    private AuthData authData;
    private GameDao gameDao;


    @BeforeEach
    void setUp()
    {
        gameDao = new GameDao();
        UserDao userDao = new UserDao();
        AuthDao authDao = new AuthDao();

        clearService = new ClearService(userDao, authDao, gameDao);
        userService = new UserService(userDao, authDao);
        gameService = new GameService(authDao, gameDao);
    }

    @AfterEach
    void breakDown() throws DataAccessException
    {
        clearService.clearDatabase();
    }

    @Test
    void listSingeGame() throws BadRequestException, AlreadyTakenException, DataAccessException, UnauthorizedException
    {
        authData = userService.registerUser(new UserData("test", "test", "test"));
        gameService.createGame(authData.authToken(), "TestGame");

        Assertions.assertFalse(gameService.listGames(authData.authToken()).isEmpty());
    }

    @Test
    void listMultipleGames() throws BadRequestException, AlreadyTakenException, DataAccessException, UnauthorizedException
    {
        authData = userService.registerUser(new UserData("test", "test", "test"));
        gameService.createGame(authData.authToken(), "TestGame");
        gameService.createGame(authData.authToken(), "TestGame1");
        gameService.createGame(authData.authToken(), "TestGame2");
        gameService.createGame(authData.authToken(), "TestGame3");

        assertEquals(4, gameService.listGames(authData.authToken()).size());
    }

    @Test
    void createGame() throws BadRequestException, AlreadyTakenException, DataAccessException, UnauthorizedException
    {
        var gameID = createGameHelper();
        var game = gameDao.getGame(gameID);
        var gameInfo = new GameInfo(game.gameId(),game.whiteUsername(),game.blackUsername(),game.gameName());

        assertTrue(gameService.listGames(authData.authToken()).contains(gameInfo));
    }

    @Test
    void joinGame() throws UnauthorizedException, BadRequestException, AlreadyTakenException, DataAccessException
    {
        createGameHelper();
        authData = userService.loginUser(new UserData("test", "test", "test"));
        int gameID = gameService.createGame(authData.authToken(), "TestGame");
        var game = gameDao.getGame(gameID);
        var gameInfo = new GameInfo(game.gameId(),game.whiteUsername(),game.blackUsername(),game.gameName());

        assertTrue(gameService.listGames(authData.authToken()).contains(gameInfo));

        gameService.joinGame(authData.authToken(),"WHITE",gameID);

        gameInfo = new GameInfo(game.gameId(),"test",game.blackUsername(),game.gameName());

        assertTrue(gameService.listGames(authData.authToken()).contains(gameInfo));

        gameService.joinGame(authData.authToken(),"BLACK",gameID);

        gameInfo = new GameInfo(game.gameId(),"test","test",game.gameName());

        assertTrue(gameService.listGames(authData.authToken()).contains(gameInfo));

    }

    @Test
    void clearGameData() throws UnauthorizedException, BadRequestException, AlreadyTakenException, DataAccessException
    {
        createGame();
        gameService.clearGameData();
        assertTrue(gameDao.listGames().isEmpty());
    }

    int createGameHelper() throws BadRequestException, AlreadyTakenException, DataAccessException, UnauthorizedException
    {
        authData = userService.registerUser(new UserData("test", "test", "test"));
        return gameService.createGame(authData.authToken(), "TestGame");
    }
}