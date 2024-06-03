package com.electrongun;

/**
 * This is the entry point of the program and controls the flow of the
 * whole program.
 */
public class Main {
    /**
     * Loads relevant data from external files then launches the editor.
     * From the editor you can test run the game and then return after testing.
     *
     * @param args - command line arguments
     */
    public static void main(String[] args) {
        LevelData.LoadLevelData();
        Editor.run();
    }
}
