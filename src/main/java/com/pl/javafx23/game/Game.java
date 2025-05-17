package com.pl.javafx23.game;

import com.pl.javafx23.MainMenu.MainMenu;
import com.pl.javafx23.board.Board;
import com.pl.javafx23.cell.Cell;
import com.pl.javafx23.gameboard.GameBoard;
import com.pl.javafx23.logic.GameLogic;
import com.pl.javafx23.player.Player;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

public class Game {
    private final Board board; // Logiczna plansza
    private final GameBoard gameBoard; // Graficzna plansza
    private final Stage stage; // Okno gry
    private Player player1; // Gracz 1
    private Player player2; // Gracz 2
    private Player currentPlayer; // Aktywny gracz
    private final boolean vsComputer; // Tryb gry
    private int computerDifficultyLevel; // Poziom trudności komputera

    // Konstruktor klasy Game
    public Game(GameBoard gameBoard, Stage stage, boolean vsComputer, String player1Name, String player2Name, int difficultyLevel) {
        this.board = new Board();
        this.gameBoard = gameBoard;
        this.stage = stage;
        this.vsComputer = vsComputer;

        // Inicjalizacja graczy
        this.player1 = new Player(1, 'X', player1Name);
        this.player2 = new Player(2, 'O', vsComputer ? "Computer" : player2Name);
        this.currentPlayer = player1;

        // Inicjalizacja poziomu trudności dla komputera
        this.computerDifficultyLevel = difficultyLevel;

        // Inicjalizacja graficznej planszy
        initGameBoard();
    }

    // Inicjalizacja graficznej planszy i przypisanie akcji
    private void initGameBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Cell cell = gameBoard.getCell(row, col);
                cell.setOnMouseClicked(event -> handleMove(cell)); // Obsługa kliknięcia w komórkę
            }
        }
    }

    // Obsługa ruchu gracza
    private void handleMove(Cell cell) {
        if (!cell.isEmpty() || board.checkWin(currentPlayer.getNumber())) {
            return; // Komórka jest zajęta lub gra się zakończyła
        }

        // Wykonanie ruchu
        board.makeMove(cell.getRow(), cell.getCol(), currentPlayer.getNumber());
        cell.setSymbol(currentPlayer.getSymbol()); // Ustaw symbol w graficznej planszy

        // Sprawdzenie czy aktywny gracz wygrał
        if (board.checkWin(currentPlayer.getNumber())) {
            endGame(currentPlayer.getName() + " wins!");
            return;
        }

        // Sprawdzenie czy plansza jest pełna (remis)
        if (board.isFull()) {
            endGame("It's a draw!");
            return;
        }

        // Zmiana gracza
        switchPlayer();

        // Automatyczny ruch komputera (jeśli tryb vsComputer i turą jest gracza 2 - komputera)
        if (vsComputer && currentPlayer == player2) {
            computerMove();
        }
    }

    // Zmiana aktywnego gracza
    private void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    // Ruch komputera
    private void computerMove() {
        int[] move;

        // Wybór ruchu w zależności od poziomu trudności
        if (computerDifficultyLevel == 1) {
            move = getComputerMoveEasy();
        } else if (computerDifficultyLevel == 2) {
            move = getComputerMoveMedium();
        } else {
            move = getComputerMoveHard();
        }

        // Wykonanie ruchu
        if (move != null) {
            board.makeMove(move[0], move[1], player2.getNumber());
            gameBoard.getCell(move[0], move[1]).setSymbol(player2.getSymbol());

            // Sprawdzenie końca gry po ruchu komputera
            if (board.checkWin(player2.getNumber())) {
                endGame(player2.getName() + " wins!");
                return;
            }

            if (board.isFull()) {
                endGame("It's a draw!");
                return;
            }

            switchPlayer(); // Powrót do gracza 1
        }
    }

    // Logika ruchu AI - łatwa
    private int[] getComputerMoveEasy() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board.isCellAvailable(row, col)) {
                    return new int[]{row, col};
                }
            }
        }
        return null; // Jeśli nie ma już dostępnych ruchów
    }

    // Logika ruchu AI - średnia
    private int[] getComputerMoveMedium() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board.isCellAvailable(row, col)) {
                    // Tymczasowo wykonaj ruch, aby ocenić sytuację
                    board.makeMove(row, col, player2.getNumber());
                    if (board.checkWin(player2.getNumber())) {
                        board.undoMove(row, col); // Cofnij ruch
                        return new int[]{row, col};
                    }
                    board.undoMove(row, col); // Cofnięcie gdy ruch nie jest wygrywający
                }
            }
        }
        return getComputerMoveEasy(); // Jeśli nie ma wygrywającego ruchu
    }

    // Logika ruchu AI - trudna (MiniMax)
    private int[] getComputerMoveHard() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board.isCellAvailable(row, col)) {
                    board.makeMove(row, col, player2.getNumber()); // Symulacja ruchu
                    int score = miniMax(false);
                    board.undoMove(row, col); // Cofnięcie symulacji

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{row, col};
                    }
                }
            }
        }
        return bestMove;
    }

    // Algorytm MiniMax
    private int miniMax(boolean isMaximizing) {
        if (board.checkWin(player2.getNumber())) return 10; // Wygrana gracza 2
        if (board.checkWin(player1.getNumber())) return -10; // Wygrana gracza 1
        if (board.isFull()) return 0; // Remis

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board.isCellAvailable(row, col)) {
                    board.makeMove(row, col, isMaximizing ? player2.getNumber() : player1.getNumber());
                    int score = miniMax(!isMaximizing);
                    board.undoMove(row, col);

                    bestScore = isMaximizing ? Math.max(score, bestScore) : Math.min(score, bestScore);
                }
            }
        }
        return bestScore;
    }

    // Zakończenie gry
    private void endGame(String message) {
        // Wyświetlenie komunikatu zakończenia gry
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType playAgain = new ButtonType("Play Again");
        ButtonType exit = new ButtonType("Exit");
        alert.getButtonTypes().setAll(playAgain, exit);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == playAgain) {
            restartGame();
        } else {
            MainMenu mainMenu = new MainMenu(stage);
            Scene scene = new Scene(mainMenu, 400, 400);
            stage.setScene(scene);
        }
    }

    // Restart gry
    private void restartGame() {
        board.clear();
        currentPlayer = player1;
        initGameBoard();
        startGame();
    }

    // Start gry
    public void startGame() {
        Scene scene = new Scene(gameBoard, 450, 450);
        stage.setScene(scene);
    }
}