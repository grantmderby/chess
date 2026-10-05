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
        List<ChessMove> possibleMoves = new ArrayList<>();

        if (piece.getPieceType() == PieceType.BISHOP)
        {
            bishopCheck(board, myPosition, piece, possibleMoves);
            return possibleMoves;
        }

        if (piece.getPieceType() == PieceType.ROOK)
        {
            rookCheck(board, myPosition, piece, possibleMoves);
            return possibleMoves;
        }

        if (piece.getPieceType() == PieceType.QUEEN)
        {
            rookCheck(board, myPosition, piece, possibleMoves);
            bishopCheck(board, myPosition, piece, possibleMoves);
            return possibleMoves;
        }

        if (piece.getPieceType() == PieceType.KING)
        {
            int row = myPosition.getRow(), col = myPosition.getColumn();

            if(row+1<=8&&col+1<=8)checkForPiece(board, myPosition, piece, possibleMoves, row+1, col+1);
            if(row+1<=8)          checkForPiece(board, myPosition, piece, possibleMoves, row+1, col);
            if(row+1<=8&&col-1>=1)checkForPiece(board, myPosition, piece, possibleMoves, row+1, col-1);
            if(col+1<=8)          checkForPiece(board, myPosition, piece, possibleMoves, row, col+1);
            if(col-1>=1)          checkForPiece(board, myPosition, piece, possibleMoves, row, col-1);
            if(row-1>=1&&col+1<=8)checkForPiece(board, myPosition, piece, possibleMoves, row-1, col+1);
            if(row-1>=1)          checkForPiece(board, myPosition, piece, possibleMoves, row-1, col);
            if(row-1>=1&&col-1>=1)checkForPiece(board, myPosition, piece, possibleMoves, row-1, col-1);

            return possibleMoves;
        }

        if (piece.getPieceType() == PieceType.KNIGHT)
        {
            int row = myPosition.getRow(), col = myPosition.getColumn();

            if(row+2<=8&&col+1<=8)checkForPiece(board, myPosition, piece, possibleMoves, row+2, col+1);
            if(row+2<=8&&col-1>=1)checkForPiece(board, myPosition, piece, possibleMoves, row+2, col-1);
            if(row-2>=1&&col+1<=8)checkForPiece(board, myPosition, piece, possibleMoves, row-2, col+1);
            if(row-2>=1&&col-1>=1)checkForPiece(board, myPosition, piece, possibleMoves, row-2, col-1);
            if(col+2<=8&&row+1<=8)checkForPiece(board, myPosition, piece, possibleMoves, row+1, col+2);
            if(col+2<=8&&row-1>=1)checkForPiece(board, myPosition, piece, possibleMoves, row-1, col+2);
            if(col-2>=1&&row+1<=8)checkForPiece(board, myPosition, piece, possibleMoves, row+1, col-2);
            if(col-2>=1&&row-1>=1)checkForPiece(board, myPosition, piece, possibleMoves, row-1, col-2);

            return possibleMoves;
        }

        if(piece.getPieceType() == PieceType.PAWN)
        {
            int row = myPosition.getRow(), col = myPosition.getColumn();
            ChessPosition attemptedPosition;

            if(pieceColor == ChessGame.TeamColor.WHITE)
            {
                row++;
                attemptedPosition = new ChessPosition(row, col);

                if(row<=8&&board.getPiece(attemptedPosition)==null)
                {
                    if(row==8)
                        for(int i=1;i<5;i++)
                            possibleMoves.add(new ChessMove(myPosition,attemptedPosition, PieceType.values()[i]));

                    else
                    {
                        possibleMoves.add(new ChessMove(myPosition,attemptedPosition, null));
                        if(row-1==2&&board.getPiece(new ChessPosition(row+1,col))==null)
                            possibleMoves.add(new ChessMove(myPosition,new ChessPosition(row+1,col), null));
                    }
                }

                col--;
                attemptedPosition = new ChessPosition(row, col);

                if(row<=8&&col>=1&&board.getPiece(attemptedPosition)!=null&&board.getPiece(attemptedPosition).pieceColor!= ChessGame.TeamColor.WHITE)
                {
                    if(row==8)
                        for(int i=1;i<5;i++)
                            possibleMoves.add(new ChessMove(myPosition,attemptedPosition, PieceType.values()[i]));

                    else
                        possibleMoves.add(new ChessMove(myPosition,attemptedPosition, null));
                }

                col+=2;
                attemptedPosition = new ChessPosition(row, col);

                if(row<=8&&col<=8&&board.getPiece(attemptedPosition)!=null&&board.getPiece(attemptedPosition).pieceColor!= ChessGame.TeamColor.WHITE)
                {
                    if(row==8)
                        for(int i=1;i<5;i++)
                            possibleMoves.add(new ChessMove(myPosition,attemptedPosition, PieceType.values()[i]));

                    else
                        possibleMoves.add(new ChessMove(myPosition,attemptedPosition, null));
                }

            }

            if(pieceColor == ChessGame.TeamColor.BLACK)
            {
                row--;
                attemptedPosition = new ChessPosition(row, col);

                if(row>=1&&board.getPiece(attemptedPosition)==null)
                {
                    if(row==1)
                        for(int i=1;i<5;i++)
                            possibleMoves.add(new ChessMove(myPosition,attemptedPosition, PieceType.values()[i]));

                    else
                    {
                        possibleMoves.add(new ChessMove(myPosition,attemptedPosition, null));
                        if(row+1==7&&board.getPiece(new ChessPosition(row-1,col))==null)
                            possibleMoves.add(new ChessMove(myPosition,new ChessPosition(row-1,col), null));
                    }
                }

                col--;
                attemptedPosition = new ChessPosition(row, col);

                if(row>=1&&col>=1&&board.getPiece(attemptedPosition)!=null&&board.getPiece(attemptedPosition).pieceColor!= ChessGame.TeamColor.BLACK)
                {
                    if(row==1)
                        for(int i=1;i<5;i++)
                            possibleMoves.add(new ChessMove(myPosition,attemptedPosition, PieceType.values()[i]));

                    else
                        possibleMoves.add(new ChessMove(myPosition,attemptedPosition, null));
                }

                col+=2;
                attemptedPosition = new ChessPosition(row, col);

                if(row>=1&&col<=8&&board.getPiece(attemptedPosition)!=null&&board.getPiece(attemptedPosition).pieceColor!= ChessGame.TeamColor.BLACK)
                {
                    if(row==1)
                        for(int i=1;i<5;i++)
                            possibleMoves.add(new ChessMove(myPosition,attemptedPosition, PieceType.values()[i]));

                    else
                        possibleMoves.add(new ChessMove(myPosition,attemptedPosition, null));
                }

            }

            return possibleMoves;
        }


        return List.of(new ChessMove(myPosition, new ChessPosition(8, 8), null));
    }

    private void bishopCheck(ChessBoard board, ChessPosition myPosition, ChessPiece piece, List<ChessMove> possibleMoves)
    {
        for (int row = myPosition.getRow() + 1, col = myPosition.getColumn() + 1; row <= 8 && col <= 8; row++, col++)
            if (checkForPiece(board, myPosition, piece, possibleMoves, row, col)) break;

        for (int row = myPosition.getRow() - 1, col = myPosition.getColumn() - 1; row >= 1 && col >= 1; row--, col--)
            if (checkForPiece(board, myPosition, piece, possibleMoves, row, col)) break;

        for (int row = myPosition.getRow() - 1, col = myPosition.getColumn() + 1; row >= 1 && col <= 8; row--, col++)
            if (checkForPiece(board, myPosition, piece, possibleMoves, row, col)) break;

        for (int row = myPosition.getRow() + 1, col = myPosition.getColumn() - 1; row <= 8 && col >= 1; row++, col--)
            if (checkForPiece(board, myPosition, piece, possibleMoves, row, col)) break;
    }

    private void rookCheck(ChessBoard board, ChessPosition myPosition, ChessPiece piece, List<ChessMove> possibleMoves)
    {
        for (int row = myPosition.getRow() + 1, col = myPosition.getColumn(); row <= 8; row++)
            if (checkForPiece(board, myPosition, piece, possibleMoves, row, col)) break;

        for (int row = myPosition.getRow() - 1, col = myPosition.getColumn(); row >= 1; row--)
            if (checkForPiece(board, myPosition, piece, possibleMoves, row, col)) break;

        for (int row = myPosition.getRow(), col = myPosition.getColumn() + 1; col <= 8; col++)
            if (checkForPiece(board, myPosition, piece, possibleMoves, row, col)) break;

        for (int row = myPosition.getRow(), col = myPosition.getColumn() - 1; col >= 1; col--)
            if (checkForPiece(board, myPosition, piece, possibleMoves, row, col)) break;
    }

    private boolean checkForPiece(ChessBoard board, ChessPosition myPosition, ChessPiece piece, List<ChessMove> possibleMoves, int row, int col)
    {
        ChessPosition attemptedPosition;
        attemptedPosition = new ChessPosition(row, col);
        if (board.getPiece(attemptedPosition) != null)
        {
            if (board.getPiece(attemptedPosition).pieceColor != piece.pieceColor)
                possibleMoves.add(new ChessMove(myPosition, attemptedPosition, null));

            return true;
        }
        possibleMoves.add(new ChessMove(myPosition, attemptedPosition, null));
        return false;
    }
}
