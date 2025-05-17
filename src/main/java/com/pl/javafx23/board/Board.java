package com.pl.javafx23.board;

public class Board {
    private final int[][] board;

    public Board() {
        board = new int[3][3]; // Inicjalizacja pustej planszy (0 oznacza brak ruchu)

    }
    public void clear() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = 0;
            }
        }
    }

    // Wykonanie ruchu na planszy
    public boolean makeMove(int row, int col, int player) {
        if (board[row][col] == 0) {
            board[row][col] = player;
            return true;
        }
        return false; // Ruch nieprawidłowy (komórka zajęta)
    }

    // Sprawdzenie, czy plansza jest pełna
    public boolean isFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    return false; // Jeśli są puste komórki, plansza nie jest pełna
                }
            }
        }
        return true;
    }

    // Sprawdzenie wygranej dla danego gracza
    public boolean checkWin(int player) {
        // Sprawdzanie wierszy i kolumn
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true; // Wygrana w wierszu
            }
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true; // Wygrana w kolumnie
            }
        }
        // Sprawdzanie przekątnych
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true; // Główna przekątna
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true; // Dodatkowa przekątna
        }
        return false;
    }

    // Sprawdzenie, czy dana komórka jest dostępna (pusta)
    public boolean isCellAvailable(int row, int col) {
        return board[row][col] == 0;
    }

    // Oznaczenie komórki (np. w symulacji ruchów)
    public void markCell(int row, int col, int player) {
        board[row][col] = player;
    }

    // Cofnięcie ruchu (np. w symulacji MiniMax)
    public void undoMove(int row, int col) {
        board[row][col] = 0;
    }

    // Zwraca rozmiar planszy, jeśli potrzebujemy operacji dynamicznych
    public int getSize() {
        return board.length; // Zawsze 3 dla tej planszy
    }

    // Debugowanie planszy (np. do konsoli)
    public void printBoard() {
        for (int[] row : board) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}
