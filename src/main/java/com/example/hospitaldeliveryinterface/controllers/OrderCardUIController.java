package com.example.hospitaldeliveryinterface.controllers;

import com.example.hospitaldeliveryinterface.model.DeliveryRequisition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class OrderCardUIController {

    @FXML
    private Label dateDisplay;

    @FXML
    private Label doseDisplay;

    @FXML
    private Label doseQuantityDisplay;

    @FXML
    private Label locationDisplay;

    @FXML
    private Label medicationDisplay;

    @FXML
    private Label orderNumDisplay;

    @FXML
    private Label patientNameDisplay;

    @FXML
    private Label notesDisplay;

    @FXML
    private GridPane orderTemplate;

    @FXML
    private Label deliveredByDisplay;

    @FXML
    private Label updatedByDisplay;

    @FXML
    private Label createdByDisplay;


    public void updateOrderLabels(DeliveryRequisition order){
        orderNumDisplay.setText("#"+order.getOrderNumberDisplay());
        patientNameDisplay.setText(order.getPatientName());
        locationDisplay.setText(order.getPatientLocation());
        medicationDisplay.setText(order.getMedication());
        doseDisplay.setText(order.getDose());
        doseQuantityDisplay.setText(order.getNumDoses());
        dateDisplay.setText(order.getDateTime());
        deliveredByDisplay.setText(order.getDeliveredBy());
        createdByDisplay.setText(order.getCreatedBy());
        updatedByDisplay.setText(order.getUpdatedBy());
    }
}
