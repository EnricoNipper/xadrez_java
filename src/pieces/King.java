package pieces;

import main.Board;
import main.Move;

import java.awt.image.BufferedImage;

public class King extends Piece {
    // Construtor da classe King
    public King(Board board, int col, int row, boolean isWhite) {
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
        this.name = "King";

        // Define a imagem da peça
        this.sprite = sheet.getSubimage(0 , isWhite ? 0 : sheetScale, sheetScale, sheetScale)
                .getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);

    }

    // Verifica se o movimento é válido para o rei
    @Override
    public boolean isValidMovement(int col, int row) {
        // O rei pode se mover uma casa em qualquer direção
        return Math.abs((col - this.col) * (row - this.row)) == 1 || Math.abs(col - this.col) + Math.abs(row - this.row) == 1 || canCastle(col, row);
    }

    // Verifica se o rei pode fazer o roque
    private boolean canCastle(int col, int row) {
        // O roque só é possível se a linha for a mesma e o rei e a torre não tiverem se movido
        if (this.row == row) {
            // Roque do lado do rei
            if (col == 6){
                // Obtem a torre do lado do rei
                Piece rook = board.getPiece(7, row);
                // Se a torre existir e não tiver se movido
                if (rook != null && rook.isFirsMove && isFirsMove){
                    // Verifica se as casas entre o rei e a torre estão vazias
                    // e se o rei não está em xeque durante o movimento
                    return board.getPiece(5, row) == null &&
                            board.getPiece(6, row)== null &&
                            !board.checkScanner.isKingChecked(new Move(board, this, 5, row));
                }
            }else if (col == 2){
                // Obtem a torre do lado da rainha
                Piece rook = board.getPiece(0, row);
                // Se a torre existir e não tiver se movido
                if (rook != null && rook.isFirsMove && isFirsMove){
                    // Verifica se as casas entre o rei e a torre estão vazias
                    // e se o rei não está em xeque durante o movimento
                    return board.getPiece(3, row) == null &&
                            board.getPiece(2, row)== null &&
                            board.getPiece(1, row)== null &&
                            !board.checkScanner.isKingChecked(new Move(board, this, 3, row));
                }

            }
        }
        // Retorna false se o roque não for possível
        return false;
    }

    @Override
    public boolean moveCollidesWithPiece(int col, int row) {
        return false; // Rei pode se mover sobre outras peças
    }

}
