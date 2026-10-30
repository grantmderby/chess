package server;

import com.google.gson.Gson;
import java.util.Map;
import io.javalin.*;
import io.javalin.http.Context;

import exception.*;
import service.*;
import dataaccess.*;
import model.*;
import request.*;
import response.*;



public class Server
{

    private final Javalin javalin;
    ClearService clearService;
    UserService userService;
    GameService gameService;
    AuthDao authDao;
    UserDao userDao;
    GameDao gameDao;


    public Server()
    {

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

        createDatabase();

        this.userDao = new MySQLUserDao();
        this.authDao = new MySQLAuthDao();
        this.gameDao = new MySQLGameDao();

        this.userService = new UserService(userDao, authDao);
        this.gameService = new GameService(authDao, gameDao);
        this.clearService = new ClearService(userDao, authDao, gameDao);


        javalin.post("/user",this::registerHandler);
        javalin.post("/session",this::loginHandler);
        javalin.post("/game",this::createGameHandler);
        javalin.get("/game",this::listGamesHandler);
        javalin.put("/game",this::joinGameHandler);
        javalin.delete("/db",this::clearDatabaseHandler);
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

    private void registerHandler(Context ctx) throws AlreadyTakenException, DataAccessException, BadRequestException
    {
        UserData newUser= new Gson().fromJson(ctx.body(),UserData.class);
        var message = userService.registerUser(newUser);
        ctx.result(new Gson().toJson(message));
        ctx.status(200);
    }

    private void loginHandler(Context ctx) throws UnauthorizedException, DataAccessException, BadRequestException
    {
        UserData newUser= new Gson().fromJson(ctx.body(),UserData.class);
        var message = userService.loginUser(newUser);
        ctx.result(new Gson().toJson(message));
        ctx.status(200);
    }

    private void logoutHandler(Context ctx) throws UnauthorizedException, DataAccessException
    {
        String authToken = ctx.header("authorization");
        userService.logoutUser(authToken);
        ctx.status(200);
        ctx.result();
    }

    private void listGamesHandler(Context ctx) throws UnauthorizedException, DataAccessException
    {
        String authToken = ctx.header("authorization");
        var gameInfo = gameService.listGames(authToken);
        ctx.result(new Gson().toJson(new ListResult(gameInfo)));
        ctx.status(200);
    }

    private void createGameHandler(Context ctx) throws UnauthorizedException, DataAccessException, BadRequestException
    {
        String authToken = ctx.header("authorization");
        record GameName(String gameName){}
        String gameName = new Gson().fromJson(ctx.body(),GameName.class).gameName();
        int gameID = gameService.createGame(authToken,gameName);
        var message = Map.of("gameID", gameID);
        ctx.result(new Gson().toJson(message));
        ctx.status(200);
    }

    private void joinGameHandler(Context ctx) throws UnauthorizedException, DataAccessException, BadRequestException, AlreadyTakenException
    {
        String authToken = ctx.header("authorization");
        var joinGame = new Gson().fromJson(ctx.body(),JoinGame.class);
        gameService.joinGame(authToken,joinGame.playerColor(), joinGame.gameID());
        ctx.status(200);
    }

    private void clearDatabaseHandler(Context ctx) throws DataAccessException
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

    private static void createDatabase()
    {
        try
        {
            DatabaseManager.createDatabase();
        }catch (DataAccessException e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }
}
