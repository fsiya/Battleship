package Controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;

import java.util.Date;

/**
 * Class will add action listeners to GUI Buttons
 */
public class GUI_Controller {
    private Server server;
    private String date_text = new Date().toString() + ":\t ";

    @FXML
    private Label lblStatus;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;
    @FXML
    private TextArea taLogBox;

    /**
     * When start button is pressed, the Battleship server will start
     */
    public void setBtnStart() {
        btnStart.setOnMouseClicked(event -> {
            lblStatus.setText("On");
            lblStatus.setTextFill(Color.GREEN);
            taLogBox.appendText(new Date().toString() + ": " + "Server started" + "\n");
            btnStart.setDisable(true);
            btnStop.setDisable(false);
//            server.setServerUp(true);
            this.server = new Server(); //Start server
        });
    }

    /**
     * When stop button is pressed, the Battleship server will stop
     */
    public void setBtnStop() {
        btnStop.setOnMouseClicked(event -> {
            lblStatus.setText("Off");
            lblStatus.setTextFill(Color.RED);
            taLogBox.appendText(new Date().toString() + ": " + "Server stopped" + "\n");
            btnStop.setDisable(true);
            btnStart.setDisable(false);
            //server.setServerUp(false);
//            this.server.closeServer();
        });
    }

    public void appendTextClientConnected() {
        System.out.println("Client connected append");
        taLogBox.appendText(new Date().toString() + ": " + "Client connected");
        System.out.println("Big test big");
    }
}
