package com.example.hospitaldeliveryinterface;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Queue;

public class HomepageController {

    @FXML
    private Button deliverReturnBtn;

    @FXML
    private Button addNoteBtn;

    @FXML
    private TextArea addNoteText;


    @FXML
    private Button completedButton;



    @FXML
    private BorderPane deliveryFormPane;



    @FXML
    private TextField doseAmountText;

    @FXML
    private TextField doseText;

    @FXML
    private Button editBtn;

    @FXML
    private Label errMessLabel;

    @FXML
    private MenuButton filterbtn;

    @FXML
    private Label deliveryFormLabel;

    @FXML
    private TextField firstnameText;

    @FXML
    private TextField lastnameText;

    @FXML
    private TextField locationText;


    @FXML
    private TextField medicationText;

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

    //variables created
    private boolean isToggleSettings;
    private boolean isNewDelivery;
    private boolean isNewAddNote;
    private boolean isEdit;
    private boolean isDelivered;

    private String currentPage;
    private String selectedCardOrderNum;
    private Node selectedCard;
    TextField[] allInputs;
    public void initialize() throws IOException {
        isToggleSettings = false;
        isNewDelivery = false;
        isNewAddNote = false;
        isEdit = false;
        selectedCard = null;
        selectedCardOrderNum = null;
        isDelivered = false;

        currentPage = "Pending";

        toggleNewDelivery();
        toggleAddNote();

        settingNavbar.setPrefWidth(0);
        displayQueue();
        selectOrder();

       //Stuff to handle new Delivery
        errMessLabel.setText("");
        allInputs = new TextField[]{
                firstnameText,
                lastnameText,
                medicationText,
                locationText,
                doseAmountText,
                doseText,
        };


        firstnameText.textProperty().addListener((observableValue, s, t1) -> {
            if(!t1.isEmpty()){
                defaultBorder(firstnameText);
            }
        });
        lastnameText.textProperty().addListener((observableValue, s, t1) -> {
            if(!t1.isEmpty()){
                defaultBorder(lastnameText);
            }
        });
        medicationText.textProperty().addListener((observableValue, s, t1) -> {
            if(!t1.isEmpty()){
                defaultBorder(medicationText);
            }
        });
        locationText.textProperty().addListener((observableValue, s, t1) -> {
            if(!t1.isEmpty()){
                defaultBorder(locationText);
            }
        });
        doseAmountText.textProperty().addListener((observableValue, s, t1) -> {
            if(!t1.isEmpty()){
                defaultBorder(doseAmountText);
            }
        });
        doseText.textProperty().addListener((observableValue, s, t1) -> {
            if(!t1.isEmpty()){
                defaultBorder(doseText);
            }
        });

    }

    @FXML
    void onAddNote(ActionEvent event) {

        isNewAddNote = !isNewAddNote;
        toggleAddNote();

    }

    public void toggleAddNote(){
        if(isNewAddNote){
            addNoteBtn.setText("Close Note");
            addNoteText.setVisible(true);
        }else{
            addNoteBtn.setText("Add Note");
            addNoteText.setVisible(false);
        }
    }


    @FXML
    void onClearText(ActionEvent event) {
        clearText();
    }
    @FXML
    void onSubmitOrder(ActionEvent event) {
        boolean checkErrors = false;
        for (TextField child: allInputs){
            if(child.getText().isEmpty()){
                errorBorder(child);
                checkErrors = true;
            }
        };

        if(checkErrors){
            errMessLabel.setText("**Error: Please fill out all required fields.**");
        }else{
            String fullName = firstnameText.getText() + " " + lastnameText.getText();
            DeliveryRequisition newOrder = new DeliveryRequisition(
                    fullName,
                    locationText.getText(),
                    medicationText.getText(),
                    doseText.getText(),
                    doseAmountText.getText()
            );

            Pending pendQueue = Pending.getInstance();
            pendQueue.addOrders(newOrder);
            if(isEdit && selectedCardOrderNum != null){
                pendQueue.getRemoveOrderByOrderNumber(selectedCardOrderNum);
                isEdit = false;
                toggleNewDelivery();
                deselectOrder();
            }
            displayQueue();
            clearText();

        }
    }

    @FXML
    void onPendingClick(ActionEvent event) throws IOException {
        System.out.println("Pending Button Clicked");
        currentPage = "Pending";
        displayQueue();
    }

