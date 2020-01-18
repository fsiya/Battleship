package Model;

import Utilities.Client_Connector;
import Utilities.Coordinates;
import Utilities.Hit;
import View.AlertBox;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for 10x10 game board
 */
public class Board extends Parent {
    private AlertBox ab = new AlertBox();
    private Client_Connector cc;
    private VBox rows = new VBox(); //Num of rows on the board
    private int numShips; //Number of pieces
    private int hitShipCounter = 0;

    /**
     * Constructor will create the base 10x10 board
     *
     * @param player //@param handler
     */
    public Board(boolean player) {
        final int NUM_COLUMNS = 10;
        this.numShips = 0;

        //Create columns
        for (int y = 0; y < NUM_COLUMNS; y++) {
            HBox column = new HBox();

            //Create spaces on board
            for (int x = 0; x < NUM_COLUMNS; x++) {
                Space space = new Space(x, y, this);

                if (player) space.setOnMouseClicked(this::selectSpace); //Reference moveShip method
                else space.setOnMouseClicked(this::shootSpace);
                column.getChildren().add(space); //Add spaces to the hbox row
            }

            rows.getChildren().add(column); //Add columns to the row
        }

        this.getChildren().add(rows); //Add vBox to Board

        //Get client connector
        this.cc = Client_Connector.getClientConnector();
//        else this.cc = null;

        System.out.println("cc" + cc);
    }

    //Setter & getter

    public int getNumShips() {
        return numShips;
    }

    public void setNumShips(int numShips) {
        this.numShips = numShips;
    }

    /**
     * Method will place a ship on the desired space
     *
     * @param ship piece
     * @param x    coordinate
     * @param y    coordinate
     * @return if it was successful
     */
    public boolean placeShipPiece(Ship ship, int x, int y) {
        Space space;

        if (isShipPositionValid(ship, x, y)) {
            if (ship.isVertical()) { //Vertical
                for (int i = y; i < y + ship.getSize(); i++) {
                    space = getSpace(x, i);
                    space.setShip(ship);
                    space.setSpaceToShipColour();
                }

            } else { //Horizontal
                for (int i = x; i < x + ship.getSize(); i++) {
                    space = getSpace(i, y);
                    space.setShip(ship);
                    space.setSpaceToShipColour();
                }
            }

            return true;
        }

        return false;
    }

    /**
     * Method will select a space to place a ship
     * Selected space will be the "head" or "root" of the ship
     *
     * @param event mouse click
     */
    public void selectSpace(MouseEvent event) {
        Space selSpace = (Space) event.getSource();
//        List<Space> shipSpaces = new ArrayList<>();
//        Space space;

/* Commented code is to check if a ship has been clicked on when selecting a space
        //Check if ship was clicked on a space
        if (selSpace.getShip() != null) {
            shipSpaces.add(selSpace);

            //Check Y up
            for (int y = selSpace.getYCoordinate() - 1; y >= 0; y--) {
                space = getSpace(selSpace.getXCoordinate(), y);

                if (space.getShip() != null) {
                    System.out.println("y up " + space.getXCoordinate() + " " + space.getYCoordinate());
                    shipSpaces.add(space);
                }
                else break;
            }

            //Check Y down
            for (int y = selSpace.getYCoordinate() + 1; y < 10; y++) {
                space = getSpace(selSpace.getXCoordinate(), y);

                if (space.getShip() != null) {
                    System.out.println("y down " + space.getXCoordinate() + " " + space.getYCoordinate());
                    shipSpaces.add(space);
                }
                else break;
            }

            //Check X left
            for (int x = selSpace.getXCoordinate() - 1; x >= 0; x--) {
                space = getSpace(x, selSpace.getYCoordinate());

                if (space.getShip() != null) {
                    System.out.println("x left " + space.getXCoordinate() + " " + space.getYCoordinate());
                    shipSpaces.add(space);
                }
                else break;
            }

            //Check X right
            for (int x = selSpace.getXCoordinate() + 1; x < 10; x++) {
                space = getSpace(x, selSpace.getYCoordinate());

                if (space.getShip() != null) {
                    System.out.println("x right " + space.getXCoordinate() + " " + space.getYCoordinate());
                    shipSpaces.add(space);
                }
                else break;
            }
        } else {
            System.out.println("no ship here");
        } */

        if (numShips != 5) {
            selSpace.setOnMouseClicked(e -> {
                boolean success = ab.placeShipWindow(selSpace, this);
                if (success) ++numShips;
                else ab.invalidPlacement();
            });
            event.consume();
        }

        checkShips();
    }

