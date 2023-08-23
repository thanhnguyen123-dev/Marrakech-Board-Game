package comp1110.ass2.player;

/**
 * Player class contains properties of each Player
 */
public class Player {
    private int dirham;
    private int numOfUnplacedRugs;
    private Colour colour;

    public Player(Colour colour) {
        this.dirham = 30;
        this.numOfUnplacedRugs = 15;
        this.colour = colour;
    }

    /**
     * getter method for the current dirhams value of a player
     * @return dirham
     */
    public int getDirham() {
        return dirham;
    }

    /**
     * getter method for the number of unplaced rugs of a player
     * @return numOfUnplacedRugs
     */
    public int getNumOfUnplacedRugs() {
        return numOfUnplacedRugs;
    }

    public Colour getColour() {
        return this.colour;
    }

    public void placeRug() {
        this.numOfUnplacedRugs--;
    }

    /**
     * If Assam is on other player's rug after movement, player need to pay dirhams
     * @param otherPlayer
     * @param amount
     */
    public void makePayment(Player otherPlayer, int amount) {
        this.dirham -= amount;
        otherPlayer.dirham += amount;
    }

    public Player(String playerString) {
        this.dirham = Integer.parseInt(playerString.substring(1, 4), 10);
        this.numOfUnplacedRugs = Integer.parseInt(playerString.substring(4, 6), 10);
        this.colour = Colour.charToColour(playerString.charAt(0));
    }
}