package comp1110.ass2.board;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {

    @Test
    void rotate() {
        Direction north = Direction.NORTH;
        Direction east = Direction.EAST;
        Direction south = Direction.SOUTH;
        Direction west = Direction.WEST;
        // Test rotating 90 degrees clockwise
        assertEquals(Direction.EAST, north.rotate(90));
        assertEquals(Direction.SOUTH, east.rotate(90));
        assertEquals(Direction.WEST, south.rotate(90));
        assertEquals(Direction.NORTH, west.rotate(90));
        // Test rotating 180 degrees
        assertEquals(Direction.SOUTH, north.rotate(180));
        assertEquals(Direction.WEST, east.rotate(180));
        assertEquals(Direction.NORTH, south.rotate(180));
        assertEquals(Direction.EAST, west.rotate(180));
        // Test rotating 270 degrees clockwise
        assertEquals(Direction.WEST, north.rotate(270));
        assertEquals(Direction.NORTH, east.rotate(270));
        assertEquals(Direction.EAST, south.rotate(270));
        assertEquals(Direction.SOUTH, west.rotate(270));
    }

    @Test
    void angleToDirection() {
        // Normal test
        assertEquals(Direction.NORTH, Direction.angleToDirection(0));
        assertEquals(Direction.EAST, Direction.angleToDirection(90));
        assertEquals(Direction.SOUTH, Direction.angleToDirection(180));
        assertEquals(Direction.WEST, Direction.angleToDirection(270));
        // Should wrap around
        assertEquals(Direction.NORTH, Direction.angleToDirection(360));
        // Invalid angle
        Random random = new Random();
        int angle = random.nextInt(1, 89);
        assertNull(Direction.angleToDirection(angle));
    }

    @Test
    void charToDirection() {
        // Normal test
        assertEquals(Direction.NORTH, Direction.charToDirection('N'));
        assertEquals(Direction.EAST, Direction.charToDirection('E'));
        assertEquals(Direction.SOUTH, Direction.charToDirection('S'));
        assertEquals(Direction.WEST, Direction.charToDirection('W'));
        // Invalid direction
        for (int i = 0; i < 26; i++) {
            char direction = (char) (i + 65);
            if (direction != 'N' && direction != 'E' && direction != 'S' && direction != 'W') {
                Assertions.assertNull(Direction.charToDirection(direction));
            }
        }
    }
}