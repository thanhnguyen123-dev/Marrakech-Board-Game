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
     * @author u7620014 Haobo Zou
     */
    public void placeRug() {
        this.numOfUnplacedRugs--;
    }

    /**
     * Pays a certain amount of dirhams to the other player
     * @param that the other player
     * @param amount amount of dirhams to be paid
     * @author u7620014 Haobo Zou
     */
    public void pay(Player that, int amount) {
        if (that != null) {
            this.dirham -= amount;
            that.dirham += amount;
        }
    }

    /**
     * Constructor: creates an instance of the Player class
     * Decodes the playerString to get corresponding values for instance fields
     * @param playerString string representation for the player
     * @author u7620014 Haobo Zou
     */
    public Player(String playerString) {
        this.dirham = Integer.parseInt(playerString.substring(2, 5), 10);
        this.numOfUnplacedRugs = Integer.parseInt(playerString.substring(5, 7), 10);
        this.colour = Colour.charToColour(playerString.charAt(1));
    }

    /**
     * Generates playerString based on data of the player
     * @return string representation for the player
     * @author u7620014 Haobo Zou
     */
    public String generatePlayerString() {
        return "P" + this.colour.getColourChar() + String.format("%03d", this.dirham) + String.format("%02d", this.numOfUnplacedRugs);
    }
}