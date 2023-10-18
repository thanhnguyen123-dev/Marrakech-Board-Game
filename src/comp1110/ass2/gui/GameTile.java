package comp1110.ass2.gui;

import javafx.scene.shape.Rectangle;

class GameTile extends Rectangle {
    private final int row;
    private final int col;

    public GameTile(double x, double y, int row, int col) {
        super(0, 0, Game.TILE_SIDE, Game.TILE_SIDE);
        this.row = row;
        this.col = col;

        this.relocate(x, y);
        this.setFill(Game.TILE_COLOR);
        this.setStroke(Game.TILE_COLOR.darker());
        this.setStrokeWidth(Game.TILE_BORDER_WIDTH);
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }
}
