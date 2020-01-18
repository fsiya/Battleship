package Utilities;

import java.io.Serializable;

/**
 * Will hold the coordinates of the space being hit.
 * Format will be in x,y (no spaces).
 * String will need to be split & parsed
 */
public class Coordinates implements Serializable {
    private static final long serialVersionUID = -3611432484224404545L;
    String coordinates;

    public Coordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getCoordinates() {
        return coordinates;
    }
}
