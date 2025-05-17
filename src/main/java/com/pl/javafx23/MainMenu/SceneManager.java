package com.pl.javafx23.MainMenu;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SceneManager {
    private final Stage primaryStage; // Główne okno aplikacji
    private Scene menuScene;          // Scena głównego menu
    private Scene gameScene;          // Scena gry

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage; // Przypisanie głównego etapu
    }

    /**
     * Wyświetla scenę głównego menu.
     */
    public void showMenuScene() {
        if (menuScene == null) {
            menuScene = createMenuScene(); // Tworzenie sceny menu, jeśli jeszcze nie istnieje
        }
        primaryStage.setScene(menuScene); // Ustawienie sceny w głównym etapie
    }

    /**
     * Wyświetla scenę gry.
     */
    public void showGameScene() {
        if (gameScene == null) {
            gameScene = createGameScene(); // Tworzenie sceny gry, jeśli jeszcze nie istnieje
        }
        primaryStage.setScene(gameScene); // Ustawienie sceny w głównym etapie
    }

    /**
     * Tworzy scenę głównego menu.
     * @return Scena głównego menu.
     */
    private Scene createMenuScene() {
        VBox menuLayout = new VBox(20); // Layout w formie pionowej z odstępami między elementami
        menuLayout.setPadding(new Insets(30)); // Margines wokół elementów
        menuLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #ACB6E5, #86FDE8);");

        Label title = new Label("Main Menu");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button startGameButton = new Button("Start Game");
        startGameButton.setOnAction(e -> showGameScene()); // Przejście do sceny gry

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> primaryStage.close()); // Zamknięcie aplikacji

        menuLayout.getChildren().addAll(title, startGameButton, exitButton); // Dodanie elementów do layoutu
        return new Scene(menuLayout, 400, 300); // Stworzenie sceny o wymiarach 400x300
    }

    /**
     * Tworzy scenę gry.
     * @return Scena gry.
     */
    private Scene createGameScene() {
        StackPane gameLayout = new StackPane(); // Layout w formie stosu (elementy na sobie)
        gameLayout.setStyle("-fx-background-color: linear-gradient(to top, #283c86, #45a247);");

        Label gameLabel = new Label("Game Scene");
        gameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        Button backToMenuButton = new Button("Back to Menu");
        backToMenuButton.setOnAction(e -> showMenuScene()); // Powrót do menu

        gameLayout.getChildren().addAll(gameLabel, backToMenuButton); // Dodanie elementów do layoutu
        return new Scene(gameLayout, 600, 400); // Stworzenie sceny o wymiarach 600x400
    }
}