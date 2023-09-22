package comp1110.ass2.utils;

import comp1110.ass2.board.Tile;

public class StringToTile {
    public static Tile getTileFromString(Tile[][] tiles, String coordinates) {
        int[] indices = StringToTile.getIndicesFromString(coordinates);
        return StringToTile.getTileFromIndices(tiles, indices);
    }

    public static Tile getTileFromString(String coordinates) {
        int[] indices = StringToTile.getIndicesFromString(coordinates);
        return new Tile(indices[0], indices[1]);
    }

    private static int[] getIndicesFromString(String coordinates) {
        int row = Integer.parseInt(coordinates.charAt(1) + "", 10);
        int col = Integer.parseInt(coordinates.charAt(0) + "", 10);
        return new int[]{row, col};
    }

    private static Tile getTileFromIndices(Tile[][] tiles, int[] indices) {
        return tiles[indices[0]][indices[1]];
    }
}
