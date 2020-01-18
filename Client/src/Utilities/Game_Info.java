package Utilities;

import java.io.Serializable;

/**
 * Game_Info class will hold the following information:
 * <ul>
 *     <li>Player turn</li>
 *     <li>Hit</li>
 *     <li>End game</li>
 * </ul>
 */
public class Game_Info implements Serializable {
    private static final long serialVersionUID = -8558534990948934331L;
    //    private static final long serialVersionUID = 456L;
    private String info;

    public Game_Info(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
