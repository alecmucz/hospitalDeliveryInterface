package com.example.hospitaldeliveryinterface.controllers;

import com.example.hospitaldeliveryinterface.firebase.DataBaseMgmt;
import com.example.hospitaldeliveryinterface.model.DeliveryRequisition;
import com.example.hospitaldeliveryinterface.model.QueueSaves;

import java.util.HashMap;

public class MultiSelectController {
    private HashMap<String, DeliveryRequisition> selectedOrders;

    public MultiSelectController() {
        this.selectedOrders = new HashMap<>();
    }

    public boolean isSelected(String orderNumber) {
        return selectedOrders.containsKey(orderNumber);
    }

    public void selectOrder(DeliveryRequisition order) {
        System.out.println("Selecting order: " + order.getOrderNumberDisplay());  // Debugging output
        if (order != null && !isSelected(order.getOrderNumberDisplay())) {
            selectedOrders.put(order.getOrderNumberDisplay(), order);
        }
    }

    public HashMap<String, DeliveryRequisition> getSelectedOrders() {
        return new HashMap<>(this.selectedOrders);
    }

    public void deselectOrder(String orderNumber) {
        selectedOrders.remove(orderNumber);
    }

    public void clearSelections() {
        this.selectedOrders.clear();
    }


    public void processSelectedOrders(boolean moveToCompleted) {
        String targetCollectionName = moveToCompleted ? "completedDeliveries" : "pendingDeliveries";
        for (DeliveryRequisition order : selectedOrders.values()) {
            if (moveToCompleted) {
                QueueSaves.getPendingLatest().remove(order);
                QueueSaves.getCompletedLatest().add(order);
            } else {
                QueueSaves.getCompletedLatest().remove(order);
                QueueSaves.getPendingLatest().add(order);
            }

            try {
                DataBaseMgmt.addToDB(order, targetCollectionName);
            } catch (Exception e) {
                System.err.println(STR."Error updating order in database: \{e.getMessage()}");
            }
        }
        selectedOrders.clear();
    }
}