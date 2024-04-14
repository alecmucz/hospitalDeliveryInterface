package com.example.hospitaldeliveryinterface.controllers;

import com.example.hospitaldeliveryinterface.firebase.DataBaseMgmt;
import com.example.hospitaldeliveryinterface.firebase.FirebaseListener;
import com.example.hospitaldeliveryinterface.model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import java.io.IOException;
import java.util.*;
import static com.example.hospitaldeliveryinterface.firebase.DataBaseMgmt.search;

public class HomepageController {
    @FXML
    private HBox mainContainer;
    @FXML
    private Button changeLanguageBtn;

    @FXML
    private TextField searchBarTextField;

    @FXML
    private Button LoginButtonChange;
    @FXML
    private Button adminButton;
    @FXML
    private VBox adminToolsNav;
    @FXML
    private ToolBar bottomToolBar;
    @FXML
    private Button completedButton;
    @FXML
    private Button deliverReturnBtn;
    @FXML
    private Button deleteOrdersBtn;
    @FXML
    private Button createUserBtn;
    @FXML
    private Button editBtn;
    @FXML
    private BorderPane mainLayout;
    @FXML
    private Button newDeliveryButton;
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
    private AnchorPane rootPane;
    @FXML
    private Label usernameLabel;
    private Set<Node> selectedCards = new HashSet<>();

    //variables created
    private boolean isToggleSettings;
    private boolean isToggleAdmin;
    private Node selectedCard;
    private String[] LangToggleBtn;

    /***Menus and controllers for different components of application********/
    private AnchorPane languageMenuUI;
    private LanguageMenuController languageMenuController;
    private BorderPane deliveryFormUI;
    private DeliveryFormController deliveryFormController;
    private VBox createUserFormUI;
    private CreateUserController createUserController;
    private AnchorPane notifyMessageUI;
    private NotifyMessageController notifyMessageController;
    private BorderPane loginFormUI;
    private LoginFormController loginFormController;

    /****************************************************************************/

    public void initialize(){

        setUpDeliveryForm();
        setUpCreateUserForm();
        setUpNotifyMessage();
        setUpLoginForm();


        LangToggleBtn = MitchTextTranslate.defaultEnglishText();

        MitchTextTranslate.initialLanguages();
        setUpLanguageMenu();

        ToggleTracking.setCurrentTab("Pending");
        ToggleTracking.setSelectedCardOrderNum(null);
        ToggleTracking.setIsEdit(false);
        ToggleTracking.setIsNewDelivery(false);
        ToggleTracking.setisCreateUser(false);

        selectedCard = null;

        isToggleSettings = false;
        int totalOrders = DataBaseMgmt.getTotalNumOrders();
        DeliveryRequisition.setOrderNumCount(totalOrders);

        searchByChoiceBox.getItems().addAll("patientName","medication","location");
        searchByChoiceBox.setValue("Search By:");

        toggleNewDelivery();

        settingNavbar.setVisible(false);
        adminToolsNav.setVisible(false);

        selectOrder();

        FirebaseListener.setController(this);
        FirebaseListener.listenToPendingDeliveries();
        FirebaseListener.listenToCompletedDeliveries();
        FirebaseListener.listenToNotifyHistory();

    }
    /****************initial SETUP BEGINS HERE**************************/