    @FXML
    void onCompleteClick(ActionEvent event) throws IOException {
        System.out.println("Delivery Button Clicked");
        currentPage = "Completed";
        displayQueue();

        isEdit = false;
        isNewDelivery = false;
        toggleNewDelivery();
    }

    @FXML
    void onSettingClick(ActionEvent event) {
        if(!isToggleSettings){
            buttonToggle(settingsButton);
            settingNavbar.setPrefWidth(161);
        }else{
            buttonNotToggle(settingsButton);
            settingNavbar.setPrefWidth(0);
        }

        isToggleSettings = !isToggleSettings;
    }

    @FXML
    protected void onNewDelivery(ActionEvent event){
        isNewDelivery = !isNewDelivery;
        isEdit = false;
        isDelivered = false;
        toggleNewDelivery();
        selectOrder();

    }
    @FXML
    void onDeliverReturn(ActionEvent event) {
        if(selectedCard != null){
            isDelivered = !isDelivered;
            isEdit = false;
            toggleNewDelivery();
            sendOrderToCompleted(selectedCardOrderNum);
        }
    }

    @FXML
    void onEditDelivery(ActionEvent event) {
        if(selectedCard != null){
            isEdit = !isEdit;
            isNewDelivery = false;
            isDelivered = false;
            toggleNewDelivery();
            openEditDelivery(selectedCard);
        }

    }

    public void toggleNewDelivery( ){

        if(isDelivered){
            buttonToggle(deliverReturnBtn);
        }

        if(!isDelivered){
            buttonNotToggle(deliverReturnBtn);
        }

        if(isEdit){

            editBtn.setText("Close Edit Delivery");
            buttonToggle(editBtn);
            buttonNotToggle(newDeliveryButton);
        }

        if(!isEdit || isDelivered){
            editBtn.setText("Edit Delivery");
            buttonNotToggle(editBtn);
        }

        if(isNewDelivery){
            selectedCard = null;
            selectedCardOrderNum = null;
            clearText();
            deliveryFormLabel.setText("New Delivery Form");
            newDeliveryButton.setText("Close New Delivery");
            buttonToggle(newDeliveryButton);
            buttonNotToggle(editBtn);
        }

        if(!isNewDelivery){
            newDeliveryButton.setText("+ New Delivery");
            buttonNotToggle(newDeliveryButton);
        }

        if(isEdit || isNewDelivery){
            deliveryFormPane.setPrefWidth(320);
            deliveryFormPane.setVisible(true);
        }

        if((isEdit && !isNewDelivery) || (!isEdit && !isNewDelivery)){
            deliveryFormPane.setPrefWidth(0);
            deliveryFormPane.setVisible(false);
        }
    }
    public void buttonToggle(Button button){
        button.setStyle("-fx-border-color: white; -fx-background-color: #22aae1;");
    }

    public void buttonNotToggle(Button button){
        button.setStyle("-fx-border-color: transparent; -fx-background-color: #22aae1;");
    }


    public void errorBorder(TextField textField){
        textField.setStyle("-fx-border-color: red;");
    }
    public void defaultBorder(TextField textfield){
        textfield.setStyle("-fx-border-color: grey;");
    }

