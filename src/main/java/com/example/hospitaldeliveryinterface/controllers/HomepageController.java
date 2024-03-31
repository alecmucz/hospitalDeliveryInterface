package com.example.hospitaldeliveryinterface.controllers;

import com.example.hospitaldeliveryinterface.PharmaTracApp;
import com.example.hospitaldeliveryinterface.firebase.DataBaseMgmt;
import com.example.hospitaldeliveryinterface.firebase.FirebaseListener;
import com.example.hospitaldeliveryinterface.model.DeliveryRequisition;
import com.example.hospitaldeliveryinterface.model.NotifyMessg;
import javafx.animation.FadeTransition;
import com.example.hospitaldeliveryinterface.model.Employee;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

import static com.example.hospitaldeliveryinterface.firebase.DataBaseMgmt.search;

public class HomepageController {
    @FXML
    private TextField searchBarTextField;
    @FXML
    private TextField textFieldPhoneNumber;
    @FXML
    private TextField textFieldFullName;
    @FXML
    private TextField textFieldPassword1;
    @FXML
    private BorderPane LogInVbox;

    @FXML
    private Button LoginButton;

    @FXML
    private Button LoginButtonChange;

    @FXML
    private Button addNoteBtn;

    @FXML
    private TextArea addNoteText;

    @FXML
    private Button adminButton;

    @FXML
    private VBox adminNavBar;

    @FXML
    private VBox adminToolsNav;

    @FXML
    private ToolBar bottomToolBar;

    @FXML
    private Button completedButton;

    @FXML
    private Button createUserButton;

    @FXML
    private Button deliverReturnBtn;

    @FXML
    private Label deliveryFormLabel;

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
    private TextField firstnameText;

    @FXML
    private TextField lastnameText;

    @FXML
    private TextField locationText;

    @FXML
    private BorderPane mainLayout;

    @FXML
    private TextField medicationText;

    @FXML
    private Button newDeliveryButton;

    @FXML
    private AnchorPane notifyBox;

    @FXML
    private Label notifyMess;

    @FXML
    private Label notifyDatetime;

    @FXML
    private VBox orderDisplayContainer;

    @FXML
    private Button pendingButton;

    @FXML
    private Button searchButton;

    @FXML
    private ChoiceBox<String> searchByChoiceBox;

    @FXML
    private VBox settingNavbar;

    @FXML
    private Button settingsButton;

    @FXML
    private PasswordField textFieldPassword;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private TextField textFieldUsername1;

    @FXML
    private Label us;

    @FXML
    private Label us1;

    @FXML
    private Label us11;

    @FXML
    private Label us111;

    @FXML
    private Label usernameLabel;


    //variables created
    private boolean isToggleSettings;

    private boolean isToggleAdmin;
    private boolean isNewDelivery;
    private boolean isNewAddNote;
    private boolean isEdit;
    private boolean isDelivered;
    private  boolean toggleCreateUser;

    private boolean beginNotify;

    private String currentPage;
    private String selectedCardOrderNum;
    private Node selectedCard;//for getting selectedOrder
    private Set<String> selectedOrders = new HashSet<>();
    TextField[] allInputs;
    
