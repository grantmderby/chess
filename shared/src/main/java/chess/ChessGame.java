package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame
{

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(teamTurn, board);
    }

    public ChessGame()
    {
        board.resetBoard();
    }

    public ChessGame(ChessBoard board)
    {
        this.board = new ChessBoard(board);
    }

    TeamColor teamTurn = TeamColor.WHITE;
    ChessBoard board = new ChessBoard();

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn()
    {
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team)
    {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor
    {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition)
    {
        var possibleMoves = getBoard().getPiece(startPosition).pieceMoves(getBoard(), startPosition);                   //gets all possible moves for this piece
        ChessGame testBoard = new ChessGame(getBoard());                                                                //creates the test board
        Collection<ChessMove> badMoves = new ArrayList<>();

        for (ChessMove move : possibleMoves)                                                                            //iterates through all possible moves
        {
            var eatenPiece = testBoard.getBoard().getPiece(move.getEndPosition());                                      //gets the piece that's eaten, to reset later
            testBoard.getBoard().addPiece(move.getEndPosition(), testBoard.getBoard().getPiece(move.getStartPosition()));
            testBoard.getBoard().removePiece(move.getStartPosition());                                                  //executes the move on the test board

            if (testBoard.isInCheck(testBoard.getBoard().getPiece(move.getEndPosition()).getTeamColor()))               //if the move we try keeps or puts us in check, it is invalid
                badMoves.add(move);

            testBoard.getBoard().addPiece(move.getStartPosition(), testBoard.getBoard().getPiece(move.getEndPosition()));
            testBoard.getBoard().removePiece(move.getEndPosition());
            testBoard.getBoard().addPiece(move.getEndPosition(), eatenPiece);                                           //resets the test board
        }

        possibleMoves.removeAll(badMoves);                                                                              //removes all the bad moves from the list of possible moves
        return possibleMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */

    public void makeMove(ChessMove move) throws InvalidMoveException
    {
        if (getBoard().getPiece(move.getStartPosition()) == null                                                        //Checks the move to be sure it corresponds with a piece,
                || getBoard().getPiece(move.getStartPosition()).getTeamColor() != getTeamTurn()                         //that it's the right color
                || !validMoves(move.getStartPosition()).contains(move))                                                 //that the move is on the list of valid moves
        {
            throw new InvalidMoveException("Move not valid");
        } else
        {
            if (move.getPromotionPiece() != null)                                                                       //Executes the pawn's promotion
                getBoard().addPiece(move.getEndPosition(),
                        new ChessPiece(getBoard().getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece()));
            else
                getBoard().addPiece(move.getEndPosition(), getBoard().getPiece(move.getStartPosition()));               //Moves non-promotion pieces
            getBoard().removePiece(move.getStartPosition());
            if (getTeamTurn() == TeamColor.WHITE) setTeamTurn(TeamColor.BLACK);                                         //Changes the team's turns
            else setTeamTurn(TeamColor.WHITE);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor)
    {
        try
        {
            var king = getBoard().getKing(teamColor);                                                                   //gets the king from the board
            var enemyMoves = getAllTeamMoves(teamColor);                                                                //gets all attacker's moves
            for (ChessMove move : enemyMoves)
                if (move.getEndPosition().getRow() == king.getRow()
                        && move.getEndPosition().getColumn() == king.getColumn())                                       //checks if the king's location is a possible enemy move
                    return true;
        } catch (InvalidMoveException _)
        {
            System.out.println("No king detected??");
            return false;
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor)
    {
        if (isInCheck(teamColor))                                                                    //First checks if current team is in check
            return mateHelper(teamColor);
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor)
    {
        if (isInCheck(teamColor))                                                                    //First checks if we are in check
            return false;

        else
            return mateHelper(teamColor);
    }

    private boolean mateHelper(TeamColor teamColor)
    {
        ChessGame testBoard = new ChessGame(getBoard());                                             //Creates a deep copy of the current board
        Collection<ChessMove> teamMoves;
        if (teamColor == TeamColor.WHITE)
            teamMoves = testBoard.getAllTeamMoves(TeamColor.BLACK);  //Gets a copy of all of our team's possible moves
        else
            teamMoves = testBoard.getAllTeamMoves(TeamColor.WHITE);                            //Does some flip-flop of the colors because of other functions
        if (teamMoves.isEmpty()) return true;

        for (ChessMove move : teamMoves)                                                          //Runs through all of our team's valid moves
        {
            var eatenPiece = testBoard.getBoard().getPiece(move.getEndPosition());
            testBoard.getBoard().addPiece(move.getEndPosition(), testBoard.getBoard().getPiece(move.getStartPosition()));
            testBoard.getBoard().removePiece(move.getStartPosition());                                                       //Executes a possible valid move on the test board
            if (!testBoard.isInCheck(teamColor))                                              //If we are taken out of check all is well, no checkMate
                return false;
            else
            {
                testBoard.getBoard().addPiece(move.getStartPosition(), testBoard.getBoard().getPiece(move.getEndPosition()));
                testBoard.getBoard().removePiece(move.getEndPosition());                          //Resets a move if we were still in check
                testBoard.getBoard().addPiece(move.getEndPosition(), eatenPiece);
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board)
    {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard()
    {
        return this.board;
    }

    private Collection<ChessMove> getAllTeamMoves(TeamColor teamColor)                                                  //Gets all the moves available for a team
    {
        Collection<ChessMove> allTeamMoves = new ArrayList<>();

        var pieces = getBoard().getPieces(teamColor);                                                                   //Gets all the pieces of a specific team from the board

        for (int row = 1; row <= 8; row++)
            for (int col = 1; col <= 8; col++)
            {
                if (pieces[row - 1][col - 1] != null)
                    allTeamMoves.addAll(getBoard().getPiece(new ChessPosition(row, col)).pieceMoves(getBoard(), new ChessPosition(row, col)));
            }
        return allTeamMoves;
    }


}
