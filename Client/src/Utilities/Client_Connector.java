package Utilities;

import Controller.Battleship_Main;
import Model.Board;
import Model.Chat;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Client_Connector connects the client to the server.
 * Class facilitates the communication between client & server
 */
public class Client_Connector implements PropertyChangeListener {
    //Server connect variables
    private static Client_Connector cc;
    private Board playerBoard, opponentBoard;
    private Battleship_Main battleship;
    private String user;
    private Socket socket;
    private ObjectOutputStream oos;

    private Client_Connector() {
    }

    /**
     * Singleton pattern
     *
     * @return Client_Connector class
     */
    public static Client_Connector getClientConnector() {
        if (cc == null) cc = new Client_Connector();
        return cc;
    }

    /**
     * Method will connect the client to the server
     *
     * @param user    username
     * @param IP      address
     * @param portNum port number
     * @throws IOException Exception for socket
     */
    public void connectToServer(String user, String IP, int portNum) throws IOException {
        this.user = user;
        playerBoard = new Board(true);
        opponentBoard = new Board(false);

        this.socket = new Socket(IP, portNum); //TODO handle ConnectException
        openGame();
        createStreamConnection();
//        openGame();
    }

    /**
     * Open streams to server
     */
    private void createStreamConnection() {
        List<PropertyChangeListener> observerList = new ArrayList<>();
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("cc" + oos);
            observerList.add(this);
            InputListener inputListener = new InputListener(observerList, this.socket);
            Thread thread = new Thread(inputListener);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open battleship main game
     */
    private void openGame() {
        System.out.println("hello");
        Stage stage = new Stage();
        battleship = new Battleship_Main();
        battleship.start(stage, playerBoard, opponentBoard);
    }

//    private void sendPlayerNum

    /**
     * Method will inform the opponent as to where the player has shot
     *
     * @param info will denote if the shot was a hit or a miss
     */
    public void sendGameInfo(String info) throws IOException {
        Game_Info gameInfo = new Game_Info(info);
        System.out.println(info);
        oos.writeObject(gameInfo);
        System.out.println("Sent game info");
    }

    /**
     * Get the coordinates that has been shot
     *
     * @throws IOException
     */
    public void sendCoordinates(int x, int y) throws IOException {
        String coord = x + "," + y;
        Coordinates coordinates = new Coordinates(coord);
        System.out.println(coordinates);
        oos.writeObject(coordinates);
        System.out.println("Sent coordinates");
    }

    public void sendHit(int x, int y, String hit) throws IOException {
        String hitInfo = x + "," + y + "," + hit;
        Hit h = new Hit(hitInfo);
        System.out.println(h);
        oos.writeObject(h);
        System.out.println("Sent hit info");
    }

    /**
     * Method will send message to other user
     *
     * @param txtMessage user input
     * @throws IOException e
     */
    public void sendMessage(String txtMessage) throws IOException {
        Message message = new Message("message", user, txtMessage);
        oos.writeObject(message);
        System.out.println("Sent message");
    }

    @Override
    public synchronized void propertyChange(PropertyChangeEvent evt) {
        String property = evt.getPropertyName();
        System.out.println("D" + property);

        switch (property) {
            case "message":
                Message message = (Message) evt.getNewValue();
                Chat.getChat().updateChatBox(message);
                break;

            case "gameInfo":
                Game_Info info = (Game_Info) evt.getNewValue();
                switch (info.getInfo()) {
                    case "yourTurn":
//                        battleship.changeTurnLabel(true);
                        Platform.runLater(() -> {//This is how you do the runnable lambda
                            battleship.changeTurnLabel(true);
                        });
                        break;

                    case "oppTurn":
                        Platform.runLater(() -> {
                            battleship.changeTurnLabel(false);
                        });
                        break;

                    case "endGame":
                        showWinDialog();
                        break;

                    case "ready":

                        break;
                }
                break;

            case "coordinates":
                Coordinates coordinates = (Coordinates) evt.getNewValue();
                playerBoard.oppShotBoard(coordinates);
                break;

            case "hit":
                Hit hit = (Hit) evt.getNewValue();
                opponentBoard.oppHitShip(hit);
                break;

            case "player":
                Player player = (Player) evt.getNewValue();
                System.out.println("P" + player.getPlayerNum());
                try {
                    if (player.getPlayerNum() == 1) sendGameInfo("yourTurn");
                    else sendGameInfo("oppTurn");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    /**
     * Show winner dialog
     */
    private void showWinDialog() {
        Platform.runLater(() -> {
            Alert win = new Alert(Alert.AlertType.INFORMATION, "You Win");
            win.setTitle("You lose");
            win.show();
        });
    }

    /**
     * Show loser dialog
     */
    public void showLoseDialog() {
        Platform.runLater(() -> {
            Alert lose = new Alert(Alert.AlertType.WARNING, "You Lose");
            lose.setTitle("You lose");
            lose.show();
        });
    }

    public String getUser() {
        return user;
    }
}
