package com.example.hospitaldeliveryinterface.model;

import com.example.hospitaldeliveryinterface.controllers.HomepageController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Employee {
    @FXML
    private TextField textFieldEmployeeID;
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldPassword1;
    @FXML
    private TextField textFieldConfirmPassword;



    private String firstName;
    private String lastName;
    private int employeeId;
    private String email;

    private static String currentLogin = null;

    public static String getCurrentLogin() {
        return currentLogin;
    }

    public static void setCurrentLogin(String currentLogin) {
        Employee.currentLogin = currentLogin;
    }

    public Employee(String firstName, String lastName, int employeeId){
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeId = employeeId;

    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setEmployeeId() {
        this.employeeId = employeeId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName(){
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public int getEmployeeId() {
        return employeeId;
    }
    public String getEmail() {
        return email;
    }




    public static String textFieldCheckCreatingAccount(String employeeID,String firstName,String lastName,String email, String password, String confirmPassword) {
        if(!(employeeID.matches("S\\d{8}"))) {
            return "**Error : Input doesn't follow staff ID format ex:S01234567";

        }
        if(!(firstName.matches("[A-Za-z]+"))) {
            return "**Error : First name must be only letters";
        }
        if(!(lastName.matches("[A-Za-z]+"))) {
            return "**Error : Last name must be only letters";
        }

        if (!(email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))) {
            return "**Error: Input must be email format ex: example@gmail.com";
        }
        if (!(password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"))) {
            return "**Error: input must include lower and uppercase letters with digits,special characters and minimum of 8 characters";
        }
        if (!(password.equals(confirmPassword))){
            return "**Error: Confirm password does not match the password text field above";
        }
        return "Successful!";
    }
    public static void errorBorder(TextField textField) {
        textField.setStyle("-fx-border-color: red;");
    }
}


