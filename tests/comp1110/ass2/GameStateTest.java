package comp1110.ass2;

import comp1110.ass2.board.Tile;
import comp1110.ass2.player.Colour;
import comp1110.ass2.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {
    Player player1 = new Player(Colour.YELLOW);
    Player player2 = new Player(Colour.RED);
    Player player3 = new Player(Colour.CYAN);
    Player player4 = new Player(Colour.PURPLE);
    Player[] players = new Player[]{this.player1, this.player2, this.player3, this.player4};

    @Test
    void nextPlayer() {
        GameState gameState = new GameState(this.players);
        Assertions.assertEquals(this.player1, gameState.getCurrentPlayer());
        gameState.nextPlayer();
        Assertions.assertEquals(this.player2, gameState.getCurrentPlayer());
        gameState.nextPlayer();
        Assertions.assertEquals(this.player3, gameState.getCurrentPlayer());
        gameState.nextPlayer();
        Assertions.assertEquals(this.player4, gameState.getCurrentPlayer());
        gameState.nextPlayer();
        Assertions.assertEquals(this.player1, gameState.getCurrentPlayer());
    }

    @Test
    void removeCurrentPlayer() {
        GameState gameState = new GameState(this.players);
        Assertions.assertEquals(this.player1, gameState.getCurrentPlayer());
        gameState.removeCurrentPlayer();
        Assertions.assertEquals(this.player1, gameState.getCurrentPlayer());
        Assertions.assertFalse(gameState.isPlayerAvailable(gameState.getCurrentPlayer()));
    }

    @Test
    void isPlayerAvailable() {
        GameState gameState = new GameState(this.players);
        for (Player player : gameState.getPlayers()) {
            Assertions.assertTrue(gameState.isPlayerAvailable(player));
        }
    }

    @Test
    void isGameOver() {
    }

    @Test
    void findPlayer() {
        Player playerRed = new Player(Colour.RED);
        Player playerCyan = new Player(Colour.CYAN);
        Player playerPurple = new Player(Colour.PURPLE);
        Player[] players = {playerRed, playerCyan, playerPurple};
        GameState gameState = new GameState(players);
        Assertions.assertEquals(null, gameState.findPlayer(Colour.YELLOW));
        Assertions.assertEquals(playerRed, gameState.findPlayer(Colour.RED));
        Assertions.assertEquals(playerCyan, gameState.findPlayer(Colour.CYAN));
        Assertions.assertNotEquals(null, gameState.findPlayer(Colour.RED));
    }


    @Test
    void findAssamRugOwner() {
    }

    @Test
    void isPaymentRequired() {
    }

    @Test
    void isPaymentAffordable() {
    }

    @Test
    void makePayment() {
    }

    @Test
    void makePlacement() {
    }

    @Test
    void isGameStringValid() {
    }

    @Test
    void getPlayerStrings() {
    }

    @Test
    void getAssamString() {
    }

    @Test
    void getBoardString() {
    }

    @Test
    void generateGameString() {
    }
}