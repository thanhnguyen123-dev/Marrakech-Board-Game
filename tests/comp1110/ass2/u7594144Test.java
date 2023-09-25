package comp1110.ass2;

import comp1110.ass2.board.Tile;
import comp1110.ass2.player.Colour;
import comp1110.ass2.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class u7594144Test {
    @Test
    //
    public void findPlayerTest() {
        Player playerRed = new Player(Colour.RED);
        Player playerCyan = new Player(Colour.CYAN);
        Player playerPurple = new Player(Colour.PURPLE);
        Player[] players = {playerRed, playerCyan, playerPurple};
        GameState gameState = new GameState(players);
        Assertions.assertEquals(null, gameState.findPlayer(Colour.YELLOW));
        Assertions.assertEquals(playerRed, gameState.findPlayer(Colour.RED));
        Assertions.assertEquals(playerCyan, gameState.findPlayer(Colour.CYAN));
        Assertions.assertNotEquals(null, gameState.findPlayer(Colour.RED));
    }

    @Test
    public void isAdjacentTest() {
        Assertions.assertTrue(new Tile(2, 4).isAdjacent(new Tile(2, 3)));
        Assertions.assertFalse(new Tile(2, 0).isAdjacent(new Tile(2, 2)));
        Assertions.assertTrue(new Tile(1, 3).isAdjacent(new Tile(1, 4)));
        Assertions.assertFalse(new Tile(2, 5).isAdjacent(new Tile(2, 7)));
        Assertions.assertFalse(new Tile(2, 3).isAdjacent(new Tile(3, 4)));
        Assertions.assertTrue(new Tile(-1, 3).isAdjacent(new Tile(-1, 4)));
        Assertions.assertFalse(new Tile(-1, 3).isAdjacent(new Tile(-1, 5)));

    }

    @Test
    public void payTest() {
        Player player1 = new Player(Colour.RED);
        Player player2 = new Player(Colour.YELLOW);
        player1.pay(player2, 5);
        Assertions.assertTrue(player1.getDirham() == 25 && player2.getDirham() == 35);
        Player player3 = new Player(Colour.CYAN);
        player3.pay(null, 5);
        Assertions.assertTrue(player3.getDirham() == 30);
        Player player4 = new Player(Colour.PURPLE);
        Player player5 = new Player(Colour.RED);
        player4.pay(player5, -2);
        Assertions.assertTrue(player4.getDirham() == 32 && player5.getDirham() == 28);
        Player player6 = new Player(Colour.PURPLE);
        Player player7 = new Player(Colour.RED);
        player6.pay(player7, 35);
        Assertions.assertTrue(player6.getDirham() == -5 && player7.getDirham() == 65);
        Player player8 = new Player(Colour.RED);
        Player player9 = new Player(Colour.PURPLE);
        player8.pay(player9, 0);
        Assertions.assertTrue(player8.getDirham() == 30 && player9.getDirham() == 30);

    }
}