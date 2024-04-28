package com.example.hospitaldeliveryinterface.controllers;

import com.example.hospitaldeliveryinterface.firebase.DataBaseMgmt;
import com.example.hospitaldeliveryinterface.firebase.FirebaseListener;
import com.example.hospitaldeliveryinterface.model.*;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Queue;

import static com.example.hospitaldeliveryinterface.Algolia.AlgoliaMgmt.createNewIndex;

public class DeliveryFormController {

    @FXML
    private Button addNoteBtn;

    @FXML
    private TextArea addNoteText;

    @FXML
    private Button clearBtn;

    @FXML
    private Label deliveryFormLabel;

    @FXML
    private BorderPane deliveryFormPane;

    @FXML
    private TextField doseAmountText;

    @FXML
    private Label doseQuantlbl;

    @FXML
    private TextField doseText;

    @FXML
    private Label doselbl;

    @FXML
    private Label errMessLabel;

    @FXML
    private TextField firstnameText;

    @FXML
    private Label formDescriplbl;

    @FXML
    private TextField lastnameText;

    @FXML
    private TextField locationText;

    @FXML
    private Label locationlbl;

    @FXML
    private Label medDescriplbl;

    @FXML
    private Label medInfoLbl;

    @FXML
    private TextField medicationText;

    @FXML
    private Label mrnLbl;

    @FXML
    private TextField mrnText;

    @FXML
    private Label nameLbl;

    @FXML
    private Label patientInfoLbl;

    @FXML
    private Button submitBtn;

    /***Non FXML Components***/
    private TextField[] allInputs;

    String tempNewDeliveryLang;
    String tempEditDeliveryLang;

    String tempAddNoteLang;
    String tempCloseNoteLang;

    boolean isNewDelivery;

