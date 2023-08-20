package comp1110.ass2.player;

import comp1110.ass2.board.Coordinate;

/**
 * Rug class defines the state of each rug
 */
public class Rug {
    // Rug's owner
    private Player owner;
    // Rug's unique id
    private int id;
    // Rug's positions
    private Coordinate[] rugTiles;

    /**
     * Constructor, get rug's state
     * @param owner
     * @param id
     * @param rugTiles
     */
    public Rug(Player owner, int id, Coordinate[] rugTiles) {
        this.owner = owner;
        this.id = id;
        this.rugTiles = rugTiles;
    }

    public Player getOwner() {
        return this.owner;
    }

    public int getId() {
        return this.id;
    }

    /**
     * Get positions of each rug
     * @return
     */
    public Coordinate[] getRugTiles() {
        return this.rugTiles;
    }
}