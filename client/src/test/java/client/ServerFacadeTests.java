package client;

import dataaccess.AuthDao;
import exception.DataAccessException;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests
{

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init()
    {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);

    }

    @BeforeEach
    public void clearDB() throws ResponseException
    {
        serverFacade.clearDatabase();
    }

    @AfterAll
    static void stopServer() throws DataAccessException
    {
        server.stop();
    }


    @Test
    public void clearDatabase()
    {
        assertDoesNotThrow(()->serverFacade.clearDatabase());
    }

    @Test
    public void registerUserTestSuccess() throws ResponseException
    {
        String username = "user";
        String password = "pass";
        String email = "email";
        var response = serverFacade.registerUser(username, password, email);
        assertDoesNotThrow(() -> serverFacade.loginUser(username,password));
        assertDoesNotThrow(() -> serverFacade.logoutUser(response.authToken()));
    }

    @Test
    public void registerUserTestDuplicateUser() throws ResponseException
    {
        String username = "user";
        String password = "pass";
        String email = "email";
        serverFacade.registerUser(username, password, email);
        assertThrows(ResponseException.class,() -> serverFacade.registerUser(username,password,email));
    }

}
