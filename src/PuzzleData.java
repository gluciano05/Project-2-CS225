/*
 * Logic Puzzle Game - CS225 Project 2
 *
 * PuzzleData class is responsible for loading the game data from the data files given.
 *
 * (I have played the game multiple times to gather its specific data and relationships
 * and transfer those into our data files, so that we could focus on the code and log
 * of the game, rather than spending time creating new data stories from imagination - Bruna)
 *
 * Data is organized into:
 * - categories
 * - items in each category
 * - clues
 * - story line
 * - correct answers
 *
 * We have implemented the idea of a folder that would contain the puzzle data files,
 * and as a new game starts, the program chooses a random file to start. Removing it
 * from the unplayed files array list (for new games always until all files have been
 * played, then that list resets). - "can be easily extended to offer more puzzles", as
 * required.
 *
 * Bruna and Gabriel
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.util.Random;

public class PuzzleData {

    public ArrayList<String> categories = new ArrayList<>(); //category's name
    public ArrayList<ArrayList<String>> categoryItems = new ArrayList<>(); //items in each category
    public ArrayList<String> clues = new ArrayList<>(); //list of clues given
    public String story = ""; //'story' sentence given
    public ArrayList<String[]> correctAnswers = new ArrayList<>(); //right answers as a list of arrays

    private static ArrayList<File> allFiles = new ArrayList<>(); //keeps track of all files loaded from the folder
    private static ArrayList<File> unplayedFiles = new ArrayList<>();//tracks unplayed files

    /**
     * Initializes a new puzzle by loading a random data file from unplayed files.
     * If all files have been played, it resets the unplayed files list.
     * - Gabriel and Bruna
     */
    public PuzzleData() throws IOException {
        File folder = new File("./Resources"); //the folder where all files exist
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files != null && files.length > 0) { //adds all .txt files to the list
            if (allFiles.isEmpty()) {
                for (File file : files) {
                    allFiles.add(file);
                }
            }

            //reset if all puzzles have been played
            if (unplayedFiles.isEmpty()) {
                resetUnplayedFiles(allFiles);
            }

            //selects random from not yet played
            File selectedFile = getRandomUnplayedFile(unplayedFiles);
            loadPuzzleData(selectedFile.getAbsolutePath());
        } else {
            throw new IOException("No data files found."); //if errors occur
        }
    }

    /**
     * getRandomUnplayedFile() selects random file from the list of unplayed files
     * - Bruna
     */
    private File getRandomUnplayedFile(ArrayList<File> unplayedFiles) {
        Random random = new Random();
        int randomIndex = random.nextInt(unplayedFiles.size());
        File selectedFile = unplayedFiles.get(randomIndex);
        unplayedFiles.remove(randomIndex); //removes file from the unplayed list
        return selectedFile;
    }

    /**
     * resetUnplayedFiles() does as described. Once all files from the game data folder have been
     * played, it resets the array list with all files for not running the risk of being unable to play.
     * - Bruna
     */
    public static void resetUnplayedFiles(ArrayList<File> allFiles) {
        unplayedFiles.clear(); //removes all file items to avoid repetition
        unplayedFiles.addAll(allFiles); //adds all files back in the array list
    }

    /**
     * loadPuzzleData() loads puzzle data from the given file path
     * - Gabriel and Bruna
     */
    private void loadPuzzleData(String filePath) throws IOException {

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
                    case "# clues": //takes what's in there for clues
                        clues.add(line);
                        break;
                    case "# story": //takes story sentence
                        story += line + "\n"; //each line
                        break;
                    case "# correct answers": //takes the correct answer's relationships
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
