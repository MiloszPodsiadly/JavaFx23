package com.pl.javafx23.logic;
import java.util.*;
public class GameLogic {
    private final char[][] board = new char[3][3];
    private char currentPlayer = 'X';
    private boolean gameOver = false;
    private int[][] winningCombination = null;

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean makeMove(int row, int col) {
        if (!isGameOver() && isCellAvailable(row, col)) {
            board[row][col] = currentPlayer;

            if (checkWinner(currentPlayer)) {
                gameOver = true;
                return true;
            } else if (isFull()) {
                gameOver = true;
                winningCombination = null; // No winner, it's a draw
            }
            return true;
        }
        return false;
    }

    public boolean isCellAvailable(int row, int col) {
        return board[row][col] == '\0';
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    public boolean checkWinner(char player) {
        // Check rows
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == player && board[row][1] == player && board[row][2] == player) {
                winningCombination = new int[][]{{row, 0}, {row, 1}, {row, 2}};
                return true;
            }
        }

        // Check columns
        for (int col = 0; col < 3; col++) {
            if (board[0][col] == player && board[1][col] == player && board[2][col] == player) {
                winningCombination = new int[][]{{0, col}, {1, col}, {2, col}};
                return true;
            }
        }

        // Check diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            winningCombination = new int[][]{{0, 0}, {1, 1}, {2, 2}};
            return true;
        }

        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            winningCombination = new int[][]{{0, 2}, {1, 1}, {2, 0}};
            return true;
        }

        return false;
    }

    public int[][] getWinningCombination() {
        return winningCombination;
    }

    public boolean isFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == '\0') {
                    return false;
                }
            }
        }
        return true;
    }

    public void resetGame() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = '\0';
            }
        }
        currentPlayer = 'X';
        gameOver = false;
        winningCombination = null;
    }

    public char[][] getBoardState() {
        char[][] copy = new char[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, 3);
        }
        return copy;
    }
    public int[] getComputerMove() {
        List<int[]> availableMoves = new ArrayList<>();

        // Find all empty cells
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (isCellAvailable(row, col)) {
                    availableMoves.add(new int[]{row, col});
                }
            }
        }

        // Return random available move
        if (!availableMoves.isEmpty()) {
            return availableMoves.get(new Random().nextInt(availableMoves.size()));
        }
        return null; // Should never happen in valid game state
    }
}