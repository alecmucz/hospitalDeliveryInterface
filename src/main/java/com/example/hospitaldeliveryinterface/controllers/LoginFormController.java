package com.example.hospitaldeliveryinterface.controllers;

import com.example.hospitaldeliveryinterface.firebase.DataBaseMgmt;
import com.example.hospitaldeliveryinterface.model.Employee;
import com.example.hospitaldeliveryinterface.model.ToggleTracking;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class LoginFormController {


    @FXML
    private Tooltip TooltipPassword;

    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
    private BorderPane LogInVbox;

    @FXML
    private Button LoginButton;

    @FXML
    private Label emplopyeeLoginLBL;

    @FXML
    private Label labelLoginError;

    @FXML
    private HBox loginErrorHbox;

    @FXML
    private VBox loginPanel;

    @FXML
    private Button returnToHomeBtn;

    @FXML
    private PasswordField textFieldPassword;

    @FXML
    private TextField textFieldUsername;



    private HomepageController controller;
    public void initialize(){
        LogInVbox.setVisible(false);
        LogInVbox.getStylesheets().clear();
        textFieldPassword.textProperty().addListener((observable, oldValue, newValue) -> updateTooltips());
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
                String userType = storedUsername.startsWith("A") ? "admin" : "employee";
                if (userType.equals("admin")) {
                    // If the user is an admin, make the admin button visible
                    controller.onSetVisibleAdmin();
                }

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
    public void showPassword() {
        // Set the tooltips based on the checkbox state
        if (showPasswordCheckBox.isSelected()) {
            updateTooltips();
            // Show tooltips instantly on hover and do not hide them automatically
            TooltipPassword.setShowDelay(Duration.ZERO);
            TooltipPassword.setAutoHide(false);

        } else {
            // Clear the tooltip texts
            TooltipPassword.setText("");

            // Hide tooltips instantly
            TooltipPassword.setAutoHide(true);
            TooltipPassword.hide(); // Hide the tooltip instantly
            TooltipPassword.setShowDelay(Duration.ZERO);

        }
    }

    private void updateTooltips() {
        // Only update the tooltips if the checkbox is selected
        if (showPasswordCheckBox.isSelected()) {
            // Update the tooltip texts with the text from the text fields
            TooltipPassword.setText(textFieldPassword.getText());
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


    public void showDialogCorrect () {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Correct Input");
        alert.setTitle("Logged in");
        alert.setContentText("You are signed in");
        Optional<ButtonType> result = alert.showAndWait();
    }

    public void updateLanguageLabel(String[] langTextChange){
        emplopyeeLoginLBL.setText(langTextChange[32]);
        textFieldUsername.setPromptText(langTextChange[35]);
        textFieldPassword.setPromptText(langTextChange[37]);
        LoginButton.setText(langTextChange[10]);
        returnToHomeBtn.setText(langTextChange[33]);
    }


}
