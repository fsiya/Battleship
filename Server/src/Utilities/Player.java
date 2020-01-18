package Utilities;

import java.io.Serializable;

/**
 * Player info that will denote who will go first
 */
public class Player implements Serializable {
    private static final long serialVersionUID = 7774940950737407748L;
    private int playerNum;

    public Player(int playerNum) {
        this.playerNum = playerNum;
    }

    public int getPlayerNum() {
        return playerNum;
    }
}
