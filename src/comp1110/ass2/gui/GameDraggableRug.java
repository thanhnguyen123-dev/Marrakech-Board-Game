package comp1110.ass2.gui;

import comp1110.ass2.player.Colour;
import comp1110.ass2.player.Rug;
import comp1110.ass2.gui.Game.Orientation;

import java.util.Comparator;

/**
 * a custom game rug class used for rug placement inside Game class
 * @author u7620014 Haobo Zou
 */
class GameDraggableRug extends GameRug {
    private final Game game;
    private Orientation orientation = Orientation.VERTICAL;
    private final Colour colour;
    private double mouseX, mouseY;

    /**
     * Constructor: creates an instance of the GameDraggableRug class
     * @param game an instance of the Game class
     * @param x the x-axis relocation
     * @param y the y-axis relocation
     * @param colour the colour of the draggable rug
     * @author u7620014 Haobo Zou
     */
    public GameDraggableRug(Game game, double x, double y, Colour colour) {
        super(x, y, Orientation.VERTICAL, colour);
        this.game = game;
        this.colour = colour;

        this.setOnMousePressed(event -> {
            this.toFront();
            this.mouseX = event.getSceneX();
            this.mouseY = event.getSceneY();
        });

        this.setOnMouseDragged(event -> {
            double movementX = event.getSceneX() - this.mouseX;
            double movementY = event.getSceneY() - this.mouseY;
            this.setLayoutX(this.getLayoutX() + movementX);
            this.setLayoutY(this.getLayoutY() + movementY);
            this.mouseX = event.getSceneX();
            this.mouseY = event.getSceneY();
            highlightNearestInvisibleRug();
        });

        this.setOnMouseReleased(event -> {
            GameInvisibleRug nearestGameInvisibleRug = findNearestInvisibleRug();
            if (nearestGameInvisibleRug != null) {
                this.setLayoutX(nearestGameInvisibleRug.getLayoutX() + Game.TILE_RELOCATION_X + Game.MARGIN_LEFT);
                this.setLayoutY(nearestGameInvisibleRug.getLayoutY() + Game.TILE_RELOCATION_Y + Game.MARGIN_TOP);
            }
        });
    }

    /**
     * getter method for orientation
     * @return the orientation of the draggable rug
     * @author u7620014 Haobo Zou
     */
    public Orientation getOrientation() {
        return this.orientation;
    }

    /**
     * setter method for orientation
     * @param orientation the new orientation of the draggable rug
     * @author u7620014 Haobo Zou
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * getter method for colour
     * @return the colour of the draggable rug
     * @author u7620014 Haobo Zou
     */
    public Colour getColour() {
        return this.colour;
    }

    /**
     * Finds the nearest valid rug placement represented with an invisible rug,
     * which has the same orientation as the draggable rug (if there is none, returns null)
     * @return the nearest valid rug placement represented with an invisible rug, null if it does not exist
     * @author u7620014 Haobo Zou
     */
    private GameInvisibleRug findNearestInvisibleRug() {
        if (this.getOrientation() == Orientation.VERTICAL) {
            this.game.vGameInvisibleRugs.sort(compareByDistance());
            for (GameInvisibleRug vGameInvisibleRug : this.game.vGameInvisibleRugs) {
                Rug rug = new Rug(this.game.gameDraggableRug.getColour(), this.game.rugID, this.game.getTilesFromInvisibleRug(vGameInvisibleRug));
                if (this.game.gameState.getBoard().isPlacementValid(rug)) {
                    return vGameInvisibleRug;
                }
            }
        }
        this.game.hGameInvisibleRugs.sort(compareByDistance());
        for (GameInvisibleRug hGameInvisibleRug : this.game.hGameInvisibleRugs) {
            Rug rug = new Rug(this.game.gameDraggableRug.getColour(), this.game.rugID, this.game.getTilesFromInvisibleRug(hGameInvisibleRug));
            if (this.game.gameState.getBoard().isPlacementValid(rug)) {
                return hGameInvisibleRug;
            }
        }
        return null;
    }

    /**
     * Highlights the nearest valid rug placement represented with an invisible rug,
     * by setting the opacity of the previously highlighted invisible rug (if any) to zero,
     * and setting the opacity of the new one (if any) to a non-zero value
     * @author u7620014 Haobo Zou
     */
    private void highlightNearestInvisibleRug() {
        if (this.game.highlighted != null) {
            this.game.highlighted.setOpacity(0);
        }
        this.game.highlighted = findNearestInvisibleRug();
        if (this.game.highlighted != null) {
            this.game.highlighted.setOpacity(Game.HIGHLIGHTED_OPACITY);
        }
    }

    /**
     * a comparator used for sorting invisible rugs by their distances to the draggable rug
     * @return a comparison function
     * @author u7620014 Haobo Zou
     */
    public Comparator<GameInvisibleRug> compareByDistance() {
        return new Comparator<>() {
            @Override
            public int compare(GameInvisibleRug rug1, GameInvisibleRug rug2) {
                return Double.compare(
                        rug1.distance(GameDraggableRug.this.getLayoutX() - Game.TILE_RELOCATION_X - Game.MARGIN_LEFT, GameDraggableRug.this.getLayoutY() - Game.TILE_RELOCATION_Y - Game.MARGIN_TOP),
                        rug2.distance(GameDraggableRug.this.getLayoutX() - Game.TILE_RELOCATION_X - Game.MARGIN_LEFT, GameDraggableRug.this.getLayoutY() - Game.TILE_RELOCATION_Y - Game.MARGIN_TOP));
            }
        };
    }
}
