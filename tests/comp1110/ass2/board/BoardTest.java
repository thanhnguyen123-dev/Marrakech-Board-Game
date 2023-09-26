package comp1110.ass2.board;

import comp1110.ass2.player.Colour;
import comp1110.ass2.player.Rug;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void placeRug() {
        Board board = new Board();
        Rug rug1 = new Rug(Colour.PURPLE, 2, new Tile[]{board.getTiles()[1][2], board.getTiles()[1][3]});
        board.placeRug(rug1);
        List<Rug> rugs = board.getPlacedRugs();
        Assertions.assertEquals(rug1, rugs.get(rugs.size() - 1));
        Assertions.assertEquals(rug1, board.getTiles()[1][2].getTopRug());
        Assertions.assertEquals(rug1, board.getTiles()[1][3].getTopRug());
        Rug rug2 = new Rug(Colour.YELLOW, 6, new Tile[]{board.getTiles()[4][3], board.getTiles()[5][3]});
        board.placeRug(rug2);
        rugs = board.getPlacedRugs();
        Assertions.assertEquals(rug2, rugs.get(rugs.size() - 1));
        Assertions.assertEquals(rug2, board.getTiles()[4][3].getTopRug());
        Assertions.assertEquals(rug2, board.getTiles()[5][3].getTopRug());
        Rug rug3 = new Rug(Colour.RED, 23, new Tile[]{board.getTiles()[0][5], board.getTiles()[0][6]});
        board.placeRug(rug3);
        rugs = board.getPlacedRugs();
        Assertions.assertEquals(rug3, rugs.get(rugs.size() - 1));
        Assertions.assertEquals(rug3, board.getTiles()[0][5].getTopRug());
        Assertions.assertEquals(rug3, board.getTiles()[0][6].getTopRug());
    }

    @Test
    void generateAssamString() {
    }

    @Test
    void generateBoardString() {
    }

    @Test
    void isTileValid() {
    }
}