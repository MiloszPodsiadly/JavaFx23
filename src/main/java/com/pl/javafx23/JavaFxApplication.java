package com.pl.javafx23;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import com.pl.javafx23.MainMenu.MainMenu;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class JavaFxApplication extends Application {

    @Override
    public void start(Stage stage) {
        MainMenu mainMenu = new MainMenu(stage);
        Scene scene = new Scene(mainMenu, 600, 600);

        stage.setTitle("Tic Tac Toe");
        stage.setScene(scene);
        stage.show();

        // Efekt fade-in przy starcie
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), mainMenu);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    public static void main(String[] args) {
        launch();
    }
}

