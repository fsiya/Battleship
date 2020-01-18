package Utilities;

import java.io.Serializable;

/**
 * Class will hold the info of the coordinates and if a ship has been hit
 */
public class Hit implements Serializable {
    private static final long serialVersionUID = -458043833033463640L;
    String hit;

    public Hit(String hit) {
        this.hit = hit;
    }

    public String getHit() {
        return hit;
    }
}
