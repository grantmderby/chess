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
                case "register" -> "You logged in as: " + registerUser(params);
                case "signin" -> "sign in";
                case "creategame" -> "create game";
                case "joingame" -> "join game";
                case "observegame" -> "observe game";
                case "logout" -> "logout";
                case "quit" -> "quit";
                default -> help();
            };
        }catch (ResponseException ex)
        {
            return ex.getMessage();
        }
    }

    private void printPrompt() {
        System.out.print("\n" + ERASE_SCREEN + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    public String help()
    {
        if (state == State.SIGNEDOUT)
        {
            return """
                    - register <username> <password> <email> (Create a chess account)
                    - signIn <username> <password>
                    - quit (closes the application)
                    """;
        }
        return """
                - listGames (List the games available to play or observe)
                - createGame <gameName> (What would you like to name the new game?)
                - joinGame <playerColor (white or black)> <gameName> (Choose a game to play in)
                - observeGame <gameName> (See the current board state of a specific game)
                - logout
                - quit (closes the application)
                """;
    }

    private String registerUser(String... params) throws ResponseException
    {
        if(params.length>=3)
        {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            var response = server.registerUser(username, password, email);
            state = State.SIGNEDIN;
            token = response.authToken();
            return response.username();
        }
        throw new ResponseException(400,"Error: requires <username> <password> <email>");
    }
}
