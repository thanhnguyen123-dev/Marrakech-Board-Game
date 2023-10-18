package comp1110.ass2.gui;

import comp1110.ass2.player.Colour;
import comp1110.ass2.player.Rug;
import comp1110.ass2.gui.Game.Orientation;

import java.util.Comparator;

class GameDraggableRug extends GameRug {
    private final Game game;
    private Orientation orientation = Orientation.VERTICAL;
    private final Colour colour;
    private double mouseX, mouseY;

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

    public Orientation getOrientation() {
        return this.orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public Colour getColour() {
        return this.colour;
    }

    private GameInvisibleRug findNearestInvisibleRug() {
        if (this.orientation == Orientation.VERTICAL) {
            game.vGameInvisibleRugs.sort(compareByDistance());
            for (GameInvisibleRug vGameInvisibleRug : game.vGameInvisibleRugs) {
                Rug rug = new Rug(game.gameDraggableRug.getColour(), game.rugID, game.getTilesFromInvisibleRug(vGameInvisibleRug));
                if (game.gameState.getBoard().isPlacementValid(rug)) {
                    return vGameInvisibleRug;
                }
            }
        }
        game.hGameInvisibleRugs.sort(compareByDistance());
        for (GameInvisibleRug hGameInvisibleRug : game.hGameInvisibleRugs) {
            Rug rug = new Rug(game.gameDraggableRug.getColour(), game.rugID, game.getTilesFromInvisibleRug(hGameInvisibleRug));
            if (game.gameState.getBoard().isPlacementValid(rug)) {
                return hGameInvisibleRug;
            }
        }
        return null;
    }

    private void highlightNearestInvisibleRug() {
        if (game.highlighted != null) {
            game.highlighted.setOpacity(0);
        }
        game.highlighted = findNearestInvisibleRug();
        if (game.highlighted != null) {
            game.highlighted.setOpacity(Game.HIGHLIGHTED_OPACITY);
        }
    }

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
