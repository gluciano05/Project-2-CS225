import java.util.ArrayList;

public class Game {
    private int score;
    private int grid_x;
    private int grid_y;
    private ArrayList<ArrayList<Integer>> correctGuesses;

    public Game() {
        score = 0;
        grid_x = 0;
        grid_y = 0;
        correctGuesses = new ArrayList<>();
    }


    public void getXandY(int x, int y) {
        //gets x and y coordinates when the user clicks on the grid.
        grid_x = x;
        grid_y = y;
    }

    public boolean isCorrect(ArrayList<String> answers, ArrayList<String> topCategory, ArrayList<String> sideCategory) {
        boolean correct = false;
        for (int i = 0; i < answers.size(); i++) {
            if (answers.get(i).contains(topCategory.get(grid_x)) && answers.get(i).contains(sideCategory.get(grid_y))) {
                /* when the user clicks on a box in the grid, there is a coordinate attached to that box,
                 * we take the x and y coordinates and compare the category array lists to the answers array list using said coordinates.
                 * if answers at the i'th index contains the top category at the grid_x'th index
                 * and the side category at the grid_y'th index, than the guess is correct and is saved into an array list of array lists
                 */
                correct = true;
                correctGuesses.get(i).set(grid_x, grid_y);
                break;
            } else {
                //does nothing
            }
        }
        return correct;
    }
}