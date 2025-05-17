package com.pl.javafx23.MainMenu;

import com.pl.javafx23.game.Game;
import com.pl.javafx23.logic.GameLogic;
import com.pl.javafx23.gameboard.GameBoard;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;



public class MainMenu extends VBox {
    private final Stage stage;
    private ComboBox<String> difficultyBox;
    private TextField player1Field;
    private TextField player2Field;

    public MainMenu(Stage stage) {
        this.stage = stage;
        setupMenu();
    }

    private void setupMenu() {
        // Main container styling
        setSpacing(20);
        setAlignment(Pos.CENTER);
        setBackground(new Background(new BackgroundFill(
                Color.web("#ACB6E5"), CornerRadii.EMPTY, Insets.EMPTY)));
        setPadding(new Insets(40));
        setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.3)));

        // Game title with icon
        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER);

        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/images/tic-tac-toe.png")));
        icon.setFitHeight(40);
        icon.setFitWidth(40);

        Label title = new Label("Tic Tac Toe");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setTextFill(Color.web("#084177"));

        titleBox.getChildren().addAll(icon, title);

        // Player inputs
        GridPane playerInputs = new GridPane();
        playerInputs.setHgap(10);
        playerInputs.setVgap(10);
        playerInputs.setAlignment(Pos.CENTER);

        Label player1Label = new Label("Player 1:");
        player1Label.setFont(Font.font(14));
        player1Label.setTextFill(Color.web("#084177"));

        player1Field = new TextField("Player 1");
        player1Field.setFont(Font.font(14));
        player1Field.setStyle("-fx-background-color: rgba(255,255,255,0.7);");
        player1Field.setMaxWidth(200);

        Label player2Label = new Label("Player 2:");
        player2Label.setFont(Font.font(14));
        player2Label.setTextFill(Color.web("#084177"));

        player2Field = new TextField("Player 2");
        player2Field.setFont(Font.font(14));
        player2Field.setStyle("-fx-background-color: rgba(255,255,255,0.7);");
        player2Field.setMaxWidth(200);

        playerInputs.add(player1Label, 0, 0);
        playerInputs.add(player1Field, 1, 0);
        playerInputs.add(player2Label, 0, 1);
        playerInputs.add(player2Field, 1, 1);

        // Difficulty selection (only visible in vs Computer mode)
        difficultyBox = new ComboBox<>();
        difficultyBox.getItems().addAll("Easy", "Medium", "Hard");
        difficultyBox.setValue("Easy");
        difficultyBox.setVisible(false);
        difficultyBox.setStyle("-fx-font-size: 14; -fx-background-color: rgba(255,255,255,0.7);");

        // Game mode buttons
        ToggleGroup gameModeGroup = new ToggleGroup();

        RadioButton pvpButton = createModeButton("Player vs Player", gameModeGroup);
        pvpButton.setSelected(true);
        pvpButton.setOnAction(e -> {
            difficultyBox.setVisible(false);
            player2Field.setText("Player 2");
            player2Field.setDisable(false);
        });

        RadioButton pvcButton = createModeButton("Player vs Computer", gameModeGroup);
        pvcButton.setOnAction(e -> {
            difficultyBox.setVisible(true);
            player2Field.setText("Computer");
            player2Field.setDisable(true);
        });

        VBox modeSelection = new VBox(10,
                new Label("Select Game Mode:"),
                pvpButton,
                pvcButton,
                difficultyBox
        );
        modeSelection.setAlignment(Pos.CENTER_LEFT);

        // Start game button
        Button startButton = new Button("START GAME");
        startButton.setPrefSize(200, 50);
        startButton.setStyle("-fx-font-size: 18; -fx-background-color: #84DCC6; -fx-text-fill: #084177; -fx-background-radius: 10;");
        startButton.setOnAction(e -> startGame(pvcButton.isSelected()));

        // Exit button
        Button exitButton = new Button("EXIT");
        exitButton.setPrefSize(200, 50);
        exitButton.setStyle("-fx-font-size: 18; -fx-background-color: #FF6161; -fx-text-fill: white; -fx-background-radius: 10;");
        exitButton.setOnAction(e -> stage.close());

        // Add all components to menu
        getChildren().addAll(
                titleBox,
                playerInputs,
                modeSelection,
                startButton,
                exitButton
        );
    }

    private RadioButton createModeButton(String text, ToggleGroup group) {
        RadioButton button = new RadioButton(text);
        button.setToggleGroup(group);
        button.setFont(Font.font(14));
        button.setTextFill(Color.web("#084177"));
        button.setStyle("-fx-background-color: transparent;");
        return button;
    }

    private void startGame(boolean vsComputer) {
        String player1Name = player1Field.getText().isEmpty() ? "Player 1" : player1Field.getText();
        String player2Name = player2Field.getText().isEmpty() ?
                (vsComputer ? "Computer" : "Player 2") :
                player2Field.getText();

        int difficultyLevel = difficultyBox.getSelectionModel().getSelectedIndex() + 1;

        // Create game components
        GameLogic gameLogic = new GameLogic();
        GameBoard gameBoard = new GameBoard(gameLogic, vsComputer, difficultyLevel);
        Game game = new Game(gameBoard, stage, vsComputer, player1Name, player2Name, difficultyLevel);

        // Animate transition to game
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), this);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> game.startGame());
        fadeOut.play();
    }
}