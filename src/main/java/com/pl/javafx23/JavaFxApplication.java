package com.pl.javafx23;

import com.pl.javafx23.game.Game;
import com.pl.javafx23.gameboard.GameBoard;
import com.pl.javafx23.logic.GameLogic;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class JavaFxApplication extends Application {

    private Stage primaryStage;
    private Scene menuScene;
    private TextField player1Field;
    private TextField player2Field;
    private ComboBox<String> difficultyBox;
    private ToggleButton pvcButton;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setupMainMenu();
        primaryStage.show();
    }

    private void setupMainMenu() {
        // Main container with background
        StackPane root = new StackPane();
        root.setBackground(new Background(new BackgroundFill(
                createGradient(), CornerRadii.EMPTY, Insets.EMPTY)));

        // Glass pane effect
        VBox glassPane = new VBox(20);
        glassPane.setAlignment(Pos.CENTER);
        glassPane.setPadding(new Insets(30));
        glassPane.setMaxWidth(400);
        glassPane.setBackground(new Background(new BackgroundFill(
                Color.rgb(255, 255, 255, 0.2), new CornerRadii(15), Insets.EMPTY)));
        glassPane.setBorder(new Border(new BorderStroke(
                Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(15), new BorderWidths(2))));
        glassPane.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.3)));

        // Game title
        Label title = new Label("TIC TAC TOE");
        title.setFont(Font.font("Orbitron", FontWeight.BOLD, 36));
        title.setTextFill(Color.WHITE);
        title.setEffect(new DropShadow(10, Color.BLACK));

        // Game mode selection
        VBox modeSelection = createModeSelection();

        // Player name inputs
        GridPane playerInputs = createPlayerInputs();

        // Start button with animation
        Button startButton = createStartButton();

        // Add all components to glass pane
        glassPane.getChildren().addAll(title, modeSelection, playerInputs, startButton);
        root.getChildren().add(glassPane);

        menuScene = new Scene(root, 600, 600);
        menuScene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());

        // Set application icon
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/tic-tac-toe.png")));
        primaryStage.setTitle("Ultimate Tic Tac Toe");
        primaryStage.setScene(menuScene);
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(500);
    }

    private VBox createModeSelection() {
        Label modeLabel = new Label("SELECT GAME MODE:");
        modeLabel.setFont(Font.font("Orbitron", FontWeight.BOLD, 14));
        modeLabel.setTextFill(Color.WHITE);

        ToggleGroup modeToggle = new ToggleGroup();

        pvcButton = new ToggleButton("Player vs Computer");
        pvcButton.setToggleGroup(modeToggle);
        pvcButton.setSelected(true);
        pvcButton.setFont(Font.font(14));
        pvcButton.setTextFill(Color.WHITE);
        pvcButton.setStyle("-fx-text-fill: white;");

        ToggleButton pvpButton = new ToggleButton("Player vs Player");
        pvpButton.setToggleGroup(modeToggle);
        pvpButton.setFont(Font.font(14));
        pvpButton.setTextFill(Color.WHITE);
        pvpButton.setStyle("-fx-text-fill: white;");

        difficultyBox = createDifficultyComboBox();

        VBox modeSelection = new VBox(10, modeLabel, pvpButton, pvcButton, difficultyBox);
        modeSelection.setAlignment(Pos.CENTER_LEFT);
        return modeSelection;
    }

    private ComboBox<String> createDifficultyComboBox() {
        ComboBox<String> difficultyBox = new ComboBox<>();
        difficultyBox.getItems().addAll("Easy", "Medium", "Hard");
        difficultyBox.setValue("Easy");
        difficultyBox.setStyle("-fx-font: 14px 'Orbitron'; -fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white;");
        difficultyBox.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setFont(Font.font("Orbitron", 14));
                setTextFill(Color.WHITE);
                setBackground(new Background(new BackgroundFill(
                        Color.rgb(50, 50, 80), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        });
        return difficultyBox;
    }

    private GridPane createPlayerInputs() {
        GridPane playerInputs = new GridPane();
        playerInputs.setHgap(10);
        playerInputs.setVgap(10);
        playerInputs.setAlignment(Pos.CENTER);

        Label player1Label = new Label("PLAYER 1:");
        player1Label.setFont(Font.font("Orbitron", FontWeight.BOLD, 14));
        player1Label.setTextFill(Color.WHITE);

        player1Field = new TextField("Player 1");
        player1Field.setFont(Font.font(14));
        player1Field.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; -fx-prompt-text-fill: #aaa;");
        player1Field.setMaxWidth(200);

        Label player2Label = new Label("PLAYER 2:");
        player2Label.setFont(Font.font("Orbitron", FontWeight.BOLD, 14));
        player2Label.setTextFill(Color.WHITE);

        player2Field = new TextField("Player 2");
        player2Field.setFont(Font.font(14));
        player2Field.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; -fx-prompt-text-fill: #aaa;");
        player2Field.setMaxWidth(200);

        playerInputs.add(player1Label, 0, 0);
        playerInputs.add(player1Field, 1, 0);
        playerInputs.add(player2Label, 0, 1);
        playerInputs.add(player2Field, 1, 1);

        return playerInputs;
    }

    private Button createStartButton() {
        Button startButton = new Button("START GAME");
        startButton.setFont(Font.font("Orbitron", FontWeight.BOLD, 18));
        startButton.setStyle("-fx-background-color: linear-gradient(to bottom, #4CAF50, #2E8B57); -fx-text-fill: white;");
        startButton.setEffect(new DropShadow(5, Color.BLACK));
        startButton.setPadding(new Insets(10, 30, 10, 30));

        // Hover effect
        startButton.setOnMouseEntered(e -> {
            startButton.setStyle("-fx-background-color: linear-gradient(to bottom, #66BB6A, #388E3C); -fx-text-fill: white;");
            startButton.setEffect(new DropShadow(10, Color.BLACK));
        });

        startButton.setOnMouseExited(e -> {
            startButton.setStyle("-fx-background-color: linear-gradient(to bottom, #4CAF50, #2E8B57); -fx-text-fill: white;");
            startButton.setEffect(new DropShadow(5, Color.BLACK));
        });

        startButton.setOnAction(e -> {
            boolean playerVsComputer = pvcButton.isSelected();
            String player1Name = player1Field.getText();
            String player2Name = player2Field.getText();
            int difficulty = difficultyBox.getSelectionModel().getSelectedIndex() + 1;

            startGame(playerVsComputer, player1Name, player2Name, difficulty);
        });

        return startButton;
    }

    private void startGame(boolean playerVsComputer, String player1Name,
                           String player2Name, int difficulty) {
        GameLogic gameLogic = new GameLogic();
        GameBoard gameBoard = new GameBoard(gameLogic, playerVsComputer, difficulty);

        Game game = new Game(gameBoard, primaryStage, playerVsComputer,
                player1Name, player2Name, difficulty);

        StackPane gamePane = new StackPane();
        gamePane.getChildren().add(gameBoard);
        Scene gameScene = new Scene(gamePane, 600, 600);
        gameScene.getStylesheets().add(getClass().getResource("/styles/game.css").toExternalForm());

        // Fade transition
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), gamePane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        primaryStage.setScene(gameScene);
        fadeIn.play();
    }

    public void returnToMainMenu() {
        primaryStage.setScene(menuScene);
    }

    private LinearGradient createGradient() {
        Stop[] stops = new Stop[] {
                new Stop(0, Color.rgb(30, 30, 60)),
                new Stop(1, Color.rgb(10, 10, 30))
        };
        return new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
    }

    public static void main(String[] args) {
        launch(args);
    }
}