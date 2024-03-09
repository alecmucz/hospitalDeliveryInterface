package com.example.hospitaldeliveryinterface;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
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
    private Button filterbtn;

    @FXML
    private Button newDeliveryButton;

    @FXML
    private VBox orderDisplayContainer;

    @FXML
    private Button pendingButton;


    @FXML
    private VBox settingNavbar;

    @FXML
    private Button settingsButton;

    private boolean isToggleSettings;
    public void initialize(){
        isToggleSettings = false;
        settingNavbar.setPrefWidth(0);
       pendingToggle();
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
        pendingToggle();
    }

    @FXML
    void onCompleteClick(ActionEvent event) {

        buttonToggle(completedButton);
        buttonNotToggle(pendingButton);


        orderDisplayContainer.getChildren().clear();

        Completed completeQueue = Completed.getInstance();
        Queue<DeliveryRequisition> currentCompleted = completeQueue.getCompletedQueue();
        if(!currentCompleted.isEmpty()){
            for(DeliveryRequisition order: currentCompleted){
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
    void onSettingClick(ActionEvent event) {
        if(!isToggleSettings){
            buttonToggle(settingsButton);
            settingNavbar.setPrefWidth(137);
        }else{
            buttonNotToggle(settingsButton);
            settingNavbar.setPrefWidth(0);
        }

        isToggleSettings = !isToggleSettings;
    }


    public void buttonToggle(Button button){
        button.setStyle("-fx-border-color: black; -fx-background-color: white");
    }

    public void buttonNotToggle(Button button){
        button.setStyle("-fx-background-color: white");
    }

    public void pendingToggle(){
        buttonToggle(pendingButton);
        buttonNotToggle(completedButton);

        orderDisplayContainer.getChildren().clear();
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
}