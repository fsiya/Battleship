package Controller;

import Utilities.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Client handler facilitates the connection between clients
 */
public class ClientHandler implements Runnable, PropertyChangeListener {
    private ObjectOutputStream oos1;
    private ObjectOutputStream oos2;
    private Socket socket1;
    private Socket socket2;
    private InputListener inputListener1;
    private InputListener inputListener2;
    private List<PropertyChangeListener> clientList;

    /**
     * Constructor
     *
     * @param connectedClients list of clients
     */
    public ClientHandler(List<Socket> connectedClients) {
        this.socket1 = connectedClients.get(0);
        this.socket2 = connectedClients.get(1);
        clientList = new ArrayList<>();
    }

    /**
     * Run Client Handler thread
     */
    @Override
    public void run() {
        createStreamConnection();
        try {
            initializePlayerNumber();
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputListener1 = new InputListener(clientList, socket1);
        inputListener2 = new InputListener(clientList, socket2);

        Thread thread1 = new Thread(inputListener1);
        Thread thread2 = new Thread(inputListener2);
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method will create the output streams for both clients connected
     */
    private void createStreamConnection() {
        try {
            oos1 = new ObjectOutputStream(socket1.getOutputStream());
            oos2 = new ObjectOutputStream(socket2.getOutputStream());

            clientList.add(this); //Add this instance to List. Must implement `PropertyChangeListener`
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializePlayerNumber() throws IOException {
        oos1.writeObject(new Player(1));
        //oos1.writeObject();

        oos2.writeObject(new Player(2));
    }

    /**
     * Close the output streams & sockets
     */
    private void closeStreams() {
        try {
            oos1.close();
            oos2.close();
            socket1.close();
            socket2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ClientHandler will receive the messages
     * Notifies input listeners of the message
     *
     * @param evt: Message
     */
    @Override
    public synchronized void propertyChange(PropertyChangeEvent evt) {
        String property = evt.getPropertyName(); //Get type of property that was changed

        try {
            //Determine which input listener to send to
            if (evt.getSource().equals(inputListener1)) {
                if (property.equals("message")) {
                    Message message = (Message) evt.getNewValue();
                    oos2.writeObject(message);
                } else if (property.equals("gameInfo")) {
                    Game_Info info = (Game_Info) evt.getNewValue();

                    switch (info.getInfo()) {
                        case "yourTurn":
                            oos1.writeObject(new Game_Info("oppTurn"));
                            oos2.writeObject(new Game_Info("yourTurn"));
                            break;

                        case "oppTurn":
                            oos1.writeObject(new Game_Info("yourTurn"));
                            oos2.writeObject(new Game_Info("oppTurn"));
                            break;

                        case "hit": //If p1 was hit
                            oos2.writeObject("hit");
                            break;

                        case "endGame":
                            oos2.writeObject(new Game_Info("endGame"));
                            break;
                    }
                } else if (property.equals("coordinates")) { //Send shot coordinates
                    Coordinates coord = (Coordinates) evt.getNewValue();
                    oos2.writeObject(coord);
                } else if (property.equals("hit")) { //Send shot coordinates
                    Hit hit = (Hit) evt.getNewValue();
                    oos2.writeObject(hit);
                }
            } else if (evt.getSource().equals(inputListener2)) {
                if (property.equals("message")) {
                    Message message = (Message) evt.getNewValue();
                    oos1.writeObject(message);
                } else if (property.equals("gameInfo")) {
                    Game_Info info = (Game_Info) evt.getNewValue();

                    switch (info.getInfo()) {
                        case "yourTurn":
                            oos2.writeObject(new Game_Info("oppTurn"));
                            oos1.writeObject(new Game_Info("yourTurn"));
                            break;

                        case "oppTurn":
                            oos2.writeObject(new Game_Info("yourTurn"));
                            oos1.writeObject(new Game_Info("oppTurn"));
                            break;

                        case "hit": //If p2 was hit
                            oos1.writeObject("hit");
                            break;

                        case "endGame": //todo figure out how to display end game
                            oos1.writeObject(new Game_Info("endGame"));
                            break;
                    }
                } else if (property.equals("coordinates")) { //Send shot coordinates
                    Coordinates coord = (Coordinates) evt.getNewValue();
                    oos1.writeObject(coord);
                } else if (property.equals("hit")) { //Send shot coordinates
                    Hit hit = (Hit) evt.getNewValue();
                    oos1.writeObject(hit);
                }


                //Player 2 sends message
//                switch (message.getType()) {
//                    case "game": //Player 2's turn
//                        oos2.writeObject(new Message("turn", "oppTurn"));
//                        oos1.writeObject(new Message("turn", "yourTurn"));
//                        oos1.writeObject(new Message("message", "System", "Your turn"));
//                        break;
//
//                    case "hit":
//                        oos2.writeObject(new Message("hit", "You were hit"));
//                        oos1.writeObject(new Message("hit", "Opponent Hit"));
//                    break;
//
//                    case "exit": //Opponent has left game
//                        socket2.close();
//                        oos1.writeObject(new Message("exit", "System", "Opponent has disconnected."));
//                        break;
//
//                    case "endGame":
//                        oos1.writeObject(new Message("endGame", "You lose"));
//                    break;
//
//                    case "message":
//                        oos1.writeObject(message);
//                        break;
//                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}