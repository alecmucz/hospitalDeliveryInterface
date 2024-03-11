package com.example.hospitaldeliveryinterface;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
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
    private ToolBar bottomToolBar;

    @FXML
    private VBox settingNavbar;

    @FXML
    private Button settingsButton;

    @FXML
    private BorderPane mainLayout;

    private boolean isToggleSettings;
    public void initialize() throws IOException {
        isToggleSettings = false;
        settingNavbar.setPrefWidth(0);
       pendingToggle();
    }

    @FXML
    protected void onNewDelivery(ActionEvent event){
        System.out.println("New Delivery Button Clicked");
        FXMLLoader fxmlLoader = new FXMLLoader(PharmaTracApp.class.getResource("CreateOrder.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            Stage newStage = new Stage();

            newStage.setTitle("PharmaTrac/New Delivery Form");
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onPendingClick(ActionEvent event) throws IOException {
        System.out.println("Pending Button Clicked");
        loadToolbar("PendingToolbar.fxml");
        pendingToggle();
    }

    @FXML
    void onCompleteClick(ActionEvent event) throws IOException {
        System.out.println("Delivery Button Clicked");
        loadToolbar("DeliveryToolbar.fxml");

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

    /**
     * @author Alec Muczysnki
     * @param fxmlFile is the javafx design of the bottom toolbar
     */
    private void loadToolbar(String fxmlFile) {
        try {
            System.out.println("Loading toolbar: " + fxmlFile);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxmlFile));
            loader.setController(this);
            ToolBar loadedToolbar = loader.load();

            mainLayout.setBottom(loadedToolbar);
            System.out.println("Successfully loaded toolbar: " + fxmlFile);
        } catch (IOException e) {
            System.err.println("Failed to load toolbar: " + fxmlFile);
            e.printStackTrace();
        }
    }
}