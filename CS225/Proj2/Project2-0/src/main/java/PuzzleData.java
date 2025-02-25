/**
 * Project 2 - Logic Puzzles Game
 *
 * PuzzleData class acts as the file handler, taking the game data from a random given input file
 * and storing it.
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class PuzzleData {
    public ArrayList<String> categories = new ArrayList<>(); //category's name
    public ArrayList<ArrayList<String>> categoryItems = new ArrayList<>(); //items in each category
    public ArrayList<String> clues = new ArrayList<>(); //list of clues given
    public String story = ""; //'story' sentence given
    public ArrayList<String[]> correctAnswers = new ArrayList<>(); //right answers as a list of arrays

    private static ArrayList<File> allFiles = new ArrayList<>(); //keeps track of all files loaded from the folder

    public PuzzleData(ArrayList<File> unplayedFiles) throws IOException { //initializes a new puzzle with data from a random file
        File folder = new File("src/main/resources/gameDataFiles"); //the folder where all files exist
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files != null && files.length > 0) { //adds all .txt files to the list
            if (allFiles.isEmpty()) {
                for (File file : files) {
                    allFiles.add(file);
                }
            }

            //reset is all puzzels have been played
            if (unplayedFiles.isEmpty()) {
                Main.resetUnplayedFiles(allFiles);
            }

            //selects random from not yet played
            File selectedFile = getRandomUnplayedFile(unplayedFiles);
            loadPuzzleData(selectedFile.getAbsolutePath());
        } else {
            throw new IOException("No data files found."); //if errors occur
        }
    }

    private File getRandomUnplayedFile(ArrayList<File> unplayedFiles) { //selects random from the list of unplayed files
        Random random = new Random();
        int randomIndex = random.nextInt(unplayedFiles.size());
        File selectedFile = unplayedFiles.get(randomIndex);
        unplayedFiles.remove(randomIndex); //removes file from the unplayed list
        return selectedFile;
    }

    private void loadPuzzleData(String filePath) throws IOException { //loads puzzle data from that file
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        String currentSection = ""; //tracks which is being read

        //clears previous game's data (if playing again)
        categories.clear();
        categoryItems.clear();
        clues.clear();
        story = "";
        correctAnswers.clear();

        //starts to read file line by line searching for specific words
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("#")) {
                currentSection = line.toLowerCase();
            } else if (!line.isEmpty()) {
                switch (currentSection) {
                    case "# categories": //takes category name and all its items
                        String[] parts = line.split(": ");
                        if (parts.length == 2) {
                            categories.add(parts[0].trim());
                            ArrayList<String> items = new ArrayList<>();
                            String[] values = parts[1].split(", ");
                            for (String value : values) {
                                items.add(value.trim());
                            }
                            categoryItems.add(items);
                        }
                        break;
                    case "# clues": //takes whats in for clues
                        clues.add(line);
                        break;
                    case "# story": //takes story sentence
                        story += line + "\n"; //each line
                        break;
                    case "# correct answers": //takes the correct answers's relationships
                        String[] answerParts = line.split(": "); //parses and stores
                        if (answerParts.length == 2) {
                            String name = answerParts[0].trim();
                            String[] details = answerParts[1].split(", ");
                            if (details.length == 2) {
                                correctAnswers.add(new String[]{name, details[0].trim(), details[1].trim()});
                            }
                        }
                        break;
                }
            }
        }
        reader.close();
    }
}
