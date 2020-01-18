package View;

import Model.Board;
import Model.Ship;
import Model.Space;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Class will pop up an alert box due to invalid coordinates input
 */
public class AlertBox {
    private boolean success;
    private ArrayList<Integer> shipTypes = new ArrayList<>();

    public boolean placeShipWindow(Space selSpace, Board board) { //Ship ship, List<Space> shipSpaces,  int numShipsToPlace,
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); //Unable to interact with other windows
        window.setTitle("Add Ship");
        window.setMinWidth(250);
        window.setMinHeight(100);
        window.setResizable(false);

        Label lblPrompt = new Label();
        lblPrompt.setText("Which ship would you like to add?");

        ToggleGroup tgOrientation = new ToggleGroup();

        RadioButton rbHorizontal = new RadioButton("Horizontal");
        rbHorizontal.setSelected(true);
        rbHorizontal.setToggleGroup(tgOrientation);

        RadioButton rbVertical = new RadioButton("Vertical");
        rbVertical.setToggleGroup(tgOrientation);

        HBox hboxRb = new HBox(10, rbHorizontal, rbVertical);
        hboxRb.setAlignment(Pos.CENTER);

        //Aircraft button
        Button btnAircraft = new Button("Aircraft Carrier\n(5 Spaces)");
        for (int i : shipTypes) { //Check if ship has been placed already
            if (i == 5) btnAircraft.setDisable(true);
        }
        btnAircraft.setOnMouseClicked(e -> {
            boolean vertical;
            vertical = !rbHorizontal.isSelected();
            shipTypes.add(5);
            AlertBox.this.success = board.placeShipPiece(new Ship(5, vertical), selSpace.getXCoordinate(), selSpace.getYCoordinate());
            if (!AlertBox.this.success) {
                shipTypes.remove((Integer) 5);
                btnAircraft.setDisable(false);
            }
            window.close();
        });

        //Battleship button
        Button btnBattleship = new Button("Battleship\n(4 Spaces)");
        for (int i : shipTypes) {
            if (i == 4) btnBattleship.setDisable(true);
        }
        btnBattleship.setOnMouseClicked(e -> {
            boolean vertical;
            vertical = !rbHorizontal.isSelected();
            shipTypes.add(4);
            AlertBox.this.success = board.placeShipPiece(new Ship(4, vertical), selSpace.getXCoordinate(), selSpace.getYCoordinate());
            if (!AlertBox.this.success) {
                shipTypes.remove((Integer) 4);
                btnBattleship.setDisable(false);
            }
            window.close();
        });

        //Cruiser button
        Button btnCruiser = new Button("Cruiser\n(3 Spaces)");
        for (int i : shipTypes) {
            if (i == 31) btnCruiser.setDisable(true);
        }
        btnCruiser.setOnMouseClicked(e -> {
            boolean vertical;
            vertical = !rbHorizontal.isSelected();
            shipTypes.add(31); //31 as it is the first "3" ☹

            AlertBox.this.success = board.placeShipPiece(new Ship(3, vertical), selSpace.getXCoordinate(), selSpace.getYCoordinate());
            if (!AlertBox.this.success) {
                shipTypes.remove((Integer) 31);
                btnCruiser.setDisable(false);
            }
            window.close();
        });

        //Submarine button
        Button btnSubmarine = new Button("Submarine\n(3 Spaces)");
        for (int i : shipTypes) {
            if (i == 32) btnSubmarine.setDisable(true);
        }
        btnSubmarine.setOnMouseClicked(e -> {
            boolean vertical;
            vertical = !rbHorizontal.isSelected();
            shipTypes.add(32); //32 as it is the second "3" ☹
            AlertBox.this.success = board.placeShipPiece(new Ship(3, vertical), selSpace.getXCoordinate(), selSpace.getYCoordinate());
            if (!AlertBox.this.success) {
                shipTypes.remove((Integer) 32);
                btnSubmarine.setDisable(false);
            }
            window.close();
        });

        //Destroyer button
        Button btnDestroyer = new Button("Destroyer\n(2 Spaces)");
        for (int i : shipTypes) {
            if (i == 2) btnDestroyer.setDisable(true);
        }
        btnDestroyer.setOnMouseClicked(e -> {
            boolean vertical;
            vertical = !rbHorizontal.isSelected();
            shipTypes.add(2);
            AlertBox.this.success = board.placeShipPiece(new Ship(2, vertical), selSpace.getXCoordinate(), selSpace.getYCoordinate());
            if (!AlertBox.this.success) {
                shipTypes.remove((Integer) 2);
                btnDestroyer.setDisable(false);
            }
            window.close();
        });

        //Add elements to Hbox & Vbox
        HBox hboxBtn = new HBox(10, btnAircraft, btnBattleship, btnCruiser, btnSubmarine, btnDestroyer);
        hboxBtn.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(10, lblPrompt, hboxRb, hboxBtn);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox);
        window.setScene(scene);
        window.showAndWait();

        return success;
    }

    /**
     * Display an error message when due to invalid ship placement
     */
    public void invalidPlacement() {
        Alert invalidWindow = new Alert(Alert.AlertType.ERROR);
        invalidWindow.setContentText("Invalid ship placement.\n - Ships must be one space away from each other\n - Ships must be on the board");
        invalidWindow.show();
    }
}
