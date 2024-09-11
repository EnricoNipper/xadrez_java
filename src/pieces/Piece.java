package pieces;

import main.Board;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Piece {

    // Posição da peça no tabuleiro em coordenadas
    public int col, row;
    // Posição da peça no tabuleiro em pixels
    public int xPos, yPos;
    // Indica se a peça é branca
    public boolean isWhite;
    // Nome da peça
    public String name;
    // Valor da peça
    public int value;

    // Indica se a peça já foi movida
    public boolean isFirsMove = true;

    // Imagem da folha de sprites
    BufferedImage sheet;

    // Escala da folha de sprites
    protected int sheetScale;

    // Imagem da peça
    Image sprite;

    // Referência ao tabuleiro
    Board board;

    // Construtor da classe Piece
    public Piece(Board board) {
        this.board = board;

        // Tenta carregar a folha de sprites
        try {
            sheet = ImageIO.read(ClassLoader.getSystemResourceAsStream("pieces7.png"));
        } catch (IOException e) {
            System.err.println("Erro ao carregar a imagem: " + e.getMessage());
        }

        // Define a escala da folha de sprites
        sheetScale = sheet.getWidth()/6;
    }

    // Método para verificar se o movimento é válido para a peça
    public boolean isValidMovement(int col, int row) {
        return true; // Por padrão, qualquer movimento é válido
    }

    // Método para verificar se o movimento da peça colide com outra peça
    public boolean moveCollidesWithPiece(int col, int row) {
        return false; // Por padrão, a peça não colide com outras peças
    }


    // Método para desenhar a peça na tela
    public void paint(Graphics2D g2d){
        g2d.drawImage(sprite, xPos, yPos, null);
    }

}