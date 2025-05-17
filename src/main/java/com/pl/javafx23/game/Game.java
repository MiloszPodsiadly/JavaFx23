package com.pl.javafx23.game;

import javafx.scene.Scene;
import javafx.stage.Stage;
import com.pl.javafx23.MainMenu.MainMenu;
import com.pl.javafx23.gameboard.GameBoard;
import com.pl.javafx23.board.Board;
import com.pl.javafx23.player.Player;
import com.pl.javafx23.game.Game;
import com.pl.javafx23.cell.Cell;


public class Game {
    private final Board board; // Plansza logiczna
    private final GameBoard gameBoard; // Graficzna plansza
    private final Player player1;
    private final Player player2;
    private Player currentPlayer;
    private final boolean vsComputer; // Tryb gry (vs Komputer lub vs Gracz)
    private final Stage stage; // Referencja do sceny głównej

    public Game(Stage stage, boolean vsComputer) {
        this.stage = stage;
        this.vsComputer = vsComputer;
        this.board = new Board();

        // Inicjalizacja graczy
        this.player1 = new Player(1, 'X', "Player 1");
        this.player2 = new Player(2, 'O', vsComputer ? "Computer" : "Player 2");
        this.currentPlayer = this.player1;

        // Tworzenie planszy gry
        this.gameBoard = new GameBoard();
        initGameBoard();
    }

    // Inicjalizacja planszy graficznej i przypisanie zdarzeń
    private void initGameBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Cell cell = gameBoard.getCell(row, col);
                cell.setOnMouseClicked(e -> handleMove(cell));
            }
        }
    }

    // Obsługa ruchu gracza
    private void handleMove(Cell cell) {
        if (!cell.isEmpty() || board.checkWin(currentPlayer.getNumber())) {
            return; // Komórka zajęta lub gra zakończona
        }

        // Wykonanie ruchu
        board.makeMove(cell.getRow(), cell.getCol(), currentPlayer.getNumber());
        cell.setSymbol(currentPlayer.getSymbol());


        // Sprawdzenie warunków wygranej
        if (board.checkWin(currentPlayer.getNumber())) {
            endGame(currentPlayer.getName() + " wins!");
            return;
        }

        // Sprawdzenie remisu
        if (board.isFull()) {
            endGame("It's a draw!");
            return;
        }

        // Zmiana gracza
        switchPlayer();

        // Ruch komputera (jeśli tryb vs komputer)
        if (vsComputer && currentPlayer == player2) {
            computerMove();
        }
    }

    // Obsługa ruchu komputera
    private void computerMove() {
        int[] move = getComputerMove(); // Prosty algorytm ruchu
        int row = move[0];
        int col = move[1];

        board.makeMove(row, col, currentPlayer.getNumber());
        gameBoard.getCell(row, col).setPlayer(currentPlayer.getSymbol());

        // Sprawdzenie warunków wygranej
        if (board.checkWin(currentPlayer.getNumber())) {
            endGame(currentPlayer.getName() + " wins!");
            return;
        }

        // Sprawdzenie remisu
        if (board.isFull()) {
            endGame("It's a draw!");
            return;
        }

        // Zmiana gracza
        switchPlayer();
    }

    private int[] getComputerMove() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board.isCellAvailable(row, col)) {
                    return new int[]{row, col};
                }
            }
        }
        return null; // Nie powinno się zdarzyć, jeśli poprawnie sprawdzamy remis
    }

    // Zakończenie gry
    private void endGame(String message) {
        // Wyświetlenie informacji o zakończeniu gry
        System.out.println(message);

        // Przeniesienie do menu głównego po zakończeniu gry
        MainMenu mainMenu = new MainMenu(stage); // Tworzymy ponownie menu główne
        Scene scene = new Scene(mainMenu, 400, 400);
        stage.setScene(scene);
    }

    // Zmiana aktualnego gracza
    private void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    // Rozpoczęcie gry z przypisaniem sceny gry
    public void startGame() {
        Scene scene = new Scene(gameBoard, 450, 450); // Tworzy scenę z planszą
        stage.setScene(scene);
    }
}
