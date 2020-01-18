package Utilities;

import java.io.Serializable;

/**
 * Text message object that will hold the clients' messages to one another
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 2687356978992378612L;
    private String type;
    private String userName;
    private String message;

    public Message(String type, String userName, String message) {
        this.type = type;
        this.userName = userName;
        this.message = message;
    }

    public Message(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return (userName + ": " + message);
    }
}