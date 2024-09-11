package pieces;

import main.Board;

import java.awt.image.BufferedImage;

public class Knight extends Piece {
    // Construtor da classe Knight
    public Knight(Board board, int col, int row, boolean isWhite) {
        super(board);
        // Define as coordenadas da peça no tabuleiro
        this.col = col;
        this.row = row;
        // Define as coordenadas da peça na tela
        this.xPos = col * board.tileSize;
        this.yPos = row * board.tileSize;

        // Define se a peça é branca ou preta
        this.isWhite = isWhite;
        // Define o nome da peça
        this.name = "Knight";

        // Define a imagem da peça
        this.sprite = sheet.getSubimage(3 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale)
                .getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);

    }

    // Verifica se o movimento é válido para o cavalo
    @Override
    public boolean isValidMovement(int col, int row) {
        // O cavalo pode se mover em forma de "L"
        return Math.abs(col - this.col) * Math.abs(row - this.row ) == 2;
    }

    @Override
    public boolean moveCollidesWithPiece(int col, int row) {
        return false; // Cavalo pode pular sobre outras peças
    }
}