package service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.*;
import exception.*;
import dataaccess.*;

class ClearServiceTest
{
    private ClearService clearService;
    private GameService gameService;
    private UserService userService;
    private GameDao gameDao;

    @BeforeEach
    void setUp()
    {
        gameDao = new MemoryGameDao();
        AuthDao authDao = new MemoryAuthDao();
        UserDao userDao = new MemoryUserDao();

        clearService = new ClearService(userDao, authDao, gameDao);
        gameService = new GameService(authDao, gameDao);
        userService = new UserService(userDao, authDao);

    }


    @Test
    void clearDatabase() throws BadRequestException, DataAccessException, AlreadyTakenException, UnauthorizedException
    {
        AuthData authData = userService.registerUser(new UserData("test", "test", "test"));
        gameService.createGame(authData.authToken(), "TestGame");

        clearService.clearDatabase();

        assertTrue(gameDao.listGames().isEmpty());
        UnauthorizedException ex = assertThrows(UnauthorizedException.class,
                () -> userService.loginUser(new UserData("test", "test", null)));
        assertEquals("Username or Password is incorrect", ex.getMessage());
    }
}