    public void initialize(){

        isNewDelivery = false;

        tempAddNoteLang = "Add Note";
        tempCloseNoteLang = "Close Add Note";
        tempNewDeliveryLang = "New Delivery Form";
        tempEditDeliveryLang = "Edit Delivery Form";

        addNoteText.setVisible(false);

        errMessLabel.setText("");
        allInputs = new TextField[]{
                mrnText,
                firstnameText,
                lastnameText,
                medicationText,
                locationText,
                doseAmountText,
                doseText,
        };

        mrnText.textProperty().addListener((observableValue, s, t1) -> {
            if(!t1.isEmpty()){
                defaultBorder(mrnText);
            }
        });

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
                String newStringHistoryMess = null;
                DeliveryRequisition  retrieveOrder = retrieveOrderFromQueueSaves();
                ArrayList<OrderHistory> tempOrderHIstory = new ArrayList<>();
                if(ToggleTracking.getIsEdit()){
                   newOrderNum = ToggleTracking.getSelectedCardOrderNum();
                   if(retrieveOrder.getOrderStatusHistory()!=null){
                       tempOrderHIstory = retrieveOrder.getOrderStatusHistory();
                   }
                    newStringHistoryMess = createOrderHistoryMessage("edit");
                }else{
                    newStringHistoryMess = createOrderHistoryMessage("new");
                }
                String hasAddNote = null;
                if(!addNoteText.getText().isEmpty()){
                    hasAddNote = addNoteText.getText();
                }

                OrderHistory tempHistory = new OrderHistory(newStringHistoryMess, hasAddNote);
                tempOrderHIstory.add(tempHistory);
                DeliveryRequisition newOrder = new DeliveryRequisition(
                        newOrderNum,
                        DeliveryRequisition.currentDateTime(),
                        mrnText.getText(),
                        fullName,
                        locationText.getText(),
                        medicationText.getText(),
                        doseText.getText(),
                        doseAmountText.getText(),
                        "",
                        "",
                        tempOrderHIstory
                );

                if(ToggleTracking.getIsEdit() && ToggleTracking.getSelectedCardOrderNum() != null){
                    clearText();
                    closeDeliveryForm();
                    ToggleTracking.setIsEdit(false);

                    NotifyMessg.createMessg("edited", ToggleTracking.getSelectedCardOrderNum());


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



                }
                else {
                    clearText();
                    NotifyMessg.createMessg("newDelivery", newOrderNum);
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

    public String  createOrderHistoryMessage(String statusType){

        String tempEmplyeeID = "EID: ";
        if(Employee.getCurrentLogin() != null){
            tempEmplyeeID += Employee.getCurrentLogin();
        }else{
            tempEmplyeeID += "NO EMPLOYEE FOUND!";
        }
        switch (statusType){
            case "new":
                return "["+DeliveryRequisition.currentDateTime()+"]: " + tempEmplyeeID + " created this order.";
            case "edit":
                return "["+DeliveryRequisition.currentDateTime()+"]: " + tempEmplyeeID + " edited this order.";
            case "delivery":
                return "["+DeliveryRequisition.currentDateTime()+"]: " + tempEmplyeeID + " delivered this order.";
            case "return":
                return "["+DeliveryRequisition.currentDateTime()+"]: " + tempEmplyeeID + " return this order to queue.";
        }

        return null;
    }

    public void closeDeliveryForm(){
        deliveryFormPane.setPrefWidth(0);
        deliveryFormPane.setVisible(false);
    }
    public void toggleAddNote(){
        if(ToggleTracking.getIsAddNote()){
            addNoteBtn.setText(tempCloseNoteLang);
            addNoteText.setVisible(true);
        }else{
            addNoteBtn.setText(tempAddNoteLang);
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
        deliveryFormLabel.setText(tempNewDeliveryLang);
        isNewDelivery = true;
        clearText();
    }
    public void openEditDelivery(){
        System.out.println("Edit Delivery is Open");
        isNewDelivery = false;
        deliveryFormPane.setPrefWidth(320);
        deliveryFormPane.setVisible(true);
        deliveryFormLabel.setText(tempEditDeliveryLang);
        clearText();

        DeliveryRequisition findOrder = retrieveOrderFromQueueSaves();

         if(findOrder != null){
             setTextFieldFromLabel(findOrder);
         }

    }

    public DeliveryRequisition retrieveOrderFromQueueSaves(){
        String retrieveOrderNum = ToggleTracking.getSelectedCardOrderNum();
        Queue<DeliveryRequisition> tempQueue = null;
        if(ToggleTracking.getCurrentTab().equals("Pending")){
            tempQueue = QueueSaves.getPendingLatest();
        }

        if(ToggleTracking.getCurrentTab().equals("Completed")){
            tempQueue = QueueSaves.getCompletedLatest();
        }
        System.out.println("Quick check: "+ retrieveOrderNum);

        DeliveryRequisition findOrder = null;
        if(tempQueue != null){
            for(DeliveryRequisition childOrder: tempQueue){
                if(childOrder.getOrderNumberDisplay().equals(retrieveOrderNum)){
                    return childOrder;

                }
            }
        }

        return null;
    }

    public void setTextFieldFromLabel(DeliveryRequisition currentOrder) {
        mrnText.setText(currentOrder.getPatientMrn() != null ? currentOrder.getPatientMrn() : "");
        if (currentOrder.getPatientName() != null && !currentOrder.getPatientName().isEmpty()) {
            String[] fullName = currentOrder.getPatientName().split("\\s+", 2); // Split into at most two parts
            firstnameText.setText(fullName.length > 0 ? fullName[0] : "");
            lastnameText.setText(fullName.length > 1 ? fullName[1] : "");
        } else {
            firstnameText.setText("");
            lastnameText.setText("");
        }
        locationText.setText(currentOrder.getPatientLocation() != null ? currentOrder.getPatientLocation() : "");
        medicationText.setText(currentOrder.getMedication() != null ? currentOrder.getMedication() : "");
        doseText.setText(currentOrder.getDose() != null ? currentOrder.getDose() : "");
        doseAmountText.setText(currentOrder.getNumDoses() != null ? currentOrder.getNumDoses() : "");
    }

  public void updateLanguageLabel(String[] langTextChange){
        tempNewDeliveryLang = langTextChange[24];
        tempEditDeliveryLang = langTextChange[43];

        if(isNewDelivery){
            deliveryFormLabel.setText(tempNewDeliveryLang);
        }else{
            deliveryFormLabel.setText(tempEditDeliveryLang);
        }

          tempCloseNoteLang = langTextChange[46];
          tempAddNoteLang = langTextChange[29];

          if(ToggleTracking.getIsAddNote()){
              addNoteBtn.setText(tempCloseNoteLang);
          }else{
              addNoteBtn.setText(tempAddNoteLang);
          }

        formDescriplbl.setText(langTextChange[25]);
        patientInfoLbl.setText(langTextChange[26]);

        nameLbl.setText(langTextChange[27]);
        firstnameText.setPromptText(langTextChange[44]);
        lastnameText.setPromptText(langTextChange[45]);
        locationlbl.setText(langTextChange[16]);
        medInfoLbl.setText(langTextChange[40]);
        medDescriplbl.setText(langTextChange[17] + " (" + langTextChange[27] + "/" + langTextChange[28] + ")");
        doselbl.setText(langTextChange[18]);
        doseQuantlbl.setText(langTextChange[19]);
        clearBtn.setText(langTextChange[30]);
        submitBtn.setText(langTextChange[31]);
    }


}
