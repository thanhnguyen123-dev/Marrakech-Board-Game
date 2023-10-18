package comp1110.ass2;

import comp1110.ass2.board.Board;
import comp1110.ass2.board.Tile;
import comp1110.ass2.player.Colour;
import comp1110.ass2.player.Player;
import comp1110.ass2.player.Rug;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

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
        GameState gameState = new GameState("Pc03014iPy03015iPp03215iPr02815iA11WBn00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00p00n00n00n00n00n00n00p00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00");
        assertNull(gameState.findAssamRugOwner());
        GameState gameState1 = new GameState("Pc00006oPy01107iPp05107iPr05907iA46WBn00c12n00r14y17y03n00r08r23p22p22y17y03n00r12r23r09r24r24p24c23n00p04c13p08p19n00n00p09p09c13n00r15n00r01n00n00r10r10p01n00r01n00n00r00p10p10p05p05");
        assertEquals(Colour.RED, gameState1.findAssamRugOwner().getColour());
        GameState gameState2 = new GameState("Pc02814iPy03214iPp03214iPr02814iA12WBn00n00y01n00n00n00n00n00n00y01r01n00n00n00n00n00n00r01n00n00n00n00r02n00p00n00n00n00n00r02n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00");
        assertEquals(Colour.YELLOW, gameState2.findAssamRugOwner().getColour());
        GameState gameState3 = new GameState("Pc02813iPy03014iPp03214iPr03014iA21EBn00n00y01n00n00n00n00n00n00y01r01n00n00n00n00c03c03r01n00n00n00n00r02n00p00n00n00n00n00r02n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00");
        assertEquals(Colour.CYAN, gameState3.findAssamRugOwner().getColour());
    }

    @Test
    void isPaymentRequired() {
        Player playerRed = new Player(Colour.RED);
        Player playerPurple = new Player(Colour.PURPLE);
        Player[] players = {playerRed, playerPurple};
        GameState gameState = new GameState(players);
        // Assam on a space tile
        assertFalse(gameState.isPaymentRequired());
        // Assam on other player's rug
        Board board = gameState.getBoard();
        Rug rugPurple = new Rug(Colour.PURPLE, 2, new Tile[]{board.getTiles()[3][2], board.getTiles()[3][3]});
        board.placeRug(rugPurple);
        assertTrue(gameState.isPaymentRequired());
        // Assam on current player's rug
        GameState gameState1 = new GameState(players);
        Rug rugRed = new Rug(Colour.RED, 2, new Tile[]{board.getTiles()[3][2], board.getTiles()[3][3]});
        Board board1 = gameState1.getBoard();
        board1.placeRug(rugRed);
        assertFalse(gameState.isPaymentRequired());
    }

    @Test
    void isPaymentAffordable() {
        // Assam on other player's rug, require payment
        Player playerRed = new Player(Colour.RED);
        Player playerPurple = new Player(Colour.PURPLE);
        Player[] players = {playerRed, playerPurple};
        GameState gameState = new GameState(players);
        Board board = gameState.getBoard();
        Rug rugPurple = new Rug(Colour.PURPLE, 1, new Tile[]{board.getTiles()[3][2], board.getTiles()[3][3]});
        board.placeRug(rugPurple);
        assertEquals(2, gameState.getPaymentAmount());
        Rug rugPurple1 = new Rug(Colour.PURPLE, 2, new Tile[]{board.getTiles()[4][4], board.getTiles()[3][4]});
        board.placeRug(rugPurple1);
        assertEquals(4, gameState.getPaymentAmount());
        Rug rugPurple2 = new Rug(Colour.PURPLE, 3, new Tile[]{board.getTiles()[5][4], board.getTiles()[5][5]});
        board.placeRug(rugPurple2);
        assertEquals(6, gameState.getPaymentAmount());
        Rug rugRed = new Rug(Colour.RED, 1, new Tile[]{board.getTiles()[4][4], board.getTiles()[4][5]});
        board.placeRug(rugRed);
        assertEquals(3, gameState.getPaymentAmount());
    }

    @Test
    void makePayment() {
        Player playerRed = new Player(Colour.RED);
        Player playerPurple = new Player(Colour.PURPLE);
        Player[] players = {playerRed, playerPurple};
        GameState gameState = new GameState(players);
        Board board = gameState.getBoard();
        Rug rugPurple = new Rug(Colour.PURPLE, 1, new Tile[]{board.getTiles()[3][2], board.getTiles()[3][3]});
        board.placeRug(rugPurple);
        gameState.makePayment();
        assertEquals(28, playerRed.getDirham());
        assertEquals(32, playerPurple.getDirham());
        Rug rugPurple1 = new Rug(Colour.PURPLE, 2, new Tile[]{board.getTiles()[4][4], board.getTiles()[3][4]});
        board.placeRug(rugPurple1);
        gameState.makePayment();
        assertEquals(24, playerRed.getDirham());
        assertEquals(36, playerPurple.getDirham());
    }

    @Test
    void makePlacement() {
        Player playerRed = new Player(Colour.RED);
        Player playerPurple = new Player(Colour.PURPLE);
        Player[] players = {playerRed, playerPurple};
        GameState gameState = new GameState(players);
        Board board = gameState.getBoard();
        Random random = new Random();
        int n = random.nextInt(7);
        Rug rug = new Rug(Colour.PURPLE, 1, new Tile[]{board.getTiles()[n][n - 1], board.getTiles()[n][n]});
        gameState.makePlacement(rug);
        Tile tile = board.getTiles()[n][n - 1];
        Tile tile1 = board.getTiles()[n][n - 1];
        assertEquals(rug, tile.getTopRug());
        assertEquals(rug, tile1.getTopRug());
    }
}