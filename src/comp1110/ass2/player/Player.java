package comp1110.ass2.player;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int dirham = 30;
    private int numOfUnplacedRugs = 15;
    private List placedRugs = new ArrayList<Rug>();
    private Colour colour;

    public Player(Colour colour) {
        this.colour = colour;
    }

    public int getDirham() {
        return dirham;
    }

    public int getNumOfUnplacedRugs() {
        return this.numOfUnplacedRugs;
    }

    public void makePayment(Player p, int amount) {
        this.dirham -= amount;
        p.dirham += amount;
    }
}