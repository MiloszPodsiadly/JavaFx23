package com.pl.javafx23.gameboard;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import com.pl.javafx23.board.Board;
import com.pl.javafx23.cell.Cell;

public class GameBoard extends GridPane {
    private final Cell[][] cells = new Cell[3][3];
    private char currentPlayer = 'X';
    private boolean gameOver = false;
    private final char[][] logicBoard = new char[3][3];

    public GameBoard() {
        setHgap(5);
        setVgap(5);
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom, #74ebd5, #ACB6E5); -fx-padding: 40;");

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Cell cell = new Cell(row, col);
                cells[row][col] = cell;
                add(cell, col, row);

                cell.setOnMouseClicked(e -> {
                    if (!gameOver && cell.isEmpty()) {
                        cell.setSymbol(currentPlayer);
                        logicBoard[cell.getRow()][cell.getCol()] = currentPlayer;

                        if (checkWin(currentPlayer)) {
                            gameOver = true;
                            showWinner(currentPlayer);
                        } else {
                            switchPlayer();
                        }
                    }
                });
            }
        }
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    private boolean checkWin(char player) {
        // Wiersze, kolumny, przekątne
        for (int i = 0; i < 3; i++) {
            if (logicBoard[i][0] == player && logicBoard[i][1] == player && logicBoard[i][2] == player)
                return true;
            if (logicBoard[0][i] == player && logicBoard[1][i] == player && logicBoard[2][i] == player)
                return true;
        }
        return (logicBoard[0][0] == player && logicBoard[1][1] == player && logicBoard[2][2] == player) ||
                (logicBoard[0][2] == player && logicBoard[1][1] == player && logicBoard[2][0] == player);
    }

    private void showWinner(char player) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Wygrał gracz " + player + "!");
        alert.showAndWait();
    }

    public Cell getCell(int row, int col) {
        return cells[row][col];
    }
}