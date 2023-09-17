package comp1110.ass2.player;

import comp1110.ass2.board.Tile;

/**
 * Rug class contains properties of each Rug
 */
public class Rug {
    // Rug's colour
    private final Colour colour;
    // Rug's unique id
    private final int id;
    // Rug's positions
    private final Tile[] rugTiles;

    /**
     * Constructor: creates a new instance of the Rug class
     * @param colour Colour of the Rug
     * @param id id of the Rug
     * @param rugTiles Tiles occupied by the Rug
     */
    public Rug(Colour colour, int id, Tile[] rugTiles) {
        this.colour = colour;
        this.id = id;
        this.rugTiles = rugTiles;
    }

    /**
     * @return Colour of the Rug
     */
    public Colour getColour() {
        return this.colour;
    }

    /**
     * @return id of the Rug
     */
    public int getID() {
        return this.id;
    }

    /**
     * @return an array of Tiles occupied by the Rug
     */
    public Tile[] getRugTiles() {
        return this.rugTiles;
    }

    /**
     * Constructor: creates an instance of the Rug class
     * Decode the shortRugString to get corresponding values for instance fields
     * (reserved for Board generation only)
     * @param shortRugString string representation for the Rug
     */
    public Rug(String shortRugString, Tile tile) {
        this.colour = Colour.charToColour(shortRugString.charAt(0));
        this.id = Integer.parseInt(shortRugString.substring(1), 10);
        this.rugTiles = new Tile[]{tile};
    }

    /**
     * Constructor: creates an instance of the Rug class
     * Decode the fullRugString to get corresponding values for instance fields
     * (reserved for Rug placement validation only)
     * @param fullRugString string representation for the Rug
     */
    public Rug(String fullRugString, Tile[][] tiles) {
        this.colour = Colour.charToColour(fullRugString.charAt(0));
        this.id = Integer.parseInt(fullRugString.substring(1, 3), 10);
        int[] position1 = parse(fullRugString.substring(3,5));
        Tile tile1 = tiles[position1[0]][position1[1]];
        int[] position2 = parse(fullRugString.substring(5,7));
        Tile tile2 = tiles[position2[0]][position2[1]];
        this.rugTiles = new Tile[]{tile1, tile2};
    }

    /**
     * Constructor: creates an instance of the Rug class
     * Decode the fullRugString to get corresponding values for instance fields
     * (reserved for Rug string validation only)
     * @param fullRugString string representation for the Rug
     */
    public Rug(String fullRugString){
        this.colour = Colour.charToColour(fullRugString.charAt(0));
        this.id = Integer.parseInt(fullRugString.substring(1, 3), 10);
        int[] position1 = parse(fullRugString.substring(3,5));
        Tile tile1 = new Tile(position1[0], position1[1]);
        int[] position2 = parse(fullRugString.substring(5,7));
        Tile tile2 = new Tile(position2[0], position2[1]);
        this.rugTiles = new Tile[]{tile1, tile2};
    }

    private static int[] parse(String coordinates) {
        int row = Integer.parseInt(coordinates.charAt(1) + "", 10);
        int col = Integer.parseInt(coordinates.charAt(0) + "", 10);
        return new int[]{row, col};
    }
}