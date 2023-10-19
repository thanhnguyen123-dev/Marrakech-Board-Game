package comp1110.ass2.player;

/**
 * Player class contains properties of each player
 * @author u7594144 Le Thanh Nguyen, u7582846 Yaolin Li, u7620014 Haobo Zou
 */
public class Player {
    private int dirham;
    private int numOfUnplacedRugs;
    private final Colour colour;
    private boolean isComputer;
    private Strategy strategy;

    /**
     * Constructor: creates an instance of the Player class
     * @param colour the colour of the player's rugs
     * @author Le Thanh Nguyen u7594144
     */
    public Player(Colour colour) {
        this.dirham = 30;
        this.numOfUnplacedRugs = 15;
        this.colour = colour;
        // Default is human player
        this.isComputer = false;
        this.strategy = null;
    }

    /**
     * getter method for dirham
     * @return the amount of dirhams possessed by the player
     * @author Le Thanh Nguyen u7594144
     */
    public int getDirham() {
        return dirham;
    }

    /**
     * getter method for numOfUnplacedRugs
     * @return the number of unplaced rugs possessed by the player
     * @author Le Thanh Nguyen u7594144
     */
    public int getNumOfUnplacedRugs() {
        return numOfUnplacedRugs;
    }

    /**
     * getter method for colour
     * @return the colour of the player's rugs
     * @author u7620014 Haobo Zou
     */
    public Colour getColour() {
        return this.colour;
    }

    /**
     * getter method for isComputer
     * @return true if the player is a computer player, false if the player is a human player
     * @author u7620014 Haobo Zou
     */
    public boolean isComputer() {
        return this.isComputer;
    }

    /**
     * setter method for isComputer
     * @param isComputer if the player is a computer player or not
     * @author u7620014 Haobo Zou
     */
    public void setIsComputer(boolean isComputer) {
        this.isComputer = isComputer;
    }

    /**
     * getter method for strategy
     * @return the strategy used by the player, null if the player is a human player
     * @author u7620014 Haobo Zou
     */
    public Strategy getStrategy() {
        return this.strategy;
    }

    /**
     * setter method for strategy
     * @param strategy the strategy used by the player, can be null if the player is a human player
     * @author u7620014 Haobo Zou
     */
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
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

    /**
     * Strategy enum represents different strategies used by the computer player
     * @author u7620014 Haobo Zou
     */
    public enum Strategy {
        RANDOM,
        INTELLIGENT;

        /**
         * Converts a Strategy enum to a string of its full name
         * @return full name of the strategy
         * @author u7620014 Haobo Zou
         */
        @Override
        public String toString() {
            return switch (this) {
                case RANDOM -> "RANDOM";
                case INTELLIGENT -> "INTELLIGENT";
            };
        }
    }
}