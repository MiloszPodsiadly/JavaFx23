package com.pl.javafx23.cell;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import com.pl.javafx23.cell.Cell;


import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Cell extends StackPane {
    private final int row, col;
    private final Text symbol = new Text();
    private char value = '\0'; // Logiczna wartość komórki

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;

        Rectangle border = new Rectangle(130, 130);
        border.setFill(Color.WHITE);
        border.setStroke(Color.LIGHTGRAY);
        border.setArcHeight(20);
        border.setArcWidth(20);

        symbol.setFont(Font.font("Verdana", 60));
        symbol.setFill(Color.DARKBLUE);

        setPrefSize(130, 130);
        getChildren().addAll(border, symbol);
    }

    // Ustawianie symbolu gracza
    public void setSymbol(char playerSymbol) {
        this.value = playerSymbol;
        this.symbol.setText(String.valueOf(playerSymbol));
    }

    // Alias dla setSymbol
    public void setPlayer(char playerSymbol) {
        setSymbol(playerSymbol);
    }

    public boolean isEmpty() {
        return value == '\0';
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public char getValue() {
        return value;
    }
}