/*
 * Logic Puzzle Game - CS225 Project 2
 *
 * PuzzleSolver class is responsible for the game logic, including tracking and validating the user's
 * guesses and checking if the puzzle is solved correctly. It compares the user's answers with the correct answers and
 * provides functionality to give hints and reset user inputs.
 *
 * Daniel and Gabriel
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PuzzleSolver {
    private String[][][] userGrids;  //user guess
    private String[][][] correctGrids;  //correct answer
    private ArrayList<String> categories; //each category's specific name
    private ArrayList<ArrayList<String>> categoryItems; //items unders each specific category

    /**
     * Constructor initializes the solver with the correct answers and data from the PuzzleData class.
     * It prepares the correct answer grids and sets up the user grids for tracking the user's guesses.
     * - Gabriel
     */
    public PuzzleSolver(PuzzleData data) {
        int size = 4;
        userGrids = new String[3][size][size];
        correctGrids = new String[3][size][size];
        categories = data.categories;
        categoryItems = data.categoryItems;

        //converts the right answers from input data into the correctGrids
        for (String[] answer : data.correctAnswers) {

            //categories
            String name = answer[0]; //category 1
            String topic = answer[1]; //category 2
            String number = answer[2]; //category 3

            //index position of each answer
            int nameIndex = categoryItems.get(0).indexOf(name);
            int topicIndex = categoryItems.get(2).indexOf(topic);
            int numberIndex = categoryItems.get(1).indexOf(number);

            correctGrids[0][numberIndex][nameIndex] = "O"; //grid 1 is number vs. name

            correctGrids[1][nameIndex][topicIndex] = "O"; //grid 2 is name vs. topic

            correctGrids[2][topicIndex][numberIndex] = "O"; //grid 3 is topic vs. number
        }
    }

    /**
     * Sets the user's answer for a specific grid cell.
     * updates the user's guess for the selected grid, row, and column.
     * - Daniel
     */
    public void setUserAnswer(int grid, int row, int col, String answer) {
        if (grid >= 0 && grid < 3 && row >= 0 && row < 4 && col >= 0 && col < 4) {
            userGrids[grid][row][col] = answer;
        }
    }

    /**
     * Checks if the user's answer for a specific grid cell is correct through comparing.
     * - Daniel
     */
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

    /**
     * Verifies if the entire puzzle is solved correctly by checking each grid cell.
     * calls isAnswerCorrect for each cell to ensure that all answers are correct.
     * - Daniel
     */
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

    /**
     * Reveals one correct answer in the puzzle as a hint. This method will find the first unfilled cell and fill it
     * with the correct answer. It returns false if all hints have already been revealed.
     * - Gabriel
     */
    public boolean revealOneCorrectAnswer() {
        for (int grid = 0; grid < 3; grid++) {
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    String correctValue = correctGrids[grid][row][col];
                    String userValue = userGrids[grid][row][col];

                    if (correctValue != null && !"O".equals(userValue)) { //if the correct answer is not yet set by user
                        //place 'O' in that grid
                        userGrids[grid][row][col] = "O";
                        return true;
                    }
                }
            }
        }
        return false; //no more hints available - all were given
    }

    /**
     * Resets the user's answers for all grid cells. It is called when user pressed 'Start Over'
     * - Gabriel
     */
    public void resetUserAnswers() {
        for (int grid = 0; grid < 3; grid++) {
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    userGrids[grid][row][col] = null; //clear
                }
            }
        }
    }

    //returns the right answer for that grid - Daniel
    public String getCorrectAnswer(int grid, int row, int col) {
        return correctGrids[grid][row][col];
    }

    //returns user's current answer in a grid cell - Daniel
    public String getUserAnswer(int grid, int row, int col) {
        return userGrids[grid][row][col];
    }
}





