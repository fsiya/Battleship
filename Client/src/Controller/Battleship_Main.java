package Controller;

import Model.Board;
import Model.Chat;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.Random;

/**
 * Initializes the battleship game
 */
public class Battleship_Main  { //extends Application
    private boolean settingUp;
    private Board playerBoard, opponentBoard;
    private Label lblTurn;

    public void start(Stage primaryStage, Board playerBoard, Board opponentBoard) {
        this.playerBoard = playerBoard;
        this.opponentBoard = opponentBoard;
        Scene scene = new Scene(createGame());
        primaryStage.setTitle("Battleship");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Method will create the game window
     * @return game window
     */
    private Parent createGame() {
        //Create outer
        BorderPane root = new BorderPane();
        final int WIDTH = 1000;
        final int HEIGHT = 600;
        root.setPrefSize(WIDTH, HEIGHT);

        //Add Chat to left
        root.setLeft(Chat.getChat());

        //Add turn label
        lblTurn = new Label("Place Ships");
        lblTurn.setTextFill(Color.CADETBLUE);
        lblTurn.setFont(new Font("Arial", 30));
        root.setBottom(lblTurn);
        root.setAlignment(lblTurn, Pos.CENTER);

        //Add board
        //createBoard();
        HBox hBox = new HBox(40, playerBoard, opponentBoard);
        hBox.setAlignment(Pos.CENTER);
        root.setCenter(hBox);

        return root;
    }

    /**
     * Method will create the player and opponent boards
     */
    private void createBoard() {
        if(playerBoard.getNumShips() != 5) settingUp = false;

        System.out.println(settingUp);

    }

    public void changeTurnLabel(boolean turn) {
        if(!turn) {
            lblTurn.setText("Opponent's turn");
            lblTurn.setTextFill(Color.RED);
        } else {
            lblTurn.setText("Your turn");
            lblTurn.setTextFill(Color.GREEN);
        }
    }


    public void opponentDisconnected() {
        ButtonType btnYes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert disconnect = new Alert(Alert.AlertType.WARNING, "Opponent has disconnected.\nWould you like to connect with another player?");
        Optional<ButtonType> choice = disconnect.showAndWait();

        if(choice.orElse(btnNo) == btnYes) { //Not sure
//            replay();
        }
    }
}
