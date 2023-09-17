package comp1110.ass2.player;

import comp1110.ass2.board.Tile;

/**
 * Rug class contains properties of each rug
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
     * @param colour the colour of the rug
     * @param id the id of the rug
     * @param rugTiles the tiles occupied by the rug
     */
    public Rug(Colour colour, int id, Tile[] rugTiles) {
        this.colour = colour;
        this.id = id;
        this.rugTiles = rugTiles;
    }

    /**
     * getter method for colour
     * @return the colour of the rug
     */
    public Colour getColour() {
        return this.colour;
    }

    /**
     * getter method for id
     * @return the id of the rug
     */
    public int getID() {
        return this.id;
    }

    /**
     * getter method for rugTiles
     * @return an array of tiles occupied by the rug
     */
    public Tile[] getRugTiles() {
        return this.rugTiles;
    }

    /**
     * Constructor: creates an instance of the Rug class
     * Decodes the shortRugString to get corresponding values for instance fields
     * (reserved for board generation only)
     * @param shortRugString string representation for the rug
     */
    public Rug(String shortRugString, Tile tile) {
        this.colour = Colour.charToColour(shortRugString.charAt(0));
        this.id = Integer.parseInt(shortRugString.substring(1), 10);
        this.rugTiles = new Tile[]{tile};
    }

    /**
     * Constructor: creates an instance of the Rug class
     * Decodes the fullRugString to get corresponding values for instance fields
     * (reserved for rug placement validation only)
     * @param fullRugString string representation for the rug
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
     * Decodes the fullRugString to get corresponding values for instance fields
     * (reserved for rug string validation only)
     * @param fullRugString string representation for the rug
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