    /**
     * Change colour of the space when shooting the board
     *
     * @param event click
     */
    public void shootSpace(MouseEvent event) {
        Space selSpace = (Space) event.getSource();
        boolean hit;
//        if(turn)
        hit = selSpace.shootBoard();
        try {
            cc.sendCoordinates(selSpace.getXCoordinate(), selSpace.getYCoordinate());
            cc.sendGameInfo("yourTurn");
//            if (getNumShips() == 0) {
//                cc.sendGameInfo("endGame");
//                cc.showLoseDialog();
//            }
            //if(hit) cc.sendGameInfo("hit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Change colour of the board when player's space has been shot
     *
     * @param coordinates incoming message with coordinates
     */
    public void oppShotBoard(Coordinates coordinates) {
        //Decode message
        String coords = coordinates.getCoordinates(); //Message will be x,y
        String spl[] = coords.split(",");
        int x = Integer.parseInt(spl[0]);
        int y = Integer.parseInt(spl[1]);

        boolean hit;

        Space shot = getSpace(x, y);
        hit = shot.shootBoard();
        try {
            if (hit) {
                cc.sendHit(x, y, "hit'");
                hitShipCounter++;
                if (hitShipCounter == 18) {
                    cc.sendGameInfo("endGame");
                    cc.showLoseDialog();
                }
            }
            cc.sendGameInfo("yourTurn");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void oppHitShip(Hit hit) {
        System.out.println("oppshiphit");
        String hitInfo = hit.getHit(); //Message will be x,y,hit
        String spl[] = hitInfo.split(",");
        int x = Integer.parseInt(spl[0]);
        int y = Integer.parseInt(spl[1]);
        String h = spl[2];

        Space hitSpace = getSpace(x, y);
        hitSpace.isHit();
    }

    /**
     * Method will check how many ships are on the board
     * When five ships are placed, player is ready to play.
     */
    private void checkShips() {
        try {
            if (numShips == 5) {
                cc.sendGameInfo("ready");
                System.out.println("sent ready");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deprecated method to change the ship orientation when clicking on the ship
     *
     * @param ship:       ship to change
     * @param shipSpaces: spaces the ship is on
     * @return success
     */
    private boolean changeShipOrientation(Ship ship, List<Space> shipSpaces) {
        final int LAST_COLUMN_ROW = 10;
        List<Space> originalSpaces = new ArrayList<>();
        Space pivotSpace = shipSpaces.get(0);
        System.out.println("p\n" + pivotSpace.toString());
        System.out.println("ship: " + ship.toString());

        for (Space old : shipSpaces) {
            System.out.println("old: " + old.toString());
        }

        //Vertical to horizontal
        if (ship.isVertical()) {
            shipSpaces.sort(Space.yCoordinateCompare);

            //Validate if the ending coordinates will be valid
            //Check right side of board
            if (LAST_COLUMN_ROW - pivotSpace.getXCoordinate() < ship.getSize()) return false;

            ship.setVertical(false);
            for (int i = 1; i < shipSpaces.size(); i++) {
                originalSpaces.add(shipSpaces.get(i));
                originalSpaces.get(i - 1).resetSpaceColour();
                originalSpaces.get(i - 1).setShip(null);
                System.out.println("original" + originalSpaces.get(i - 1).toString());

//                if (isShipPositionValid(ship, shipSpaces.get(i).getXCoordinate(), shipSpaces.get(i).getYCoordinate())) {
                System.out.println("supp x:" + (pivotSpace.getXCoordinate() + i) + " y: " + pivotSpace.getYCoordinate());
                shipSpaces.get(i).setXCoordinate(pivotSpace.getXCoordinate() + i);
                shipSpaces.get(i).setYCoordinate(pivotSpace.getYCoordinate());
                shipSpaces.get(i).setShip(ship);
                if (shipSpaces.get(i).getShip() != null) {
                    System.out.println("new " + shipSpaces.get(i).toString());
                    System.out.println("set ship: " + shipSpaces.get(i).getShip().toString());
                    shipSpaces.get(i).setFill(Color.RED);
                    System.out.println("colour success");
                } else {
                    System.out.println(shipSpaces.get(i).toString());
                    System.out.println("colour fail");
                }
            }

//            ship.setVertical(false);
            return true;
        } else { //Horizontal to vertical
            shipSpaces.sort(Space.xCoordinateCompare);

            //Validate if the ending coordinates will be valid
            //Check right side of board
            if (LAST_COLUMN_ROW - pivotSpace.getYCoordinate() < ship.getSize()) return false;

            ship.setVertical(true);
            for (int i = 1; i < shipSpaces.size(); i++) {
                originalSpaces.add(shipSpaces.get(i));
                originalSpaces.get(i - 1).resetSpaceColour();
                originalSpaces.get(i - 1).setShip(null);
                System.out.println("original" + originalSpaces.get(i - 1).toString());

//                if (isShipPositionValid(ship, shipSpaces.get(i).getXCoordinate(), shipSpaces.get(i).getYCoordinate())) {
                System.out.println("supp x:" + (pivotSpace.getXCoordinate()) + " y: " + pivotSpace.getYCoordinate() + i);
                shipSpaces.get(i).setXCoordinate(pivotSpace.getXCoordinate());
                shipSpaces.get(i).setYCoordinate(pivotSpace.getYCoordinate() + i); //Add how far space is from pivot
//                shipSpaces.get(i).setSpaceToShipColour();
                shipSpaces.get(i).setShip(ship);
                if (shipSpaces.get(i).getShip() != null) {
                    System.out.println("new " + shipSpaces.get(i).toString());
                    System.out.println("set ship: " + shipSpaces.get(i).getShip().toString());
                    shipSpaces.get(i).setFill(Color.RED);
                    System.out.println("colour success");
                }
            }

            return true;
        }
    }

    /**
     * Method will check if the placed Ship piece position is valid
     * Checks if the placed ship is within the bounds of the board
     * Checks if the placed ship is on an occupied space
     *
     * @param ship Ship piece
     * @param x    coordinate
     * @param y    coordinate
     * @return if the ship's position is valid
     */
    private boolean isShipPositionValid(Ship ship, int x, int y) {
        //Check vertical ships
        if (ship.isVertical()) {
            for (int i = y; i < y + ship.getSize(); i++) {
                if (isNotValidCoordinate(x, i)) {
                    System.out.println("Bad coords x");
                    return false;//Check coordinates
                }

                //Check if there is a ship present in the cell
                Space space = getSpace(x, i);
                if (space.getShip() != null) {
                    System.out.println("ship here vert");
                    return false;
                }

                //Check if there is at least one space from each other
                for (Space neighbour : checkNeighbours(x, i)) {
                    if (isNotValidCoordinate(x, i)) {
                        System.out.println("Neighbour bad coords vert");
                        return false;
                    }
                    if (neighbour.getShip() != null) {
                        System.out.println("neighbour ship here vert");
                        return false; //There is a ship on that neighbour space
                    }
                }
            }
        } else { //Horizontal ships
            for (int i = x; i < x + ship.getSize(); i++) {
                if (isNotValidCoordinate(i, y)) {
                    System.out.println("bad coords y");
                    return false;
                }

                Space space = getSpace(i, y);
                if (space.getShip() != null) {
                    System.out.println("ship here hor");
                    return false;
                }

                //Check if it is one space from one another
                for (Space neighbour : checkNeighbours(i, y)) {
                    if (isNotValidCoordinate(i, y)) {
                        System.out.println("neighbour bad coords hor");
                        return false;
                    }
                    if (neighbour.getShip() != null) {
                        System.out.println("neighbour ship here hor");
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Method will check if the x & y coordinates are valid on the game board
     *
     * @param x coordinate
     * @param y coordinate
     * @return if the coordinates are valid
     */
    private boolean isNotValidCoordinate(int x, int y) {
        final int X_MIN_COORDINATE = 0;
        final int X_MAX_COORDINATE = 10;
        final int Y_MIN_COORDINATE = 0;
        final int Y_MAX_COORDINATE = 10;

        return (x < X_MIN_COORDINATE || x >= X_MAX_COORDINATE || y < Y_MIN_COORDINATE || y >= Y_MAX_COORDINATE);
    }

    /**
     * Method will check the coordinates are valid based on the neighbouring space's coordinates
     *
     * @param coordinate Space coordinates
     * @return if the coordinates are valid
     */
    private boolean isNotValidCoordinate(Point2D coordinate) {
        return isNotValidCoordinate((int) coordinate.getX(), (int) coordinate.getY());
    }

    /**
     * Method will get the neighbouring spaces of a space that contains a ship
     * This is used to prevent two ships from touching one another
     *
     * @param x coordinate
     * @param y coordinate
     * @return array of neighbouring Spaces
     */
    private Space[] checkNeighbours(int x, int y) {
        //Array will create the neighbouring coordinates
        Point2D[] coordinateNeighbours = new Point2D[]{
                new Point2D(x - 1, y),
                new Point2D(x + 1, y),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1),
        };

        List<Space> spaceNeighbours = new ArrayList<>();

        for (Point2D coordinate : coordinateNeighbours) {
            if (!isNotValidCoordinate(coordinate))
                spaceNeighbours.add(getSpace((int) coordinate.getX(), (int) coordinate.getY()));
        }

        return spaceNeighbours.toArray(new Space[0]);
    }

    /**
     * Method will return the specific selected space
     * In order to access the specific space at coordinate "x"
     * I use the `getChildren()` method that is in the `Parent` class
     * However, this returns a Node, which then requires casting to both `HBox` and `Space`
     * First, find the specific row from the VBox with the coordinate "y"
     * Second, find the space from that row with the coordinate "x"
     *
     * @param x coordinate
     * @param y coordinate
     * @return specific selected space
     */
    public Space getSpace(int x, int y) {
        return (Space) ((HBox) rows.getChildren().get(y)).getChildren().get(x);
    }
}
