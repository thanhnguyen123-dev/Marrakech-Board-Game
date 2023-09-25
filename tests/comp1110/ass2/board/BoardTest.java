package comp1110.ass2.board;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void isTileValid() {
        Random random=new Random();
        int row=random.nextInt(Board.NUM_OF_ROWS);
        int col=random.nextInt(Board.NUM_OF_COLS);
        Assertions.assertTrue(Board.isTileValid(new Tile(row,col)));
    }
}