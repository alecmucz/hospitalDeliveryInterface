package com.example.hospitaldeliveryinterface.model;

import java.util.LinkedList;
import java.util.Queue;

public class QueueSaves {

    private static Queue<DeliveryRequisition> pendingLatest = new LinkedList<>();
    private static Queue<DeliveryRequisition> completedLatest = new LinkedList<>();

    public static void setPendingLatest(Queue<DeliveryRequisition> latestPending){
        pendingLatest = latestPending;
    }

    public static void setCompletedLatest(Queue<DeliveryRequisition> lastestCompleted){
        completedLatest = lastestCompleted;
    }

    public static Queue<DeliveryRequisition> getPendingLatest(){
        return pendingLatest;
    }

    public static Queue<DeliveryRequisition> getCompletedLatest(){
        return completedLatest;
    }
}
