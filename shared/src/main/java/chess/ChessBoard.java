package chess;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard
{
    ChessPiece[][] squares = new ChessPiece[8][8];

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode()
    {
        return Arrays.deepHashCode(squares);
    }

    public ChessBoard()
    {}

    public ChessBoard(ChessBoard other)
    {
        for(int row=0;row<8;row++)
            for(int col=0;col<8;col++)
            {
                var oldPiece=other.getPiece(new ChessPosition(row+1,col+1));
                if(oldPiece!=null)squares[row][col]=new ChessPiece(oldPiece.getTeamColor(),oldPiece.getPieceType());
            }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece)
    {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position)
    {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    public void removePiece(ChessPosition position)
    {
        squares[position.getRow()-1][position.getColumn()-1]=null;
    }

    public ChessPosition getKing(ChessGame.TeamColor TeamColor) throws InvalidMoveException
    {
        for (int row=0;row<8;row++)
            for(int col=0;col<8;col++)
            {
                var piece=squares[row][col];
                if(piece!=null && piece.getPieceType() == ChessPiece.PieceType.KING&&piece.getTeamColor() == TeamColor)
                    return new ChessPosition(row+1,col+1);
            }
        throw new InvalidMoveException("No King Found");
    }

    public ChessPiece[][] getEnemies(ChessGame.TeamColor TeamColor)
    {
        if(TeamColor == ChessGame.TeamColor.WHITE)TeamColor = ChessGame.TeamColor.BLACK;
        else TeamColor = ChessGame.TeamColor.WHITE;

        ChessPiece[][] enemies = new ChessPiece[8][8];
        for (int row=0;row<8;row++)
            for(int col=0;col<8;col++)
            {
                var piece=squares[row][col];
                if(piece!=null && piece.getTeamColor() == TeamColor)enemies[row][col]=piece;
            }
        return enemies;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard()
    {
        squares = new ChessPiece[8][8];
        addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        addPiece(new ChessPosition(8, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));

        for(int i = 1;i<=8;i++)
        {
            addPiece(new ChessPosition(2, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }


    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < 8; i++)
        {
            for (int k = 0; k < 8; k++)
            {
                result.append("[");
                if (squares[i][k] != null)
                {
                    if (squares[i][k].getTeamColor() == ChessGame.TeamColor.WHITE)
                    {
                        switch (squares[i][k].getPieceType())
                        {
                            case ChessPiece.PieceType.KING -> result.append("k");
                            case ChessPiece.PieceType.QUEEN -> result.append("q");
                            case ChessPiece.PieceType.KNIGHT -> result.append("n");
                            case ChessPiece.PieceType.ROOK -> result.append("r");
                            case ChessPiece.PieceType.BISHOP -> result.append("b");
                            case ChessPiece.PieceType.PAWN -> result.append("p");
                        }
                    } else
                    {
                        switch (squares[i][k].getPieceType())
                        {
                            case ChessPiece.PieceType.KING -> result.append("K");
                            case ChessPiece.PieceType.QUEEN -> result.append("Q");
                            case ChessPiece.PieceType.KNIGHT -> result.append("N");
                            case ChessPiece.PieceType.ROOK -> result.append("R");
                            case ChessPiece.PieceType.BISHOP -> result.append("B");
                            case ChessPiece.PieceType.PAWN -> result.append("P");
                        }
                    }

                } else
                {
                    result.append(" ");
                }

                result.append("]");
            }
            result.append("\n");
        }

        return "" + result;
    }
}
