package comp1110.ass2.player;

/**
 * Player class contains properties of each Player
 */
public class Player {
    private int dirham = 30;
    private int numOfUnplacedRugs = 15;
    private Colour colour;

    public Player(Colour colour) {
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
}