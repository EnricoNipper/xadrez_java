package pieces;

import main.Board;

import java.awt.image.BufferedImage;

public class Pawn extends Piece {
    // Construtor da classe Pawn
    public Pawn(Board board, int col, int row, boolean isWhite) {
        super(board); // Chama o construtor da classe pai (Piece)
        this.col = col; // Define a coluna da peça
        this.row = row; // Define a linha da peça
        this.xPos = col * board.tileSize; // Define a posição x da peça
        this.yPos = row * board.tileSize; // Define a posição y da peça

        this.isWhite = isWhite; // Define a cor da peça
        this.name = "pawn"; // Define o nome da peça

        // Define a imagem da peça
        this.sprite = sheet.getSubimage(5 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale)
                .getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);

    }

    // Verifica se a movimentação do peão é válida

    public boolean isValidMovement(int col, int row) {
        int colorIndex = isWhite ? 1: -1; // Define o índice de cor para a direção de movimento

        // Movimento para frente uma casa (se a casa estiver vazia)
        if (this.col == col && row == this.row - colorIndex && board.getPiece(col , row) == null)
            return true;

        // Movimento para frente duas casas (se o peão estiver na posição inicial e as duas casas estiverem vazias)
        if (this.row == (isWhite ? 6 : 1) && this.col == col && row == this.row - colorIndex * 2 && board.getPiece(col , row) == null && board.getPiece(col, row + colorIndex) == null)
            return true;

        // Captura diagonal à esquerda (se houver uma peça inimiga)
        if (col == this.col -1 && row == this.row - colorIndex && board.getPiece(col , row) != null)
            return true;

        // Captura diagonal à direita (se houver uma peça inimiga)
        if (col == this.col +1  && row == this.row - colorIndex && board.getPiece(col , row) != null)
            return true;

        // En passant (à esquerda)
        if (board.getTileNum(col, row) == board.enPassantTile && col == this.col -1 && row == this.row - colorIndex && board.getPiece(col, row +colorIndex) != null) {
            return true;
        }

        // En passant (à direita)
        if (board.getTileNum(col, row) == board.enPassantTile && col == this.col +1 && row == this.row - colorIndex && board.getPiece(col, row +colorIndex) != null) {
            return true;
        }

        // Se nenhuma das condições acima for satisfeita, a movimentação é inválida
        return false;

    }
}