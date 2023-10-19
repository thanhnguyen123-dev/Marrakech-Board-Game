package comp1110.ass2.gui;

import javafx.scene.shape.Rectangle;

/**
 * a custom rectangle class used to represent tiles on the board inside Game class
 * @author u7620014 Haobo Zou
 */
class GameTile extends Rectangle {
    private final int row;
    private final int col;

    /**
     * Constructor: creates an instance of the GameTile class
     * @param x the x-axis relocation
     * @param y the y-axis relocation
     * @param row its row index on the board
     * @param col its column index on the board
     * @author u7620014 Haobo Zou
     */
    public GameTile(double x, double y, int row, int col) {
        super(0, 0, Game.TILE_SIDE, Game.TILE_SIDE);
        this.row = row;
        this.col = col;

        this.relocate(x, y);
        this.setFill(Game.TILE_COLOR);
        this.setStroke(Game.TILE_COLOR.darker());
        this.setStrokeWidth(Game.TILE_BORDER_WIDTH);
    }

    /**
     * getter method for row
     * @return the row index
     * @author u7620014 Haobo Zou
     */
    public int getRow() {
        return this.row;
    }

    /**
     * getter method for col
     * @return the column index
     * @author u7620014 Haobo Zou
     */
    public int getCol() {
        return this.col;
    }
}
