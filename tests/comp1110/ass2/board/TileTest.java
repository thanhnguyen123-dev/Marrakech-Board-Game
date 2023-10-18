package comp1110.ass2.board;

import comp1110.ass2.player.Rug;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {

    @Test
    void isAdjacent() {
        Assertions.assertTrue(new Tile(2, 4).isAdjacent(new Tile(2, 3)));
        Assertions.assertFalse(new Tile(2, 0).isAdjacent(new Tile(2, 2)));
        Assertions.assertTrue(new Tile(1, 3).isAdjacent(new Tile(1, 4)));
        Assertions.assertFalse(new Tile(2, 5).isAdjacent(new Tile(2, 7)));
        Assertions.assertFalse(new Tile(2, 3).isAdjacent(new Tile(3, 4)));
        Assertions.assertTrue(new Tile(-1, 3).isAdjacent(new Tile(-1, 4)));
        Assertions.assertFalse(new Tile(-1, 3).isAdjacent(new Tile(-1, 5)));
    }

    @Test
    void hasRug() {
        Random random = new Random();
        Tile tile = new Tile(random.nextInt(6), random.nextInt(6));
        assertFalse(tile.hasRug());
        Rug rug = new Rug("r01", tile);
        tile.setTopRug(rug);
        assertTrue(tile.hasRug());
    }
}