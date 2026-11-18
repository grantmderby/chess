package client;

import exception.ResponseException;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient
{
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private String token;
    private String currentUser;

    public ChessClient(String serverURL)
    {
        server = new ServerFacade(serverURL);
    }

    public void run()
    {
        System.out.println(SET_TEXT_COLOR_GREEN + WHITE_KING + "Welcome to Chess. Sign in or create an account to start." );
        System.out.print(help());


        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit"))
        {
            //System.out.print(ERASE_SCREEN);

            //System.out.println(help());
            printPrompt();
            String line = scanner.nextLine();

            try
            {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result + SET_TEXT_COLOR_GREEN);
            } catch (Throwable e)
            {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public String eval(String input)
    {
        try
        {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens,1,tokens.length);
            return switch(cmd)
            {
                case "register" -> registerUser(params);
                case "login" -> loginUser(params);
                case "creategame" -> String.format(ERASE_SCREEN + "%s was created successfully\n" + help(), createGame(params));
                case "joingame" -> "join game";
                case "observegame" -> "observe game";
                case "listgames" -> String.format(ERASE_SCREEN + "%s\n" + help(), listGames());
                case "logout" -> logoutUser();
                case "quit" -> "quit";
                default -> help();
            };
        }catch (ResponseException ex)
        {
            return ex.getMessage();
        }
    }

    private void printPrompt() {
        System.out.print("\n" + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    public String help()
    {
        if (state == State.SIGNEDOUT)
        {
            return """
                    - help
                    - register <username> <password> <email> (Create a chess account)
                    - login <username> <password>
                    - quit (closes the application)
                    """;
        }
        return """
                - help
                - listGames (List the games available to play or observe)
                - createGame <gameName> (What would you like to name the new game?)
                - joinGame <playerColor> (WHITE|BLACK) <gameName> (Choose a game to play in)
                - observeGame <gameName> (See the current board state of a specific game)
                - logout
                - quit (closes the application)
                """;
    }

    private String registerUser(String... params) throws ResponseException
    {
        assertSignedOut();
        if(params.length>=3)
        {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            var response = server.registerUser(username, password, email);
            state = State.SIGNEDIN;
            token = response.authToken();
            currentUser = response.username();
            return String.format(ERASE_SCREEN + "%s logged in\n" + help(), currentUser);
        }
        throw new ResponseException(400,"Error: requires <username> <password> <email>\n" + help());
    }

    private String loginUser(String... params) throws ResponseException
    {
        assertSignedOut();
        if(params.length>=2)
        {
            String username = params[0];
            String password = params[1];
            var response = server.loginUser(username,password);
            state = State.SIGNEDIN;
            token = response.authToken();
            currentUser = response.username();
            return String.format(ERASE_SCREEN + "%s logged in\n" + help(), currentUser);
        }
        throw new ResponseException(400, "Error: requires <username> <password>\n" + help());
    }

    private String logoutUser() throws ResponseException
    {
        assertSignedIn();
        server.logoutUser(token);
        state = State.SIGNEDOUT;
        return String.format(ERASE_SCREEN + "%s logged out\n" + help(), currentUser);
    }

    private String createGame(String... params) throws ResponseException
    {
        assertSignedIn();
        if(params.length>=1)
        {
            String gameName = params[0];
            var response = server.createGame(token, gameName);
            return String.format(ERASE_SCREEN + "Game: %s with ID: %d was created successfully\n" + help(), gameName,response);
        }
        throw new ResponseException(400, "Error: requires <gameName>\n" + help());
    }

    private String listGames() throws ResponseException
    {
        assertSignedIn();
        var response = server.listGames(token);
        System.out.println(response);
        return "bob";
    }

    private void assertSignedIn() throws ResponseException
    {
        if(state==State.SIGNEDOUT)
        {
            throw new ResponseException(400,"You must log in\n" + help());
        }
    }

    private void assertSignedOut() throws ResponseException
    {
        if(state==State.SIGNEDIN)
        {
            throw new ResponseException(400, "You must log out fist in order to register or log in\n" + help());
        }
    }
}
