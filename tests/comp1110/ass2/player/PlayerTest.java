package comp1110.ass2.player;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    public void pay() {
        Player player1 = new Player(Colour.RED);
        Player player2 = new Player(Colour.YELLOW);
        player1.pay(player2, 5);
        Assertions.assertTrue(player1.getDirham() == 25 && player2.getDirham() == 35);
        Player player3 = new Player(Colour.CYAN);
        player3.pay(null, 5);
        Assertions.assertTrue(player3.getDirham() == 30);
        Player player4 = new Player(Colour.PURPLE);
        Player player5 = new Player(Colour.RED);
        player4.pay(player5, 2);
        Assertions.assertTrue(player4.getDirham() == 28 && player5.getDirham() == 32);
        Player player6 = new Player(Colour.PURPLE);
        Player player7 = new Player(Colour.RED);
        player6.pay(player7, 3);
        Assertions.assertTrue(player6.getDirham() == 27 && player7.getDirham() == 33);
        Player player8 = new Player(Colour.RED);
        Player player9 = new Player(Colour.PURPLE);
        player8.pay(player9, 0);
        Assertions.assertTrue(player8.getDirham() == 30 && player9.getDirham() == 30);
    }
}