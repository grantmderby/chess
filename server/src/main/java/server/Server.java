package server;

import dataaccess.DataAccessException;
import io.javalin.*;
import io.javalin.http.Context;
import service.*;
import dataaccess.*;

public class Server
{

    private final Javalin javalin;
    ClearService clearService;


    public Server()
    {




        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        UserDao userDao = new UserDao();
        AuthDao authDao = new AuthDao();
        GameDao gameDao = new GameDao();

        this.clearService = new ClearService(userDao, authDao, gameDao);

        //javalin.post("")
        javalin.delete("/db",this::clearDatabase);

    }

    private void clearDatabase(Context ctx) throws DataAccessException
    {
        clearService.clearUserData();
        System.out.println("Ran Delete");
        ctx.result("200");
    }

    public int run(int desiredPort)
    {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop()
    {
        javalin.stop();
    }

}