    public void setUpLoginForm(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hospitaldeliveryinterface/LoginForm.fxml"));
            loginFormUI = loader.load();
            loginFormController = loader.getController();

            loginFormController.setHomepageController(this);

            rootPane.getChildren().add(loginFormUI);

            AnchorPane.setBottomAnchor(loginFormUI,0.0);
            AnchorPane.setLeftAnchor(loginFormUI,0.0);
            AnchorPane.setTopAnchor(loginFormUI, 0.0);
            AnchorPane.setRightAnchor(loginFormUI,0.0);

            loginFormUI.setVisible(false);

        } catch (IOException e) {
            System.out.println("Failed to find LoginForm.fxml");
        }
    }
    public void setUpNotifyMessage(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hospitaldeliveryinterface/NotifyMessage.fxml"));
            notifyMessageUI = loader.load();
            notifyMessageController = loader.getController();

            rootPane.getChildren().add(notifyMessageUI);

            AnchorPane.setBottomAnchor(notifyMessageUI,70.0);
            AnchorPane.setLeftAnchor(notifyMessageUI,10.0);
            notifyMessageUI.setVisible(false);

        } catch (IOException e) {
            System.out.println("Failed to find NotifyMessage.fxml");
        }
    }

    public  void setUpDeliveryForm(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hospitaldeliveryinterface/DeliveryForm.fxml"));
            deliveryFormUI = loader.load();
            deliveryFormController = loader.getController();
            mainContainer.getChildren().add(deliveryFormUI);

            deliveryFormUI.setPrefWidth(0);
            deliveryFormUI.setVisible(false);

        } catch (IOException e) {
            System.out.println("Failed to find OrderCard.fxml");
        }
    }

    public  void setUpCreateUserForm(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hospitaldeliveryinterface/CreateUser.fxml"));
            createUserFormUI = loader.load();
            createUserController = loader.getController();
            mainContainer.getChildren().add(createUserFormUI);

            createUserFormUI.setPrefWidth(0);
            createUserFormUI.setVisible(false);

        } catch (IOException e) {
            System.out.println("Failed to find CreateUserForm.fxml");
        }
    }

    public void setUpLanguageMenu(){

        changeLanguageBtn.setText("Language: English");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hospitaldeliveryinterface/LanguageMenu.fxml"));
            languageMenuUI = loader.load();
            languageMenuController = loader.getController();

            languageMenuController.setButton(changeLanguageBtn);
            languageMenuController.setHomeController(this);

            rootPane.getChildren().add(languageMenuUI);

            AnchorPane.setTopAnchor(languageMenuUI, 127.0);
            AnchorPane.setRightAnchor(languageMenuUI, 200.0);

            languageMenuUI.setVisible(false);

        } catch (IOException e) {
            System.out.println("Failed to find LanguageMenu.fxml");
        }

    }

    /****************initial SETUP ENDS HERE**************************/

