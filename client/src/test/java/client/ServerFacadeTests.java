package client;

import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;


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

    @AfterAll
    static void stopServer()
    {
        server.stop();
    }


    @Test
    public void sampleTest()
    {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerUserTest() throws ResponseException
    {
        String username = "user";
        String password = "pass";
        String email = "email";
        serverFacade.registerUser(username, password, email);
    }



}
