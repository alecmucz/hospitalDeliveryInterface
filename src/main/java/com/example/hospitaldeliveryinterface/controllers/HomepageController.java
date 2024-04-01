package com.example.hospitaldeliveryinterface.controllers;

import com.example.hospitaldeliveryinterface.PharmaTracApp;
import com.example.hospitaldeliveryinterface.firebase.DataBaseMgmt;
import com.example.hospitaldeliveryinterface.firebase.FirebaseListener;
import com.example.hospitaldeliveryinterface.model.DeliveryRequisition;
import com.example.hospitaldeliveryinterface.model.MitchTextTranslate;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;
import net.suuft.libretranslate.Translator;

import java.io.IOException;
import java.util.*;








import java.util.concurrent.ExecutionException;

import static com.example.hospitaldeliveryinterface.firebase.DataBaseMgmt.search;
import static com.example.hospitaldeliveryinterface.firebase.DataBaseMgmt.updateLoginStatus;

public class HomepageController {
    @FXML
    private TextField textFieldConfirmPassword;
    @FXML
    private TextArea createUserError;
    @FXML
    private TextField textFieldEmployeeID;
    @FXML
    private TextField searchBarTextField;
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
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
    private Button deleteOrdersBtn;

    @FXML
    private Button createUserBtn;

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
    private TextField textFieldEmail;

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

    @FXML
    private MenuButton languageMenu;

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
    private TextField[] allInputs;

    private TextField[] createUserInputs;

    private Button[] toolBarBtn;

   private String[] LangToggleBtn;

