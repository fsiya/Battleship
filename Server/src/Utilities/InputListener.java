package Utilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

/**
 * Input listener class that wil notify the client when a message has been passed
 */
public class InputListener implements Runnable {
    private ObjectInputStream ois;
    private Socket socket;
    private List<PropertyChangeListener> clientList;

    public InputListener(List<PropertyChangeListener> clientList, Socket socket) {
        this.clientList = clientList;
        this.socket = socket;
        try {
            this.ois = new ObjectInputStream(socket.getInputStream()); //Create ois by getting socket's InputStream
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the incoming message
     */
    @Override
    public void run() {
        while (true) {
            try {
//            this.ois = new ObjectInputStream(socket.getInputStream());
//                Message message = (Message) ois.readObject();
//                System.out.println("server" + message);
//                notifyClient(message, message.getType()); //Notify clients of change
                Object obj = ois.readObject();

                if (obj instanceof Message) { //Get the instance of the Message object
                    Message message = (Message) obj;
                    notifyClient(message, "message");
                } else if (obj instanceof Game_Info) {
                    Game_Info info = (Game_Info) obj;
                    notifyClient(info, "gameInfo");
                } else if (obj instanceof Player) {
                    Player player = (Player) obj;
                    notifyClient(player, "player");
                } else if (obj instanceof Coordinates) {
                    Coordinates coord = (Coordinates) obj;
                    notifyClient(coord, "coordinates");
                } else if (obj instanceof Hit) {
                    Hit hit = (Hit) obj;
                    notifyClient(hit, "hit");
                }
            } catch (SocketException e) {
                System.out.println("someone disconnected");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to notify clients of a message that has been passed
     *
     * @param newMessage text message
     */
    private synchronized void notifyClient(Message newMessage, String property) {
        System.out.println("server notifies message");
        for (PropertyChangeListener clients : clientList) {
            clients.propertyChange(new PropertyChangeEvent(this, property, null, newMessage));
        }
    }

    /**
     * Method to notify clients of game information that has been passed
     *
     * @param info     info of the game that has been passed
     * @param property game
     */
    private synchronized void notifyClient(Game_Info info, String property) {
        System.out.println("server notifies game");
        for (PropertyChangeListener clients : clientList) {
            clients.propertyChange(new PropertyChangeEvent(this, property, null, info));
        }
    }

    /**
     * Method to notify clients of player information that has been passed
     *
     * @param player   player information
     * @param property player
     */
    private synchronized void notifyClient(Player player, String property) {
        System.out.println("server notifies player");
        for (PropertyChangeListener clients : clientList) {
            clients.propertyChange(new PropertyChangeEvent(this, property, null, player));
        }
    }

    /**
     * Method to notify clients of coordinates that has been passed
     *
     * @param coord    coordinates
     * @param property coordinates
     */
    private synchronized void notifyClient(Coordinates coord, String property) {
        System.out.println("server notifies coords");
        for (PropertyChangeListener clients : clientList) {
            clients.propertyChange(new PropertyChangeEvent(this, property, null, coord));
        }
    }

    /**
     * Method to notify clients if a ship has been hit
     *
     * @param hit      if ship has been hit
     * @param property hit
     */
    private synchronized void notifyClient(Hit hit, String property) {
        System.out.println("server notifies hit");
        for (PropertyChangeListener clients : clientList) {
            clients.propertyChange(new PropertyChangeEvent(this, property, null, hit));
        }
    }
}
