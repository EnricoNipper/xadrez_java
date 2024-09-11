package main;

import pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Board extends JPanel {

    // Posição inicial do tabuleiro em notação FEN (Forsyth-Edwards Notation)
    public String fenStartingPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1" ;
    // Posição de teste do tabuleiro em notação FEN
    public final String fenTest = "r3k2r/8/8/82Pp4/8/8/R3K2R b KQkq c3 0 1" ;
    // Tamanho de cada quadrado do tabuleiro
    public int tileSize = 85;
    // A peça atualmente selecionada
    public Piece selectedPiece;
    // JLabel para exibir a vez de jogar
    private JLabel turnLabel;
    // Número de colunas no tabuleiro
    int cols = 8;
    // Número de linhas no tabuleiro
    int rows = 8;

    // Lista de todas as peças no tabuleiro
    ArrayList<Piece> pieceList = new ArrayList<>();

    // Classe para lidar com entrada do usuário (clique do mouse, movimento)
    Input input = new Input(this);

    // Classe para verificar se o rei está em xeque
    public CheckScanner checkScanner = new CheckScanner(this);

    // Posição de captura "en passant" (se disponível)
    public int enPassantTile = -1;

    // Indica se é a vez das brancas jogarem
    private boolean isWhiteToMove = true;
    // Indica se o jogo terminou
    private boolean isGameOver = false;


    public Board() {
        // Define as dimensões do tabuleiro
        this.setPreferredSize(new Dimension(cols * tileSize, rows * tileSize ));
        // Adiciona as peças no tabuleiro
        loadPositionFromFEN(fenStartingPosition);

        // Adiciona um ouvinte de cliques do mouse
        this.addMouseListener(input);
        // Adiciona um ouvinte de movimento do mouse
        this.addMouseMotionListener(input);

        // Cria o JLabel para a vez de jogar
        turnLabel = new JLabel("White's Turn");
        turnLabel.setFont(new Font("Arial", Font.BOLD, 20));
        turnLabel.setForeground(Color.WHITE);
        // Adiciona o JLabel ao layout do Board
        setLayout(new BorderLayout()); // Define o layout do Board como BorderLayout
        add(turnLabel, BorderLayout.NORTH); // Adiciona o JLabel ao topo do Board

    }


    // Retorna a peça na posição especificada (coluna, linha)
    public Piece getPiece(int col, int row){
        for (Piece piece : pieceList)
            if (piece != null)
                if (piece.col == col && piece.row == row)
                    return piece;
        return null;
    }

    // Executa a movimentação da peça
    public void makeMove(Move move) {
        // Verifica se o jogo terminou
        if(isGameOver){
            return;
        }

        // Verifica se é a vez da peça correta se mover
        if(move.piece.isWhite != isWhiteToMove){
            return;
        }

        // Se a peça for um peão
        if(move.piece.name.equals("pawn")){
            movePawn(move); // Movimenta o peão
        } else if (move.piece.name.equals("King")) {
            moveKing((move)); // Movimenta o rei
        }

        // Move a peça para a nova posição
        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * tileSize;
        move.piece.yPos = move.newRow * tileSize;

        // Define que a peça já foi movida
        move.piece.isFirsMove = false;

        // Captura a peça se houver
        capture(move.capture);

        // Troca o jogador
        isWhiteToMove = !isWhiteToMove;
        // Atualiza o texto
        updateTurnText();

        // Atualiza o estado do jogo
        updateGameState();
    }

    // Movimentação específica para o rei
    private void moveKing(Move move){
        // Se o rei mover duas casas (roque)
        if(Math.abs(move.piece.col - move.newCol) == 2){
            Piece rook;
            // Roque do lado do rei
            if(move.piece.col < move.newCol) {
                rook = getPiece(7, move.piece.row);
                rook.col = 5;
            }else { // Roque do lado da rainha
                rook = getPiece(0, move.piece.row);
                rook.col = 3;
            }
            rook.xPos =rook.col * tileSize;
        }
    }

    // Movimentação específica para peões
    private void movePawn(Move move) {
        // Define o índice de cor para en passant
        int colorIndex = move.piece.isWhite ? 1 : -1;

        // Se o movimento for en passant
        if (getTileNum(move.newCol, move.newRow) == enPassantTile) {
            move.capture = getPiece(move.newCol, move.newRow + colorIndex);
        }

        // Se o peão mover duas casas
        if (Math.abs(move.piece.row - move.newRow) == 2) {
            enPassantTile = getTileNum(move.newCol, move.newRow + colorIndex);
        } else {
            // Limpa a posição de en passant
            enPassantTile = -1;
        }

        // Define o índice de cor para promoção
        colorIndex = move.piece.isWhite ? 0 : 7;
        // Se o peão atingir a última linha
        if(move.newRow == colorIndex) {
            promotePawn(move); // Promove o peão
        }
    }

    // Promove o peão para uma rainha
    private void promotePawn(Move move) {
        // Cria uma rainha
        pieceList.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite));
        // Remove o peão
        capture(move.piece);
    }

    // Remove uma peça do tabuleiro
    public void capture(Piece piece){
        // Remove a peça da lista de peças
        pieceList.remove(piece);
    }

    // Verifica se uma movimentação é válida
    public boolean isValidMove(Move move) {

        // Verifica se o jogo terminou
        if(isGameOver){
            return false;
        }

        // Verifica se é a vez da peça correta se mover
        if(move.piece.isWhite != isWhiteToMove){
            return false;
        }

        // Se as peças forem do mesmo time, a movimentação é inválida
        if (sameTeam(move.piece, move.capture)) {
            return false;
        }
        // Se a peça não pode se mover para a posição de destino, a movimentação é inválida
        if (!move.piece.isValidMovement(move.newCol, move.newRow)) {
            return false;
        }
        // Se a peça colidir com outra peça durante a movimentação, a movimentação é inválida
        if (move.piece.moveCollidesWithPiece(move.newCol, move.newRow)) {
            return false;
        }
        // Se o rei está em xeque após o movimento, a movimentação é inválida
        if(checkScanner.isKingChecked(move)){
            return false;
        }
        // Se todas as verificações passarem, a movimentação é válida
        return true;
    }

    // Verifica se duas peças pertencem ao mesmo time
    public boolean sameTeam(Piece p1, Piece p2) {
        // Se alguma das peças for nula, elas não são do mesmo time
        if(p1 == null || p2 == null){
            return  false;
        }
        // Peças do mesmo time tem o mesmo valor para isWhite
        return p1.isWhite == p2.isWhite;
    }

    // Retorna o número do quadrado no tabuleiro (começando em 0)
    public int getTileNum(int col, int row){
        return  row * rows + col;
    }

    // Encontra o rei no tabuleiro, dado a cor do rei
    Piece findKing(boolean iswhite) {
        // Percorre a lista de peças
        for (Piece piece : pieceList) {
            // Verifica se a peça é o rei e tem a cor correta
            if(iswhite == piece.isWhite && piece.name.equals("King")) {
                return piece;
            }
        }
        // Retorna nulo se o rei não foi encontrado
        return null;
    }

    // Adiciona as peças no tabuleiro em suas posições iniciais
    public void loadPositionFromFEN(String fenString){
        // Limpa a lista de peças
        pieceList.clear();
        // Divide a string FEN em partes
        String[] parts = fenString.split(" ");

        // Define a posição das peças
        String position = parts[0];
        int row = 0;
        int col = 0;
        // Percorre a string FEN
        for (int i = 0; i < position.length(); i++) {
            char ch = position.charAt(i);
            // Se o caractere for '/', muda para a próxima linha
            if (ch == '/') {
                row++;
                col = 0;
            } else if (Character.isDigit(ch)) {
                // Se o caractere for um número, pula as colunas vazias
                col += Character.getNumericValue(ch);
            }else{
                // Define a cor da peça
                boolean isWhite = Character.isUpperCase(ch);
                // Define o tipo da peça
                char pieceChar = Character.toLowerCase(ch);

                // Cria a peça com base no tipo e na cor
                switch (pieceChar) {
                    case 'r':
                        pieceList.add(new Rook(this, col, row, isWhite));
                        break;
                    case 'n':
                        pieceList.add(new Knight(this, col, row, isWhite));
                        break;
                    case 'b':
                        pieceList.add(new Bishop(this, col, row, isWhite));
                        break;
                    case 'q':
                        pieceList.add(new Queen(this, col, row, isWhite));
                        break;
                    case 'k':
                        pieceList.add(new King(this, col, row, isWhite));
                        break;
                    case 'p':
                        pieceList.add(new Pawn(this, col, row, isWhite));
                        break;
                }
                // Incrementa a coluna
                col++;
            }
        }
        // Define a cor que deve jogar
        isWhiteToMove = parts[1].equals("w");

        // Define as informações de roque
        Piece bqr = getPiece(0, 0);
        if (bqr instanceof Rook) {
            bqr.isFirsMove = parts[2].contains("q");
        }
        Piece bkr = getPiece(7, 0);
        if (bkr instanceof Rook) {
            bkr.isFirsMove = parts[2].contains("k");
        }
        Piece wqr = getPiece(0, 7);
        if (wqr instanceof Rook) {
            wqr.isFirsMove = parts[2].contains("Q");
        }
        Piece wkr = getPiece(7, 7);
        if (wkr instanceof Rook) {
            wkr.isFirsMove = parts[2].contains("K");
        }

        // Define a posição de captura "en passant"
        if (parts[3].equals("-")){
            enPassantTile = -1;
        }else{
            // Calcula a posição de captura "en passant"
            enPassantTile = (7 - (parts[3].charAt(1) - '1'))* 8 + (parts[3].charAt(0) - 'a');

        }

    }

    // Atualiza o estado do jogo
    private void updateGameState() {
        // Encontra o rei da cor que deve jogar
        Piece king = findKing(isWhiteToMove);
        // Verifica se o jogo terminou
        if (checkScanner.isGameOver(king)) {
            // Se o rei está em xeque, o jogador que está em xeque perde
            if (checkScanner.isKingChecked(new Move(this, king, king.col, king.row))) {
                String message = isWhiteToMove ? "Black Wins!" : "White Wins!";
                showGameOverDialog(message, "Checkmate!", "checkmate.png"); // Substitua "path/to/checkmate.png" pelo caminho do ícone
            } else { // Caso contrário, é um empate por xeque-mate
                showGameOverDialog("Stalemate!", "Stalemate!", "path/to/stalemate.png"); // Substitua "path/to/stalemate.png" pelo caminho do ícone
            }
            // Define que o jogo terminou
            isGameOver = true;
        } else if (insufficientMaterial(true) && insufficientMaterial(false)) { // Verifica se há material suficiente para continuar o jogo
            // Se não há material suficiente, é um empate
            showGameOverDialog("Insufficient Material!", "Draw", "path/to/draw.png"); // Substitua "path/to/draw.png" pelo caminho do ícone
            isGameOver = true;
        }
    }

    private void showGameOverDialog(String message, String title, String iconPath) {
        JOptionPane.showMessageDialog(null,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE,
                new ImageIcon(iconPath));
    }
    // Verifica se há material suficiente para continuar o jogo
    private boolean insufficientMaterial(boolean isWhite) {
        // Filtra as peças que são da cor especificada
        ArrayList<String> names = pieceList.stream()
                .filter(p -> p.isWhite == isWhite)
                .map(p -> p.name)
                .collect(Collectors.toCollection(ArrayList::new));

        // Se o jogador tem rainha, torre ou peão, há material suficiente
        if(names.contains("Queen")|| names.contains("Rook") || names.contains("Pawn")) {
            return false;
        }
        // Se o jogador tem menos de 3 peças, há material insuficiente
        return names.size() < 3;
    }
    // Atualiza o texto da vez de jogar
    private void updateTurnText() {
        // Define o texto da vez de jogar com base na cor que deve jogar
        turnLabel.setText(isWhiteToMove ? "White's Turn" : "Black's Turn");
    }

    // Desenha o tabuleiro e as peças
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Desenha os quadrados do tabuleiro
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                // Define a cor do quadrado
                g2d.setColor((c + r) % 2 == 0 ? new Color(0, 0, 0, 255) : new Color(133, 0, 199));
                // Desenha o quadrado
                g2d.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
            }


        // Destaca as casas válidas para a peça selecionada
        if(selectedPiece != null)
            for(int r = 0; r < tileSize; r++)
                for(int c = 0 ; c < cols; c++){
                    // Se o movimento é válido, destaca a casa
                    if(isValidMove(new Move(this, selectedPiece, c, r))){
                        g2d.setColor(new Color(68, 180,57, 190));
                        g2d.fillRect(c * tileSize, r * tileSize,tileSize, tileSize);
                    }
                }

        // Desenha as peças
        for (Piece piece : pieceList){
            piece.paint(g2d);
        }
    }
}