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

    private DeliveryRequisition order;
    private MultiSelectController multiSelectController; // Reference to the MultiSelectController
    private boolean isSelected = false; // Initial selection state

    public void setOrder(DeliveryRequisition order) {
        this.order = order;
        updateOrderLabels(order);
    }

    public void setMultiSelectController(MultiSelectController multiSelectController) {
        this.multiSelectController = multiSelectController;
    }

    @FXML
    public void initialize() {
        orderTemplate.setOnMouseClicked(event -> toggleSelection());
    }

    private void toggleSelection() {
        isSelected = !isSelected;
        if (isSelected) {
            setSelectedStyle();
            multiSelectController.selectOrder(order);
        } else {
            setUnselectedStyle();
            multiSelectController.deselectOrder(order.getOrderNumberDisplay());
        }
    }

    private void setSelectedStyle() {
        orderTemplate.setStyle("-fx-border-color: #009688; -fx-background-color: #B2DFDB;");
    }

    private void setUnselectedStyle() {
        orderTemplate.setStyle("-fx-background-color: transparent; -fx-border-color: #22aae1;");
    }
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
        notesDisplay.setText(order.getNotes());
    }
}
