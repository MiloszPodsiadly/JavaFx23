package com.pl.javafx23.logic;

import javafx.scene.control.Alert;

public class GameLogic {
    private char[][] board = new char[3][3];
    private char currentPlayer = 'X';
    private boolean gameOver = false;

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void switchPlayer() {
        currentPlayer = currentPlayer == 'X' ? 'O' : 'X';
    }

    public void makeMove(int row, int col) {
        board[row][col] = currentPlayer;
    }

    public boolean checkWinner() {
        // Sprawdzamy wszystkie możliwości
        for (int i = 0; i < 3; i++) {
            if (same(board[i][0], board[i][1], board[i][2]) ||  // wiersz
                    same(board[0][i], board[1][i], board[2][i]))    // kolumna
                return gameOver = true;
        }
        if (same(board[0][0], board[1][1], board[2][2]) ||      // przekątne
                same(board[0][2], board[1][1], board[2][0]))
            return gameOver = true;

        return false;
    }

    private boolean same(char a, char b, char c) {
        return a != 0 && a == b && b == c;
    }

    public void showWinner(char winner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Koniec gry");
        alert.setHeaderText(null);
        alert.setContentText("Gracz " + winner + " wygrał!");
        alert.showAndWait();
    }
}