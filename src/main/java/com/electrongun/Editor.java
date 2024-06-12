package com.electrongun;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.awt.*;
import java.util.*;

/**
 * This allows fast iteration of levels, preventing needing to
 * manually adjust values then re-run the program.
 */
public class Editor extends Application {

    // The current editable node
    private static Node focusedNode = null;
    private static BorderPane EDITOR_ROOT;

    /**
     * Sets up the stage
     *
     * @param stage - default stage given
     */
    @Override
    public void start(Stage stage) {
        System.out.println("Launching the editor.");

        // Lots of this should be abstracted into its own functions but that's a problem for another day

        // Finds the dimensions of the screen, so it will always be fullscreen
        // This might cause problems as the size of the level editor is hard-coded
        // I will make this more sophisticated at some point maybe.
        Dimension screenDimensions = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

        EDITOR_ROOT = new BorderPane();
        Scene scene = new Scene(EDITOR_ROOT, screenDimensions.getWidth(), screenDimensions.getHeight());

        // topBox will have buttons relating to the whole application
        HBox topBox = new HBox();
        topBox.setSpacing(50);
        // bottomBox will have something maybe
        HBox bottomBox = new HBox();
        // leftBox will have level information
        VBox leftBox = new VBox();
        // leftBox will have editing functionality
        VBox rightBox = new VBox();
        rightBox.setSpacing(10);
        // LEVEL_ROOT will hold the visuals for the current level
        Group LEVEL_ROOT = new Group();
        EDITOR_ROOT.setTop(topBox);
        EDITOR_ROOT.setBottom(bottomBox);
        EDITOR_ROOT.setLeft(leftBox);
        EDITOR_ROOT.setRight(rightBox);
        EDITOR_ROOT.setCenter(LEVEL_ROOT);

        // This calls an update function every .01s
        // This shouldn't be in this class, but I don't know what to do with it, yet
        // I gave up and couldn't find another way to get the rectangles to update if the text fields change
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(Editor::update);
            }
        }, 0, 10);

        // Hands control over to GameApplication
        Button runGameButton = new Button("Run Game");
        runGameButton.setOnAction(e -> {
            GameApplication app = new GameApplication();
            app.start(stage);
        });
        // Closes the entire program
        Button closeButton = new Button("Close Editor");
        closeButton.setOnAction(e -> {
            timer.cancel();
            System.exit(0);
        });

        // Text fields for adjusting the x position, y position, width, height and colour of focusedNode respectively
        TextField x = new TextField();
        TextField y = new TextField();
        TextField w = new TextField();
        TextField h = new TextField();
        TextField c = new TextField();

        // Adds a new rectangle to the scene
        Button addRectButton = getAddRectButton();
        topBox.getChildren().addAll(runGameButton, closeButton);

        // Saves the current level
        Button saveLevelButton = new Button("Save Level");
        saveLevelButton.setOnAction(e -> LevelData.saveLevel(EDITOR_ROOT));

        // "Fuck text fields"
        rightBox.getChildren().addAll(addRectButton, x, y, w, h, c, new Button("Fuck text fields"), saveLevelButton);

        // This is the background for the level, makes sure it is a minimum size in the scene
        Rectangle background = new Rectangle(0, 0, 1200, 700);
        background.setFill(Paint.valueOf("#aaaaaa"));
        LEVEL_ROOT.getChildren().add(background);

        EventHandler<KeyEvent> keyPressedEventHandler = Editor::move;
        scene.addEventHandler(KeyEvent.KEY_PRESSED, keyPressedEventHandler);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Moves the focusedNode 10 pixels in the given direction
     *
     * @param keyEvent holds information about the key pressed
     */
    private static void move(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case W:
                focusedNode.setTranslateY(focusedNode.getTranslateY()-10);
                ((TextField) ((VBox) EDITOR_ROOT.getRight()).getChildren().get(2)).setText(String.valueOf((focusedNode).getTranslateY()));
                break;
            case A:
                focusedNode.setTranslateX(focusedNode.getTranslateX()-10);
                ((TextField) ((VBox) EDITOR_ROOT.getRight()).getChildren().get(1)).setText(String.valueOf((focusedNode).getTranslateX()));
                break;
            case S:
                focusedNode.setTranslateY(focusedNode.getTranslateY()+10);
                ((TextField) ((VBox) EDITOR_ROOT.getRight()).getChildren().get(2)).setText(String.valueOf((focusedNode).getTranslateY()));
                break;
            case D:
                focusedNode.setTranslateX(focusedNode.getTranslateX()+10);
                ((TextField) ((VBox) EDITOR_ROOT.getRight()).getChildren().get(1)).setText(String.valueOf((focusedNode).getTranslateX()));
                break;
        }
    }

    /**
     * Runs once every frame to periodically check events or whatnot
     */
    private static void update() {
        // Updates the focusedNode based on the text fields
        // they are sometimes empty, so I added a few catches and ignored it
        try {
            // This is a nightmare to read
            // have fun :)
            focusedNode.setTranslateX(Double.parseDouble(((TextField) ((VBox) EDITOR_ROOT.getRight()).getChildren().get(1)).getText()));
            focusedNode.setTranslateY(Double.parseDouble(((TextField) ((VBox) EDITOR_ROOT.getRight()).getChildren().get(2)).getText()));
            ((Rectangle) focusedNode).setWidth(Double.parseDouble(((TextField) ((VBox) EDITOR_ROOT.getRight()).getChildren().get(3)).getText()));
            ((Rectangle) focusedNode).setHeight(Double.parseDouble(((TextField) ((VBox) EDITOR_ROOT.getRight()).getChildren().get(4)).getText()));
            StringBuilder colourString = new StringBuilder(((TextField) ((VBox) EDITOR_ROOT.getRight()).getChildren().get(5)).getText());
            colourString.append("0".repeat(Math.max(0, 6 - colourString.length())));
            ((Rectangle) focusedNode).setFill(Paint.valueOf("#" + colourString));
        } catch (NullPointerException | NumberFormatException e) {
            System.out.print("");
        }
    }

    /**
     * Creates the add rectangle button and implements its functionality
     *
     * @return Button addRectButton
     */
    private static Button getAddRectButton() {
        // See, I can make things their own function
        // I am a good programmer

        Button addRectButton = new Button("Add rectangle");
        addRectButton.setOnAction(e -> {
            // randomly places the rectangle in the level for some extra fun
            Random random = new Random();
            double x = random.nextDouble(900) + 100;
            double y = random.nextDouble(400) + 100;
            double w = random.nextDouble(50) + 50;
            double h = random.nextDouble(50) + 50;

            // generates a random colour - ignore 16777215
            StringBuilder colourString = new StringBuilder(Integer.toHexString(random.nextInt(16777215)));
            for (int i = 0; i < 6 - colourString.length(); i++) {
                colourString.insert(0, "0");
            }

            // everything works by translate x and y not the direct x and y
            Rectangle rect = new Rectangle(0, 0, w, h);
            rect.setTranslateX(rect.getX());
            rect.setTranslateY(rect.getY());
            rect.setFill(Paint.valueOf("#" + colourString));
            ((Group)EDITOR_ROOT.getCenter()).getChildren().add(rect);
            focusedNode = rect;
            ((TextField) ((VBox) ((BorderPane) EDITOR_ROOT.getCenter().getParent()).getRight()).getChildren().get(1)).setText(x + "");
            ((TextField) ((VBox) ((BorderPane) EDITOR_ROOT.getCenter().getParent()).getRight()).getChildren().get(2)).setText(y + "");
            ((TextField) ((VBox) ((BorderPane) EDITOR_ROOT.getCenter().getParent()).getRight()).getChildren().get(3)).setText(w + "");
            ((TextField) ((VBox) ((BorderPane) EDITOR_ROOT.getCenter().getParent()).getRight()).getChildren().get(4)).setText(h + "");
            ((TextField) ((VBox) ((BorderPane) EDITOR_ROOT.getCenter().getParent()).getRight()).getChildren().get(5)).setText(colourString + "");
            rect.setOnMouseClicked(event -> {
                // Sets this rectangle as the focusedNode and updates the text fields accordingly
                focusedNode = rect;
                // More casting yay
                ((TextField) ((VBox) ((BorderPane) EDITOR_ROOT.getCenter().getParent()).getRight()).getChildren().get(1)).setText(rect.getX() + "");
                ((TextField) ((VBox) ((BorderPane) EDITOR_ROOT.getCenter().getParent()).getRight()).getChildren().get(2)).setText(rect.getY() + "");
                ((TextField) ((VBox) ((BorderPane) EDITOR_ROOT.getCenter().getParent()).getRight()).getChildren().get(3)).setText(rect.getWidth() + "");
                ((TextField) ((VBox) ((BorderPane) EDITOR_ROOT.getCenter().getParent()).getRight()).getChildren().get(4)).setText(rect.getHeight() + "");
                Color colour = (Color)rect.getFill();
                String red = Integer.toHexString((int)Math.round(colour.getRed()*255));
                red += (red.length()==1)?"0":"";
                String green = Integer.toHexString((int)Math.round(colour.getGreen()*255));
                green += (green.length()==1)?"0":"";
                String blue = Integer.toHexString((int)Math.round(colour.getBlue()*255));
                blue += (blue.length()==1)?"0":"";
                ((TextField) ((VBox) ((BorderPane) EDITOR_ROOT.getCenter().getParent()).getRight()).getChildren().get(5)).setText(red+green+blue);

            });
        });
        return addRectButton;
    }

    public static void run() {
        launch();
    }

    public static void main(String[] args) {
        Main.main(args);
    }
}