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
        this.teamTurn=team;
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
        var pieceMove = board.getPiece(startPosition).pieceMoves(board,startPosition);
        /*if(!isInCheck(getTeamTurn())) return pieceMove;
        else return pieceMove; //temporary line*/
        return pieceMove;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */

    public void makeMove(ChessMove move) throws InvalidMoveException
    {
        if(!validMoves(move.getStartPosition()).contains(new ChessMove(move.getStartPosition(),move.getEndPosition(),move.getPromotionPiece())))
        {
            throw new InvalidMoveException("Move not valid");
        }
        else
        {
            board.addPiece(move.getEndPosition(),board.getPiece(move.getStartPosition()));
            board.removePiece(move.getStartPosition());
            if(getTeamTurn()==TeamColor.WHITE)setTeamTurn(TeamColor.BLACK);
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
            var king=board.getKing(teamColor);
            var enemyMoves=getAllTeamMoves(teamColor);
            for(ChessMove move : enemyMoves)
                if(move.getEndPosition().getRow()==king.getRow()&&move.getEndPosition().getColumn()==king.getColumn())
                    return true;
        }
        catch(InvalidMoveException _)
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
        if(isInCheck(teamColor))                                                                    //First checks if current team is in check
        {
            ChessGame testBoard = new ChessGame(board);                                             //Creates a deep copy of the current board
            Collection<ChessMove> teamMoves;
            if(teamColor==TeamColor.WHITE) teamMoves = testBoard.getAllTeamMoves(TeamColor.BLACK);  //Gets a copy of all of our team's possible moves
            else teamMoves = testBoard.getAllTeamMoves(TeamColor.WHITE);                            //Does some flip-flop of the colors because of other functions

            for(ChessMove move : teamMoves)                                                          //Runs through all of our team's valid moves
            {
                try
                {
                    var eatenPiece=testBoard.getBoard().getPiece(move.getEndPosition());
                    testBoard.makeMove(move);                                                        //Executes a possible valid move on the test board
                    if(!testBoard.isInCheck(teamColor))                                              //If we are taken out of check all is well, no checkMate
                        return false;
                    else
                    {
                        testBoard.getBoard().addPiece(move.getStartPosition(),testBoard.getBoard().getPiece(move.getEndPosition()));
                        testBoard.getBoard().removePiece(move.getEndPosition());                          //Resets a move if we were still in check
                        testBoard.getBoard().addPiece(move.getEndPosition(),eatenPiece);
                    }
                }catch (InvalidMoveException e)                                                      //If we throw this exception, something is really broken. IDK how to cause this
                {
                    System.out.println("Something went wrong");
                    return false;
                }
            }
            return true;
        }
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
        return false;
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

    private Collection<ChessMove> getAllTeamMoves(TeamColor teamColor)
    {
        Collection<ChessMove> allEnemyMoves = new ArrayList<>();

        var enemyPieces = board.getEnemies(teamColor);

        for(int row=1;row<=8;row++)
            for(int col=1;col<=8;col++)
            {
                if(enemyPieces[row-1][col-1]!=null)allEnemyMoves.addAll(validMoves(new ChessPosition(row,col)));
            }
        return allEnemyMoves;
    }
}
