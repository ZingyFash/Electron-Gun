package com.electrongun;

import javafx.scene.Group;
import javafx.scene.layout.BorderPane;

import java.util.concurrent.atomic.AtomicBoolean;

public class LevelData {
    /**
     * Loads the level data from the LevelData.txt file which contains information
     * about the positions and nature of all the objects in each level.
     */
    public static void LoadLevelData() {
        System.out.println("Loading the saved level data.");
    }

    public static void main(String[] args) {
        Main.main(args);
    }

    /**
     * Just a placeholder for now
     *
     * @param levelRoot the root ... of the level
     */
    public static void saveLevel(BorderPane levelRoot) {
        levelRoot.getChildren().forEach(l -> {
            try {
                AtomicBoolean skip = new AtomicBoolean(true);
                ((Group) l).getChildren().forEach(n -> {
                    if (skip.get()) {
                        skip.set(false); return;
                    }
                    System.out.println(n.getTranslateX());
                });
            } catch (Exception e) {
                System.out.print("");
            }
        });
    }
}
