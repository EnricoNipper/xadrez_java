package pieces;

import java.awt.image.BufferedImage;

import main.Board;


public class Queen extends Piece {
    public Queen(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.tileSize;
        this.yPos = row * board.tileSize;

        this.isWhite = isWhite;
        this.name = "Queen";

        // Carrega a imagem da rainha do sprite sheet
        this.sprite = sheet.getSubimage(sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale)
                .getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);

    }

    // Verifica se o movimento é válido para a rainha
    public boolean isValidMovement(int col, int row) {
        // A rainha pode mover-se na horizontal, vertical ou diagonal
        return this.col == col || this.row == row || Math.abs(this.col - col) == Math.abs(this.row - row);
    }

    // Verifica se o movimento da rainha colide com outra peça
    public boolean moveCollidesWithPiece(int col, int row) {
        // Verifica se o movimento é na horizontal ou vertical
        if (this.col == col || this.row == row) {
            // Verifica se a rainha está se movendo para a esquerda
            if (this.col > col)
                // Verifica se há alguma peça na linha entre a posição atual e a posição de destino
                for (int c = this.col - 1; c > col; c--)
                    if (board.getPiece(c, this.row) != null)
                        return true;
            // Verifica se a rainha está se movendo para a direita
            if (this.col < col)
                // Verifica se há alguma peça na linha entre a posição atual e a posição de destino
                for (int c = this.col + 1; c < col; c++)
                    if (board.getPiece(c, this.row) != null)
                        return true;
            // Verifica se a rainha está se movendo para cima
            if (this.row > row)
                // Verifica se há alguma peça na coluna entre a posição atual e a posição de destino
                for (int r = this.row - 1; r > row; r--)
                    if (board.getPiece(this.col, r) != null)
                        return true;
            // Verifica se a rainha está se movendo para baixo
            if (this.row < row)
                // Verifica se há alguma peça na coluna entre a posição atual e a posição de destino
                for (int r = this.row + 1; r < row; r++)
                    if (board.getPiece(this.col, r) != null)
                        return true;
        } else {
            // Verifica se o movimento é diagonal
            // Verifica se a rainha está se movendo para cima e para a esquerda
            if (this.col > col && this.row > row)
                // Verifica se há alguma peça na diagonal entre a posição atual e a posição de destino
                for (int i = 1; i < Math.abs(this.col - col); i++)
                    if (board.getPiece(this.col - i, this.row - i) != null)
                        return true;
            // Verifica se a rainha está se movendo para cima e para a direita
            if (this.col < col && this.row > row)
                // Verifica se há alguma peça na diagonal entre a posição atual e a posição de destino
                for (int i = 1; i < Math.abs(this.col - col); i++)
                    if (board.getPiece(this.col + i, this.row - i) != null)
                        return true;
            // Verifica se a rainha está se movendo para baixo e para a esquerda
            if (this.col > col && this.row < row)
                // Verifica se há alguma peça na diagonal entre a posição atual e a posição de destino
                for (int i = 1; i < Math.abs(this.col - col); i++)
                    if (board.getPiece(this.col - i, this.row + i) != null)
                        return true;
            // Verifica se a rainha está se movendo para baixo e para a direita
            if (this.col < col && this.row < row)
                // Verifica se há alguma peça na diagonal entre a posição atual e a posição de destino
                for (int i = 1; i < Math.abs(this.col - col); i++)
                    if (board.getPiece(this.col + i, this.row + i) != null)
                        return true;
        }
        // Se não houver nenhuma peça no caminho, o movimento é válido
        return false;
    }
}