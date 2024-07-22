import java.util.*;

public class Map {
    //######
    // Attributes
    //######

    private Cell[][] grid; // Stores the map on which the game is played.
    private int numRows; // Stores the number of rows in the grid.
    private int numColumns; // Stores the number of columns in the grid.
    private List<Cell> liveCells; // Stores the cells which are live.

    //######
    // Constructor
    //######
    
    public Map(int rows, int columns) {
        grid = new Cell[rows][columns]; // Initialising the grid with null values.
        // Initialising the number of rows and columns variables.
        this.numRows = rows;
        this.numColumns = columns;
        liveCells = new ArrayList<>(); // Initialising the empty list.

        // Creating the starting grid (with all cells dead and connected to form a toroidal map).
        populateMap();
        toroidalMapLink();
    }


    //######
    // Getters and Setters
    //######

    // Returns the grid (2D array).
    public Cell[][] getGrid() {
        return grid;
    }


    // Returns the list of live cells.
    public List<Cell> getLiveCells() {
        return liveCells;
    }


    public int getNumRows() {
        return numRows;
    }


    public int getNumColumns() {
        return numColumns;
    }


    //######
    // Functional Methods
    //######
    
    // Populates the grid with dead Cell objects (which are not yet linked).
    private void populateMap() {
        // Iterates for the correct number of rows.
        for (int i = 0; i < numRows ; i++) {
            // Iterates for the correct number of columns.
            for (int j = 0; j < numColumns; j++) {
                grid[i][j] = new Cell(8); // Initialising a Cell object with 8 neighbours in the current array position.
            }
        }
    }


    // Links all of the Cells to their neighbours to form a toroidal map.
    private void toroidalMapLink() {
        int[] neighbouringRows = new int[3]; // Stores the indices of the neighbouring rows to a given cell.
        int[] neighbouringColumns = new int[3]; // Stores the indices of the neighbouring columns to a given cell.


        // Iterating through the rows of the grid.
        for (int i = 0; i < numRows; i++) {
            
            neighbouringRows[0] = i - 1; // Stores the index of the row of Cells above the current row.
            if (i == 0) {
                neighbouringRows[0] = grid.length - 1; // For a Cell in the top row, its neighbours above are on the bottom row of the grid.
            }

            neighbouringRows[1] = i; // Sores the index of the current row.

            neighbouringRows[2] = i + 1; // Stores the index of the row of Cells below the current row.
            if (i == grid.length - 1) {
                neighbouringRows[2] = 0; // For a Cell in the bottom row, its neighbours below are on the top row of the grid.
            }

            // Iterating through the items in the current row.
            for (int j = 0; j < numColumns; j++) {
                Cell currentCell = grid[i][j]; // Stores the Cell currently being iterated through;

                neighbouringColumns[0] = j - 1; // Stores the index of the column to the left of the column the current Cell is in.
                if (j == 0) {
                    neighbouringColumns[0] = grid[i].length - 1; // For a Cell in the leftmost column, its neighbours to the left are on the rightmost column.
                }

                neighbouringColumns[1] = j; // Stores the index of the current column.

                neighbouringColumns[2] = j + 1; // Stores the index of the column to the right of the column the current Cell is in.
                if (j == grid[i].length - 1) {
                    neighbouringColumns[2] = 0; // For a Cell in the rightmost column, its neighbours to the right are on the leftmost column.
                }

                // Iterating through the neighbours' index positions.
                for (int row: neighbouringRows) {
                    for (int column: neighbouringColumns) {
                        // Ensuring the current neighbour is not the current Cell.
                        if (!(row == i && column == j)) {
                            currentCell.addNeighbour(grid[row][column]); // Adding the current neighbour to the current Cell's neighbours array.
                        }
                    }
                }
            }
        }
    }


    // Adds a given Cell to the liveCells list while also bringing that cell to life.
    public void addLiveCell(Cell cellID) {
        cellID.bringToLife(); // bringing the Cell to life (also includes updating its neighbour's numLiveNeighbours counts).
        liveCells.add(cellID);
    }


    // Removes a given Cell from the liveCells list while also killing that cell.
    public void removeLiveCell(Cell cellID) {
        cellID.kill(); // killing the Cell (also includes updating its neighbour's numLiveNeighbours counts).
        liveCells.remove(cellID);
    }


    // Resets all Cell objects to dead.
    public void killAllCells() {
        // Iterates through all the cells on the grid.
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                removeLiveCell(grid[i][j]);
            }
        }
    }
}
