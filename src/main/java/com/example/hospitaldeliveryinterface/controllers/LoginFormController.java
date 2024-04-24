package com.example.hospitaldeliveryinterface.controllers;

import com.example.hospitaldeliveryinterface.firebase.DataBaseMgmt;
import com.example.hospitaldeliveryinterface.model.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class LoginFormController {

    @FXML
    private BorderPane LogInVbox;

    @FXML
    private Button LoginButton;

    @FXML
    private Label labelLoginError;

    @FXML
    private HBox loginErrorHbox;

    @FXML
    private VBox loginPanel;

    @FXML
    private PasswordField textFieldPassword;

    @FXML
    private TextField textFieldUsername;

    private HomepageController controller;
    public void initialize(){
        LogInVbox.setVisible(false);
        LogInVbox.getStylesheets().clear();
    }

    public void setHomepageController(HomepageController control){
        controller = control;
    }
    public void setLoginVBoxVisibility(boolean currentStatus){
        LogInVbox.setVisible(currentStatus);
    }
    @FXML
    public void loginUser() throws ExecutionException, InterruptedException {
        loginErrorHbox.setVisible(false);
        if(textFieldUsername.getText().isEmpty() || textFieldPassword.getText().isEmpty()) {
            labelLoginError.setText("Username or Password is empty");
            labelLoginError.setStyle("-fx-text-fill: red;");
            loginErrorHbox.setVisible(true);
            errorBorder(textFieldUsername);
            errorBorder(textFieldPassword);
            return;
        }
        // Retrieve user data based on the provided username
        Map<String, Object> userData = DataBaseMgmt.retrieveUserData(textFieldUsername.getText(),"employees");
        if (userData != null) {
            // Check if the provided username and password match the stored username and password
            String storedUsername = (String) userData.get("Username");
            String storedPasswordHash = (String) userData.get("Password");

            // Hash the password provided by the user
            String providedPasswordHash = hashPassword(textFieldPassword.getText());

            if (storedPasswordHash.equals(providedPasswordHash)) {

                // Password matches
                DataBaseMgmt.updateLoginStatus(textFieldUsername.getText(),"True");
                LogInVbox.setVisible(false);
                Employee.setCurrentLogin(textFieldUsername.getText());
                controller.setLoginButtonText("EID: " + textFieldUsername.getText());
                System.out.println("Logged in");
                //changes text and style and changes it back to default border
                showDialogCorrect();
                defaultBorder(textFieldUsername);
                defaultBorder(textFieldPassword);
                controller.onSetDisabled(false);

            } else {
                // Password does not match
                labelLoginError.setText("Password is incorrect");
                labelLoginError.setStyle("-fx-text-fill: red;");
                loginErrorHbox.setVisible(true);
                errorBorder(textFieldPassword);
            }
        } else {
            // User does not exist in the database
            labelLoginError.setText("User does not exist");
            labelLoginError.setStyle("-fx-text-fill: red;");
            loginErrorHbox.setVisible(true);
            errorBorder(textFieldUsername);
        }
        textFieldUsername.clear();
        textFieldPassword.clear();
    }
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
    void onReturnToHome(ActionEvent event) {
        LogInVbox.setVisible(false);
        loginErrorHbox.setVisible(false);
        defaultBorder(textFieldUsername);
        defaultBorder(textFieldPassword);
        textFieldUsername.clear();
        textFieldPassword.clear();
    }

    public  void errorBorder(TextField textField){
        textField.setStyle("-fx-border-color: red;");
    }

    public  void defaultBorder(TextField textfield){
        textfield.setStyle("-fx-border-color: grey;");
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

    /*@FXML
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

    }*/

}
