package Controller;

import Utilities.Client_Connector;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Controller class for the Login Window
 * Class will facilitate the connection between the client and the server
 */
public class Login_Controller {

    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfIP;
    @FXML
    private TextField tfPortNum;
    @FXML
    private Button btnConnect;
    private Client_Connector cc;

    public Login_Controller() {
        this.cc = Client_Connector.getClientConnector();
    }

    /**
     * Method will connect the user to the server
     */
    public void connectToServer() {
        btnConnect.setOnMouseClicked(event -> {
            String user = tfUsername.getText().trim();
            String IP = tfIP.getText();
            int portNum = 5555;
            try {
                portNum = Integer.parseInt(tfPortNum.getText());
            } catch (NumberFormatException e) {
                invalidPopup();
            }

            try {
                cc.connectToServer(user, IP, portNum);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //createGame();
            ((Node)(event.getSource())).getScene().getWindow().hide(); //Hide login window
        });

    }

    /**
     * Popup to denote when there is no input in the "port" field
     */
    private void invalidPopup() {
        Alert invalidWindow = new Alert(Alert.AlertType.ERROR);
        invalidWindow.setContentText("Invalid Input\nPlease enter in a number");
        invalidWindow.show();
    }
}
