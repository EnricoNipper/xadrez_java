package main;

import pieces.Piece;

import java.awt.event.*;

public class Input extends MouseAdapter {

    // Referência ao tabuleiro
    Board board;

    // Construtor da classe Input
    public Input(Board board) {
        this.board = board;
    }

    // Evento de entrada do mouse em um componente
    @Override
    public void mouseEntered(MouseEvent e) {
        // Não implementado
    }

    // Evento de arrastar o mouse
    @Override
    public void mouseDragged(MouseEvent e) {
        // Obtem as coordenadas do mouse
        int xPos = e.getX();
        int yPos = e.getY();

        // Se uma peça estiver selecionada
        if (board.selectedPiece != null) {
            // Move a peça para a posição do mouse
            board.selectedPiece.xPos = xPos - board.tileSize / 2;
            board.selectedPiece.yPos = yPos - board.tileSize / 2;

            // Redesenha o tabuleiro
            board.repaint();
        }

    }


    // Evento de pressionar o botão do mouse
    @Override
    public void mousePressed(MouseEvent e) {
        // Obtem as coordenadas do clique do mouse
        int col = e.getX() / board.tileSize;
        int row = e.getY() / board.tileSize;

        // Obtem a peça na posição do clique
        Piece pieceXY = board.getPiece(col, row);
        // Se a peça não for nula, seleciona a peça
        if(pieceXY != null){
            board.selectedPiece = pieceXY;
        }

    }

    // Evento de soltar o botão do mouse
    @Override
    public void mouseReleased(MouseEvent e) {
        // Obtem as coordenadas do mouse
        int col = e.getX() / board.tileSize;
        int row = e.getY() / board.tileSize;

        // Se uma peça estiver selecionada
        if (board.selectedPiece != null) {
            // Cria um novo movimento
            Move move = new Move(board,board.selectedPiece, col, row);

            // Se o movimento for válido
            if (board.isValidMove(move)){
                // Realiza o movimento
                board.makeMove(move);
            } else {
                // Se o movimento não for válido, retorna a peça para a posição original
                board.selectedPiece.xPos = board.selectedPiece.col* board.tileSize;
                board.selectedPiece.yPos = board.selectedPiece.row* board.tileSize;
            }
        }
        // Desseleciona a peça
        board.selectedPiece = null;

        // Redesenha o tabuleiro
        board.repaint();
    }



}
