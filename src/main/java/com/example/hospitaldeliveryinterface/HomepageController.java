package com.example.hospitaldeliveryinterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Queue;

public class HomepageController {

    @FXML
    private Button addNoteButton;

    @FXML
    private Button completedButton;

    @FXML
    private Button deliverPackageButton;

    @FXML
    private Button editDeliveryButton;

    @FXML
    private Button newDeliveryButton;

    @FXML
    private Button pendingButton;

    @FXML
    private Button settingsButton;

    @FXML
    private VBox orderDisplayContainer;

    public void initialize(){
        buttonToggle(pendingButton);
        buttonNotToggle(completedButton);
        buttonNotToggle(settingsButton);

        Pending pendQueue = Pending.getInstance();
        Queue<DeliveryRequisition> currentPending = pendQueue.getPendingQueue();
        if(!currentPending.isEmpty()){
            for(DeliveryRequisition order: currentPending){
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("orderCard.fxml"));
                    GridPane orderTemplate = loader.load();
                    OrderCardUIController controller = loader.getController();
                    controller.updateOrderLabels(order);
                    orderDisplayContainer.getChildren().add(orderTemplate);
                } catch (IOException e) {
                    System.out.println("Failed to find orderCard.fxml");
                }
            }
        }
    }

    @FXML
    protected void onNewDelivery(ActionEvent event){

        FXMLLoader fxmlLoader = new FXMLLoader(PharmaTracApp.class.getResource("CreateOrder.fxml"));
        Stage stage = PharmaTracApp.getStage();
        Scene scene = PharmaTracApp.getScene();
        try {
            scene.setRoot(fxmlLoader.load());
            stage.setTitle("PharmaTrac/New Delivery Form");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onPendingClick(ActionEvent event) {
        buttonToggle(pendingButton);
        buttonNotToggle(completedButton);
        buttonNotToggle(settingsButton);
    }

    @FXML
    void onCompleteClick(ActionEvent event) {
        buttonToggle(completedButton);
        buttonNotToggle(pendingButton);
        buttonNotToggle(settingsButton);
    }

    @FXML
    void onSettingClick(ActionEvent event) {
        buttonToggle(settingsButton);
        buttonNotToggle(completedButton);
        buttonNotToggle(pendingButton);
    }


    public void buttonToggle(Button button){
        button.setStyle("-fx-border-color: black; -fx-background-color: white");
    }

    public void buttonNotToggle(Button button){
        button.setStyle("-fx-background-color: white");
    }

}