package comp1110.ass2.player;

/**
 * Player class defines the state and property of each player
 */
public class Player {
    private int dirham = 30;
    private int numOfUnplacedRugs = 15;
    private Colour colour;

    public Player(Colour colour) {
        this.colour = colour;
    }

    /**
     * Get current number of dirhams of the player
     * @return
     */
    public int getDirham() {
        return dirham;
    }

    /**
     * Get the number of unplaced rugs of player
     * @return
     */
    public int getNumOfUnplacedRugs() {
        return this.numOfUnplacedRugs;
    }

    public Colour getColour() {
        return this.colour;
    }

    public void placeRug() {
        this.numOfUnplacedRugs--;
    }

    /**
     * If Assam is on other player's rug after movement, player need to pay dirhams
     * @param p
     * @param amount
     */
    public void makePayment(Player p, int amount) {
        this.dirham -= amount;
        p.dirham += amount;
    }
}