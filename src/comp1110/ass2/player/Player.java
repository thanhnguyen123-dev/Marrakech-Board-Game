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
     * @param colour the Colour of the Player
     */
    public Player(Colour colour) {
        this.dirham = 30;
        this.numOfUnplacedRugs = 15;
        this.colour = colour;
    }

    /**
     * @return the amount of dirhams the Player has
     */
    public int getDirham() {
        return dirham;
    }

    /**
     * @return the number of unplaced Rugs the Player has
     */
    public int getNumOfUnplacedRugs() {
        return numOfUnplacedRugs;
    }

    /**
     * @return the Colour of the Player
     */
    public Colour getColour() {
        return this.colour;
    }

    /**
     * Places a Rug on the Board, number of unplaced Rugs decreases by 1
     */
    public void placeRug() {
        this.numOfUnplacedRugs--;
    }

    /**
     * Pays a certain amount of dirhams to the other Player
     * @param otherPlayer the other Player
     * @param amount amount of dirhams to be paid
     */
    public void makePayment(Player otherPlayer, int amount) {
        this.dirham -= amount;
        otherPlayer.dirham += amount;
    }

    /**
     * Constructor: creates an instance of the Player class
     * Decode the playerString to get corresponding values for instance fields
     * @param playerString string representation for the Player
     */
    public Player(String playerString) {
        this.dirham = Integer.parseInt(playerString.substring(1, 4), 10);
        this.numOfUnplacedRugs = Integer.parseInt(playerString.substring(4, 6), 10);
        this.colour = Colour.charToColour(playerString.charAt(0));
    }
}