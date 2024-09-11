package pieces;

import main.Board;

import java.awt.image.BufferedImage;

public class Bishop extends Piece {
    // Construtor da classe Bishop
    public Bishop(Board board, int col, int row, boolean isWhite) {
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
        this.name = "Bishop";

        // Define a imagem da peça
        this.sprite = sheet.getSubimage(2 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale)
                .getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);

    }

    // Verifica se o movimento é válido para o bispo
    @Override
    public boolean isValidMovement(int col, int row) {
        // O bispo pode se mover diagonalmente
        return Math.abs(this.col - col) == Math.abs(this.row - row);
    }

    // Verifica se o movimento do bispo colide com outra peça
    @Override
    public boolean moveCollidesWithPiece(int col, int row) {
        // Verifica se há colisão em cada direção diagonal
        // up left
        if (this.col > col && this.row > row)
            for (int i = 1; i < Math.abs(this.col - col); i++)
                if (board.getPiece(this.col - i, this.row - i) != null)
                    return true;
        // up right
        if (this.col < col && this.row > row)
            for (int i = 1; i < Math.abs(this.col - col); i++)
                if (board.getPiece(this.col + i, this.row - i) != null)
                    return true;
        // down left
        if (this.col > col && this.row < row)
            for (int i = 1; i < Math.abs(this.col - col); i++)
                if (board.getPiece(this.col - i, this.row + i) != null)
                    return true;
        // down right
        if (this.col < col && this.row < row)
            for (int i = 1; i < Math.abs(this.col - col); i++)
                if (board.getPiece(this.col + i, this.row + i) != null)
                    return true;
        // Se não houver colisão, retorna false
        return false;
    }
}
