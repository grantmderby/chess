package server;

import com.google.gson.Gson;
import java.util.Map;
import io.javalin.*;
import io.javalin.http.Context;

import exception.*;
import service.*;
import dataaccess.*;
import model.*;


public class Server
{

    private final Javalin javalin;
    ClearService clearService;
    UserService userService;
    //AuthService authService;
    //GameService gameService;


    public Server()
    {

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        UserDao userDao = new UserDao();
        AuthDao authDao = new AuthDao();
        GameDao gameDao = new GameDao();


        this.userService = new UserService(userDao, authDao);
        //this.authService = new AuthService(userDao, authDao);
        //this.gameService = new GameService(authDao, gameDao);
        this.clearService = new ClearService(userDao, authDao, gameDao);


        javalin.post("/user",this::registerHandler);
        javalin.post("/session",this::loginHandler);
        javalin.delete("/db",this::clearDatabase);
        javalin.delete("/session",this::logoutHandler);


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

    private void registerHandler(Context ctx) throws AlreadyTakenException
    {
        UserData newUser= new Gson().fromJson(ctx.body(),UserData.class);
        var message = userService.registerUser(newUser);
        ctx.result(new Gson().toJson(message));
        ctx.status(200);
    }

    private void loginHandler(Context ctx) throws UnauthorizedException
    {
        UserData newUser= new Gson().fromJson(ctx.body(),UserData.class);
        var message = userService.loginUser(newUser);
        ctx.result(new Gson().toJson(message));
        ctx.status(200);
    }

    private void logoutHandler(Context ctx) throws UnauthorizedException
    {
        String authToken = ctx.header("authorization");
        userService.logoutUser(authToken);
        ctx.status(200);
        ctx.result();
    }

    private void clearDatabase(Context ctx) throws DataAccessException
    {
        clearService.clearDatabase();
        ctx.status(200);
        ctx.result();
    }

    private void exceptionHandler(Exception ex,int errorCode, Context ctx)
    {
        ctx.status(errorCode);
        var message = Map.of("message","Error: " + ex.getMessage());
        ctx.result(new Gson().toJson(message));
    }
}
