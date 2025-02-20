//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    //testing if the categories in the input file are being read correctly - Bruna
    public static void main(String[] args) throws IOException {
        String filePath = "resources/dataFiles/gameData1.txt";

        FileHandler fileHandler = new FileHandler();
        fileHandler.loadPuzzleData(filePath);

        ArrayList<String> presenters = fileHandler.getPresenters();
        ArrayList<String> days = fileHandler.getDays();
        ArrayList<String> topics = fileHandler.getTopics();

        System.out.println("Presenters: " + presenters);
        System.out.println("Days: " + days);
        System.out.println("Topics: " + topics);

//THIS IS A TEST COMMENT - GABE

    }
}