package com.pl.javafx23.MainMenu;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.pl.javafx23.gameboard.GameBoard;
import javafx.geometry.Insets;
import javafx.scene.layout.CornerRadii;


public class MainMenu extends StackPane {

    public MainMenu(Stage stage) {
        // Gradientowe tło
        BackgroundFill backgroundFill = new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#74ebd5")),
                        new Stop(1, Color.web("#9face6"))
                ),
                CornerRadii.EMPTY, Insets.EMPTY
        );
        setBackground(new Background(backgroundFill));

        // Tytuł gry
        Text title = new Text("Tic Tac Toe");
        title.setFont(Font.font("Verdana", 48));
        title.setFill(Color.WHITE);
        title.setEffect(new DropShadow(10, Color.DARKSLATEBLUE));

        // Przycisk Start
        Button startButton = new Button("Start Game");
        startButton.setFont(Font.font("Arial", 24));
        startButton.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #333; -fx-background-radius: 20;");
        startButton.setEffect(new DropShadow());

        startButton.setOnAction(e -> {
            GameBoard gameBoard = new GameBoard();
            Scene gameScene = new Scene(gameBoard, 600, 600);
            stage.setScene(gameScene);
        });

        // Kontener centralny
        VBox menuBox = new VBox(30, title, startButton);
        menuBox.setAlignment(Pos.CENTER);
        getChildren().add(menuBox);
    }
}