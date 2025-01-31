package Utilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

/**
 * Input listener class that wil notify the client when a message has been passed
 */
public class InputListener implements Runnable {
    private ObjectInputStream ois;
    private List<PropertyChangeListener> clientList;
    private Socket socket;

    public InputListener(List<PropertyChangeListener> clientList, Socket socket) {
        this.clientList = clientList;
        this.socket = socket;
    }

    /**
     * Read the incoming message
     */
    @Override
    public void run() {
        try {
            this.ois = new ObjectInputStream(socket.getInputStream());
            while (true) {
                Object obj = ois.readObject();

                if (obj instanceof Message) { //Get the instance of the Message object
                    Message message = (Message) obj;
                    System.out.println("heard message");
                    notifyClient(message, "message");
                } else if (obj instanceof Game_Info) {
                    Game_Info info = (Game_Info) obj;
                    notifyClient(info, "gameInfo");
                } else if(obj instanceof Player) {
                    Player player = (Player) obj;
                    notifyClient(player, "player");
                } else if(obj instanceof Coordinates) {
                    Coordinates coord = (Coordinates) obj;
                    System.out.println("Heard coord");
                    notifyClient(coord, "coordinates");
                } else if(obj instanceof Hit) {
                    Hit hit = (Hit) obj;
                    System.out.println("Heard hit");
                    notifyClient(hit, "hit");
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            notifyClient(new Message("System", "Player has left the server"), "exit");
        }
    }


    /**
     * Method to notify clients of a message that has been passed
     *
     * @param newMessage text message
     */
    private synchronized void notifyClient(Message newMessage, String property) {
        System.out.println("client notifies message");
        for (PropertyChangeListener clients : clientList) {
            clients.propertyChange(new PropertyChangeEvent(this, property, null, newMessage));
        }
    }

    /**
     * Method to notify clients of game information that has been passed
     * @param info info of the game that has been passed
     * @param property game
     */
    private synchronized void notifyClient(Game_Info info, String property) {
        System.out.println("client notifies game");
        for (PropertyChangeListener clients : clientList) {
            clients.propertyChange(new PropertyChangeEvent(this, property, null, info));
        }
    }

    /**
     * Method to notify clients of player information that has been passed
     * @param player player information
     * @param property player
     */
    private synchronized void notifyClient(Player player, String property) {
        System.out.println("client notifies player");
        for (PropertyChangeListener clients : clientList) {
            clients.propertyChange(new PropertyChangeEvent(this, property, null, player));
        }
    }

    /**
     * Method to notify clients of coordinates that has been passed
     * @param coord coordinates
     * @param property coordinates
     */
    private synchronized void notifyClient(Coordinates coord, String property) {
        System.out.println("client notifies coords");
        for (PropertyChangeListener clients : clientList) {
            clients.propertyChange(new PropertyChangeEvent(this, property, null, coord));
        }
    }

    /**
     * Method to notify clients if a ship has been hit
     * @param hit if ship has been hit
     * @param property hit
     */
    private synchronized void notifyClient(Hit hit, String property) {
        System.out.println("client notifies hit");
        for (PropertyChangeListener clients : clientList) {
            clients.propertyChange(new PropertyChangeEvent(this, property, null, hit));
        }
    }
}