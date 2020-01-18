package Model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Comparator;

/**
 * Used to create different spaces on the game board
 * Holds the coordinates for each space
 */
public class Space extends Rectangle {
    private int xCoordinate, yCoordinate;
    private Ship ship = null; //Check if there is a ship in the space
    private Board board;
    boolean shot = false;

    public Space(int x, int y, Board board) {
        super(30, 30);
        this.xCoordinate = x;
        this.yCoordinate = y;
        this.board = board;

        //Set colour of space
        resetSpaceColour();
    }

    public boolean isShot() {
        return shot;
    }

    public void setShot(boolean shot) {
        this.shot = shot;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Ship getShip() {
        return ship;
    }

    public void setXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public void setYCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    /**
     * Method to change the colour of the Ship when it has been shot
     * Will change the space to grey if there is no ship in that space
     * Will change the space to red if there is a ship present
     *
     * @return boolean to determine if a ship was hit
     */
    public boolean shootBoard() {
        //this.setShot(true);
        this.shot = true;
        setFill(Color.GREY);
        setStroke(Color.BLACK);

        //If a ship was hit locally
        if (getShip() != null) {
            ship.hit();
            setFill(Color.PALEVIOLETRED);

            //Check if ship has been destroyed
            if (ship.isDestroyed()) board.setNumShips((board.getNumShips() - 1));

            return true;
        }

        return false;
    }

    /**
     * Method will change the colour of the opponent's board space (Player who shot)
     */
    public void isHit() {
        setFill(Color.PALEVIOLETRED);
    }

    /**
     * Set/reset space's colour to lightblue with a black outline
     */
    public void resetSpaceColour() {
        setFill(Color.LIGHTBLUE);
        setStroke(Color.BLACK);
    }

    /**
     * Set space colour to white with green outline if there is a ship present
     */
    public void setSpaceToShipColour() {
        setFill(Color.WHITE);
        setStroke(Color.GREEN);
    }

    /**
     * Deprecated method used to sort an array list of spaces by the x coordinate
     */
    public static Comparator<Space> xCoordinateCompare = (space1, space2) -> {
        int space1X = space1.getXCoordinate();
        int space2X = space2.getXCoordinate();

        return space1X - space2X;
    };

    /**
     * Deprecated method used to sort an array list of spaces by the y coordinate
     */
    public static Comparator<Space> yCoordinateCompare = (space1, space2) -> {
        int space1Y = space1.getYCoordinate();
        int space2Y = space2.getYCoordinate();

        return space1Y - space2Y;
    };

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("xCoordinate: ").append(xCoordinate);
        sb.append("\tyCoordinate: ").append(yCoordinate);
        sb.append("\n");

        return sb.toString();
    }
}
