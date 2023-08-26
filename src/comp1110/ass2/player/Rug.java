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

    public Rug(String shortRugString, Tile tile) {
        this.colour = Colour.charToColour(shortRugString.charAt(0));
        this.id = Integer.parseInt(shortRugString.substring(1), 10);
        this.rugTiles = new Tile[]{tile};
    }

    public Rug(String fullRugString, Tile[] tiles) {
        this.colour = Colour.charToColour(fullRugString.charAt(0));
        this.id = Integer.parseInt(fullRugString.substring(1, 3), 10);
        this.rugTiles = tiles;
    }

    /**
     * Decode full rug string to object
     * @param fullRugString
     */
    public Rug(String fullRugString){
        this.colour = Colour.charToColour(fullRugString.charAt(0));
        this.id = Integer.parseInt(fullRugString.substring(1, 3), 10);
        Tile tile1=new Tile(Integer.parseInt(fullRugString.charAt(3)+"",10),Integer.parseInt(fullRugString.charAt(4)+"",10));
        Tile tile2=new Tile(Integer.parseInt(fullRugString.charAt(5)+"",10),Integer.parseInt(fullRugString.charAt(6)+"",10));
        this.rugTiles = new Tile[]{tile1, tile2};
    }
}