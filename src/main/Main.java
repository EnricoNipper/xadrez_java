package main;

import javax.swing.*;
import java.awt.*;

public class Main {


    public static void main(String[] args) {

        // Cria a janela principal do jogo
        JFrame frame = new JFrame();
        // Define o layout da janela como GridBagLayout
        frame.setLayout(new GridBagLayout());
        // Define o fundo da janela como cinza
        frame.getContentPane().setBackground(Color.gray);
        // Maximiza a janela em ambas as dimensões
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Define o comportamento ao fechar a janela (encerra o programa)
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // Define o tamanho mínimo da janela
        frame.setMinimumSize(new Dimension(888, 888));
        // Centraliza a janela na tela
        frame.setLocationRelativeTo(null);

        // Cria um objeto Board para representar o tabuleiro de xadrez
        Board board = new Board();
        // Adiciona a tabela à janela
        frame.add(board);

        // Revalida o layout do tabuleiro
        board.revalidate();
        // Redesenha o tabuleiro
        board.repaint();

        // Torna a janela visível
        frame.setVisible(true);

    }
}