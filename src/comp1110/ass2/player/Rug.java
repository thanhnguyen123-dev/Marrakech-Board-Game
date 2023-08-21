package comp1110.ass2.player;

import comp1110.ass2.board.Coordinate;

/**
 * Rug class contains properties of each Rug
 */
public class Rug {
    // Rug's owner
    private Player owner;
    // Rug's unique id
    private int id;
    // Rug's positions
    private Coordinate[] rugTiles;

    /**
     * Constructor: creates a new instance of the Rug class
     * @param owner
     * @param id
     * @param rugTiles
     */
    public Rug(Player owner, int id, Coordinate[] rugTiles) {
        this.owner = owner;
        this.id = id;
        this.rugTiles = rugTiles;
    }

    /**
     * getter method for the owner of the Rug
     * @return owner
     */
    public Player getOwner() {
        return this.owner;
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
    public Coordinate[] getRugTiles() {
        return this.rugTiles;
    }
}