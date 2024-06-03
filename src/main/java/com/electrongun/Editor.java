package com.electrongun;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * This allows fast iteration of levels, preventing needing to
 * manually adjust values then re-run the program.
 */
public class Editor extends Application {
    @Override
    public void start(Stage stage) {
        System.out.println("Launching the editor.");

        Group ROOT = new Group();
        Scene scene = new Scene(ROOT, 800, 500);

        Button runGameButton = new Button("Run Game");
        runGameButton.setOnAction(e-> {
            GameApplication app = new GameApplication();
            app.start(stage);
        });
        ROOT.getChildren().add(runGameButton);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void run() {
        launch();
    }

    public static void main(String[] args) {
        Main.main(args);
    }
}