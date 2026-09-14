package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece
{

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(pieceColor, type);
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type)
    {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType
    {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor()
    {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType()
    {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        ChessPiece piece = board.getPiece(myPosition);

        ChessPosition attemptedPosition;

        if (piece.getPieceType() == PieceType.BISHOP)
        {
            List<ChessMove> possibleMoves = new ArrayList<>();

            for (int row = myPosition.getRow() + 1, col = myPosition.getColumn() + 1; row <= 8 && col <= 8; row++, col++)
                if (checkForPiece(board, myPosition, piece, possibleMoves, row, col))break;

            for (int row = myPosition.getRow() - 1, col = myPosition.getColumn() - 1; row >= 1 && col >= 1; row--, col--)
                if (checkForPiece(board, myPosition, piece, possibleMoves, row, col))break;

            for (int row = myPosition.getRow() - 1, col = myPosition.getColumn() + 1; row >= 1 && col <= 8; row--, col++)
                if (checkForPiece(board, myPosition, piece, possibleMoves, row, col))break;

            for (int row = myPosition.getRow() + 1, col = myPosition.getColumn() - 1; row <= 8 && col >= 1; row++, col--)
                if (checkForPiece(board, myPosition, piece, possibleMoves, row, col))break;

            return possibleMoves;
        }

        if(piece.getPieceType()==PieceType.ROOK)
        {
            List<ChessMove> possibleMoves = new ArrayList<>();

            for (int row = myPosition.getRow() + 1, col = myPosition.getColumn(); row <= 8; row++)
                if (checkForPiece(board, myPosition, piece, possibleMoves, row, col))break;

            for (int row = myPosition.getRow() - 1, col = myPosition.getColumn(); row >= 1; row--)
                if (checkForPiece(board, myPosition, piece, possibleMoves, row, col))break;

            for (int row = myPosition.getRow(), col = myPosition.getColumn() + 1; col <= 8; col++)
                if (checkForPiece(board, myPosition, piece, possibleMoves, row, col))break;

            for (int row = myPosition.getRow(), col = myPosition.getColumn() - 1; col >= 1; col--)
                if (checkForPiece(board, myPosition, piece, possibleMoves, row, col))break;

            return possibleMoves;
        }




        return List.of(new ChessMove(myPosition, new ChessPosition(8, 8), null));
    }

    private boolean checkForPiece(ChessBoard board, ChessPosition myPosition, ChessPiece piece, List<ChessMove> possibleMoves, int row, int col)
    {
        ChessPosition attemptedPosition;
        attemptedPosition = new ChessPosition(row, col);
        if(board.getPiece(attemptedPosition)!=null)
        {
            if (board.getPiece(attemptedPosition).pieceColor != piece.pieceColor)
                possibleMoves.add(new ChessMove(myPosition, attemptedPosition, null));

            return true;
        }
        possibleMoves.add(new ChessMove(myPosition, attemptedPosition, null));
        return false;
    }
}
