package com.example.hospitaldeliveryinterface.controllers;

import com.example.hospitaldeliveryinterface.firebase.DataBaseMgmt;
import com.example.hospitaldeliveryinterface.firebase.FirebaseListener;
import com.example.hospitaldeliveryinterface.model.DeliveryRequisition;
import com.example.hospitaldeliveryinterface.model.NotifyMessg;
import com.example.hospitaldeliveryinterface.model.ToggleTracking;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import static com.example.hospitaldeliveryinterface.Algolia.AlgoliaMgmt.createNewIndex;

public class DeliveryFormController {

    @FXML
    private Button addNoteBtn;

    @FXML
    private TextArea addNoteText;

    @FXML
    private Label deliveryFormLabel;

    @FXML
    private BorderPane deliveryFormPane;

    @FXML
    private TextField doseAmountText;

    @FXML
    private TextField doseText;

    @FXML
    private Label errMessLabel;

    @FXML
    private TextField firstnameText;

    @FXML
    private TextField lastnameText;

    @FXML
    private TextField locationText;

    @FXML
    private TextField medicationText;

    /***Non FXML Components***/
    private TextField[] allInputs;

    public void initialize(){

        addNoteText.setVisible(false);

        errMessLabel.setText("");
        allInputs = new TextField[]{
                firstnameText,
                lastnameText,
                medicationText,
                locationText,
                doseAmountText,
                doseText,
        };

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
        ToggleTracking.setIsAddNote(!ToggleTracking.getIsAddNote());
        toggleAddNote();
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
                String firstnoWhiteSpace = firstnameText.getText().replaceAll("\\s","");
                String lastnoWhiteSpace = lastnameText.getText().replaceAll("\\s","");

                String fullName = firstnoWhiteSpace + " " + lastnoWhiteSpace;
                String newOrderNum = DeliveryRequisition.generateOrderNum();


                if(ToggleTracking.getIsEdit()){
                   newOrderNum = ToggleTracking.getSelectedCardOrderNum();
                }
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
                        ""
                );

                if(ToggleTracking.getIsEdit() && ToggleTracking.getSelectedCardOrderNum() != null){
                    NotifyMessg.createMessg("edited", "[Employee ID]", ToggleTracking.getSelectedCardOrderNum());


                    if(ToggleTracking.getCurrentTab().equals("Pending")) {
                        DataBaseMgmt.editOrder("pendingDeliveries", ToggleTracking.getSelectedCardOrderNum(), newOrder);
                        FirebaseListener.listenToPendingDeliveries();
                        createNewIndex(newOrder);
                    }
                    if(ToggleTracking.getCurrentTab().equals("Completed")) {
                        DataBaseMgmt.editOrder("completedDeliveries", ToggleTracking.getSelectedCardOrderNum(), newOrder);
                        FirebaseListener.listenToCompletedDeliveries();
                        createNewIndex(newOrder);
                    }

                    ToggleTracking.setIsEdit(false);

                }
                else {
                    NotifyMessg.createMessg("newDelivery", "[Employee ID]", newOrderNum);
                    DataBaseMgmt.addToDB(newOrder, "pendingDeliveries",true);
                    createNewIndex(newOrder);
                }
                clearText();
            }else{
                errMessLabel.setText("**Error: Only numbers for this field.**");
                errorBorder(doseAmountText);
            }
        }
    }



    /**other methoid helper**/

    public void toggleAddNote(){
        if(ToggleTracking.getIsAddNote()){
            addNoteBtn.setText("Close Note");
            addNoteText.setVisible(true);
        }else{
            addNoteBtn.setText("Add Note");
            addNoteText.setVisible(false);
        }
    }

    public  void errorBorder(TextField textField){
        textField.setStyle("-fx-border-color: red;");
    }


    public  void defaultBorder(TextField textfield){
        textfield.setStyle("-fx-border-color: grey;");
    }

    public void clearText(){
        for(TextField child: allInputs){
            child.setText("");
        }
        addNoteText.setText("");
        errMessLabel.setText("");
    }

    public void openNewDlivery(){
        deliveryFormLabel.setText("New Delivery Form");
        clearText();
    }
    public void openEditDelivery(Node selectCard){
        System.out.println("Edit Delivery is Open");
        deliveryFormPane.setPrefWidth(320);
        deliveryFormPane.setVisible(true);
        deliveryFormLabel.setText("Edit Delivery Form");
        clearText();

        if(selectCard instanceof HBox){
            HBox hBoxpane = (HBox) selectCard;
            for(Node selectChild: hBoxpane.getChildren()){
                if (selectChild instanceof GridPane) {
                    GridPane gridPane = (GridPane) selectChild;
                    for(Node gridpaneNode : gridPane.getChildren()){
                        if(gridpaneNode instanceof Label){
                            setTextFieldFromLabel((Label)gridpaneNode);
                        }
                    }
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
                    System.out.println("NO ID EXIST ON ORDERCARD: " + label.getId());
            }
        }



    }


}
