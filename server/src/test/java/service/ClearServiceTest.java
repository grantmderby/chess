package service;

import org.junit.jupiter.api.Assertions;
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
    private AuthData authData;
    private GameDao gameDao;

    @BeforeEach
    void setUp() throws UnauthorizedException, BadRequestException, DataAccessException, AlreadyTakenException
    {
        gameDao = new GameDao();
        AuthDao authDao = new AuthDao();
        UserDao userDao = new UserDao();

        clearService = new ClearService(userDao, authDao, gameDao);
        gameService = new GameService(authDao,gameDao);
        userService = new UserService(userDao, authDao);


        authData= userService.registerUser(new UserData("bob","password","email"));
        gameService.createGame(authData.authToken(),"TestGame");
    }


    @Test
    void clearDatabase() throws BadRequestException, DataAccessException, AlreadyTakenException, UnauthorizedException
    {
        Assertions.assertFalse(gameService.listGames(authData.authToken()).isEmpty());
        Assertions.assertNotNull(userService.loginUser(new UserData("bob","password",null)));

        clearService.clearDatabase();

        Assertions.assertTrue(gameDao.listGames().isEmpty());
        UnauthorizedException ex = Assertions.assertThrows(UnauthorizedException.class,
                () -> userService.loginUser(new UserData("bob","password",null)));
        Assertions.assertEquals("Username or Password is incorrect",ex.getMessage());
    }
}