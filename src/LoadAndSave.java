import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import java.awt.Color;

//Class is responsible for loading and saving the gameMap, writing down and displaying appropriate messages and outputs.
public class LoadAndSave {
    
    //Save the Map
    public static void saveGrid(Map gameMap, int x, int y, int z, int rows, int columns) throws IOException{

        //Change the background color of the JFileChooser and JOptionPane
        UIManager.put("OptionPane.background",new Color(197, 180, 227));
        UIManager.put("Panel.background",new Color(197, 180, 227));

        //Choose a specific directory where to save a file
        JFileChooser fileChooser = new JFileChooser("../data");

        //To check if the file was chosen by the user
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

            //Assign the selected file to the variable
            File file = fileChooser.getSelectedFile();
            //Check if the filepath ends with a proper extension (.gol) and add one if not. 
            String filename = file.toString().endsWith(".gol") ? file.toString() : file.toString() + ".gol";
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            //goes throug the gameMap and saves it to the file
            for (int i = 0; i < gameMap.getNumRows(); i++) {
                for (int j = 0; j < gameMap.getNumColumns(); j++) {
                    //writes down the gameMap to the file. If the element of the gameMap is alive - write down 'o', if dead - '.'
                    writer.write(gameMap.getGrid()[i][j].isAlive() ? 'o' : '.');
                }
                writer.newLine();
            }

            //Create a String variable to store the comment the user might input after saving the gameMap
            String notes = JOptionPane.showInputDialog(null, "", "Input some short comment: ", JOptionPane.INFORMATION_MESSAGE);
            //Write it down in a special format to later allow loadGrid() method to extract it and display to the user
            writer.write("*x = " + x + ";y = " + y + ";z = " + z + ";Rows = " + rows + ";Columns = " + columns + "\n");
            writer.write("/*Comments: " + notes);
            writer.close();            
        }
    }

    //Load the Grid
    public static Map loadGrid() throws IOException, FileNotFoundException{
        
        //Change the background color of the JFileChooser and JOptionPane
        UIManager.put("OptionPane.background",new Color(197, 180, 227));
        UIManager.put("Panel.background",new Color(197, 180, 227));

        //Create variables for the dimentions of the future gameMap. They are unknown for now, so both of them are 0 by default.
        int rows = 0;
        int columns = 0;
        //Create variable for the message, that will be displayed later
        String message = "";

        //Choose a specific directory from where to load a file
        JFileChooser fileChooser = new JFileChooser("../data");

        //To check if the file was chosen by the user and if the chosen file has an appropriate extention (.gol)
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile().toString().contains(".gol")) {

            //Assign the selected file to the variable
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    //Adds lines marked by '*' to the message
                    if (line.contains("*")) {
                        message+=line;
                    }
                    //Gets dimentions for the future gameMap. Only counts the lines without '*'
                    else{
                        rows++;
                        columns = line.length();
                    }
                }

                //Gets rid of the extra characters and formats the message
                message = message.replaceAll("[;/]", "\n");
                message = message.replaceAll("[*]", "");

                //Displays the message
                JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);

            } 
            catch (IOException ex) {
                ex.printStackTrace();
            }

            //Creates future gameMap
            Map gameMap = new Map(rows, columns);

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

                //Goes through the file again, but now loades the data and creates the gameMap (using rows, columns for dimensions)
                String line;
                for (int row = 0; row < rows; row++) {
                    line = reader.readLine();
                    for (int column = 0; column < columns; column++) {
                        //If the character in the file = 'o', then the cell becomes alive and is added to the list
                        if (String.valueOf(line.charAt(column)).equals("o")) {
                            gameMap.addLiveCell(gameMap.getGrid()[row][column]);
                        }
                    }
                }
            } 
            catch (IOException ex) {
                ex.printStackTrace();
            }
            //retuens the updated map
            return gameMap;
        }
        //returns null if the user didn`t choose a file 
        else {
            return null;
        }
    }
}