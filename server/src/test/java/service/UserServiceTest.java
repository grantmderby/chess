package service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.*;
import exception.*;
import dataaccess.*;

class UserServiceTest
{
    private ClearService clearService;
    private UserService userService;


    @BeforeEach
    void setUp()
    {
        GameDao gameDao = new MemoryGameDao();
        AuthDao authDao = new MemoryAuthDao();
        UserDao userDao = new MemoryUserDao();

        clearService = new ClearService(userDao, authDao, gameDao);
        userService = new UserService(userDao, authDao);
    }

    @AfterEach
    void breakDown() throws DataAccessException
    {
        clearService.clearDatabase();
    }

    @Test
    void registerUser() throws BadRequestException, AlreadyTakenException, DataAccessException
    {
        assertDoesNotThrow(() -> userService.registerUser(new UserData("test", "test", "test")));

        UnauthorizedException ex = assertThrows(UnauthorizedException.class,
                () -> userService.logoutUser("BadAuthToken"));
        assertEquals("Unauthorized to perform this action", ex.getMessage());

        var authData = userService.registerUser(new UserData("test2", "test", "test"));

        assertDoesNotThrow(() -> userService.logoutUser(authData.authToken()));
    }


    @Test
    void loginUser() throws BadRequestException, AlreadyTakenException, DataAccessException
    {
        UnauthorizedException ex = assertThrows(UnauthorizedException.class,
                () -> userService.loginUser(new UserData("test", "test", null)));
        assertEquals("Username or Password is incorrect", ex.getMessage());

        userService.registerUser(new UserData("test", "test", "test"));

        assertDoesNotThrow(() -> userService.loginUser(new UserData("test", "test", null)));
    }

    @Test
    void logoutUser() throws BadRequestException, AlreadyTakenException, DataAccessException
    {
        var authData = userService.registerUser(new UserData("test", "test", "test"));
        assertDoesNotThrow(() -> userService.logoutUser(authData.authToken()));

        UnauthorizedException ex = assertThrows(UnauthorizedException.class,
                () -> userService.logoutUser(authData.authToken()));
        assertEquals("Unauthorized to perform this action", ex.getMessage());

    }

    @Test
    void clearUserData() throws BadRequestException, AlreadyTakenException, DataAccessException
    {
        userService.registerUser(new UserData("test", "test", "test"));

        clearService.clearDatabase();

        UnauthorizedException ex = assertThrows(UnauthorizedException.class,
                () -> userService.loginUser(new UserData("test", "test", null)));
        assertEquals("Username or Password is incorrect", ex.getMessage());
    }
}