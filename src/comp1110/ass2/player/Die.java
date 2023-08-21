package comp1110.ass2.player;

import java.util.Random;

public class Die {
    private static final int[] SIDES = {1, 2, 2 , 3, 3, 4};

    /**
     * randomize die roll
     * @return a value from 1-4
     */
    public static int getSide() {
        int index = new Random().nextInt(SIDES.length);
        return SIDES[index];
    }


}
