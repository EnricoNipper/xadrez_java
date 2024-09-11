package main;

import pieces.Piece;

public class CheckScanner {

    // Referência ao tabuleiro
    Board board;

    // Construtor da classe CheckScanner
    public CheckScanner (Board board) {
        this.board = board;
    }

    // Verifica se o rei está em xeque após um movimento
    public boolean isKingChecked(Move move) {
        // Encontra o rei da cor que está sendo verificada
        Piece king = board.findKing(move.piece.isWhite);
        // Verifica se o rei foi encontrado
        assert king != null;

        // Define as coordenadas do rei
        int kingCol = king.col;
        int kingRow = king.row;
        // Se a peça selecionada for o rei, usa as novas coordenadas do movimento
        if (board.selectedPiece != null && board.selectedPiece.name.equals("King")) {
            kingCol = move.newCol;
            kingRow = move.newRow;
        }

        // Verifica se o rei está sendo atacado por alguma peça
        return    hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 0, 1) ||
                hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 1, 0) ||
                hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 0, -1) ||
                hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, -1, 0) ||

                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, -1) ||
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, -1) ||
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, 1) ||
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1,1) ||

                hitByKnight(move.newCol, move.newRow, king, kingCol, kingRow) ||
                hitByPawn(move.newCol, move.newRow, king, kingCol, kingRow) ||
                hitByKing(king, kingCol, kingRow);
    }

    // Verifica se o rei está sendo atacado por uma torre ou rainha
    private boolean hitByRook(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int romVal){
        // Percorre as casas na direção da torre/rainha
        for (int i = 1; i < 9; i++){
            // Se a casa atual for a posição do rei, sai do loop
            if (kingCol + (i * colVal) == col && kingRow + (i * romVal) == row ) {
                break;
            }

            // Obtem a peça na casa atual
            Piece piece = board.getPiece(kingCol + (i * colVal), kingRow + (i * romVal));
            // Se a peça não for nula e não for a peça selecionada
            if (piece != null && piece != board.selectedPiece) {
                // Se a peça for uma torre ou rainha e não for do mesmo time do rei
                if(!board.sameTeam(piece, king) && (piece.name.equals("Rook") || piece.name.equals("Queen"))){
                    // Retorna true, pois o rei está sendo atacado
                    return true;
                }
                // Se a peça não for uma torre ou rainha ou for do mesmo time do rei, sai do loop
                break;
            }
        }
        // Retorna false, pois o rei não está sendo atacado por uma torre ou rainha
        return false;
    }

    // Verifica se o rei está sendo atacado por um bispo ou rainha
    private boolean hitByBishop(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int romVal){
        // Percorre as casas na direção do bispo/rainha
        for (int i = 1; i < 9; i++){
            // Se a casa atual for a posição do rei, sai do loop
            if (kingCol + (i * colVal) == col && kingRow - (i * romVal) == row ) {
                break;
            }

            // Obtem a peça na casa atual
            Piece piece = board.getPiece(kingCol - (i * colVal), kingRow - (i * romVal));
            // Se a peça não for nula e não for a peça selecionada
            if (piece != null && piece != board.selectedPiece) {
                // Se a peça for um bispo ou rainha e não for do mesmo time do rei
                if(!board.sameTeam(piece, king) && (piece.name.equals("Bishop") || piece.name.equals("Queen"))){
                    // Retorna true, pois o rei está sendo atacado
                    return true;
                }
                // Se a peça não for um bispo ou rainha ou for do mesmo time do rei, sai do loop
                break;
            }
        }
        // Retorna false, pois o rei não está sendo atacado por um bispo ou rainha
        return false;
    }

    // Verifica se o rei está sendo atacado por um cavalo
    private boolean hitByKnight(int col, int row, Piece king,int kingCol, int kingRow) {
        // Verifica se o rei está sendo atacado por um cavalo em cada direção possível
        return checkKnight(board.getPiece(kingCol - 1, kingRow - 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 1, kingRow - 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 2, kingRow - 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 2, kingRow + 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 1, kingRow + 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 1, kingRow + 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 2, kingRow + 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 2, kingRow - 1), king, col, row);

    }

    // Verifica se o rei está sendo atacado por um cavalo
    private boolean checkKnight(Piece p, Piece k, int col,int row) {
        // Se a peça não for nula, não for do mesmo time do rei e for um cavalo
        // e não for a peça selecionada
        return p != null && !board.sameTeam(p, k) && p.name.equals("knight") && !(p.col == col && p.row == row);
    }

    // Verifica se o rei está sendo atacado por um rei
    private boolean hitByKing( Piece king, int kingCol, int kingRow) {
        // Verifica se o rei está sendo atacado por um rei em cada direção possível
        return checkKing(board.getPiece(kingCol - 1, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol - 1, kingRow), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow), king) ||
                checkKing(board.getPiece(kingCol - 1, kingRow + 1), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow + 1), king) ||
                checkKing(board.getPiece(kingCol, kingRow + 1), king);
    }

    // Verifica se o rei está sendo atacado por um rei
    private boolean checkKing(Piece p, Piece k){
        // Se a peça não for nula, não for do mesmo time do rei e for um rei
        return  p != null && !board.sameTeam(p,k) && p.name.equals("King");
    }

    // Verifica se o rei está sendo atacado por um peão
    private  boolean hitByPawn(int col, int row, Piece king, int kingCol, int kingRow) {
        // Define o sentido do movimento do peão de acordo com a cor
        int colorVal = king.isWhite? -1: 1;
        // Verifica se o rei está sendo atacado por um peão em cada direção possível
        return checkPawn(board.getPiece(kingCol + 1, kingRow + colorVal), king, col, row) ||
                checkPawn(board.getPiece(kingCol - 1, kingRow + colorVal), king, col, row);
    }

    // Verifica se o rei está sendo atacado por um peão
    private  boolean checkPawn(Piece p, Piece k, int ignoredCol, int ignoredRow) {
        // Se a peça não for nula, não for do mesmo time do rei e for um peão
        return p != null && !board.sameTeam(p, k) && p.name.equals("Pawn");
    }

    // Verifica se o jogo terminou
    public boolean isGameOver(Piece king) {
        // Percorre a lista de peças
        for(Piece piece : board.pieceList) {
            // Verifica se a peça é do mesmo time do rei
            if(board.sameTeam(piece, king)) {
                // Define a peça selecionada como a peça atual
                board.selectedPiece = piece == king ? king : null;
                // Percorre o tabuleiro
                for(int row = 0; row < board.rows; row++) {
                    for (int col = 0; col < board.cols; col++){
                        // Cria um novo movimento
                        Move move = new Move(board, piece, col, row);
                        // Verifica se o movimento é válido
                        if(board.isValidMove(move)){
                            // Se o movimento é válido, o jogo não terminou
                            return false;
                        }
                    }
                }

            }
        }
        // Se nenhum movimento válido foi encontrado, o jogo terminou
        return true;
    }

}