package com.example.hospitaldeliveryinterface;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.time.LocalDateTime;

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
    private GridPane orderTemplate;

    public void updateOrderLabels(DeliveryRequisition order){
        orderNumDisplay.setText("#"+order.getOrderNumberDisplay());
        patientNameDisplay.setText(order.getPatientName());
        locationDisplay.setText(order.getPatientLocation());
        medicationDisplay.setText(order.getMedication());
        doseDisplay.setText(order.getDose());
        doseQuantityDisplay.setText(Integer.toString(order.getNumDoses()));
        dateDisplay.setText(order.getDateTime());
    }


}
