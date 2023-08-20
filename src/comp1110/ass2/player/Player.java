package comp1110.ass2.player;

import java.util.List;
import java.util.ArrayList;

public class Player {
    private int dirhams = 30;
    private int numOfRemainingRugs = 15;
    private List placedRugs = new ArrayList<Rug>();
    private Colour colour;

    public Player(Colour colour){
        this.colour = colour;
    }
}
