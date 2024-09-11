package main;

import pieces.Piece;

public class Move {
    // A peça que está sendo movida
    Piece piece;
    // A peça que está sendo capturada (se houver)
    Piece capture;

    // Coordenadas da posição inicial da peça
    int oldCol;
    int oldRow;
    // Coordenadas da posição final da peça
    int newCol;
    int newRow;

    // Construtor da classe Move
    public Move(Board board, Piece piece, int newCol, int newRow) {
        // Salva as coordenadas da posição inicial
        this.oldCol = piece.col;
        this.oldRow = piece.row;
        // Salva as coordenadas da posição final
        this.newCol = newCol;
        this.newRow = newRow;

        // Salva a peça que está sendo movida
        this.piece = piece;
        // Obtem a peça que está na posição final (se houver)
        this.capture = board.getPiece(newCol, newRow);
    }
}