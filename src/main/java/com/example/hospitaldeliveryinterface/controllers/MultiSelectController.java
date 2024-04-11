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
        if (order != null) {
            this.selectedOrders.put(order.getOrderNumberDisplay(), order);
        }
    }

    public void deselectOrder(String orderNumber) {
        this.selectedOrders.remove(orderNumber);
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