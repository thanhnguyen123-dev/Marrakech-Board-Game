package comp1110.ass2.player;

import comp1110.ass2.board.Coordinate;

public class Rug {
    private Player owner;
    private int id;
    private Coordinate[] rugTiles;

    public Rug(Player owner, int id, Coordinate[] rugTiles) {
        this.owner = owner;
        this.id = id;
        this.rugTiles = rugTiles;
    }
}
