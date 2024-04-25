package com.example.hospitaldeliveryinterface.controllers;

import
        com.example.hospitaldeliveryinterface.model.DeliveryRequisition;
import com.example.hospitaldeliveryinterface.model.MitchTextTranslate;
import com.example.hospitaldeliveryinterface.model.OrderHistory;
import com.example.hospitaldeliveryinterface.model.ToggleTracking;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;

public class OrderCardUIController {


    @FXML
    private Label doseDisplay;

    @FXML
    private Label doseLbl;

    @FXML
    private Label doseQuantityDisplay;

    @FXML
    private Label locationDisplay;

    @FXML
    private Label locationLbl;

    @FXML
    private Label medicationDisplay;

    @FXML
    private Label medicationLbl;

    @FXML
    private Label mrnDisplay;

    @FXML
    private Label mrnlbl;

    @FXML
    private Label numDoseLbl;

    @FXML
    private Label orderHistoryLbl;

    @FXML
    private Label orderNumDisplay;

    @FXML
    private Label orderNumLbl;

    @FXML
    private Pane orderSplit;

    @FXML
    private TextArea orderStatusDisplay;

    @FXML
    private HBox orderTemplate;

    @FXML
    private Label patientNameDisplay;

    @FXML
    private Label patientNameLbl;

    @FXML
    private Button viewOrderHistoryMore;

    private boolean toggleOpenMoreOrderHistory;
    private  static final double textAreaDefaultHeight = 104.7;
    private static final double orderCardDefaultHeight = 174;
    private static final int defaultVisibleRows = 6;
    public void initialize(){
        orderTemplate.getStylesheets().clear();
        toggleOpenMoreOrderHistory = false;

    }

    @FXML
    void onMoreOrderHistory(ActionEvent event) {

        String translateOpen = "[ view more ]";
        String translateClose = "[ close view more ]";
        HashMap<String,String[]> checkStoredLang = MitchTextTranslate.getStoredLang();
        if(checkStoredLang.containsKey(ToggleTracking.getLanguageTrack())) {
            String[] retrieveTranslatedText = checkStoredLang.get(ToggleTracking.getLanguageTrack());

            if(retrieveTranslatedText != null){
                translateOpen = "[ " + retrieveTranslatedText[21] + " ]";
                translateClose = "[ " + retrieveTranslatedText[22] + " ]";
            }

        }

        if(toggleOpenMoreOrderHistory){
            viewOrderHistoryMore.setText(translateOpen);



            orderTemplate.setMinHeight(orderCardDefaultHeight);
            orderTemplate.setMaxHeight(orderCardDefaultHeight);

            orderStatusDisplay.setMaxHeight(TextArea.USE_COMPUTED_SIZE);
            orderStatusDisplay.setMinHeight(TextArea.USE_COMPUTED_SIZE);

        }else{
            viewOrderHistoryMore.setText(translateClose);


            if(!orderStatusDisplay.getText().isEmpty()){
                adjustTextArea();
            }

        }

        toggleOpenMoreOrderHistory = !toggleOpenMoreOrderHistory;
    }

    public void adjustTextArea(){

            int numRows = calculateNumRows(orderStatusDisplay);
        if(numRows > defaultVisibleRows){
            System.out.println("Number of Rows in Order History: " + numRows);
            double oneRowTAHeight = (textAreaDefaultHeight/defaultVisibleRows);
            double newTA = (oneRowTAHeight * numRows);
            double newTemplateHeight = (newTA * orderCardDefaultHeight) / textAreaDefaultHeight;

            System.out.println("newTemplateHeight: " + newTemplateHeight);

            orderStatusDisplay.setMaxHeight(newTA);
            orderStatusDisplay.setMinHeight(newTA);

            orderTemplate.setMinHeight(TextArea.USE_COMPUTED_SIZE);
            orderTemplate.setMaxHeight(TextArea.USE_COMPUTED_SIZE);
        }else{

            orderTemplate.setMinHeight(orderCardDefaultHeight);
            orderTemplate.setMaxHeight(orderCardDefaultHeight);

            orderStatusDisplay.setMaxHeight(textAreaDefaultHeight);
            orderStatusDisplay.setMinHeight(textAreaDefaultHeight);
        }

    }
    public void updateOrderLabels(DeliveryRequisition order){

        HashMap<String,String[]> checkStoredLang = MitchTextTranslate.getStoredLang();
        if(checkStoredLang.containsKey(ToggleTracking.getLanguageTrack())){
            String[] retrieveTranslatedText = checkStoredLang.get(ToggleTracking.getLanguageTrack());

            orderNumLbl.setText(retrieveTranslatedText[14]);
            patientNameLbl.setText(retrieveTranslatedText[15]);
            locationLbl.setText(retrieveTranslatedText[16]);
            medicationLbl.setText(retrieveTranslatedText[17]);
            doseLbl.setText(retrieveTranslatedText[18]);
            numDoseLbl.setText(retrieveTranslatedText[19]);
            orderHistoryLbl.setText(retrieveTranslatedText[20]);
            viewOrderHistoryMore.setText("[ " + retrieveTranslatedText[21] + " ]");
        }

        orderNumDisplay.setText("#"+order.getOrderNumberDisplay());
        patientNameDisplay.setText(order.getPatientName());
        locationDisplay.setText(order.getPatientLocation());
        medicationDisplay.setText(order.getMedication());
        doseDisplay.setText(order.getDose());
        doseQuantityDisplay.setText(order.getNumDoses());
        mrnDisplay.setText(order.getPatientMrn());
        populateOrderHistory(order);


        int orderHistRows = calculateNumRows(orderStatusDisplay);

       if(orderHistRows > 5){
           viewOrderHistoryMore.setVisible(true);
       }else{
           viewOrderHistoryMore.setVisible(false);
       }

    }
    private void populateOrderHistory(DeliveryRequisition currentOrder){
        if(currentOrder.getOrderStatusHistory() != null){
            List<OrderHistory> history = currentOrder.getOrderStatusHistory();

            for (int i = history.size() - 1; i >= 0; i--) {
                OrderHistory childMess = history.get(i);
                orderStatusDisplay.setText(orderStatusDisplay.getText() + "- " + childMess.getStatusMessage() + "\n");
                if(childMess.getNotes() != null){
                    orderStatusDisplay.setText(orderStatusDisplay.getText() + "**Notes** " + childMess.getNotes() + "\n");
                }

            }
        }

    }

    private int calculateNumRows(TextArea textArea) {

        String text = textArea.getText();

        int numRows = text.split("\n", -1).length;

        if (text.endsWith("\n")) {
            numRows--;
        }

        return numRows;
    }

}
