package Model;

/**
 * Base class for ship pieces
 */
public class Ship {
    private int size; //Will determine type of ship
    private int healthPoints;
    private boolean vertical;
    private boolean placed;
    private boolean destroyed;

    /**
     * Constructor
     * @param size ship size
     * @param vertical vertical
     */
    public Ship(int size, boolean vertical) {
        this.size = size;
        this.healthPoints = size;
        this.vertical = vertical;
        this.placed = true;
        this.destroyed = false;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public boolean isVertical() {
        return vertical;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    public boolean isPlaced() {
        return placed;
    }

    public boolean isDestroyed() {
        if (healthPoints == 0) destroyed = true;
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    /**
     * Method to decrease health points when the ship has been hit
     */
    public void hit() {
        this.healthPoints--;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("size: ").append(size);
        sb.append("\tvertical: ").append(vertical);

        return sb.toString();
    }
}


