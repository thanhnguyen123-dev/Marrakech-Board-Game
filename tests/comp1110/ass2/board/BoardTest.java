package comp1110.ass2.board;

import comp1110.ass2.player.Colour;
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
        // Test initial situation
        Board board = new Board();
        assertEquals(board.generateAssamString(), "A33N");
    }

    @Test
    void generateBoardString() {
        // Test initial situation
        Board board = new Board();
        assertEquals(board.generateBoardString(), "Bn00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00");
    }

    @Test
    void isTileValid() {
        Random random = new Random();
        int row = random.nextInt(Board.NUM_OF_ROWS);
        int col = random.nextInt(Board.NUM_OF_COLS);
        Assertions.assertTrue(Board.isTileValid(new Tile(row, col)));
    }
}