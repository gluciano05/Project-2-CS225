// Worked on By Bruna, Gabe, and Daniel

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PuzzleGUI {
    private static JLabel timerLabel;
    private static Timer timer;
    private static long startTime;
    private JFrame frame;
    private JButton[][][] gridButtons; //buttons for the grids
    private PuzzleSolver solver; //puzzle logic
    private PuzzleData data; //handles data
    private JTextArea cluesArea; //displays clues
    private JTextArea notesArea; //user possible notes
    private JTextArea storyArea; //displays the 'story'
    private long totalTimeElapsed; //for keeping track of user scores and time to completion

    //constructor - initializes the game
    public PuzzleGUI(PuzzleData data, PuzzleSolver solver) {
        this.solver = solver;
        this.data = data;
        showWelcomeScreen(); //calls first screen
    }

    //welcome screen
    private void showWelcomeScreen() {
        JFrame welcomeFrame = new JFrame("Logic Puzzle Game");
        welcomeFrame.setSize(600, 400);
        welcomeFrame.setLayout(new BorderLayout());

        //title
        JLabel titleLabel = new JLabel("Welcome to the Logic Puzzle Game!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeFrame.add(titleLabel, BorderLayout.NORTH);

        //game instructions
        JTextArea instructions = new JTextArea(
                "Instructions:\n\n" +
                        "1. Fill the grids by clicking on the cells.\n" +
                        "2. Click once for 'X' (guess) and twice for 'O' (final answer).\n" +
                        "3. Use the Hint button to reveal a correct answer.\n" +
                        "4. Clear Errors removes incorrect 'O's, and Start Over resets the game.\n\n" +
                        "Good luck!"
        );
        instructions.setEditable(false);
        instructions.setMargin(new Insets(10, 10, 10, 10));
        instructions.setLineWrap(true);
        instructions.setWrapStyleWord(true);
        welcomeFrame.add(instructions, BorderLayout.CENTER);

        //play button
        JButton playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.BOLD, 18));
        playButton.addActionListener(e -> {
            welcomeFrame.dispose();  //close the welcome screen
            showGameScreen(); //launch the main game
            startTimer(); //starts the timer when the game starts
        });
        welcomeFrame.add(playButton, BorderLayout.SOUTH);

        welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        welcomeFrame.setLocationRelativeTo(null);
        welcomeFrame.setVisible(true);
    }

    //main game screen
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

        //notes tab
        notesArea = new JTextArea();
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setMargin(new Insets(10, 10, 10, 10));
        tabbedPane.addTab("Notes", notesArea);

        //control buttons (hint, clear errors, start over, submit)
        //each calls their own methods when pressed
        JPanel controlPanel = new JPanel();
        JButton hintButton = new JButton("Hint");
        hintButton.addActionListener(e -> showHint());

        JButton clearErrorsButton = new JButton("Clear Errors");
        clearErrorsButton.addActionListener(e -> clearErrors());

        JButton startOverButton = new JButton("Start Over");
        startOverButton.addActionListener(e -> startOver());

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e ->  {
            stopTimer();
            submitAnswers();
        });

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

    //creates grids with labels
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

    //clicking the button and selecting the answer logic
    private void selectAnswer(int grid, int row, int col) {
        String currentText = gridButtons[grid][row][col].getText();

        // 'X' for user guess, 'O' for their final answer.
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

    //hint
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
                            gridButtons[grid][row][col].setBackground(Color.GREEN); //highlights answer with green
                        }
                    }
                }
            }
        } else { //when all hints have been revealed
            JOptionPane.showMessageDialog(frame, "No more hints available! All correct answers have been revealed.");
        }
    }

    //clear errors
    private void clearErrors() {
        for (int grid = 0; grid < 3; grid++) {
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    String userAnswer = solver.getUserAnswer(grid, row, col);
                    String correctAnswer = solver.getCorrectAnswer(grid, row, col);

                    //if user answer ('O') is incorrect, clear that cell
                    if ("O".equals(userAnswer) && !"O".equals(correctAnswer)) {
                        gridButtons[grid][row][col].setText(""); //clears button text
                        gridButtons[grid][row][col].setBackground(null); //resets background color (if any)
                        solver.setUserAnswer(grid, row, col, null); //clears user's answer
                    }
                }
            }
        }

        JOptionPane.showMessageDialog(frame, "All incorrect answers have been cleared!");
    }

    //start over
    private void startOver() {
        solver.resetUserAnswers(); //resets all answers

        // Reset all buttons in the GUI
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

    //submit
    private void submitAnswers() {
        boolean allCorrect = solver.checkEntirePuzzle();

        if (allCorrect) {
            JOptionPane.showMessageDialog(frame, "Congratulations! All answers are correct!");
        } else {
            //highlights incorrect answers visually
            for (int grid = 0; grid < 3; grid++) {
                for (int row = 0; row < 4; row++) {
                    for (int col = 0; col < 4; col++) {
                        boolean isCorrect = solver.isAnswerCorrect(grid, row, col);
                        if (!isCorrect) {
                            gridButtons[grid][row][col].setBackground(Color.RED); //incorrect answer
                        } else if ("O".equals(solver.getUserAnswer(grid, row, col))) {
                            gridButtons[grid][row][col].setBackground(Color.GREEN); //correct answer
                        } else {
                            gridButtons[grid][row][col].setBackground(null);
                        }
                    }
                }
            }
            JOptionPane.showMessageDialog(frame, "Some or all answers are incorrect.");
        }

        //takes the user back to the welcome screen once submit was pressed
        frame.dispose();
        Main.startNewGame(); //new game with main, taking data from the unplayed files
    }
    private void startTimer () {
        startTime = System.currentTimeMillis();
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //has the timer display in this format "seconds.milliseconds", example 10.2 seconds
                totalTimeElapsed = (System.currentTimeMillis() - startTime) / 1000;
                timerLabel.setText("Timer: " + totalTimeElapsed + "s");
            }
        });
        timer.start();
    }
    private void stopTimer () {
        timer.stop();
        System.out.println("Total time elapsed: " + totalTimeElapsed + "s");
    }
}
