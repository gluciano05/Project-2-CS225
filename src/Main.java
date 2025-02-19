//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.IOException;
import java.util.List;

public class Main {

    //testing if the categories in the input file are being read correctly - Bruna
    public static void main(String[] args) throws IOException {
        FileHandler fileHandler = new FileHandler();
        fileHandler.loadPuzzleData("resources/gameData1.txt");

        List<String> days = fileHandler.getCategoryData("Days");
        List<String> presenters = fileHandler.getCategoryData("Presenters");
        List<String> topics = fileHandler.getCategoryData("Topics");

        System.out.println("Days: " + days);
        System.out.println("Presenters: " + presenters);
        System.out.println("Topics: " + topics);

//THIS IS A TEST COMMENT - GABE

    }
}