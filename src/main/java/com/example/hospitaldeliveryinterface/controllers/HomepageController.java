package com.example.hospitaldeliveryinterface.controllers;

import com.example.hospitaldeliveryinterface.PharmaTracApp;
import com.example.hospitaldeliveryinterface.firebase.DataBaseMgmt;
import com.example.hospitaldeliveryinterface.firebase.FirebaseListener;
import com.example.hospitaldeliveryinterface.model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import net.suuft.libretranslate.util.JsonUtil;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

import static com.example.hospitaldeliveryinterface.Algolia.AlgoliaMgmt.deleteRecord;
import static com.example.hospitaldeliveryinterface.Algolia.AlgoliaMgmt.searchAlgolia;


public class HomepageController {
    @FXML
    private HBox searchHBox;
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
    private Button createUserBtn;

    @FXML
    private Button darkLightBtn;

    @FXML
    private Button deleteOrdersBtn;

    @FXML
    private Button deliverReturnBtn;

    @FXML
    private Button editBtn;

    @FXML
    private HBox editDeliverButtonsHbox;

    @FXML
    private ImageView lightDarkIcon;


    @FXML
    private BorderPane mainLayout;

    @FXML
    private ScrollPane mainOrderScroll;

    @FXML
    private Button newDeliveryButton;

    @FXML
    private VBox orderDisplayContainer;

    @FXML
    private Button pendingButton;

    @FXML
    private Button reportsButton;

    @FXML
    private AnchorPane rootPane;



    @FXML
    private Button searchButton;

    @FXML
    private VBox settingNavbar;

    @FXML
    private Button settingsButton;

    @FXML
    private Button signOutBtn;

    @FXML
    private VBox signOutVbox;

    //variables created
    private boolean isLoginToggle;
    private boolean isToggleSettings;
    private boolean isToggleAdmin;
    private Node selectedCard;
    private String[] LangToggleBtn;

    private boolean isLightMode;

    /***Menus and controllers for different components of application********/
    private AnchorPane languageMenuUI;
    private LanguageMenuController languageMenuController;
    private BorderPane deliveryFormUI;
    private DeliveryFormController deliveryFormController;
    private BorderPane createUserFormUI;
    private CreateUserController createUserController;
    private AnchorPane notifyMessageUI;
    private NotifyMessageController notifyMessageController;
    private BorderPane loginFormUI;
    private LoginFormController loginFormController;

    /****************************************************************************/

    private Button[] buttonsForAdjustWidth;

    private Image lightModeIcon;
    private Image darkModeIcon;

