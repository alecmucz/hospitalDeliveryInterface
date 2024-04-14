package com.example.hospitaldeliveryinterface.controllers;

import com.example.hospitaldeliveryinterface.PharmaTracApp;
import com.example.hospitaldeliveryinterface.firebase.DataBaseMgmt;
import com.example.hospitaldeliveryinterface.model.Employee;
import com.example.hospitaldeliveryinterface.model.ToggleTracking;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CreateUserController {

    @FXML
    private VBox adminNavBar;

    @FXML
    private Button createUserButton;

    @FXML
    private Label createUserError;

    @FXML
    private HBox errorMessageHbox;

    @FXML
    private PasswordField textFieldConfirmPassword;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldEmployeeID;

    @FXML
    private TextField textFieldFirstName;

    @FXML
    private TextField textFieldLastName;

    @FXML
    private PasswordField textFieldPassword1;

    @FXML
    private Label us1;

    @FXML
    private Label us11;

    @FXML
    private Label us111;

    @FXML
    private Label us112;


    /**non fxml components***/
    private TextField[] createUserInputs;

    public void initialize(){
        createUserInputs = new TextField[]{
                textFieldEmployeeID,
                textFieldFirstName,
                textFieldLastName,
                textFieldEmail,
                textFieldPassword1,
                textFieldConfirmPassword,
        };
    }

    @FXML
    public void createUser() throws ExecutionException, InterruptedException {
        for (TextField childInput : createUserInputs) {
            //defaultBorder(childInput);
        }
        createUserError.setStyle("-fx-text-fill: red;");
        boolean checkErrors = false;
        //enhanced for loop, loops through array full of textFields
        for (TextField childInput : createUserInputs) {
            //if statement to check if its empty
            if (childInput.getText().isEmpty()) {
                errorBorder(childInput);
                checkErrors = true;
            }
        }
        //checking if checkError equals true
        if (checkErrors) {
            errorMessageHbox.setVisible(true);
            createUserError.setText("**Error: Please fill out all text fields");

        } else {
            Map<String, Object> isUserExist = DataBaseMgmt.retrieveUserData(textFieldEmployeeID.getText(), "employees");
            if (isUserExist != null) {
                errorMessageHbox.setVisible(true);
                createUserError.setText("**Error: Account " + textFieldEmployeeID.getText() + " already exist");
                //for loop goes through every text field and clears it
                clearFieldsAndBorders();
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

                // If validation fails, display error message and apply error borders
                if (!requiredCheck.equals("Successful!")) {
                    errorMessageHbox.setVisible(true);
                    createUserError.setText(requiredCheck);
                    createUserError.setStyle("-fx-text-fill: red;");

                    // Apply error borders to text fields based on validation results
                    if (requiredCheck.contains("staff ID")) {
                        errorBorder(textFieldEmployeeID);
                        errorMessageHbox.setVisible(true);
                        createUserError.setText(requiredCheck);
                    }
                    if (requiredCheck.contains("First name")) {
                        errorBorder(textFieldFirstName);
                        errorMessageHbox.setVisible(true);
                        createUserError.setText(requiredCheck);
                    }
                    if (requiredCheck.contains("Last name")) {
                        errorBorder(textFieldLastName);
                        errorMessageHbox.setVisible(true);
                        createUserError.setText(requiredCheck);
                    }
                    if (requiredCheck.contains("email format")) {
                        errorBorder(textFieldEmail);
                        errorMessageHbox.setVisible(true);
                        createUserError.setText(requiredCheck);
                    }
                    if (requiredCheck.contains("password")) {
                        errorBorder(textFieldPassword1);
                        errorBorder(textFieldConfirmPassword);
                        errorMessageHbox.setVisible(true);
                        createUserError.setText(requiredCheck);
                    }
                    if(requiredCheck.contains("lower and upper")) {
                        errorBorder(textFieldPassword1);
                        errorBorder(textFieldConfirmPassword);
                        errorMessageHbox.setVisible(true);
                        createUserError.setText(requiredCheck);
                    }
                } else {
                    // User creation successful, add user to database
                    errorMessageHbox.setVisible(true);
                    createUserError.setStyle("-fx-text-fill: green;");
                    //Hash the password using SHA-256
                    String hashedPassword = hashPassword(textFieldPassword1.getText());
                    DataBaseMgmt.addCreateUserDB(
                            textFieldEmployeeID.getText(),
                            textFieldFirstName.getText(),
                            textFieldLastName.getText(),
                            hashedPassword,
                            textFieldEmployeeID.getText(),
                            textFieldEmail.getText());

                    // Clear all text fields and reset borders to default
                    clearFieldsAndBorders();
                    createUserError.setText(requiredCheck);
                    /**defaultBorder(textFieldEmployeeID);
                     defaultBorder(textFieldFirstName);
                     defaultBorder(textFieldLastName);
                     defaultBorder(textFieldEmail);
                     defaultBorder(textFieldPassword1);
                     defaultBorder(textFieldConfirmPassword);**/
                }
            }
        }
    }
    //this uses SHA-256 algorithm and hashes the password for create user
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return null; // Or throw an exception
        }
    }

    @FXML
    void onCloseUserCreate(ActionEvent event) {
        adminNavBar.setPrefWidth(0);
        ToggleTracking.setisCreateUser(false);
        adminNavBar.setVisible(false);
        clearFieldsAndBorders();
        errorMessageHbox.setVisible(false);
    }


    /**other Methods***/

    public  void errorBorder(TextField textField){
        textField.setStyle("-fx-border-color: red;");
    }

    public  void defaultBorder(TextField textfield){
        textfield.setStyle("-fx-border-color: grey;");
    }
    public void clearFieldsAndBorders() {
        for (TextField childInput : createUserInputs) {
            defaultBorder(childInput);
            childInput.clear();
        }
    }

    public void onCreateUserForm(){
        if(!ToggleTracking.getIsCreateUser()){
            adminNavBar.setVisible(true);
            adminNavBar.setPrefWidth(314);
        }else {
            adminNavBar.setPrefWidth(0);
            adminNavBar.setVisible(false);
        }
    }

    /**public boolean registerUser() {
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
    }**/

}
