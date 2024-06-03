package com.electrongun;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * This is where the actual game loop will take place
 */
public class GameApplication extends javafx.application.Application {

    @Override
    public void start(Stage stage) {
        System.out.println("Running the game.");

        Group ROOT = new Group();
        Scene scene = new Scene(ROOT, 800, 500);

        Button editorButton = new Button("Return to Editor");
        editorButton.setOnAction(e-> {
            Editor editor = new Editor();
            editor.start(stage);
        });
        ROOT.getChildren().add(editorButton);


        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        Main.main(args);
    }
}
