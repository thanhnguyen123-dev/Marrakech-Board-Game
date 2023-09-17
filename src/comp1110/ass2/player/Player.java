package comp1110.ass2.player;

/**
 * Player class contains properties of each Player
 */
public class Player {
    private int dirham;
    private int numOfUnplacedRugs;
    private final Colour colour;

    /**
     * Constructor: creates an instance of the Player class
     * @param colour the colour of the player's rugs
     */
    public Player(Colour colour) {
        this.dirham = 30;
        this.numOfUnplacedRugs = 15;
        this.colour = colour;
    }

    /**
     * getter method for dirham
     * @return the amount of dirhams that the player has
     */
    public int getDirham() {
        return dirham;
    }

    /**
     * getter method for numOfUnplacedRugs
     * @return the number of unplaced rugs that the player has
     */
    public int getNumOfUnplacedRugs() {
        return numOfUnplacedRugs;
    }

    /**
     * getter method for colour
     * @return the colour of the player's rugs
     */
    public Colour getColour() {
        return this.colour;
    }

    /**
     * Places a rug on the board, number of unplaced rugs decreases by 1
     */
    public void placeRug() {
        this.numOfUnplacedRugs--;
    }

    /**
     * Pays a certain amount of dirhams to the other player
     * @param otherPlayer the other player
     * @param amount amount of dirhams to be paid
     */
    public void makePayment(Player otherPlayer, int amount) {
        this.dirham -= amount;
        otherPlayer.dirham += amount;
    }

    /**
     * Constructor: creates an instance of the Player class
     * Decodes the playerString to get corresponding values for instance fields
     * @param playerString string representation for the player
     */
    public Player(String playerString) {
        this.dirham = Integer.parseInt(playerString.substring(1, 4), 10);
        this.numOfUnplacedRugs = Integer.parseInt(playerString.substring(4, 6), 10);
        this.colour = Colour.charToColour(playerString.charAt(0));
    }

    /**
     * Generates playerString based on data of the player
     * @return string representation for the player
     */
    public static String generatePlayerString(Player player) {
        return "P" + player.colour.colourChar + String.format("%03d", player.dirham) + String.format("%02d", player.numOfUnplacedRugs);
    }
}