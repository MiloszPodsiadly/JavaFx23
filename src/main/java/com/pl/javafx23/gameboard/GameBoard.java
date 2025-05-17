package com.pl.javafx23.gameboard;

import com.pl.javafx23.cell.Cell;
import com.pl.javafx23.logic.GameLogic;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import java.util.*;
import com.pl.javafx23.JavaFxApplication;

public class GameBoard extends GridPane {
    private final Cell[][] cells = new Cell[3][3];
    private final GameLogic gameLogic;
    private final boolean isPlayerVsComputer;
    private final int computerDifficulty;
    private ParallelTransition currentAnimation;

    public GameBoard(GameLogic logic, boolean isPlayerVsComputer, int computerDifficulty) {
        this.gameLogic = logic;
        this.isPlayerVsComputer = isPlayerVsComputer;
        this.computerDifficulty = computerDifficulty;
        initializeBoard();
        createCells();
    }

    private void initializeBoard() {
        setHgap(10);
        setVgap(10);
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); " +
                "-fx-padding: 20; " +
                "-fx-background-radius: 15;");
    }

    private void createCells() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                final int finalRow = row;
                final int finalCol = col;

                Cell cell = new Cell(row, col);
                cells[row][col] = cell;
                add(cell, col, row);

                setupCellHoverEffects(cell);
                cell.setOnMouseClicked(e -> handleCellClick(cell, finalRow, finalCol));
            }
        }
    }

    private void setupCellHoverEffects(Cell cell) {
        cell.setOnMouseEntered(e -> {
            if (cell.isEmpty() && !gameLogic.isGameOver()) {
                cell.setHoverEffect(gameLogic.getCurrentPlayer());
            }
        });

        cell.setOnMouseExited(e -> {
            if (cell.isEmpty()) {
                cell.clearHoverEffect();
            }
        });
    }

    private void handleCellClick(Cell cell, int row, int col) {
        if (!gameLogic.isGameOver() && gameLogic.isCellAvailable(row, col)) {
            // Player's move
            gameLogic.makeMove(row, col);
            cell.setSymbol(gameLogic.getCurrentPlayer());
            cell.playPlacementAnimation();

            if (checkGameEnd()) {
                return;
            }

            gameLogic.switchPlayer();

            // Computer's move if applicable
            if (isPlayerVsComputer && gameLogic.getCurrentPlayer() == 'O') {
                makeComputerMove();
            }
        }
    }

    private boolean checkGameEnd() {
        if (gameLogic.isGameOver()) {
            highlightWinningCells();
            showGameEndDialog();
            return true;
        } else if (gameLogic.isFull()) {
            showGameEndDialog();
            return true;
        }
        return false;
    }

    private void makeComputerMove() {
        new Thread(() -> {
            try {
                // Simulate computer thinking time based on difficulty
                Thread.sleep(calculateComputerDelay());

                int[] move = gameLogic.getComputerMove();
                if (move != null) {
                    Platform.runLater(() -> {
                        Cell cell = cells[move[0]][move[1]];
                        gameLogic.makeMove(move[0], move[1]);
                        cell.setSymbol(gameLogic.getCurrentPlayer());
                        cell.playPlacementAnimation();

                        if (!checkGameEnd()) {
                            gameLogic.switchPlayer();
                        }
                    });
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private long calculateComputerDelay() {
        // Easy: 1-2s, Medium: 0.5-1.5s, Hard: 0-1s
        return (long) (1000 + (new Random().nextInt(1000) * (1 - computerDifficulty * 0.25)));
    }

    private void highlightWinningCells() {
        int[][] winningCombination = gameLogic.getWinningCombination();
        if (winningCombination != null) {
            for (int[] position : winningCombination) {
                cells[position[0]][position[1]].highlightAsWinner();
            }
        }
    }

    private void showGameEndDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Over");

        if (gameLogic.getWinningCombination() != null) {
            alert.setHeaderText("Player " + gameLogic.getCurrentPlayer() + " wins!");
        } else {
            alert.setHeaderText("It's a draw!");
        }

        ButtonType playAgain = new ButtonType("Play Again");
        ButtonType mainMenu = new ButtonType("Main Menu");
        ButtonType exit = new ButtonType("Exit");

        alert.getButtonTypes().setAll(playAgain, mainMenu, exit);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == playAgain) {
                resetBoard();
            } else if (result.get() == mainMenu) {
                ((JavaFxApplication) getScene().getWindow().getUserData()).returnToMainMenu();
            } else if (result.get() == exit) {
                Platform.exit();
            }
        }
    }

    public void resetBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                cells[row][col].clear();
            }
        }
        gameLogic.resetGame();

        if (isPlayerVsComputer && gameLogic.getCurrentPlayer() == 'O') {
            makeComputerMove();
        }
    }

    public Cell getCell(int row, int col) {
        return cells[row][col];
    }
}