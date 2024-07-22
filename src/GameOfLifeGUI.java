import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


public class GameOfLifeGUI extends JFrame {
    // GUI objects
    private JPanel gamePanel;
    private JPanel controlPanel;
    private JButton playButton;
    private JButton pauseButton;
    private JButton stepButton;
    private JButton saveButton;
    private JButton loadButton;
    private JButton clearButton;
    private JSlider speedSlider;
    private JButton[][] gridButtons;
    private JSpinner xSpinner;
    private JSpinner ySpinner;
    private JSpinner zSpinner;
    private JSpinner rowSpinner;
    private JSpinner colSpinner;


    // Functional variables
    private static Map gameMap = new Map(50,50); // Creating a default map
    private boolean isPlaying; // variable to track whether player is still playing
    private int speedDelay = 1000; // default speed delay
    private int x = 2; // default x value
    private int y = 3; // default y value
    private int z = 3; // default z value
    private int rows = 50; // default rows value
    private int columns = 50; // default columns value
   
    public GameOfLifeGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Game of Life", SwingConstants.CENTER); 
        welcomeLabel.setForeground(Color.ORANGE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(welcomeLabel, BorderLayout.NORTH); // adds welcome label to teop of screen (North)

        gamePanel = new JPanel(); 
        gamePanel.setLayout(new GridLayout(50, 50)); // Default layout
        gamePanel.setBackground(Color.WHITE);
        add(gamePanel, BorderLayout.CENTER); // adds game panel to center of screen

        controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // creates control panel
        controlPanel.setBackground(Color.ORANGE); // sets background colour to rgb colour for specific shade of purple
        add(controlPanel, BorderLayout.SOUTH); // adds control panel to bottom of screen (South)

        
        // creates a nicer looking version of a button for each button type
        playButton = createStyledButton("Play");
        pauseButton = createStyledButton("Pause");
        stepButton = createStyledButton("Step");
        saveButton = createStyledButton("Save");
        loadButton = createStyledButton("Load");
        clearButton = createStyledButton("Clear");

        // creates a speedSlider object to allow user to customise speed at which game is played
        speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintTicks(true);

       // creates Jspinner to allow user to input various values of x,y,z rows and columns as long as they're in specified range
        controlPanel.add(new JLabel("x:"));
        xSpinner = new JSpinner(new SpinnerNumberModel(2,1,8,1));
        controlPanel.add(xSpinner);

        controlPanel.add(new JLabel("y:"));
        ySpinner = new JSpinner(new SpinnerNumberModel(3,1,8,1));
        controlPanel.add(ySpinner);

        controlPanel.add(new JLabel("z:"));
        zSpinner = new JSpinner(new SpinnerNumberModel(3,1,8,1));
        controlPanel.add(zSpinner);

        controlPanel.add(new JLabel("rows:"));
        rowSpinner = new JSpinner(new SpinnerNumberModel(50,5,50,1 ));
        controlPanel.add(rowSpinner);

        controlPanel.add(new JLabel("columns"));
        colSpinner = new JSpinner(new SpinnerNumberModel(50,5,50,1 ));
        controlPanel.add(colSpinner);
        
     
        
        // action listener for play button isPlaying variable is set to true which ensures game loop runs
        // various buttons are disabled and enabled
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isPlaying = true;
                playButton.setEnabled(false); 
                pauseButton.setEnabled(true);
                stepButton.setEnabled(false);
                loadButton.setEnabled(false);
                saveButton.setEnabled(false);
                clearButton.setEnabled(false);
                rowSpinner.setEnabled(false);
                colSpinner.setEnabled(false);;
                startGameLoop();
            }
        });

        // action listener for pause button
        // various buttons are enabled and disabled to ensure proper functionality
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isPlaying = false;
                playButton.setEnabled(true);
                pauseButton.setEnabled(false);
                stepButton.setEnabled(true);
                loadButton.setEnabled(true);
                saveButton.setEnabled(true);
                clearButton.setEnabled(true);
                rowSpinner.setEnabled(true);
                colSpinner.setEnabled(true);


            }
        });

        // action listener for clear button
        // allows user to clear screen and start again
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                gameMap.killAllCells(); // kill all cells whether already dead or alive
                updateGridButtons(); // update grid buttons to reflect this change
            }
        });

        // action listener for step button
        stepButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isPlaying) {
                    GameOfLifeLogic.step(gameMap, x, y, z); // //enacts one step of the game logic at a time
                    updateGridButtons(); // grid buttons are updated after step is carried out
                }
            }
        });

        // action listener for save button 
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                try {
                    // allows user to save grid
                    LoadAndSave.saveGrid(gameMap, x, y, z, rows, columns); 
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Map tempMap = LoadAndSave.loadGrid();
                    if(tempMap==null){
                        JOptionPane.showMessageDialog(GameOfLifeGUI.this, "Select the appropriate file (.gol)","Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                        updateRowsColumns(tempMap.getGrid().length,tempMap.getGrid()[0].length);
                        gameMap = tempMap;
                    }
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                initialiseGridButtons(gameMap);
            }
        });

        xSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e){
                int temp =(int)xSpinner.getValue(); // casts object to an integer
                if (temp > y){ // input validation ensuring that x value cannot be greater than y and outputting error message if condition evaluates to true
                    JOptionPane.showMessageDialog(GameOfLifeGUI.this, "Please ensure x is less than y!","X greater than Y", JOptionPane.ERROR_MESSAGE);
                    xSpinner.setValue(x); 
                } else {
                    x = temp; // passes validation check so new x value is now one read in from JSpinner
                }
            }
        });

        ySpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e ){
                int temp = (int)(ySpinner.getValue()); // casts object to integer
                if (temp < x){ // input validation ensuring that y value cannot be less than x and outputting error message if condition evaluates to true
                    JOptionPane.showMessageDialog(GameOfLifeGUI.this, "Please ensure y is greater than x!","Y less than X", JOptionPane.ERROR_MESSAGE);
                    ySpinner.setValue(y);
                } else {
                    y = temp; // passes validation check so new y value is now one read in from JSpinner
                }
            }
        });

        zSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e){
                z = (int)(zSpinner.getValue()); // casting z value from object to integer
            }
        });

        colSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e){
                columns = (int)colSpinner.getValue(); // gets value of columns from spinner
                initialiseGridButtons(new Map(rows, columns)); // initialising grid buttons again with new dimensions
            }
        });

        rowSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e){
                rows = (int)rowSpinner.getValue(); // gets value of rows from spinner
                initialiseGridButtons(new Map(rows,columns)); // initialises grid buttons again with new dimensions
            }
        });

      
        speedSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {

                int value = speedSlider.getValue(); // gets value of speedSlider
                speedDelay = 1000 / value; // calculates how long to delay execution
            }
        });

        // adds various buttons to control panel
        controlPanel.add(playButton);
        controlPanel.add(pauseButton);
        controlPanel.add(stepButton);
        controlPanel.add(saveButton);
        controlPanel.add(loadButton);
        controlPanel.add(clearButton);
        controlPanel.add(speedSlider);
      
        // initialises grid buttons with 50x50 default map
        initialiseGridButtons(new Map(rows,columns));
      

        pack(); // all contents of frame are above or at preferred sizes
        setLocationRelativeTo(null); // centres window on screen
        setVisible(true); // makes GUI visible
    }


    // A method to pause the execution of the program.
    public static void pause(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        }
        catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to create styled button 
    // this method was adapted from a stack overflow post full reference in report 
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(100, 40));
        button.setBorderPainted(false);
        return button;
    } // end of adaptation

    private void initialiseGridButtons(Map mapGrid) {
        gameMap = mapGrid;
    
        gamePanel.removeAll(); // Clears everything from the game panel
        gamePanel.setLayout(new GridLayout(rows, columns));
        gridButtons = new JButton[rows][columns]; // creates grid buttons
    
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                JButton button = new JButton();
    
                 // designs look of buttons
                button.setPreferredSize(new Dimension(10, 10));
                button.setBackground(gameMap.getGrid()[i][j].isAlive() ? new Color(100, 149, 237) : Color.WHITE);
                button.setOpaque(true);
                button.setBorder(BorderFactory.createLineBorder(new Color(173,216,230))); // Add border to each button
    
                int x = i;
                int y = j;
    
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (!gameMap.getGrid()[x][y].isAlive()) {  // Toggle cell state when the button is clicked
                            gameMap.addLiveCell(gameMap.getGrid()[x][y]);  // Add cell to live cells
                        } else {
                            gameMap.removeLiveCell(gameMap.getGrid()[x][y]); // remove cell from live cells
                        }
    
                        updateGridButtons();  //update grid buttons to reflect changes 
                    }
                });
                gridButtons[i][j] = button; // store button reference in grid buttons 2d array
                gamePanel.add(button); //  add button to game panel
            }
        }
        gamePanel.revalidate(); // revalidates the game panel ensuring that layout managers recalculate layout
        gamePanel.repaint();  // repaints the game panel triggering a redraw of its components to reflect any changes
    }
    

    // Method to update grid buttons
    private void updateGridButtons() {
        for (int i = 0; i < gridButtons.length; i++) {
            for (int j = 0; j < gridButtons[i].length; j++) {
                // checks whether cell is alive or dead and if alive cell is blue if dead cell is white
                gridButtons[i][j].setBackground(gameMap.getGrid()[i][j].isAlive() ? new Color(100, 149, 237) : Color.WHITE);
            }
        }
    }

    // updates value of rows and columns
    private void updateRowsColumns(int numRows, int numColumns) {
        rows = numRows;
        columns = numColumns;
        rowSpinner.setValue(rows);
        colSpinner.setValue(columns);
    }



    // Method to start game loop
    private void startGameLoop() {
        new Thread(new Runnable() {
            public void run() {
                while (isPlaying) { // while still playing execute a step of game logic and update grid buttons
                    GameOfLifeLogic.step(gameMap, x, y, z);
                    updateGridButtons();
                    try {
                        Thread.sleep(speedDelay); // thread sleeps in conjunction with value of speed delay
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }

   
    // main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GameOfLifeGUI();
            }
        });
    }
}