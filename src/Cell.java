public class Cell {
    //######
    // Attributes
    //######

    private boolean alive; // Stores the life status of a cell.
    private Cell[] neighbours; // Stores pointers to the adjacent cells.
    private int numLiveNeighbours; // Stores the number of adjacent cells that are 'alive'.

    //######
    // Constructor
    //######

    public Cell(int numNeighbours) {
        alive = false; // On creation of the map, all cells will be dead.
        neighbours = new Cell[numNeighbours]; // Initialising the nieghbours array to have the correct size (i.e. number of neighbours).
        numLiveNeighbours = 0; // On creation of the map, the cells have no live neighbours as all cells are dead.
    }


    //######
    // Setters and getters
    //######

    // Returns the life status of the cell.
    public boolean isAlive() {
        return alive;
    }


    // Returns an array of the cell's neighbours.
    public Cell[] getNeighboursArray() {
        return neighbours;
    }

    // Returns the number of live neighbours.
    public int getNumLiveNeighbours() {
        return numLiveNeighbours;
    }


    //######
    // Functional Methods
    //######

    // Adds the id of a given neighbour to the neighbours array in the first empty location.
    public void addNeighbour(Cell neighbour) {
        for (int i = 0; i < neighbours.length; i++) {
            // Adding the given neighbouring Cell to the first empty array position.
            if (neighbours[i] == null) {
                neighbours[i] = neighbour; // Inserting the given neighbour into a free array position.
                return; // Exiting the loop so as not to double-add the given neighbour.
            }
        }
    }


    // Kills a cell and decreases the live neighbours count of each of its neighbours.
    public void kill() {
        alive = false;

        // Decrementing the number of live neighbours each of the current Cell's neighbours has.
        for (Cell neighbour: neighbours) {
            neighbour.decrementLiveNeighbours();
        }
    }


    // Makes a cell come to life and increases the count of live neighbours each of its neighbours has.
    public void bringToLife() {
        alive = true;

        // Incrementing the number of live neighbours each of the current Cell's neighbours have.
        for (Cell neighbour: neighbours) {
            neighbour.incrementLiveNeighbours();
        }
    }


    // Increments the 'numLiveNeighbours' count by 1.
    public void incrementLiveNeighbours() {
        if (numLiveNeighbours < 8) {
            numLiveNeighbours ++;
        }
    } 


    // Decrements the 'numLiveNeighbours' count by 1.
    public void decrementLiveNeighbours() {
        if (numLiveNeighbours > 0) {
            numLiveNeighbours --;
        }
    }
}
