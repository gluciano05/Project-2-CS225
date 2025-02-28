/*
 * Logic Puzzle Game - CS225 Project 2
 *
 * Main class that creates a new game by initializing the PuzzleData
 * class (deals with the input data) and the PuzzleSolver class (game
 * logic) and passes it into the PuzzleGUI class' constructor to display
 * the GUI.
 *
 * Daniel
 */

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        startNewGame();
    }

    /**
     * startNewGame() starts a new game by loading a data file from the list of unplayed
     * puzzles, and launches the solver logic as well as the GUI.
     * It catches any errors that may occur during the loading of the data files.
     * - Daniel
     */
    public static void startNewGame() {
        try {
            PuzzleData data = new PuzzleData();
            PuzzleSolver solver = new PuzzleSolver(data);
            new PuzzleGUI(data, solver);
        } catch (IOException e) {
            System.out.println("Something went wrong while loading the files:" + e.getMessage());
        }
    }
}



