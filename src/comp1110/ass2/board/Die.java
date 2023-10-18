package comp1110.ass2.board;

import java.util.Random;

/**
 * Representation of Die
 * @author Le Thanh Nguyen u7594144
 */
public class Die {
    private static final int[] SIDES = {1, 2, 2, 3, 3, 4};

    /**
     * randomize die roll
     * @return a value from 1-4
     * @author Le Thanh Nguyen u7594144
     */
    public static int getSide() {
        int index = new Random().nextInt(SIDES.length);
        return SIDES[index];
    }
}
