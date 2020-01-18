package Model;

import Utilities.Client_Connector;
import Utilities.Message;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Create Chat box GUI
 */
public class Chat extends Parent implements PropertyChangeListener {
    private HBox hbox;
    private ImageView imageView;
    private ScrollPane scrollPane;
    private TextArea taMessageArea;
    private TextField tfMessageField;
    private final double CHAT_WIDTH = 250.0;
    private Client_Connector cc;
    private static Chat chat;

    /**
     * Constructor
     */
    private Chat() {
        this.cc = Client_Connector.getClientConnector();
        createChat();
    }

    public static Chat getChat() {
        if(chat == null) chat = new Chat();
        return chat;
    }

    /**
     * Method will create the chat GUI
     */
    private void createChat() {
        //createLogo();
        createMessageArea();
        createInputArea();

        final double CHAT_HEIGHT = 590.0;
//        VBox vbox = new VBox(this.imageView, this.scrollPane, this.hbox);
        VBox vbox = new VBox(this.scrollPane, this.hbox);
        vbox.setPrefHeight(CHAT_HEIGHT);
        vbox.setPrefWidth(CHAT_WIDTH);

        this.getChildren().add(vbox); //Add vbox to Chat parent
    }

    /**
     * Create chat logo
     */
    private void createLogo() {
        final String IMAGE_PATH = "etc/img/chat.png";
        final double IMAGE_HEIGHT = 100.0;
        try {
            FileInputStream file = new FileInputStream(IMAGE_PATH);
            Image image = new Image(file);
            this.imageView = new ImageView(image);
            this.imageView.setFitWidth(CHAT_WIDTH);
            this.imageView.setFitHeight(IMAGE_HEIGHT);
            this.imageView.setPreserveRatio(true);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create message area
     */
    private void createMessageArea() {
        final double MESSAGEAREA_HEIGHT = 550.0;

        taMessageArea = new TextArea();
        taMessageArea.setEditable(false);
        taMessageArea.setPrefHeight(MESSAGEAREA_HEIGHT);
        taMessageArea.setPrefWidth(CHAT_WIDTH);
        taMessageArea.setFont(Font.font("Arial", FontWeight.NORMAL, 15));

        scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(taMessageArea);
    }

    /**
     * Create input area
     */
    private void createInputArea() {
        final double BOTTOM_HEIGHT = 40.0;
        final double MESSAGEFIELD_WIDTH = 180.0;
        final double BTNSEND_WIDTH = 70.0;

        tfMessageField = new TextField();
        tfMessageField.setPrefHeight(BOTTOM_HEIGHT);
        tfMessageField.setPrefWidth(MESSAGEFIELD_WIDTH);
        tfMessageField.setPromptText("Type a message");
        tfMessageField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                try {
                    sendMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Button btnSend = new Button("Send");
        btnSend.setPrefHeight(BOTTOM_HEIGHT);
        btnSend.setPrefWidth(BTNSEND_WIDTH);
        btnSend.setOnMouseClicked(keyEvent -> {
            try {
                sendMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        hbox = new HBox(tfMessageField, btnSend);
        hbox.setPrefHeight(BOTTOM_HEIGHT);
        hbox.setPrefWidth(CHAT_WIDTH);
    }

    /**
     * Method will send the message
     */
    private void sendMessage() throws IOException {
        String message = this.tfMessageField.getText();
        cc.sendMessage(message);
        tfMessageField.setText("");
        updateChatBox(message);
    }

    /**
     * Method will update chatbox by sending the message
     * @param message message
     */
    public void updateChatBox(Message message) {
        System.out.println("updating chat box server");
        taMessageArea.appendText(message.toString() + "\n");
    }

    /**
     * Method will update own chatbox by appending sent message
     * @param message message
     */
    private void updateChatBox(String message) {
        System.out.println("updating chat box local");
        taMessageArea.appendText(cc.getUser() + ": " + message + "\n");
    }

    /**
     * Send message
     * @param evt message event
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("chat notified");
        Message message = (Message) evt.getNewValue();
        if(message.getType().equals("message"))
            taMessageArea.appendText(message.toString());
    }
}