/**
 *  Project 2 - Logic Puzzles Game
 *  Bruna A, Daniel W, Gabriel L.
 *
 *  FileHandler class
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileHandler {

    private Map<String, List<String>> categoryData;

    public FileHandler() {
        categoryData = new HashMap<>();
    }

    public void loadPuzzleData(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath)); //reads the file
        parseCategories(lines);
    }

    private void parseCategories(List<String> lines) { //parses from input file
        for (String line : lines) {
            line = line.trim();

            //if the line contains ": " then it's a category
            if (line.contains(": ")) {
                String[] parts = line.split(": ");
                if (parts.length == 2) {
                    String category = parts[0].trim();
                    List<String> values = Arrays.asList(parts[1].split(", "));
                    categoryData.put(category, values);
                }
            }
        }
    }

    public List<String> getCategoryData(String category) { //list for the specific category
        return categoryData.getOrDefault(category, new ArrayList<>());
    }
}
