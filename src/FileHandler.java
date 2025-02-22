/**
 *  Project 2 - Logic Puzzles Game
 *  Bruna A, Daniel W, Gabriel L.
 *
 *  FileHandler class - using arraylists
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileHandler {
    private ArrayList<String> categories; // Store categories dynamically
    private ArrayList<ArrayList<String>> values; // Store corresponding values

    public FileHandler() {
        categories = new ArrayList<>();
        values = new ArrayList<>();
    }

    public void loadPuzzleData(String filePath) throws IOException {
        ArrayList<String> currentValues = null;

        for (String line : Files.readAllLines(Paths.get(filePath))) {
            line = line.trim();

            if (line.contains(":")) {
                String categoryName = line.split(":")[0].trim();
                categories.add(categoryName); // Add dynamic category
                currentValues = new ArrayList<>();
                values.add(currentValues);
            } else if (!line.isEmpty() && currentValues != null) {
                currentValues.add(line); // Add values dynamically
            }
        }
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public ArrayList<ArrayList<String>> getValues() {
        return values;
    }
}
