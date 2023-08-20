package comp1110.ass2.player;

public class Player {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 4;
    private int dirham;
    private int rug;

    public Player(int dirham, int rug) {
        this.dirham = dirham;
        this.rug = rug;
    }

    public int getDirham() {
        return dirham;
    }

    public int getRug() {
        return rug;
    }

    public void adjustDirham(int offsetDirham) {
        dirham += offsetDirham;
    }





}
