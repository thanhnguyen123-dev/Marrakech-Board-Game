package comp1110.ass2.player;

import comp1110.ass2.board.Tile;

/**
 * Rug class contains properties of each Rug
 */
public class Rug {
    // Rug's colour
    private Colour colour;
    // Rug's unique id
    private int id;
    // Rug's positions
    private Tile[] rugTiles;

    /**
     * Constructor: creates a new instance of the Rug class
     * @param colour
     * @param id
     * @param rugTiles
     */
    public Rug(Colour colour, int id, Tile[] rugTiles) {
        this.colour = colour;
        this.id = id;
        this.rugTiles = rugTiles;
    }

    /**
     * getter method for the colour of the Rug
     * @return colour
     */
    public Colour getColour() {
        return this.colour;
    }

    /**
     * getter method for the ID of the Rug
     * @return ID
     */
    public int getID() {
        return this.id;
    }

    /**
     * getter method for the positions occupied by the Rug
     * @return rugTiles
     */
    public Tile[] getRugTiles() {
        return this.rugTiles;
    }
}