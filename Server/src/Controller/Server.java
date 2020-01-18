package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class will accept 2 clients at a time to create a session
 */
public class Server {
    private final int PORT = 5555;
    private final int NUM_SESSION_CLIENTS = 2; //Number of clients per session
    private ServerSocket serverSocket;
    private boolean serverUp = false;

    @FXML
    private Label lblStatus;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;
    @FXML
    private TextArea taLogBox;

    /**
     * Server for Battleship
     */
    public Server() {
        bindServerSocket();
    }

    /**
     * Method will bind the server to port
     */
    private void bindServerSocket() {
        try {
            this.serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Turn off server
     */
    private void closeServer() {
        try {
            serverSocket.close();
            System.out.println("Server off");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * When start button is pressed, the Battleship server will start
     * Method holds an inner class where a new thread is created for starting the server
     */
    public void setBtnStart() {
        btnStart.setOnMouseClicked(event -> {
            lblStatus.setText("On");
            lblStatus.setTextFill(Color.GREEN);
            taLogBox.appendText(new Date().toString() + ": " + "Server started\n");
            btnStart.setDisable(true);
            btnStop.setDisable(false);
            this.serverUp = true;

            /*
            Create new thread in btnStart
            This will allow a responsive GUI via multithreading
            Main thread: Server
            Secondary: starting server
             */
            Thread thread = new Thread(() -> {
                List<Socket> clientList = new ArrayList<>(); //List of clients connected to server
                System.out.println("Server is running");

                while (serverUp) {
                    try {
                        Socket socket = Server.this.serverSocket.accept();
                        clientList.add(socket);
                        System.out.println("Client connected");
                        appendTextClientConnected();

                        if (clientList.size() == NUM_SESSION_CLIENTS) { //Create session for connected players
                            Server.this.taLogBox.appendText(new Date().toString() + ": " + "Session created\n");
//                            ClientHandler clientHandler = new ClientHandler(clientList.get(0), clientList.get(1));
                            ClientHandler clientHandler = new ClientHandler(clientList);
                            Thread thread1 = new Thread(clientHandler);
                            thread1.start();

                            clientList.clear(); //Accept more
                        }
                    } catch (SocketException e) {

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        });
    }

    /**
     * When stop button is pressed, the Battleship server will stop
     */
    public void setBtnStop() {
        btnStop.setOnMouseClicked(event -> {
            lblStatus.setText("Off");
            lblStatus.setTextFill(Color.RED);
            taLogBox.appendText(new Date().toString() + ": " + "Server stopped\n");
            btnStop.setDisable(true);
            btnStart.setDisable(false);
            this.serverUp = false;
            closeServer();
        });
    }

    /**
     * Method will append text to the server's log when a client connects
     */
    private void appendTextClientConnected() {
        System.out.println("nClient connected append");
        taLogBox.appendText(new Date().toString() + ": " + "Client connected\n");
    }
}