/**
 * Project 2 - Logic Puzzles Game
 *
 * PuzzleSolver class handles most of the logic of the puzzle game when it comes to verifying answers.
 */

import java.util.ArrayList;

public class PuzzleSolver {
    private String[][][] userGrids;  //user guess
    private String[][][] correctGrids;  //correct answer
    private ArrayList<String> categories; //each category's specific name
    private ArrayList<ArrayList<String>> categoryItems; //items unders each specific category

    public PuzzleSolver(PuzzleData data) { //initializes the solver with correct answers
        int size = 4;
        userGrids = new String[3][size][size];
        correctGrids = new String[3][size][size];
        categories = data.categories;
        categoryItems = data.categoryItems;

        //converting the right answers from input data into the correctGrids
        for (String[] answer : data.correctAnswers) {

            //categories
            String name = answer[0]; //like the presenters we had mentioned
            String topic = answer[1]; //the topics
            String number = answer[2]; //the days

            //index positon of each answer
            int nameIndex = categoryItems.get(0).indexOf(name);
            int topicIndex = categoryItems.get(2).indexOf(topic);
            int numberIndex = categoryItems.get(1).indexOf(number);

            correctGrids[0][numberIndex][nameIndex] = "O"; //grid 1 is number vs. name

            correctGrids[1][nameIndex][topicIndex] = "O"; //grid 2 is name vs. topic

            correctGrids[2][topicIndex][numberIndex] = "O"; //grid 3 is topic vs. number
        }
    }

    //tracks user's guess for a specific grid cell
    public void setUserAnswer(int grid, int row, int col, String answer) {
        if (grid >= 0 && grid < 3 && row >= 0 && row < 4 && col >= 0 && col < 4) {
            userGrids[grid][row][col] = answer;
        }
    }

    //checks if their specific answer is correct
    public boolean isAnswerCorrect(int grid, int row, int col) {
        String userAnswer = userGrids[grid][row][col];
        String correctAnswer = correctGrids[grid][row][col];

        if (correctAnswer != null && "O".equals(userAnswer)) {
            return true; //correct answer placed
        } else if (correctAnswer == null && (userAnswer == null || !"O".equals(userAnswer))) {
            return true; //empty where there's no answer
        }
        return false; //incorrect answer
    }

    //verifys the whole grid for the right answer, using isAnswerCorrect
    public boolean checkEntirePuzzle() {
        for (int grid = 0; grid < 3; grid++) {
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    if (!isAnswerCorrect(grid, row, col)) {
                        return false;
                    }
                }
            }
        }
        return true; //correct
    }

    //hint button, when it is clicked, one correct answer is revealed
    public boolean revealOneCorrectAnswer() {
        for (int grid = 0; grid < 3; grid++) {
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    String correctValue = correctGrids[grid][row][col];
                    String userValue = userGrids[grid][row][col];

                    if (correctValue != null && !"O".equals(userValue)) { // if the correct answer is not yet set by user
                        //place 'O' in that grid
                        userGrids[grid][row][col] = "O";
                        return true;
                    }
                }
            }
        }
        return false; //no more hints available - all were given
    }

    //start over button, when it is clicked, all grid spaces become null (empty)
    public void resetUserAnswers() {
        for (int grid = 0; grid < 3; grid++) {
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    userGrids[grid][row][col] = null; //clear
                }
            }
        }
    }

    //returns the right answer for that grid
    public String getCorrectAnswer(int grid, int row, int col) {
        return correctGrids[grid][row][col];
    }

    //returns user's current answer in a grid cell (wathever it is)
    public String getUserAnswer(int grid, int row, int col) {
        return userGrids[grid][row][col];
    }
}
