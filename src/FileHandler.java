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

public class FileHandler { //Bruna 02/20
    private ArrayList<String> presenters;
    private ArrayList<String> days;
    private ArrayList<String> topics;

    public FileHandler() {
        presenters = new ArrayList<>();
        days = new ArrayList<>();
        topics = new ArrayList<>();
    }

    public void loadPuzzleData(String filePath) throws IOException {
        for (String line : Files.readAllLines(Paths.get(filePath))) {
            line = line.trim();

            if (line.startsWith("Presenters:")) {
                addToList(presenters, line.substring(11)); // Store values in ArrayList
            } else if (line.startsWith("Days:")) {
                addToList(days, line.substring(6));
            } else if (line.startsWith("Topics:")) {
                addToList(topics, line.substring(8));
            }
        }
    }

    private void addToList(ArrayList<String> list, String data) {
        for (String value : data.split(", ")) {
            list.add(value.trim());
        }
    }

    public ArrayList<String> getPresenters() {
        return presenters;
    }
    public ArrayList<String> getDays() {
        return days;
    }
    public ArrayList<String> getTopics() {
        return topics;
    }
}