/**************************MITCHELL LANGUAGE STUFF*************************/

    public void setUpLangText(String[] langTextChange){

        newDeliveryButton.setText(ToggleTracking.getIsNewDelivery()?langTextChange[8]:langTextChange[7]);
        editBtn.setText(ToggleTracking.getIsEdit()?langTextChange[6]:langTextChange[5]);

        settingsButton.setText(langTextChange[2]);
        completedButton.setText(langTextChange[0]);
        pendingButton.setText(langTextChange[1]);
        createUserBtn.setText(langTextChange[13]);
        deleteOrdersBtn.setText(langTextChange[12]);
        adminButton.setText("< "+langTextChange[9]);
        LoginButtonChange.setText(langTextChange[10]);

        switch (ToggleTracking.getCurrentTab()){
            case "Pending":
                deliverReturnBtn.setText(langTextChange[3]);
                break;
            case "Completed":

                deliverReturnBtn.setText(langTextChange[4]);
                break;

            default:
                System.out.println("No Page/Tab exist on Application!");
        }


    }

    @FXML
    void onChangeLanguageClick(ActionEvent event) {
        toggleLanguageMenu();
    }

    public void toggleLanguageMenu(){
        if (languageMenuUI != null) {
            languageMenuUI.setVisible(!languageMenuUI.isVisible());
        }
    }
    public void setLangToggleBtn(String[] newText) {
       LangToggleBtn = newText;
        setUpLangText(LangToggleBtn);

    }
    /********************************Language Menu ENDS****************************/
    @FXML
    void onPendingClick(ActionEvent event) throws IOException {
        System.out.println("Pending Button Clicked");
        if(!ToggleTracking.getCurrentTab().equals("Pending")){
            ToggleTracking.setCurrentTab("Pending");
            deliverReturnBtn.setText(LangToggleBtn[3]);
            FirebaseListener.navBarDataDisplay("Pending");
        }
    }

    @FXML
    void onCompleteClick(ActionEvent event) throws IOException {
        System.out.println("Completed Button Clicked");
        if(!ToggleTracking.getCurrentTab().equals("Completed")){
            ToggleTracking.setCurrentTab("Completed");
            deliverReturnBtn.setText(LangToggleBtn[4]);
            FirebaseListener.navBarDataDisplay("Completed");
            ToggleTracking.setIsEdit(false);
            ToggleTracking.setIsNewDelivery(false);
            toggleNewDelivery();
        }

    }

    @FXML
    void onCompleteDeliverPress(MouseEvent event) {
        buttonToggle(deliverReturnBtn);
    }

    @FXML
    void onCompleteDeliverRelease(MouseEvent event) {
        buttonNotToggle(deliverReturnBtn);
    }

    @FXML
    void onSettingClick(ActionEvent event) {
        if(!isToggleSettings){
            buttonToggle(settingsButton);
            settingNavbar.setVisible(true);
        }else{
            buttonNotToggle(settingsButton);
            settingNavbar.setVisible(false);
            loginFormController.setLoginVBoxVisibility(false);//Bug Fix: discards widgets within LoginVBOX if the Login button is clicked and settings is closed
            adminToolsNav.setVisible(false);
            isToggleAdmin = false;
            languageMenuUI.setVisible(false);
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
        ToggleTracking.setIsNewDelivery(!ToggleTracking.getIsNewDelivery());
        ToggleTracking.setIsEdit(false);
        toggleNewDelivery();
        selectOrder();
        deliveryFormController.openNewDlivery();
    }
    @FXML
    void onDeliverReturn(ActionEvent event) {
        System.out.println("Delivery orders button clicked");
        // Check if any orders are selected at all
        if (!selectedCards.isEmpty()) {
            ToggleTracking.setIsEdit(false); // Reset edit mode
            sendOrderToCompleted(); // Process all selected orders
            toggleNewDelivery(); // Reset UI states if necessary
        } else {
            System.out.println("No orders selected for delivery/return.");
        }
    }

    @FXML
    void onEditDelivery(ActionEvent event) {
        if(ToggleTracking.getSelectedCardOrderNum() != null){
            ToggleTracking.setIsEdit(!ToggleTracking.getIsEdit());
            ToggleTracking.setIsNewDelivery(false);
            toggleNewDelivery();
           if(deliveryFormController != null && selectedCard != null){
               deliveryFormController.openEditDelivery(selectedCard);
           }
        }

    }

    public void onNotifyMessage(){
        notifyMessageController.displayNotfications();
    }

    public void setLoginButtonText(String currentStatus){
        LoginButtonChange.setText(currentStatus);
    }

    public void setCurrentSignIn(String userEmployeeID){
        usernameLabel.setText(userEmployeeID);
    }

    public void toggleNewDelivery( ){

        if(ToggleTracking.getIsEdit()){
            editBtn.setText(LangToggleBtn[6]);
            buttonToggle(editBtn);
            buttonNotToggle(newDeliveryButton);
        }

        if(!ToggleTracking.getIsEdit()){
            editBtn.setText(LangToggleBtn[5]);
            buttonNotToggle(editBtn);
        }

        if(ToggleTracking.getIsNewDelivery()){
            selectedCard = null;
            ToggleTracking.setSelectedCardOrderNum(null);
            newDeliveryButton.setText(LangToggleBtn[8]);
            buttonToggle(newDeliveryButton);
            buttonNotToggle(editBtn);
        }

        if(!ToggleTracking.getIsNewDelivery()){
            newDeliveryButton.setText(LangToggleBtn[7]);
            buttonNotToggle(newDeliveryButton);
        }

        if(ToggleTracking.getIsEdit() || ToggleTracking.getIsNewDelivery()){
            deliveryFormUI.setPrefWidth(320);
            deliveryFormUI.setVisible(true);
        }

        if((ToggleTracking.getIsEdit()  && !ToggleTracking.getIsNewDelivery()) || (!ToggleTracking.getIsEdit()  && !ToggleTracking.getIsNewDelivery())){
            deliveryFormUI.setPrefWidth(0);
            deliveryFormUI.setVisible(false);
        }
    }

    public void buttonToggle(Button button){
        button.setStyle("-fx-border-color: white; -fx-background-color: #22aae1;");
    }

    public void buttonNotToggle(Button button){
        button.setStyle("-fx-border-color: transparent; -fx-background-color: #22aae1;");
    }

    public void displayQueue(Queue<DeliveryRequisition> currentQueue, String collectionName){
        Platform.runLater(() -> {
            //System.out.println("TESTING DISPLAY QUEUE HAS BEEN CALLED IN HOMEPAGECONTROLLER");
            //System.out.println("CHECKING SIZE OF ORDERS QUEUE IN DISPLAY QUEUE: " + currentQueue.size());
            Queue<DeliveryRequisition> tempQueue = new LinkedList<>();

            if(ToggleTracking.getCurrentTab().equals("Completed") && collectionName.equals("completedDeliveries")){
                 orderDisplayContainer.getChildren().clear();
                buttonToggle(completedButton);
                buttonNotToggle(pendingButton);
                tempQueue = currentQueue;
            }

            if(ToggleTracking.getCurrentTab().equals("Pending") && collectionName.equals("pendingDeliveries")){
                orderDisplayContainer.getChildren().clear();
                buttonToggle(pendingButton);
                buttonNotToggle(completedButton);
                tempQueue = currentQueue;
            }

            if(tempQueue == null && tempQueue.isEmpty()){
                orderDisplayContainer.getChildren().clear();
                return;
            }

            for(DeliveryRequisition order: tempQueue){
                   // System.out.println("CHECKING DISPLAY QUEUE ORDERS: " + order.toString());
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
    public void selectOrder() {
        for (Node node : orderDisplayContainer.getChildren()) {
            node.setOnMouseClicked(mouseEvent -> {
                GridPane gridPane = (GridPane) node;
                if (selectedCards.contains(node)) {
                    // Deselect the node
                    deselectNode(gridPane);
                } else {
                    // Select the node
                    selectNode(gridPane);
                }
            });
        }
    }

    private void selectNode(GridPane node) {
        selectedCards.add(node);
        node.setStyle("-fx-background-color: #98FF98; -fx-border-color: #22aae1; -fx-border-width: 2;");
        System.out.println("Selected order: " + node.getId());
    }

    private void deselectNode(GridPane node) {
        selectedCards.remove(node);
        node.setStyle("-fx-background-color: transparent; -fx-border-color: #22aae1; -fx-border-width: 2;");
        System.out.println("Deselected order: " + node.getId());
    }
    /*
    public void selectOrder(){
            for(Node node: orderDisplayContainer.getChildren()){
                node.setOnMouseClicked(mouseEvent -> {
                    if(selectedCard != null &&  selectedCard != node){
                        selectedCard.setStyle("-fx-border-color: #22aae1; -fx-border-width: 2; -fx-background-color: transparent");
                        selectedCard = null;
                        ToggleTracking.setIsEdit(false);
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
                                            ToggleTracking.setSelectedCardOrderNum(labelText);
                                            //System.out.println("ORDER NUMBER RETRIEVED: " + labelText);
                                            break;
                                        }
                                    }
                                }
                            }
                        selectedCard = node;

                    }else{
                        selectedCard.setStyle("-fx-border-color: #22aae1; -fx-border-width: 2; -fx-background-color: transparent");

                        selectedCard = null;
                        ToggleTracking.setIsEdit(false);

                        toggleNewDelivery();
                    }
                });
            }

            if(!ToggleTracking.getIsEdit()){
                for(Node node: orderDisplayContainer.getChildren()){
                    node.setStyle("-fx-border-color: #22aae1; -fx-border-width: 2; -fx-background-color: transparent");
                }
            }
    }

     */

    public void sendOrderToCompleted(){
        Set<Node> cardsCopy = new HashSet<>(selectedCards);

        for (Node node : cardsCopy) {
            if (node instanceof GridPane) {
                GridPane gridPane = (GridPane) node;
                String orderNum = gridPane.getId(); // Make sure this ID is correctly assigned to be the order number

                // Swap database entries based on the current tab
                if (ToggleTracking.getCurrentTab().equals("Pending")) {
                    DataBaseMgmt.swapDB(orderNum, "pendingDeliveries", "completedDeliveries");
                    NotifyMessg.createMessg("delivered", "[Employee ID]", orderNum);
                } else if (ToggleTracking.getCurrentTab().equals("Completed")) {
                    DataBaseMgmt.swapDB(orderNum, "completedDeliveries", "pendingDeliveries");
                    NotifyMessg.createMessg("returnToPending", "[Employee ID]", orderNum);
                }

                // Remove the node from the original set
                selectedCards.remove(node);
                // UI update for deselection
                deselectNode(gridPane);
            }
        }

        toggleNewDelivery(); // Reset UI states if necessary
        selectedCard = null;
        ToggleTracking.setSelectedCardOrderNum(null);
    }




    public void showDialogSignOut() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Logged out");
        alert.setTitle("Signed out");
        alert.setContentText("You are signing out");
        Optional<ButtonType> result = alert.showAndWait();
    }

    @FXML
    void handleLoginButtonChange() {
        if (LoginButtonChange.getText().equals("Login")) {
            loginFormController.setLoginVBoxVisibility(true);
        }
        else if (LoginButtonChange.getText().equals("Sign out")) {
            showDialogSignOut();
            DataBaseMgmt.updateLoginStatus(Employee.getCurrentLogin(),"False");
            Employee.setCurrentLogin(null);
            LoginButtonChange.setText("Login");
            usernameLabel.setText("");
            //LogInVbox.setVisible(true);
        }
    }

    @FXML
    void onCreateUserClick(ActionEvent event){
        createUserController.onCreateUserForm();
        ToggleTracking.setisCreateUser(!ToggleTracking.getIsCreateUser());
    }

    public void searchButton() {
        orderDisplayContainer.getChildren().clear();

        Queue<DeliveryRequisition> searchResults = search(searchBarTextField.getText(),ToggleTracking.getCurrentTab(), searchByChoiceBox.getValue());

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
}