import java.util.*;

public class GameOfLifeLogic {

    // Static function which takes a map (and x, y, z parameters) and updates all cells in the map that change in one step of gol logic.
    public static void step(Map gameMap, int x, int y, int z) {

        List<Cell> cellsToKill = new ArrayList<>(); // Stores the Cells that will need to be killed at the end of the step.
        List<Cell> cellsToEnliven = new ArrayList<>(); // Stores the Cells that will need to be brought to life at the end of the step.
        // The above two lists need to be used as otherwise the status of certain cells would be changed between comparisons within a single step - potentially resulting in invalid comparisons.

        Set<Cell> liveCellNeighbours = new HashSet<>(); // A buffer to store all the (dead) Cells neighbouring any live cells on the entire grid (without repeats).

        // Iterating through all live cells in the grid.
        for (Cell liveCell: gameMap.getLiveCells()) {
            // Killing the current cell if it has fewer than x live neighbours or more than y live neighbours.
            if (liveCell.getNumLiveNeighbours() < x || liveCell.getNumLiveNeighbours() > y) {
                cellsToKill.add(liveCell); // Adding the current Cell to the kill list.
            }

            // Adding any dead neighbours of the current cell to the appropriate set defined above.
            for (Cell neighbour: liveCell.getNeighboursArray()) {
                // Checking if the current neighbour is dead.
                if (!neighbour.isAlive()) {
                    liveCellNeighbours.add(neighbour);
                }
            }

        }

        // Adding dead Cells with the appropriate number of live neighbours to the enliven list.
        for (Cell deadNeighbour: liveCellNeighbours) {
            if (deadNeighbour.getNumLiveNeighbours() == z) {
                cellsToEnliven.add(deadNeighbour);
            }
        }

        // Killing the appropriate cells.
        for (Cell liveCell: cellsToKill) {
            gameMap.removeLiveCell(liveCell);
        }

        // Bringing the appropriate cells to life.
        for (Cell deadCell: cellsToEnliven) {
            gameMap.addLiveCell(deadCell);
        }
    }
}
