package comp1110.ass2.board;

import comp1110.ass2.GameState;
import comp1110.ass2.player.Colour;
import comp1110.ass2.player.Player;
import comp1110.ass2.player.Rug;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void placeRug() {
        Board board = new Board();
        Rug rug1 = new Rug(Colour.PURPLE, 2, new Tile[]{board.getTiles()[1][2], board.getTiles()[1][3]});
        board.placeRug(rug1);
        Assertions.assertEquals(rug1, board.getTiles()[1][2].getTopRug());
        Assertions.assertEquals(rug1, board.getTiles()[1][3].getTopRug());
        Rug rug2 = new Rug(Colour.YELLOW, 6, new Tile[]{board.getTiles()[4][3], board.getTiles()[5][3]});
        board.placeRug(rug2);
        Assertions.assertEquals(rug2, board.getTiles()[4][3].getTopRug());
        Assertions.assertEquals(rug2, board.getTiles()[5][3].getTopRug());
        Rug rug3 = new Rug(Colour.RED, 23, new Tile[]{board.getTiles()[0][5], board.getTiles()[0][6]});
        board.placeRug(rug3);
        Assertions.assertEquals(rug3, board.getTiles()[0][5].getTopRug());
        Assertions.assertEquals(rug3, board.getTiles()[0][6].getTopRug());
    }

    @Test
    void generateAssamString() {
        Player playerRed = new Player(Colour.RED);
        Player playerPurple = new Player(Colour.PURPLE);
        Player[] players = {playerRed, playerPurple};
        GameState gameState = new GameState(players);
        Board board = gameState.getBoard();
        // Test initial situation
        assertEquals("A33N", board.generateAssamString());
        // Test after Assam move
        gameState.rotateAssam(90);
        gameState.moveAssam(3);
        assertEquals("A63E", board.generateAssamString());
        // Test Assam move out of bound
        gameState.moveAssam(2);
        assertEquals("A54W", board.generateAssamString());
    }

    @Test
    void generateBoardString() {
        Player playerRed = new Player(Colour.RED);
        Player playerPurple = new Player(Colour.PURPLE);
        Player[] players = {playerRed, playerPurple};
        GameState gameState = new GameState(players);
        // Test initial situation
        Board board = gameState.getBoard();
        assertEquals("Bn00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00", board.generateBoardString());
        // Test after place rug
        Rug rug = new Rug(Colour.RED, 01, new Tile[]{board.getTiles()[1][2], board.getTiles()[1][3]});
        board.placeRug(rug);
        assertEquals("Bn00n00n00n00n00n00n00n00n00n00n00n00n00n00n00r01n00n00n00n00n00n00r01n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00", board.generateBoardString());
        Rug rug1 = new Rug(Colour.PURPLE, 01, new Tile[]{board.getTiles()[3][3], board.getTiles()[3][4]});
        board.placeRug(rug1);
        assertEquals("Bn00n00n00n00n00n00n00n00n00n00n00n00n00n00n00r01n00n00n00n00n00n00r01n00p01n00n00n00n00n00n00p01n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00", board.generateBoardString());
    }

    @Test
    void isTileValid() {
        Random random = new Random();
        int row = random.nextInt(Board.NUM_OF_ROWS);
        int col = random.nextInt(Board.NUM_OF_COLS);
        Assertions.assertTrue(Board.isTileValid(new Tile(row, col)));
    }
}