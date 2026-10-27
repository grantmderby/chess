package server;

import com.google.gson.Gson;
import java.util.Map;
import io.javalin.*;
import io.javalin.http.Context;

import exception.*;
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


        javalin.exception(BadRequestException.class,(e,ctx) -> exceptionHandler(e,400,ctx));
        javalin.exception(UnauthorizedException.class,(e,ctx) -> exceptionHandler(e,401,ctx));
        javalin.exception(AlreadyTakenException.class,(e,ctx) -> exceptionHandler(e,403,ctx));
        javalin.exception(DataAccessException.class,(e,ctx) -> exceptionHandler(e,500,ctx));
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

    private void clearDatabase(Context ctx) throws DataAccessException
    {
        clearService.clearUserData();
        System.out.println("Ran Delete");
        ctx.status(200);
    }

    private void exceptionHandler(Exception ex,int errorCode, Context ctx)
    {
        ctx.status(errorCode);
        var message = Map.of("message","Error: " + ex.getMessage());
        ctx.result(new Gson().toJson(message));
    }
}
