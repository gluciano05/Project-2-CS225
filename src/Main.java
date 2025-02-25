import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static ArrayList<File> unplayedFiles = new ArrayList<>(); //tracks unplayed games. Helpful since we decided to pick a random data file from the many inside the folder.

    public static void main(String[] args) {
        startNewGame();
    }

    public static void startNewGame() { //initializes the game, loading the puzzle data and launching the GUI
        try {
            PuzzleData data = new PuzzleData(unplayedFiles);
            PuzzleSolver solver = new PuzzleSolver(data);
            new PuzzleGUI(data, solver);
        } catch (IOException e) {
            System.out.println("Something went wrong with the loading of files:" + e.getMessage());
        }
    }

    //resets unplayed files once all puzzles have been played, so that we don't run into an error once we have played all files.
    public static void resetUnplayedFiles(ArrayList<File> allFiles) {
        unplayedFiles.clear();
        unplayedFiles.addAll(allFiles);
    }
}



