package com.pl.javafx23.cell;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.animation.ParallelTransition;
import javafx.animation.Animation;

public class Cell extends StackPane {
    private final int row;
    private final int col;
    private char symbol = ' ';
    private Rectangle border;
    private Circle circle;
    private Line line1, line2;
    private Circle hoverCircle;
    private Line hoverLine1, hoverLine2;
    private ScaleTransition currentAnimation;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        initializeCell();
    }

    private void initializeCell() {
        border = new Rectangle(80, 80);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.rgb(255, 255, 255, 0.3));
        border.setStrokeWidth(2);
        border.setArcWidth(15);
        border.setArcHeight(15);
        border.getStyleClass().add("cell-border");

        getChildren().add(border);

        // Hover effects (initially invisible)
        hoverCircle = createHoverCircle();
        hoverLine1 = createHoverLine(45);
        hoverLine2 = createHoverLine(-45);

        getChildren().addAll(hoverCircle, hoverLine1, hoverLine2);
        setVisibleHoverEffect(null);

        // Initialize cell style
        getStyleClass().add("game-cell");
    }

    private Circle createHoverCircle() {
        Circle circle = new Circle(30);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.rgb(255, 255, 255, 0.2));
        circle.setStrokeWidth(2);
        circle.getStyleClass().add("hover-symbol");
        return circle;
    }

    private Line createHoverLine(double angle) {
        Line line = new Line();
        line.setStartX(-30);
        line.setStartY(0);
        line.setEndX(30);
        line.setEndY(0);
        line.setStroke(Color.rgb(255, 255, 255, 0.2));
        line.setStrokeWidth(2);
        line.setRotate(angle);
        line.getStyleClass().add("hover-symbol");
        return line;
    }

    public void setHoverEffect(char player) {
        setVisibleHoverEffect(player);
    }

    public void clearHoverEffect() {
        setVisibleHoverEffect(null);
    }

    private void setVisibleHoverEffect(Character player) {
        hoverCircle.setVisible(player != null && player == 'O');
        hoverLine1.setVisible(player != null && player == 'X');
        hoverLine2.setVisible(player != null && player == 'X');
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
        drawSymbol();
    }

    private void drawSymbol() {
        // Remove previous symbols
        getChildren().removeAll(circle, line1, line2);

        if (symbol == 'O') {
            circle = new Circle(30);
            circle.setFill(Color.TRANSPARENT);
            circle.setStroke(Color.web("#4FC3F7"));
            circle.setStrokeWidth(4);
            circle.getStyleClass().add("o-symbol");
            getChildren().add(circle);
        } else if (symbol == 'X') {
            line1 = new Line(-30, -30, 30, 30);
            line2 = new Line(-30, 30, 30, -30);
            line1.setStroke(Color.web("#FF5252"));
            line2.setStroke(Color.web("#FF5252"));
            line1.setStrokeWidth(4);
            line2.setStrokeWidth(4);
            line1.getStyleClass().add("x-symbol");
            line2.getStyleClass().add("x-symbol");
            getChildren().addAll(line1, line2);
        }
    }

    public void clear() {
        symbol = ' ';
        getChildren().removeAll(circle, line1, line2);
        circle = null;
        line1 = null;
        line2 = null;
        getStyleClass().remove("winning-cell");
    }

    public void highlightAsWinner() {
        getStyleClass().add("winning-cell");
    }

    public void resetAnimationState() {
        if (currentAnimation != null) {
            currentAnimation.stop();
        }
        setScaleX(1);
        setScaleY(1);
        setOpacity(1);
        getStyleClass().remove("winning-cell");
    }

    public boolean isEmpty() {
        return symbol == ' ';
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void playPlacementAnimation() {
        ScaleTransition scale = new ScaleTransition(Duration.millis(150), this);
        scale.setFromX(0.8);
        scale.setFromY(0.8);
        scale.setToX(1.0);
        scale.setToY(1.0);

        FadeTransition fade = new FadeTransition(Duration.millis(150), this);
        fade.setFromValue(0.6);
        fade.setToValue(1.0);

        ParallelTransition transition = new ParallelTransition(scale, fade);
        transition.play();
    }
}