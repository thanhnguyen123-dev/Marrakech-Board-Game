package comp1110.ass2.player;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int dirham = 30;
    private int availableRugs = 15 ;
    private List placedRugs = new ArrayList<Rug>();
    private Colour colour;

    public Player(Colour colour) {
        this.colour = colour;
    }

    public int getDirham() {
        return dirham;
    }

    public int getRug() {
        return availableRugs;
    }

    public void adjustDirham(int offsetDirham) {
        dirham += offsetDirham;
    }



}