    public void displayQueue(){

        orderDisplayContainer.getChildren().clear();
        Queue<DeliveryRequisition> currentQueue;
        if(currentPage.equals("Completed")){
            deliverReturnBtn.setText("Return to Pending");
            buttonToggle(completedButton);
            buttonNotToggle(pendingButton);
            Completed completeQueue = Completed.getInstance();
            currentQueue = completeQueue.getCompletedQueue();
        }else{
            deliverReturnBtn.setText("Deliver Package");
            buttonToggle(pendingButton);
            buttonNotToggle(completedButton);
            Pending pendQueue = Pending.getInstance();
            currentQueue = pendQueue.getPendingQueue();
        }

        if(!currentQueue.isEmpty()){

            for(DeliveryRequisition order: currentQueue){
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("orderCard.fxml"));
                    GridPane orderTemplate = loader.load();
                    OrderCardUIController controller = loader.getController();
                    controller.updateOrderLabels(order);
                    orderDisplayContainer.getChildren().add(orderTemplate);

                    //Checking child IDs
                    for (Node childNode : orderTemplate.getChildren()) {
                        if (childNode instanceof Label) {
                            System.out.println("Current child of Order: " + childNode.getId());
                        }
                    }

                } catch (IOException e) {
                    System.out.println("Failed to find orderCard.fxml");
                }
            }
        }

        selectOrder();

    }

    public void selectOrder(){
            for(Node node: orderDisplayContainer.getChildren()){
                node.setOnMouseClicked(mouseEvent -> {
                    if(selectedCard != null &&  selectedCard != node){
                        selectedCard.setStyle("-fx-border-color: #22aae1; -fx-border-width: 2; -fx-background-color: transparent");
                        selectedCard = null;
                        isEdit = false;
                        isDelivered = false;
                        toggleNewDelivery();
                    }

                    if(selectedCard != node || selectedCard == null){
                            if (node instanceof GridPane) {
                                GridPane gridpane = (GridPane) node;
                                gridpane.setStyle("-fx-border-color: #22aae1; -fx-border-width: 2; -fx-background-color: #ffbdbd");
                                for(Node childNode : gridpane.getChildren()){
                                    if (childNode instanceof Label) {
                                        Label label = (Label) childNode;
                                        if ("orderNumDisplay".equals(label.getId())) {
                                            String labelText  = label.getText().substring(1); // Remove the "#" symbol
                                            selectedCardOrderNum = labelText;
                                            System.out.println("ORDER NUMBER RETRIEVED: " + labelText);
                                            break;
                                        }
                                    }
                                }
                            }
                        selectedCard = node;

                    }else{
                        selectedCard.setStyle("-fx-border-color: #22aae1; -fx-border-width: 2; -fx-background-color: transparent");

                        selectedCard = null;
                        isEdit = false;
                        isDelivered = false;
                        toggleNewDelivery();
                    }
                });
            }

            if(!isEdit){
                for(Node node: orderDisplayContainer.getChildren()){
                    node.setStyle("-fx-border-color: #22aae1; -fx-border-width: 2; -fx-background-color: transparent");
                }
            }
    }



    public void deselectOrder(){
        if(selectedCard != null) {
            selectedCard.setStyle("-fx-border-color: #22aae1; -fx-border-width: 2; -fx-background-color: transparent");
            selectedCard = null;
        }
    }

    public void openEditDelivery(Node selectCard){
        System.out.println("Edit Delivery is Open");
        deliveryFormPane.setPrefWidth(320);
        deliveryFormPane.setVisible(true);
        deliveryFormLabel.setText("Edit Delivery Form");
        clearText();

        if(selectCard instanceof GridPane){
            GridPane gridpane = (GridPane) selectCard;
            for(Node selectChild: gridpane.getChildren()){
                if(selectChild instanceof Label){
                    setTextFieldFromLabel((Label)selectChild);
                }
            }
        }


    }

    public void setTextFieldFromLabel(Label label){
        if(label.getId() != null){
            switch (label.getId()){
                case "patientNameDisplay":
                    String[] fullName = label.getText().split(" ");
                    firstnameText.setText(fullName[0]);
                    lastnameText.setText(fullName[1]);
                    break;
                case "locationDisplay":
                    locationText.setText(label.getText());
                    break;

                case "medicationDisplay":
                    medicationText.setText(label.getText());
                    break;

                case "doseDisplay":
                    doseText.setText(label.getText());
                    break;

                case "doseQuantityDisplay":
                    doseAmountText.setText(label.getText());
                    break;
                default:
                    System.out.println("NO ID EXIST ON ORDERCARD");
            }
        }



    }

    public void clearText(){
        for(TextField child: allInputs){
            child.setText("");
        }
        addNoteText.setText("");
        errMessLabel.setText("");
    }


    public void sendOrderToCompleted(String selectedorderNum){
        System.out.print("sendOrderToComplete method called!!!");
        System.out.println("selectOrderNum: " + selectedorderNum);
        Pending pendQueue = Pending.getInstance();
        Completed  completedQueue = Completed.getInstance();
        if(selectedorderNum != null){

            DeliveryRequisition toBeDelivered = pendQueue.getPendingOrderByOrderNumber(selectedCardOrderNum);
            completedQueue.addOrders(toBeDelivered);
            pendQueue.getRemoveOrderByOrderNumber(selectedorderNum);
            isDelivered = false;
            toggleNewDelivery();
            displayQueue();

            selectedCard = null;
            selectedCardOrderNum = null;
            deselectOrder();
        }


    }


}