package pieces;

import main.Board;

import java.awt.image.BufferedImage;

public class Rook extends Piece {
    // Construtor da classe Rook
    public Rook(Board board, int col, int row, boolean isWhite) {
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
        this.name = "Rook";

        // Define a imagem da peça
        this.sprite = sheet.getSubimage(4 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale)
                .getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);

    }

    // Verifica se o movimento é válido para a torre
    @Override
    public boolean isValidMovement(int col, int row) {
        // A torre pode se mover na horizontal ou vertical
        return this.col == col || this.row == row;
    }

    // Verifica se o movimento da torre colide com outra peça
    @Override
    public boolean moveCollidesWithPiece(int col, int row) {
        // Verifica se há colisão em cada direção
        // left
        if (this.col > col)
            for (int c = this.col - 1; c > col; c--)
                if (board.getPiece(c, this.row) != null)
                    return true;
        // right
        if (this.col < col)
            for (int c = this.col + 1; c < col; c++)
                if (board.getPiece(c, this.row) != null)
                    return true;
        //  up
        if (this.row > row)
            for (int r = this.row - 1; r > row; r--)
                if (board.getPiece(this.col, r) != null)
                    return true;
        // down
        if (this.row < row)
            for (int r = this.row + 1; r < row; r++)
                if (board.getPiece(this.col, r) != null)
                    return true;
        // Se não houver colisão, retorna false
        return false;
    }

}