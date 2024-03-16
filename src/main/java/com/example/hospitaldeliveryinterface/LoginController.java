package com.example.hospitaldeliveryinterface;

import com.google.firebase.database.*;
import com.google.firebase.remoteconfig.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import java.util.Optional;


public class LoginController {

    @FXML
    private Button LoginButton;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private TextField textFieldPassword;



    //this method checks if the parameters are empty, if they are checker is turned on becoming true
    //if it passes the first test it checks if username and password matches the specified requirements, if they don't match checker is turned on becoming true
    //if it passes all the test then it stays turned off
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
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("correct input");
        alert.setTitle("Warning");
        alert.setContentText("next page");
        Optional<ButtonType> result = alert.showAndWait();
    }

    @FXML
    void handleLoginButton() {
            if (textFieldCheck(textFieldUsername.getText(), textFieldPassword.getText()) == false) {
                showDialogCorrect();
            } else {
                showDialog();
            }
        }








}