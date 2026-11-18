package client;

import exception.ResponseException;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient
{
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverURL)
    {
        server = new ServerFacade(serverURL);
    }

    public void run()
    {
        System.out.println(WHITE_KING + "Welcome to Chess. Sign in or create an account to start.");
        System.out.print(help());


        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit"))
        {
            printPrompt();
            String line = scanner.next();

            try
            {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
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
        //try
        //{
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens,1,tokens.length);
            return switch(cmd)
            {
                case "register" -> "register";
                case "signin" -> "sign in";
                case "creategame" -> "create game";
                case "joingame" -> "join game";
                case "observegame" -> "observe game";
                case "logout" -> "logout";
                case "quit" -> "quit";
                default -> help();
            };
        //}catch (ResponseException ex)
        //{
            //return ex.getMessage();
        //}
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
}
