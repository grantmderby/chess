package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.EscapeSequences;

import static ui.EscapeSequences.*;



public class BoardMaker
{

    public String boardRenderer(String playerColor, ChessBoard board)
    {
        StringBuilder result = new StringBuilder();
        result.append(SET_TEXT_COLOR_BLUE + EMPTY);
        for(char c = 'a'; c<='h';c++)
        {
            result.append(c).append("\u2003 ");
        }
        result.append("\n");
        for (int i = 0; i < 8; i++)
        {
            result.append(8 - i).append(" ");
            for (int k = 0; k < 8; k++)
            {
                if((i+k)%2==1)
                {
                    result.append(SET_BG_COLOR_DARK_GREY);
                }
                else
                {
                    result.append(SET_BG_COLOR_WHITE);
                }


                symbolFor(i, k, board, result);

                result.append(RESET_BG_COLOR);
            }
            result.append(" ").append(8 - i).append("\n");;
        }
        result.append(EMPTY);
        for(char c = 'a'; c<='h';c++)
        {
            result.append(c).append("\u2003 ");
        }

        return "" + result;
    }

    private void symbolFor(int i, int k, ChessBoard board, StringBuilder result)
    {
        var piece = board.getPiece(new ChessPosition(i+1,k+1));
        if (piece != null)
        {
            if(piece.getTeamColor() == ChessGame.TeamColor.BLACK)
            {
                pieceSelector(result,
                        piece,
                        WHITE_KING,
                        WHITE_QUEEN,
                        WHITE_KNIGHT,
                        WHITE_ROOK,
                        WHITE_BISHOP,
                        WHITE_PAWN);
            }

            else
            {
                pieceSelector(result,
                        piece,
                        BLACK_KING,
                        BLACK_QUEEN,
                        BLACK_KNIGHT,
                        BLACK_ROOK,
                        BLACK_BISHOP,
                        BLACK_PAWN);
            }
        }
        else
        {
            result.append(EMPTY);
        }
    }

    private void pieceSelector(StringBuilder result, ChessPiece piece, String blackKing, String blackQueen, String blackKnight, String blackRook, String blackBishop, String blackPawn)
    {
        switch (piece.getPieceType())
        {
            case ChessPiece.PieceType.KING -> result.append(blackKing);
            case ChessPiece.PieceType.QUEEN -> result.append(blackQueen);
            case ChessPiece.PieceType.KNIGHT -> result.append(blackKnight);
            case ChessPiece.PieceType.ROOK -> result.append(blackRook);
            case ChessPiece.PieceType.BISHOP -> result.append(blackBishop);
            case ChessPiece.PieceType.PAWN -> result.append(blackPawn);
        }
    }
}