    public void initialize(){

        LangToggleBtn = new String[]{
                "Completed",
                "Pending",
                "Settings",
                "Deliver Package",
                "Return To Pending",
                "Edit Delivery",
                "Close Edit Delivery",
                "+ New Delivery",
                "Close New Delivery",
                "Admin Tools",
                "Login",
                "Sign Out",
                "Delete Orders",
                "Create Users"
        };


        MitchTextTranslate.initialLanguages();
        populateLanguageMenu();


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
        createUserInputs = new TextField[]{
                textFieldEmployeeID,
                textFieldFirstName,
                textFieldLastName,
                textFieldEmail,
                textFieldPassword1,
                textFieldConfirmPassword,
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


/**************************MITCHELL LANGUAGE STUFF*************************/

public void setUpLangText(String[] langTextChange){

    newDeliveryButton.setText(isNewDelivery?langTextChange[8]:langTextChange[7]);
    editBtn.setText(isEdit?langTextChange[6]:langTextChange[5]);

    settingsButton.setText(langTextChange[2]);
    completedButton.setText(langTextChange[0]);
    pendingButton.setText(langTextChange[1]);
    createUserBtn.setText(langTextChange[13]);
    deleteOrdersBtn.setText(langTextChange[12]);
    adminButton.setText("< "+langTextChange[9]);
    LoginButtonChange.setText(langTextChange[10]);


    switch (currentPage){
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
public void populateLanguageMenu(){

        for (Map.Entry<String, String> entry : MitchTextTranslate.getLanguagesMap().entrySet()) {

            CheckBox tempCheckBox = new CheckBox(entry.getKey());
            tempCheckBox.setOnAction(event -> {
                for (MenuItem menuItem : languageMenu.getItems()) {
                    if (menuItem instanceof CustomMenuItem) {
                        CustomMenuItem customMenuItem = (CustomMenuItem) menuItem;
                        CheckBox checkBox = (CheckBox) customMenuItem.getContent();
                        if (checkBox != tempCheckBox) {
                            checkBox.setSelected(false);
                        }
                    }
                }
                CheckBox selectedCheckBox = (CheckBox) event.getSource();
                String selectedLanguage = selectedCheckBox.getText();
                languageMenu.setText("Language: " + selectedLanguage);

                HashMap<String,String[]> checkStoredLang = MitchTextTranslate.getStoredLang();

                if(checkStoredLang.containsKey(entry.getKey())){
                    LangToggleBtn = checkStoredLang.get(entry.getKey());
                    setUpLangText(LangToggleBtn);
                }

            });

            CustomMenuItem tempCustomItem = new CustomMenuItem(tempCheckBox);
            languageMenu.getItems().add(tempCustomItem);

            if ("English".equals(entry.getKey())) {
                tempCheckBox.setSelected(true);
            }
        }
        languageMenu.setMaxHeight(200);
        languageMenu.setText("Language: English");
         MitchTextTranslate.addOrUpdateEntry(LangToggleBtn);
}




    /***********************************************************************************/
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
        System.out.println("Pending Button Clicked");
        if(!currentPage.equals("Pending")){
            currentPage = "Pending";
            deliverReturnBtn.setText(LangToggleBtn[3]);
            FirebaseListener.navBarDataDisplay("Pending");
        }
    }

    @FXML
    void onCompleteClick(ActionEvent event) throws IOException {
        System.out.println("Completed Button Clicked");
        if(!currentPage.equals("Completed")){
            currentPage = "Completed";
            deliverReturnBtn.setText(LangToggleBtn[4]);
            FirebaseListener.navBarDataDisplay("Completed");
            isEdit = false;
            isNewDelivery = false;
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
            editBtn.setText(LangToggleBtn[6]);
            buttonToggle(editBtn);
            buttonNotToggle(newDeliveryButton);
        }

        if(!isEdit || isDelivered){
            editBtn.setText(LangToggleBtn[5]);
            buttonNotToggle(editBtn);
        }

        if(isNewDelivery){
            selectedCard = null;
            selectedCardOrderNum = null;
            clearText();
            deliveryFormLabel.setText("New Delivery Form");
            newDeliveryButton.setText(LangToggleBtn[8]);
            buttonToggle(newDeliveryButton);
            buttonNotToggle(editBtn);
        }

        if(!isNewDelivery){
            newDeliveryButton.setText(LangToggleBtn[7]);
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
                buttonToggle(completedButton);
                buttonNotToggle(pendingButton);
                tempQueue = currentQueue;
            }

            if(currentPage.equals("Pending") && collectionName.equals("pendingDeliveries")){
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
                case "notesDisplay":
                    addNoteText.setText(label.getText());

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
        if(selectedorderNum != null){
            if(currentPage.equals("Pending")) {
                DataBaseMgmt.swapDB(selectedCardOrderNum, "pendingDeliveries","completedDeliveries");
                NotifyMessg.createMessg("delivered", "[Employee ID]", selectedCardOrderNum);
               // FirebaseListener.navBarDataDisplay("Pending");

            }

            if(currentPage.equals("Completed")) {
                DataBaseMgmt.swapDB(selectedCardOrderNum, "completedDeliveries","pendingDeliveries");
                NotifyMessg.createMessg("returnToPending", "[Employee ID]", selectedCardOrderNum);
               // FirebaseListener.navBarDataDisplay("Completed");

            }
            isDelivered = false;
            toggleNewDelivery();
            //deselectOrder();
            selectedCard = null;
            selectedCardOrderNum = null;
        }
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


    @FXML
    void handleLoginButtonChange() {
        if (LoginButtonChange.getText().equals("Login")) {
            textFieldUsername.clear();
            textFieldPassword.clear();
            LogInVbox.setVisible(true);
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
                .setEmail(textFieldEmail.getText())
                .setEmailVerified(false)
                .setPassword(textFieldPassword1.getText())
                .setPhoneNumber(textFieldLastName.getText())
                .setDisplayName(textFieldFirstName.getText())
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

    public void createUser() throws ExecutionException, InterruptedException {

        boolean checkErrors = false;
        //enhanced for loop, loops through array full of textFields
        for (TextField childInput : createUserInputs) {
            //if statement to check if its empty
            if(childInput.getText().isEmpty()) {
                errorBorder(childInput);
                checkErrors = true;
            }
        }
        //checking if checkError equals true
        if(checkErrors) {
            createUserError.setText("**Error: Please fill out all text fields");
        } else {
            Map<String, Object> isUserExist = DataBaseMgmt.retrieveUserData(textFieldEmployeeID.getText(), "employees");
            if (isUserExist != null) {
                createUserError.setText("**Error: Account " + textFieldEmployeeID.getText() + " already exist");
            } else {
                //init calls method that has return value of string
                String requiredCheck = Employee.textFieldCheckCreatingAccount(
                        textFieldEmployeeID.getText(),
                        textFieldFirstName.getText(),
                        textFieldLastName.getText(),
                        textFieldEmail.getText(),
                        textFieldPassword1.getText(),
                        textFieldConfirmPassword.getText()
                );
                if (requiredCheck.equals("Successful!")) {
                    DataBaseMgmt.addCreateUserDB(
                            textFieldEmployeeID.getText(),
                            textFieldFirstName.getText(),
                            textFieldLastName.getText(),
                            textFieldPassword1.getText(),
                            textFieldEmployeeID.getText(),
                            textFieldEmail.getText());
                    createUserError.setText(requiredCheck);
                } else {
                    createUserError.setText(requiredCheck);
                }
            }
        }
    }

    public void loginUser() throws ExecutionException, InterruptedException {
        if(textFieldUsername.getText().isEmpty() || textFieldPassword.getText().isEmpty()) {
            System.out.println("Username or Password is empty");
            return;
        }
        // Retrieve user data based on the provided username
        Map<String, Object> userData = DataBaseMgmt.retrieveUserData(textFieldUsername.getText(),"employees");
        if (userData != null) {
            // Check if the provided username and password match the stored username and password
            String storedUsername = (String) userData.get("Username");
            String storedPassword = (String) userData.get("Password");
            if (storedPassword.equals(textFieldPassword.getText())) {
                // Password matches
                showDialogCorrect();
                DataBaseMgmt.updateLoginStatus(textFieldUsername.getText(),"True");
                LogInVbox.setVisible(false);
                LoginButtonChange.setText("Sign out");
                Employee.setCurrentLogin(textFieldUsername.getText());
                usernameLabel.setText(textFieldUsername.getText());
                System.out.println("Logged in");

            } else {
                // Password does not match
                System.out.println("Password is incorrect");
            }
        } else {
            // User does not exist in the database
            System.out.println("User does not exist");
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
}