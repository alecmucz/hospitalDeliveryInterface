package com.example.hospitaldeliveryinterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateOrderController {

    @FXML
    private TextArea addNotesTextfield;

    @FXML
    private TextField doseNumTextfield;

    @FXML
    private TextField doseTextfield;

    @FXML
    private TextField firstnameTextfield;

    @FXML
    private TextField lastnameTextfield;

    @FXML
    private TextField medDescTextfield;

    @FXML
    private TextField locationTextfield;

    @FXML
    private Label errorMessTextfield;

    TextField[] allInputs;


    public void initialize(){

        errorMessTextfield.setText("");
        allInputs = new TextField[]{
                firstnameTextfield,
                lastnameTextfield,
                medDescTextfield,
                locationTextfield,
                doseNumTextfield,
                doseTextfield
        };


        firstnameTextfield.textProperty().addListener((observableValue, s, t1) -> {
            if(!t1.isEmpty()){
                defaultBorder(firstnameTextfield);
            }
        });
        lastnameTextfield.textProperty().addListener((observableValue, s, t1) -> {
            if(!t1.isEmpty()){
                defaultBorder(lastnameTextfield);
            }
        });
        medDescTextfield.textProperty().addListener((observableValue, s, t1) -> {
            if(!t1.isEmpty()){
                defaultBorder(medDescTextfield);
            }
        });
        locationTextfield.textProperty().addListener((observableValue, s, t1) -> {
            if(!t1.isEmpty()){
                defaultBorder(locationTextfield);
            }
        });
        doseNumTextfield.textProperty().addListener((observableValue, s, t1) -> {
            if(!t1.isEmpty()){
                defaultBorder(doseNumTextfield);
            }
        });
        doseTextfield.textProperty().addListener((observableValue, s, t1) -> {
            if(!t1.isEmpty()){
                defaultBorder(doseTextfield);
            }
        });


    }

    @FXML
    protected void onCancelForm(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onCreateForm(ActionEvent event) {
        boolean checkErrors = false;
        for (TextField child: allInputs){
            if(child.getText().isEmpty()){
                errorBorder(child);
                checkErrors = true;
            }
        };

        if(checkErrors){
            errorMessTextfield.setText("**Error: Please fill out all required fields.**");
        }else{
            try {
                int numDose = Integer.parseInt(doseNumTextfield.getText());
                String fullName = firstnameTextfield.getText() + " " + lastnameTextfield.getText();
                DeliveryRequisition newOrder = new DeliveryRequisition(
                        fullName,
                        locationTextfield.getText(),
                        medDescTextfield.getText(),
                        doseTextfield.getText(),
                        numDose
                        );
                Pending pendQueue = Pending.getInstance();
                pendQueue.addOrders(newOrder);
                navHomePage();
            }catch (NumberFormatException ex) {
                errorMessTextfield.setText("**Error: The fields selected should be Numeric.**");
                errorBorder(doseNumTextfield);

            }
        }
    }


    public void errorBorder(TextField textField){
        textField.setStyle("-fx-border-color: red;");
    }
    public void defaultBorder(TextField textfield){
        textfield.setStyle("-fx-border-color: grey;");
    }

    protected void navHomePage(){

        FXMLLoader fxmlLoader = new FXMLLoader(PharmaTracApp.class.getResource("Homepage.fxml"));
        Stage stage = PharmaTracApp.getStage();
        Scene scene = PharmaTracApp.getScene();
        try {
            scene.setRoot(fxmlLoader.load());
            stage.setTitle("PharmaTrac");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
