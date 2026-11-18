package client;

import chess.*;

public class ClientMain
{
    public static void main(String[] args)
    {
        String serverURL = "http://localhost:8081";
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
    }
}