    String tempDarkMode;
    String tempLightMode;
    public void initialize(){

        tempLightMode = "Light Mode";
        tempDarkMode = "Dark Mode";


        buttonsForAdjustWidth = new Button[]{
                pendingButton,
                completedButton,
                reportsButton,
                editBtn,
                deliverReturnBtn,
                newDeliveryButton,
                settingsButton,
                LoginButtonChange,
                adminButton
        };
        setUpAdjustWidth();

        onSetDisabled(true);



        searchBarTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(searchBarTextField.getText().isEmpty()){
                orderDisplayContainer.getChildren().clear();
            }
            else {
                onSearchClick();
            }
        });



        setUpDeliveryForm();
        setUpCreateUserForm();
        setUpNotifyMessage();
        setUpLoginForm();



        LangToggleBtn = MitchTextTranslate.defaultEnglishText();

        MitchTextTranslate.initialLanguages();
        setUpLanguageMenu();
        ToggleTracking.setHomepageController(this);
        ToggleTracking.setCurrentTab("Pending");
        ToggleTracking.setSelectedCardOrderNum(null);
        ToggleTracking.setIsEdit(false);
        ToggleTracking.setIsNewDelivery(false);
        ToggleTracking.setisCreateUser(false);

        selectedCard = null;

        isToggleSettings = false;
        isLoginToggle = false;
        int totalOrders = DataBaseMgmt.getTotalNumOrders();
        DeliveryRequisition.setOrderNumCount(totalOrders);

        //toggleNewDelivery();

        settingNavbar.setVisible(false);
        adminToolsNav.setVisible(false);
        signOutVbox.setVisible(false);

        searchHBox.setPrefHeight(0.0);
        searchBarTextField.setVisible(false);

        selectOrder();


        FirebaseListener.setController(this);
        FirebaseListener.listenToPendingDeliveries();
        FirebaseListener.listenToCompletedDeliveries();
        FirebaseListener.listenToNotifyHistory();


        isLightMode = true;
        rootPane.getStylesheets().clear();

        lightModeIcon = new Image(String.valueOf(getClass().getResource("/com/example/hospitaldeliveryinterface/lightModeIcon.png")));
        darkModeIcon = new Image(String.valueOf(getClass().getResource("/com/example/hospitaldeliveryinterface/darkModeIcon.png")));


    }
    /****************initial SETUP BEGINS HERE**************************/

    public void setUpAdjustWidth(){
      for(Button childBtn: buttonsForAdjustWidth){
          childBtn.textProperty().addListener(((observableValue, s, t1) -> {
              childBtn.setPrefWidth(Button.USE_COMPUTED_SIZE);
          }));
      }
    }

    public void onSetVisibleAdmin(){
        adminButton.setVisible(true);
    }

    public void onSetDisabled(boolean temp){
        editBtn.setDisable(temp);
        newDeliveryButton.setDisable(temp);
        deliverReturnBtn.setDisable(temp);

    }
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
            AnchorPane.setRightAnchor(languageMenuUI, 180.0);

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

        //settingsButton.setText(langTextChange[2]);
        completedButton.setText(langTextChange[0]);
        pendingButton.setText(langTextChange[1]);
        reportsButton.setText(langTextChange[23]);
        createUserBtn.setText(langTextChange[13]);
        deleteOrdersBtn.setText(langTextChange[12]);
        adminButton.setText(langTextChange[9]);

        if(Employee.getCurrentLogin() == null){
            LoginButtonChange.setText(langTextChange[10]);
        }

        tempLightMode = langTextChange[41];
        tempDarkMode = langTextChange[42];

        if(isLightMode){
            darkLightBtn.setText(langTextChange[41]);
        }else{
            darkLightBtn.setText(langTextChange[42]);
        }
        signOutBtn.setText(langTextChange[11]);

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

        deliveryFormController.updateLanguageLabel(langTextChange);
        loginFormController.updateLanguageLabel(langTextChange);
        createUserController.updateLanguageLabel(langTextChange);

    }

    @FXML
    void onChangeLanguageClick(ActionEvent event) {
        toggleLanguageMenu();
    }

    public void toggleLanguageMenu(){
        if (languageMenuUI != null) {
            languageMenuUI.setVisible(!languageMenuUI.isVisible());
        }

        if(isToggleAdmin){
            adminToolsNav.setVisible(false);
            isToggleAdmin = false;
        }
    }
    public void setLangToggleBtn(String[] newText) {
       LangToggleBtn = newText;
        setUpLangText(LangToggleBtn);

    }
    /********************************Language Menu ENDS****************************/
    /*******************************handle dark/light mode changes*********************/
    @FXML
    void onDarkLightClick(ActionEvent event) {
        handleDarkLightChanges();
    }

    public void handleDarkLightChanges(){

        String currentStyleSheet = "";
        if (isLightMode) {
            darkLightBtn.setText(tempLightMode);
            lightDarkIcon.setImage(lightModeIcon);
            currentStyleSheet = "darkMode.css";

        } else {
            darkLightBtn.setText(tempDarkMode);
            lightDarkIcon.setImage(darkModeIcon);
            currentStyleSheet = "lightMode.css";
        }

        Scene currentScene = PharmaTracApp.getScene();
        currentScene.getStylesheets().clear();
        currentScene.getStylesheets().add(PharmaTracApp.class.getResource(currentStyleSheet).toExternalForm());
        isLightMode = !isLightMode;

    }

    /*********************************************************************************/
    @FXML
    void onPendingClick(ActionEvent event) throws IOException {
        System.out.println("Pending Button Clicked: Clearing Selection");
        System.out.println("Pending Button Clicked");
        if(ToggleTracking.getCurrentTab().equals("Reports")) {
            toggleReports();
        }
        System.out.println("Pending Button Clicked");
        if(!ToggleTracking.getCurrentTab().equals("Pending")){
            ToggleTracking.setCurrentTab("Pending");
            deliverReturnBtn.setText(LangToggleBtn[3]);
            FirebaseListener.navBarDataDisplay("Pending");
            ToggleTracking.clearOrders(); //clears the selection of orders when changing screens
        }

    }

    @FXML
    void onCompleteClick(ActionEvent event) throws IOException {
        System.out.println("Completed Button Clicked: Clearing Selection");
        System.out.println("Completed Button Clicked");

        if(ToggleTracking.getCurrentTab().equals("Reports")){
            toggleReports();
        }
        if(!ToggleTracking.getCurrentTab().equals("Completed")){
            ToggleTracking.setCurrentTab("Completed");
            deliverReturnBtn.setText(LangToggleBtn[4]);
            FirebaseListener.navBarDataDisplay("Completed");
            ToggleTracking.setIsEdit(false);
            ToggleTracking.setIsNewDelivery(false);
            toggleNewDelivery();
            ToggleTracking.clearOrders(); //clears the selection of orders when changing screens
        }

    }
    @FXML
    void onReportsClick(ActionEvent event) throws IOException {
        System.out.println("Reports Button Clicked");
        if(!ToggleTracking.getCurrentTab().equals("Reports")) {
            ToggleTracking.setCurrentTab("Reports");
            orderDisplayContainer.getChildren().clear();
            deliverReturnBtn.setText("Deliver Package");
            buttonToggle(reportsButton);
            buttonNotToggle(pendingButton);
            buttonNotToggle(completedButton);
            toggleReports();
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
    void handleLoginButtonChange() {

        if(Employee.getCurrentLogin() != null){
            signOutVbox.setVisible(!signOutVbox.isVisible());
        }else{
            loginFormController.setLoginVBoxVisibility(true);
        }

        languageMenuUI.setVisible(false);
        adminToolsNav.setVisible(false);
        settingNavbar.setVisible(false);
        buttonNotToggle(settingsButton);
        isToggleSettings = false;
        isToggleAdmin = false;

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
        }
        adminToolsNav.setVisible(false);
        isToggleAdmin = false;
        languageMenuUI.setVisible(false);
        signOutVbox.setVisible(false);

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

        languageMenuUI.setVisible(false);
        settingNavbar.setVisible(false);
        signOutVbox.setVisible(false);

        buttonNotToggle(settingsButton);
        isToggleSettings = false;

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
        if (!ToggleTracking.getSelectedOrders().isEmpty()) {
            String currentTab = ToggleTracking.getCurrentTab();
            String collectionFrom = currentTab.equals("Pending") ? "pendingDeliveries" : "completedDeliveries";
            String collectionTo = currentTab.equals("Pending") ? "completedDeliveries" : "pendingDeliveries";

            String tempNewMess = null;
            OrderHistory tempHistory = null;
            for (DeliveryRequisition order : ToggleTracking.getSelectedOrders()) {
                DeliveryRequisition swapOrder = DataBaseMgmt.findOrder(order.getOrderNumberDisplay(), collectionFrom);
                if(currentTab.equals("Pending")){
                    tempNewMess = deliveryFormController.createOrderHistoryMessage("delivery");
                    tempHistory = new OrderHistory(tempNewMess,null);
                    swapOrder.getOrderStatusHistory().add(tempHistory);
                    NotifyMessg.createMessg("delivered", order.getOrderNumberDisplay());
                }
                if(currentTab.equals("Completed")){
                    tempNewMess = deliveryFormController.createOrderHistoryMessage("return");
                    tempHistory = new OrderHistory(tempNewMess,null);
                    swapOrder.getOrderStatusHistory().add(tempHistory);
                    NotifyMessg.createMessg("returnToPending", order.getOrderNumberDisplay());
                }


                DataBaseMgmt.swapDB(swapOrder, swapOrder.getOrderNumberDisplay(), collectionFrom, collectionTo);


            }
            Platform.runLater(() -> {
                for (DeliveryRequisition order : ToggleTracking.getSelectedOrders()) {
                    Node node = findNodeByRequisition(order);
                    if (node != null) {
                        orderDisplayContainer.getChildren().remove(node);
                    }
                }
            });

            ToggleTracking.clearOrders();
            System.out.println("Processed all selected orders for delivery/return.");
        } else {
            System.out.println("No orders selected for delivery/return.");
        }
    }

    @FXML
    void onDeleteSelectedOrders(ActionEvent event) {
        System.out.println("Delivery orders button clicked");
        if (!ToggleTracking.getSelectedOrders().isEmpty()) {
            String currentTab = ToggleTracking.getCurrentTab();
            String collectionFrom = currentTab.equals("Pending") ? "pendingDeliveries" : "completedDeliveries";
            for (DeliveryRequisition order : ToggleTracking.getSelectedOrders()) {
                DeliveryRequisition deleteOrder = DataBaseMgmt.findOrder(order.getOrderNumberDisplay(), collectionFrom);
                NotifyMessg.createMessg("deleted", deleteOrder.getOrderNumberDisplay());
                DataBaseMgmt.deleteFromDB(deleteOrder.getOrderNumberDisplay(), collectionFrom);
                deleteRecord(order.getOrderNumberDisplay());
            }
            Platform.runLater(() -> {
                for (DeliveryRequisition order : ToggleTracking.getSelectedOrders()) {
                    Node node = findNodeByRequisition(order);
                    if (node != null) {
                        orderDisplayContainer.getChildren().remove(node);
                    }
                }
            });
            ToggleTracking.clearOrders();
            System.out.println("Processed all selected orders for delivery/return.");
        } else {
            System.out.println("No orders selected for delivery/return.");
        }
    }

    private Node findNodeByRequisition(DeliveryRequisition requisition) {
        for (Node node : orderDisplayContainer.getChildren()) {
            if (requisition.equals(node.getUserData())) {
                return node;
            }
        }
        return null; // Not found
    }

    @FXML
    void onEditDelivery(ActionEvent event) {
        System.out.println("Edit Button pressed");
        if (ToggleTracking.getSelectedOrders().size() == 1) {
            String selectedOrderNum = ToggleTracking.getSelectedCardOrderNum();
            System.out.println("OnEditDelivery check: " + selectedOrderNum);
            if (ToggleTracking.getIsEdit()) {
                ToggleTracking.setIsEdit(false);
                ToggleTracking.setIsNewDelivery(false);
            } else {
                ToggleTracking.setIsEdit(true);
                ToggleTracking.setIsNewDelivery(false);
            }

            toggleNewDelivery();

            if (ToggleTracking.getIsEdit() && deliveryFormController != null) {
                deliveryFormController.openEditDelivery();
            } else {
                if (selectedOrderNum != null) {
                    System.out.println("Edit mode closed but order remains selected: " + selectedOrderNum);
                }
            }
        } else {
            System.out.println("No order selected or multiple orders selected. Cannot edit.");
            if (ToggleTracking.getIsEdit()) {
                toggleNewDelivery();
            }
        }
    }


    /**
     * turns off unneeded buttons and makes the search bar visible when you go to the reports tab
     */

    public void onNotifyMessage(){
        notifyMessageController.displayNotfications();
    }

    public void setLoginButtonText(String currentStatus){
        LoginButtonChange.setText(currentStatus);
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
        button.getStyleClass().clear();
        button.getStyleClass().add("isToggled");
    }

    public void buttonNotToggle(Button button){
        button.getStyleClass().clear();
        button.getStyleClass().add("isNotToggled");
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
                buttonNotToggle(reportsButton);
                tempQueue = currentQueue;
            }

            if(ToggleTracking.getCurrentTab().equals("Pending") && collectionName.equals("pendingDeliveries")){
                orderDisplayContainer.getChildren().clear();
                buttonToggle(pendingButton);
                buttonNotToggle(reportsButton);
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
                            HBox orderTemplate = loader.load();
                            OrderCardUIController controller = loader.getController();
                            controller.updateOrderLabels(order);
                            orderDisplayContainer.getChildren().add(orderTemplate);

                    // Link the DeliveryRequisition with the GridPane
                    orderTemplate.setUserData(order);
                } catch (IOException e) {
                    System.out.println("Failed to find OrderCard.fxml");
                }
            }


            selectOrder();
        });
    }

    /**
     * Displays a queue of search results to the homepage
     * @param searchQueue, queue of delivery requisitions that holds the search results
     */
    public void displaySearchResults(Queue<DeliveryRequisition> searchQueue) {

        orderDisplayContainer.getChildren().clear();
        if(searchQueue == null && searchQueue.isEmpty()){

            return;
        }

        for(DeliveryRequisition order: searchQueue){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hospitaldeliveryinterface/OrderCard.fxml"));
                HBox orderTemplate = loader.load();
                OrderCardUIController controller = loader.getController();
                controller.updateOrderLabels(order);
                orderDisplayContainer.getChildren().add(orderTemplate);

            } catch (IOException e) {
                System.out.println("Failed to find OrderCard.fxml");
            }
        }
    }

    public void selectOrder() {
        for (Node node : orderDisplayContainer.getChildren()) {
            node.setOnMouseClicked(mouseEvent -> {
                if (node instanceof HBox) {
                    HBox hbox = (HBox) node;
                    DeliveryRequisition requisition = (DeliveryRequisition) hbox.getUserData();
                    if (ToggleTracking.getSelectedOrders().contains(requisition)) {
                        ToggleTracking.deselectNode(hbox);
                    } else {
                        ToggleTracking.selectNode(hbox);
                    }
                }
            });
        }
    }

    public void showDialogSignOut() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Logged out");
        alert.setTitle("Signed out");
        alert.setContentText("You are signing out");
        Optional<ButtonType> result = alert.showAndWait();
        signOutVbox.setVisible(false);

    }

    @FXML
    void onSignOutClick(ActionEvent event) {
        if (Employee.getCurrentLogin() != null) {
            showDialogSignOut();
            DataBaseMgmt.updateLoginStatus(Employee.getCurrentLogin(), "False");
            Employee.setCurrentLogin(null);
            HashMap<String, String[]> checkStoredLang = MitchTextTranslate.getStoredLang();
            String[] retrieveTranslatedText = checkStoredLang.get(ToggleTracking.getLanguageTrack());

            String translateExist = retrieveTranslatedText != null ? retrieveTranslatedText[10] : "Log In";

            LoginButtonChange.setText(translateExist);
            onSetDisabled(true);
            adminButton.setVisible(false);
            //LogInVbox.setVisible(true);
        }
    }

    @FXML
    void onCreateUserClick(ActionEvent event){
        createUserController.onCreateUserForm();
        ToggleTracking.setisCreateUser(!ToggleTracking.getIsCreateUser());
        adminToolsNav.setVisible(false);
        settingNavbar.setVisible(false);
        buttonNotToggle(settingsButton);
        isToggleSettings = false;
    }

    public void onSearchClick() {
        Queue<DeliveryRequisition> tempQueue = searchAlgolia(searchBarTextField.getText());
        displaySearchResults(tempQueue);
    }

    /**
     * turns off unneeded buttons and makes the search bar visible when you go to the reports tab
     */
   private void toggleReports() {
        editDeliverButtonsHbox.setVisible(!editDeliverButtonsHbox.isVisible());
        searchHBox.setPrefHeight((searchHBox.getHeight() + 50.0) % 100.0);
        searchBarTextField.setVisible(!searchBarTextField.isVisible());

    }

}