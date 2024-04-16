package com.example.hospitaldeliveryinterface.controllers;

import
        com.example.hospitaldeliveryinterface.model.DeliveryRequisition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class OrderCardUIController {

    @FXML
    private Label dateDisplay;

    @FXML
    private Label deliveredByDisplay;

    @FXML
    private Label doseDisplay;

    @FXML
    private Label doseQuantityDisplay;

    @FXML
    private Label locationDisplay;

    @FXML
    private Label medicationDisplay;

    @FXML
    private TextArea notesDisplay;

    @FXML
    private Label orderNumDisplay;

    @FXML
    private TextArea orderStatusDisplay;

    @FXML
    private HBox orderTemplate;

    @FXML
    private Label patientNameDisplay;

    public void initialize(){
        orderTemplate.getStylesheets().clear();
    }


    public void updateOrderLabels(DeliveryRequisition order){
        orderNumDisplay.setText("#"+order.getOrderNumberDisplay());
        patientNameDisplay.setText(order.getPatientName());
        locationDisplay.setText(order.getPatientLocation());
        medicationDisplay.setText(order.getMedication());
        doseDisplay.setText(order.getDose());
        doseQuantityDisplay.setText(order.getNumDoses());
        dateDisplay.setText(order.getDateTime());
        deliveredByDisplay.setText(order.getDeliveryInfo());
        notesDisplay.setText(order.getNotes());
    }
}