    public void initialize() throws IOException {

        LogInVbox.setVisible(false);

        beginNotify = false;
        isToggleSettings = false;
        isNewDelivery = false;
        isNewAddNote = false;
        isEdit = false;
        selectedCard = null;
        selectedCardOrderNum = null;
        isDelivered = false;
        toggleCreateUser = false;
        int totalOrders = DataBaseMgmt.getTotalNumOrders();
        DeliveryRequisition.setOrderNumCount(totalOrders);
        Employee currentEmployee = null; // employee who is logged in

        searchByChoiceBox.getItems().addAll("patientName","medication","location");
        searchByChoiceBox.setValue("Search By:");


        currentPage = "Pending";

        toggleNewDelivery();
        toggleAddNote();

        settingNavbar.setVisible(false);
        adminToolsNav.setVisible(false);
        notifyBox.setVisible(false);
        adminNavBar.setPrefWidth(0);
        adminNavBar.setVisible(false);
        notifyMess.setVisible(false);
        notifyBox.setVisible(false);
        notifyDatetime.setVisible(false);
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

        FirebaseListener.setController(this);
        FirebaseListener.listenToPendingDeliveries();
        FirebaseListener.listenToCompletedDeliveries();
        FirebaseListener.listenToNotifyHistory();


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
            boolean checkOnlyNum = doseAmountText.getText().matches("[0-9]+");

            if(checkOnlyNum){
                String fullName = firstnameText.getText() + " " + lastnameText.getText();
                String newOrderNum = DeliveryRequisition.generateOrderNum();
                DeliveryRequisition newOrder = new DeliveryRequisition(
                        newOrderNum,
                        DeliveryRequisition.currentDateTime(),
                        fullName,
                        locationText.getText(),
                        medicationText.getText(),
                        doseText.getText(),
                        doseAmountText.getText(),
                        addNoteText.getText(),
                        "",
                        "",
                        ""
                );

                if(isEdit && selectedCardOrderNum != null){
                    NotifyMessg.createMessg("edited", "[Employee ID]", selectedCardOrderNum);

                    if(currentPage.equals("Pending")) {
                        DataBaseMgmt.editOrder("pendingDeliveries", selectedCardOrderNum, newOrder);
                        FirebaseListener.listenToPendingDeliveries();

                    }
                    if(currentPage.equals("Completed")) {
                        DataBaseMgmt.editOrder("completedDeliveries", selectedCardOrderNum, newOrder);
                        FirebaseListener.listenToCompletedDeliveries();
                    }

                    isEdit = false;
                    toggleNewDelivery();
                    deselectOrder();
                }
                else {
                    NotifyMessg.createMessg("newDelivery", "[Employee ID]", newOrderNum);
                    DataBaseMgmt.addToDB(newOrder, "pendingDeliveries");

                }
                clearText();
            }else{
                errMessLabel.setText("**Error: Only numbers for this field.**");
                errorBorder(doseAmountText);
            }
        }
    }

    @FXML
    void onPendingClick(ActionEvent event) throws IOException {
        clearSelections(); // Clear selections before switching
        System.out.println("Pending Button Clicked");
        if (!currentPage.equals("Pending")) {
            currentPage = "Pending";
            FirebaseListener.onDataDisplay("pendingDeliveries");
        }
    }

    @FXML
    void onCompleteClick(ActionEvent event) throws IOException {
        clearSelections(); // Clear selections before switching
        System.out.println("Completed Button Clicked");
        if (!currentPage.equals("Completed")) {
            currentPage = "Completed";
            FirebaseListener.onDataDisplay("completedDeliveries");
            isEdit = false;
            isNewDelivery = false;
            toggleNewDelivery();
        }
    }
    @FXML
    void onSettingClick(ActionEvent event) {
        if(!isToggleSettings){
            buttonToggle(settingsButton);
            settingNavbar.setVisible(true);
        }else{
            buttonNotToggle(settingsButton);
            settingNavbar.setVisible(false);
            LogInVbox.setVisible(false);    //Bug Fix: discards widgets within LoginVBOX if the Login button is clicked and settings is closed
            adminToolsNav.setVisible(false);
            isToggleAdmin = false;

        }

        isToggleSettings = !isToggleSettings;
    }

    @FXML
    void onAdminClick(ActionEvent event) {
        isToggleAdmin = !isToggleAdmin; // Toggle the state at the beginning

        if (isToggleAdmin) {
            adminToolsNav.setVisible(true);
        } else {
            adminToolsNav.setVisible(false);
        }
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
        if (!selectedOrders.isEmpty()) {

            String collectionFrom = currentPage.equals("Pending") ? "pendingDeliveries" : "completedDeliveries";
            String collectionTo = currentPage.equals("Pending") ? "completedDeliveries" : "pendingDeliveries";

            List<String> orderNumbers = new ArrayList<>(selectedOrders);

            DataBaseMgmt.swapDB(orderNumbers, collectionFrom, collectionTo);

            clearSelections();

            FirebaseListener.onDataDisplay(collectionFrom);
            FirebaseListener.onDataDisplay(collectionTo);
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

    public void displayQueue(Queue<DeliveryRequisition> currentQueue, String collectionName){
        Platform.runLater(() -> {

            //System.out.println("TESTING DISPLAY QUEUE HAS BEEN CALLED IN HOMEPAGECONTROLLER");
            //System.out.println("CHECKING SIZE OF ORDERS QUEUE IN DISPLAY QUEUE: " + currentQueue.size());

            Queue<DeliveryRequisition> tempQueue = new LinkedList<>();

            if(currentPage.equals("Completed") && collectionName.equals("completedDeliveries")){
                orderDisplayContainer.getChildren().clear();
                deliverReturnBtn.setText("Return to Pending");
                buttonToggle(completedButton);
                buttonNotToggle(pendingButton);
                tempQueue = currentQueue;
            }

            if(currentPage.equals("Pending") && collectionName.equals("pendingDeliveries")){
                orderDisplayContainer.getChildren().clear();
                deliverReturnBtn.setText("Deliver Package");
                buttonToggle(pendingButton);
                buttonNotToggle(completedButton);
                tempQueue = currentQueue;
            }

            if(tempQueue == null && tempQueue.isEmpty()){
                orderDisplayContainer.getChildren().clear();
                return;
            }



            for(DeliveryRequisition order: tempQueue){
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hospitaldeliveryinterface/OrderCard.fxml"));
                            GridPane orderTemplate = loader.load();
                            OrderCardUIController controller = loader.getController();
                            controller.updateOrderLabels(order);
                            orderDisplayContainer.getChildren().add(orderTemplate);

                        } catch (IOException e) {
                            System.out.println("Failed to find OrderCard.fxml");
                        }

            }


            selectOrder();
        });
    }

    public void selectOrder(){
        for(Node node: orderDisplayContainer.getChildren()){
            node.setOnMouseClicked(mouseEvent -> {
                String orderNum = getOrderNumFromNode(node);
                if(selectedOrders.contains(orderNum)){
                    selectedOrders.remove(orderNum);
                    deselectNode(node);
                } else {
                    selectedOrders.add(orderNum);
                    selectNode(node);
                }
            });
        }
    }

    private void selectNode(Node node){
        node.setStyle("-fx-border-color: #22aae1; -fx-border-width: 2; -fx-background-color: #98FF98");
    }

    private void deselectNode(Node node){
        node.setStyle("-fx-border-color: #22aae1; -fx-border-width: 2; -fx-background-color: transparent");
    }

    private String getOrderNumFromNode(Node node){
        if (node instanceof GridPane) {
            for(Node childNode : ((GridPane)node).getChildren()){
                if (childNode instanceof Label) {
                    Label label = (Label) childNode;
                    if ("orderNumDisplay".equals(label.getId())) {
                        return label.getText().substring(1);
                    }
                }
            }
        }
        return null;
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


    public static boolean textFieldCheck(String username,String password) {
        boolean checker = false;
        if (username.length() == 0 || password.length() == 0) {
            checker = true;
        }
        if (!(username.matches("S\\d{8}") && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"))) {

            checker = true;
        }
        return checker;

    }
    public static boolean textFieldCheckCreatingAccount(String email, String password, String phone) {

        if (email.length() == 0 || password.length() == 0) {
            System.out.println("Textfield is empty");
            return false;
        }
        if (!(email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))) {
            System.out.println("Incorrect email");
            return false;
        }
        if (!(password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"))) {
            System.out.println("Incorrect password");
            return false;
        }
        if (!(phone.matches("^\\+\\d{11}$"))){
            System.out.println("incorrect phone number");
            return false;
        }
        return true;
        }



    public void showDialog () {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Invalid input");
        alert.setTitle("Warning");
        alert.setContentText("Username or password is incorrect");
        Optional<ButtonType> result = alert.showAndWait();
    }


    public void showDialogCorrect () {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Correct Input");
        alert.setTitle("Logged in");
        alert.setContentText("You are signed in");
        Optional<ButtonType> result = alert.showAndWait();
    }
    public void showDialogSignOut() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Logged out");
        alert.setTitle("Signed out");
        alert.setContentText("You are signing out");
        Optional<ButtonType> result = alert.showAndWait();
    }

    public void showDialogCreatedUser() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Succesfully Created User");
        alert.setTitle("Succesfully Created User");
        alert.setContentText("Succesfully Created User");
        Optional<ButtonType> result = alert.showAndWait();
    }
    public void showDialogCreatedUserError() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Unable to create User");
        alert.setTitle("Unable to create User");
        alert.setContentText("Unable to create User");
        Optional<ButtonType> result = alert.showAndWait();
    }



    @FXML
    void handleLoginButtonChange() {
        if (LoginButtonChange.getText().equals("Login")) {
            LogInVbox.setVisible(true);
        }
        else if (LoginButtonChange.getText().equals("Sign out")) {
            showDialogSignOut();
            LoginButtonChange.setText("Login");
            LogInVbox.setVisible(true);

        }
    }

    @FXML
    void adminCreateUserChange(ActionEvent event){

        if(!toggleCreateUser){
            adminNavBar.setVisible(true);
            adminNavBar.setPrefWidth(314);
        }else {
            adminNavBar.setPrefWidth(0);
            adminNavBar.setVisible(false);
        }
        toggleCreateUser = !toggleCreateUser;
    }
    @FXML
    void handleLoginButton() {

            if (textFieldCheck(textFieldUsername.getText(), textFieldPassword.getText()) == false) {
                showDialogCorrect();
                LogInVbox.setVisible(false);
                usernameLabel.setText(String.valueOf(textFieldUsername.getText()));

                LoginButtonChange.setText("Sign out");
            } else{
                showDialog();
            }
            textFieldUsername.clear();
            textFieldPassword.clear();

    }

    public boolean registerUser() {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(textFieldUsername1.getText())
                .setEmailVerified(false)
                .setPassword(textFieldPassword1.getText())
                .setPhoneNumber(textFieldPhoneNumber.getText())
                .setDisplayName(textFieldFullName.getText())
                .setDisabled(false);

        UserRecord userRecord;
        try {
            userRecord = PharmaTracApp.fauth.createUser(request);
            System.out.println("Successfully created new user with Firebase Uid: " + userRecord.getUid()
                    + " check Firebase > Authentication > Users tab");
            //showDialogCreatedUser();
            return true;

        } catch (FirebaseAuthException ex) {
            // Logger.getLogger(FirestoreContext.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error creating a new user in the firebase");
            return false;
        }
    }

    public void createUser() {
        String email = textFieldUsername1.getText();
        String password = textFieldPassword1.getText();
        String phone = textFieldPhoneNumber.getText();

        if (textFieldCheckCreatingAccount(email, password, phone)) {
            registerUser();
        } else {
            showDialogCreatedUserError();
        }
    }


    @FXML
    void onReturnToHome(ActionEvent event) {
        LogInVbox.setVisible(false);
    }

    @FXML
    void onCreateUser(ActionEvent event) {

    }

    @FXML
    void onCloseUserCreate(ActionEvent event) {
        adminNavBar.setPrefWidth(0);
        toggleCreateUser = false;
        adminNavBar.setVisible(false);
    }

    public void searchButton() {
        orderDisplayContainer.getChildren().clear();

        Queue<DeliveryRequisition> searchResults = search(searchBarTextField.getText(),currentPage, searchByChoiceBox.getValue());

        for(DeliveryRequisition order: searchResults){
            System.out.println("CHECKING SEARCH ORDERS: " + order.toString());
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hospitaldeliveryinterface/OrderCard.fxml"));
                GridPane orderTemplate = loader.load();
                OrderCardUIController controller = loader.getController();
                controller.updateOrderLabels(order);
                orderDisplayContainer.getChildren().add(orderTemplate);

            } catch (IOException e) {
                System.out.println("Failed to find OrderCard.fxml");
            }

        }
    }


    public void displayNotfications(){

        if(!beginNotify){

            beginNotify = true;



            new Thread(()->{//allows to run independently from the main applicaiton flow

                Queue<NotifyMessg> retrieveMessgQueue = NotifyMessg.getMessgQueue();

                while(!retrieveMessgQueue.isEmpty()){
                    try {
                        NotifyMessg selectedNotify = NotifyMessg.removeMessg();

                        if(selectedNotify == null){break;}

                       Platform.runLater(()->{
                           FadeTransition fade1 = new FadeTransition(Duration.millis(3000), notifyBox);
                           FadeTransition fade2 = new FadeTransition(Duration.millis(3000), notifyBox);
                           FadeTransition fade3 = new FadeTransition(Duration.millis(3000), notifyBox);

                           notifyMess.setText("");
                           notifyDatetime.setText("");


                           notifyBox.setVisible(true);
                           notifyMess.setVisible(true);
                           notifyDatetime.setVisible(true);


                           fade1.setFromValue(1.0);
                           fade1.setToValue(0.0);
                           fade2.setFromValue(1.0);
                           fade2.setToValue(0.0);
                           fade3.setFromValue(1.0);
                           fade3.setToValue(0.0);
                           /**
                           // Set actions when transitions finish
                           fade1.setOnFinished(event -> notifyBox.setVisible(false));
                           fade2.setOnFinished(event -> notifyMess.setVisible(false));
                           fade3.setOnFinished(event -> notifyDatetime.setVisible(false));
                            */
                           // Play all fade transitions
                           fade1.play();
                           fade2.play();
                           fade3.play();

                           notifyMess.setText(selectedNotify.getMessage());
                           notifyDatetime.setText(selectedNotify.getMssgDate()+" - "+selectedNotify.getMssgTime());
                       });
                        retrieveMessgQueue = NotifyMessg.getMessgQueue();
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                beginNotify = false;
                notifyBox.setVisible(false);
                notifyMess.setVisible(false);
                notifyDatetime.setVisible(false);
            }).start();


        }

    }
    public void clearSelections() {
        for (Node node : orderDisplayContainer.getChildren()) {
            deselectNode(node);
        }
        selectedOrders.clear();
    }
}