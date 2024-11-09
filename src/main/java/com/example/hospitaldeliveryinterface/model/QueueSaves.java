package com.example.hospitaldeliveryinterface.model;

import java.util.LinkedList;
import java.util.Queue;

public class QueueSaves {

    private static Queue<DeliveryRequisition> pendingLatest = new LinkedList<>();
    private static Queue<DeliveryRequisition> completedLatest = new LinkedList<>();

    public static void setPendingLatest(Queue<DeliveryRequisition> latestPending){
        pendingLatest = new LinkedList<>(latestPending);
    }

    public static void setCompletedLatest(Queue<DeliveryRequisition> lastestCompleted){
        completedLatest = new LinkedList<>(lastestCompleted);
    }

    public static Queue<DeliveryRequisition> getPendingLatest(){
        return new LinkedList<>(pendingLatest);
    }

    public static Queue<DeliveryRequisition> getCompletedLatest(){
        return new LinkedList<>(completedLatest);
    }
}
