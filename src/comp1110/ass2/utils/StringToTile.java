package comp1110.ass2.utils;

import comp1110.ass2.board.Tile;

public class StringToTile {
    /**
     * (reserved for rug placement validation only)
     * @param tiles a 2D array of tiles to obtain the tile from
     * @param coordinates string representation of the tile's coordinates on the board
     * @return the tile corresponding to the provided coordinates
     */
    public static Tile getTileFromString(Tile[][] tiles, String coordinates) {
        int[] indices = getIndicesFromString(coordinates);
        return getTileFromIndices(tiles, indices);
    }

    /**
     *(reserved for rug string validation only)
     * @param coordinates string representation of the tile's coordinates on the board
     * @return a tile having those coordinates
     */
    public static Tile getTileFromString(String coordinates) {
        int[] indices = getIndicesFromString(coordinates);
        return new Tile(indices[0], indices[1]);
    }

    /**
     * @param coordinates string representation of coordinates on the board
     * @return the corresponding row and column indices of a 2D array
     */
    private static int[] getIndicesFromString(String coordinates) {
        int row = Integer.parseInt(coordinates.charAt(1) + "", 10);
        int col = Integer.parseInt(coordinates.charAt(0) + "", 10);
        return new int[]{row, col};
    }

    /**
     * @param tiles a 2D array of tiles to obtain the tile from
     * @param indices row and column indices of the tile
     * @return the tile corresponding to the provided indices
     */
    private static Tile getTileFromIndices(Tile[][] tiles, int[] indices) {
        return tiles[indices[0]][indices[1]];
    }
}
