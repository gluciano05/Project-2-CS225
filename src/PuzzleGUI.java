/*
 * Logic Puzzle Game - CS225 Project 2
 *
 * PuzzleGUI class is responsible for managing the UI.
 * It includes all visual components as well as user interactions.
 *
 * Bruna, Daniel, and Gabriel.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PuzzleGUI {
    private JFrame frame;
    private JButton[][][] gridButtons; //buttons for the grids
    private PuzzleSolver solver; //puzzle logic
    private PuzzleData data; //handles data
    private JTextArea cluesArea; //displays clues
    private JTextArea notesArea; //user possible notes
    private JTextArea storyArea; //displays the 'story'
    private static JLabel timerLabel;
    private static Timer timer;
    private static long startTime;
    private long totalTimeElapsed; //tracks total elapsed time
    private long adjustedTime; //tracks adjusted time with the addition of penalties
    private static long totalElapsedTime = 0;  //stores time value
    private static long totalAdjustedTime = 0; //stores adjusted time value

    /**
     * Constructor - initializes the GUI with the data and logic.
     */
    public PuzzleGUI(PuzzleData data, PuzzleSolver solver) {
        this.solver = solver;
        this.data = data;
        showWelcomeScreen(); //calls first screen
    }

    /**
     * Displays the welcome screen with instructions, timer scores, and a play button.
     * Starts the game when the play button is clicked.
     *
     * all visuals - Bruna and Gabriel
     */
    private void showWelcomeScreen() {
        JFrame welcomeFrame = new JFrame("Logic Puzzle Game");
        welcomeFrame.setSize(600, 400);
        welcomeFrame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Welcome to the Logic Puzzle Game!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeFrame.add(titleLabel, BorderLayout.NORTH);

        //instructions
        JTextArea instructions = new JTextArea(
                "Instructions:\n\n" +
                        "1. Find the logic relationships between the categories in the grid.\n" +
                        "2. Select relationship by clicking on the cells.\n" +
                        "  - Click once for 'X' (guess) and twice for 'O' (final answer).\n" +
                        "3. Use the Hint button to reveal a correct answer (+20s penalty).\n" +
                        "4. Clear Errors removes incorrect selections (+20s penalty).\n" +
                        "5. Start Over resets the game and timer.\n" +
                        "6. You can only 'Submit' if all answers are correct.\n\n" +
                        "Ps.: The lower the adjusted time, the better. Good luck! :)"
        );
        instructions.setEditable(false);
        instructions.setMargin(new Insets(10, 10, 10, 10));
        instructions.setLineWrap(true);
        instructions.setWrapStyleWord(true);

        //timer tracking details - Gabriel
        JLabel elapsedTimeLabel = new JLabel("Total Time Elapsed: " + totalElapsedTime + "s", SwingConstants.CENTER);
        JLabel adjustedTimeLabel = new JLabel("Adjusted Time: " + totalAdjustedTime + "s", SwingConstants.CENTER);
        JPanel scorePanel = new JPanel(new GridLayout(2, 1));
        scorePanel.add(elapsedTimeLabel);
        scorePanel.add(adjustedTimeLabel);

        //adds to center panel, both instructions and scores
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(instructions, BorderLayout.NORTH);
        centerPanel.add(scorePanel, BorderLayout.SOUTH);

        welcomeFrame.add(centerPanel, BorderLayout.CENTER); //adds to frame

        //play button
        JButton playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.BOLD, 18));
        playButton.addActionListener(e -> {
            PuzzleGUI.resetTimers(); //resets for every new play
            welcomeFrame.dispose(); //closes welcome screen
            showGameScreen(); //start game
            startTimer(); //start timer
        });
        welcomeFrame.add(playButton, BorderLayout.SOUTH); //so 'Play' button can be displayed separately.

        welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        welcomeFrame.setLocationRelativeTo(null);
        welcomeFrame.setVisible(true);
    }

    /**
     * Displays the main game screen with puzzle grids and the interface.
     * Sets up buttons, clues, story, and controls (buttons).
     *
     * all visuals - Bruna and Gabriel
     */
    private void showGameScreen() {
        frame = new JFrame("Logic Puzzle Game");
        frame.setSize(1200, 700);
        frame.setLayout(new BorderLayout());

        //grids
        JPanel gridsPanel = new JPanel(new GridLayout(3, 1));
        gridsPanel.setPreferredSize(new Dimension((int) (frame.getWidth() * 0.66), frame.getHeight()));
        gridButtons = new JButton[3][4][4];

        //creates the grids
        for (int grid = 0; grid < 3; grid++) {
            JPanel gridPanel = createGridPanel(grid);
            gridsPanel.add(gridPanel);
        }

        //tabs panel (for clues, story, and notes)
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension((int) (frame.getWidth() * 0.33), frame.getHeight()));

        //clues tab
        cluesArea = new JTextArea();
        cluesArea.setEditable(false);
        cluesArea.setLineWrap(true);
        cluesArea.setWrapStyleWord(true);
        cluesArea.setMargin(new Insets(10, 10, 10, 10));

        int clueNumber = 1;
        for (String clue : data.clues) {
            cluesArea.append(clueNumber + ". " + clue + "\n\n");
            clueNumber++;
        }
        tabbedPane.addTab("Clues", cluesArea);

        //story tab
        storyArea = new JTextArea();
        storyArea.setEditable(false);
        storyArea.setLineWrap(true);
        storyArea.setWrapStyleWord(true);
        storyArea.setMargin(new Insets(10, 10, 10, 10));
        storyArea.setText(data.story);
        tabbedPane.addTab("Story", storyArea);

        //notes tab - editable
        notesArea = new JTextArea();
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setMargin(new Insets(10, 10, 10, 10));
        tabbedPane.addTab("Notes", notesArea);

        //control buttons (hint, clear errors, start over, submit)
        //each calls their own methods when pressed
        JPanel controlPanel = new JPanel();
        JButton hintButton = new JButton("Hint");
        hintButton.addActionListener(e -> {
            showHint();
            adjustedTime += 20; //applies penalty
        });

        JButton clearErrorsButton = new JButton("Clear Errors");
        clearErrorsButton.addActionListener(e -> {
            clearErrors();
            adjustedTime += 20;
        });

        JButton startOverButton = new JButton("Start Over");
        startOverButton.addActionListener(e -> {
            startOver();
            totalTimeElapsed = 0; //resets all and sets to 0
            adjustedTime = 0;
        });

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e ->  {
            stopTimer();
            submitAnswers();
        });

        //timer - Gabriel
        timerLabel = new JLabel("Timer: ");

        controlPanel.add(hintButton);
        controlPanel.add(clearErrorsButton);
        controlPanel.add(startOverButton);
        controlPanel.add(submitButton);
        controlPanel.add(timerLabel);

        //adds all of this to the frame
        frame.add(gridsPanel, BorderLayout.CENTER);
        frame.add(tabbedPane, BorderLayout.EAST);
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Creates a grid panel with labels for rows and columns, and grid buttons.
     * - Daniel
     */
    private JPanel createGridPanel(int gridIndex) {
        JPanel panel = new JPanel(new GridLayout(5, 5));  //4x4 grid + labels
        panel.setBorder(BorderFactory.createTitledBorder("Grid " + (gridIndex + 1)));

        ArrayList<String> rowLabels;
        ArrayList<String> colLabels;

        //grid 1 - organizing
        if (gridIndex == 0) {
            rowLabels = data.categoryItems.get(1);
            colLabels = data.categoryItems.get(0);
        } else {
            rowLabels = data.categoryItems.get(gridIndex);
            colLabels = data.categoryItems.get((gridIndex + 1) % 3);
        }

        panel.add(new JLabel(""));  //empty cells for the labels to be placed

        //col. labels
        for (String colLabel : colLabels) {
            panel.add(new JLabel(colLabel, SwingConstants.CENTER));
        }

        //row labels
        for (int i = 0; i < 4; i++) {
            panel.add(new JLabel(rowLabels.get(i), SwingConstants.CENTER));
            for (int j = 0; j < 4; j++) {
                JButton button = new JButton("");
                final int row = i, col = j, grid = gridIndex;
                button.addActionListener(e -> selectAnswer(grid, row, col));
                gridButtons[grid][i][j] = button;
                panel.add(button);
            }
        }
        return panel;
    }

    /**
     * Handles user clicks on the grid, either 'X'(guess) and 'O' (final) answers.
     * -Bruna and Daniel
     */
    private void selectAnswer(int grid, int row, int col) {
        String currentText = gridButtons[grid][row][col].getText();

        if (currentText.isEmpty()) {
            gridButtons[grid][row][col].setText("X");
            solver.setUserAnswer(grid, row, col, "X");
        } else if (currentText.equals("X")) {
            gridButtons[grid][row][col].setText("O");
            solver.setUserAnswer(grid, row, col, "O");
        } else {
            gridButtons[grid][row][col].setText("");
            solver.setUserAnswer(grid, row, col, null);
        }
    }

    /**
     * Reveals one correct answer as a hint and updates the grid at each press.
     * and adds 20 seconds to timer as a penalty for using hints.
     * -Bruna
     */
    private void showHint() {
        boolean hintProvided = solver.revealOneCorrectAnswer();

        if (hintProvided) {
            //update GUI with the answer
            for (int grid = 0; grid < 3; grid++) {
                for (int row = 0; row < 4; row++) {
                    for (int col = 0; col < 4; col++) {
                        String userAnswer = solver.getUserAnswer(grid, row, col);
                        if ("O".equals(userAnswer)) {
                            gridButtons[grid][row][col].setText("O");
                        }
                    }
                }
            }
            totalAdjustedTime += 20; //adds 20 seconds penalty
        } else { //when all hints have been revealed
            JOptionPane.showMessageDialog(frame, "No more hints available! All correct answers have been revealed.");
        }
    }

    /**
     * Clears all incorrect answers from the grid and also adds a time penalty.
     * -Bruna
     */
    private void clearErrors() {
        for (int grid = 0; grid < 3; grid++) {
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    String userAnswer = solver.getUserAnswer(grid, row, col);
                    String correctAnswer = solver.getCorrectAnswer(grid, row, col);

                    //if user answer ('O') is in incorrect place, clear that cell
                    if ("O".equals(userAnswer) && !"O".equals(correctAnswer)) {
                        gridButtons[grid][row][col].setText(""); //clears button text
                        solver.setUserAnswer(grid, row, col, null); //clears user's answer
                    }
                }
            }
        }
        totalAdjustedTime += 20; //adds 20 seconds penalty
        JOptionPane.showMessageDialog(frame, "All incorrect answers have been cleared!"); //quick warning
    }

    /**
     * Resets the game, including clearing the grid and resetting the timer.
     * -Gabriel
     */
    private void startOver() {
        solver.resetUserAnswers(); //resets all answers
        totalTimeElapsed = 0; //resets timer
        totalAdjustedTime = 0;

        //resets all buttons in the GUI
        for (int grid = 0; grid < 3; grid++) {
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    gridButtons[grid][row][col].setText("");
                    gridButtons[grid][row][col].setBackground(null);
                }
            }
        }
        JOptionPane.showMessageDialog(frame, "The puzzle has been reset! You can start over.");
    }

    /**
     * Submits the user's answers, checks if they're correct, and stops the timer. Sending the user back to the welcome screen.
     * If any of the answers are missing or incorrect, resume play and timer.
     * -Bruna
     */
    private void submitAnswers() {
        boolean allCorrect = solver.checkEntirePuzzle();
        timer.stop(); //ensures timer stops when submit is clicked

        if (allCorrect) {
            totalElapsedTime = totalTimeElapsed; //saves elapsed time
            totalAdjustedTime = totalElapsedTime + (totalAdjustedTime - totalTimeElapsed); //penalties included
            JOptionPane.showMessageDialog(frame, "Congratulations! All answers are correct!");
            frame.dispose(); //closes game screen
            Main.startNewGame(); //returns to welcome screen
        } else {
            JOptionPane.showMessageDialog(frame, "Some or all answers are incorrect. Keep playing until all are correct.");

            timer.start(); //resumes the timer
        }
    }

    /**
     * Starts the timer when the game begins.
     * - Gabriel
     */
    private void startTimer () {
        startTime = System.currentTimeMillis();
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                totalTimeElapsed = (System.currentTimeMillis() - startTime) / 1000;
                timerLabel.setText("Timer: " + totalTimeElapsed + "s");
            }
        });
        timer.start();
    }

    /**
     * Stops the timer when the game ends or is paused.
     * - Gabriel
     */
    private void stopTimer () {
        timer.stop();
    }

    /**
     * Resets all timers to 0 for a new game or when user decides to start over.
     * - Bruna
     */
    public static void resetTimers() {
        totalElapsedTime = 0;
        totalAdjustedTime = 0;
    }
}
