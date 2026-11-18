package client;

import exception.DataAccessException;
import exception.ResponseException;
import model.AuthData;
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
        createUser();
        String username = "user";
        String password = "pass";
        String email = "email";
        assertThrows(ResponseException.class,() -> serverFacade.registerUser(username,password,email));
    }

    @Test
    public void loginUserNoUsernameTest() throws ResponseException
    {
        createUser();
        String username = "user";
        String password = "pass";
        String email = "email";
        assertThrows(ResponseException.class,()->serverFacade.loginUser(null,password));
    }

    @Test
    public void logoutUserWithAuthToken() throws ResponseException
    {
        var response = createUser();
        assertDoesNotThrow(()-> serverFacade.logoutUser(response.authToken()));
    }

    @Test
    public void logoutUserNullAuthToken()
    {
        assertThrows(ResponseException.class,()-> serverFacade.logoutUser(null));
    }

    @Test
    public void createGameWithAuthToken() throws ResponseException
    {
        var response = createUser();
        String gameName = "game 1";
        assertDoesNotThrow(()->serverFacade.createGame(response.authToken(),gameName));
    }

    @Test
    public void createGameNullAuthToken()
    {
        String gameName = "game 1";
        assertThrows(ResponseException.class,()->serverFacade.createGame(null,gameName));
    }

    @Test
    public void createGameNullGameName() throws ResponseException
    {
        var response = createUser();
        assertThrows(ResponseException.class,()->serverFacade.createGame(response.authToken(),null));
    }

    @Test
    public void listGamesTestWithAuthToken() throws ResponseException
    {
        var response = createUser();
        String gameName = "game 1";
        serverFacade.createGame(response.authToken(),gameName);
        assert(serverFacade.listGames(response.authToken())!=null);
    }

    @Test
    public void listGamesTestNullAuthToken() throws ResponseException
    {
        var response = createUser();
        String gameName = "game 1";
        serverFacade.createGame(response.authToken(),gameName);
        assertThrows(ResponseException.class,()->serverFacade.listGames(null));
    }



    private static AuthData createUser() throws ResponseException
    {
        String username = "user";
        String password = "pass";
        String email = "email";
        return serverFacade.registerUser(username, password, email);
    }

}
