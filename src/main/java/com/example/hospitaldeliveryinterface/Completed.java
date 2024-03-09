package com.example.hospitaldeliveryinterface;

import java.util.LinkedList;
import java.util.Queue;

public class Completed {

    private Queue<DeliveryRequisition> completedQueue = new LinkedList<>();
    private static Completed instance = new Completed();

    public static Completed getInstance() {
        return instance;
    }

    public void addOrders(DeliveryRequisition order){
        System.out.println("ORDER HAS BEEN ADDED TO PENDING QUEUE");
        completedQueue.add(order);
        printCompletedueue();
    }

    public DeliveryRequisition getPendingSingleData(){
        return completedQueue.poll();
    }

    public Queue<DeliveryRequisition> getCompletedQueue(){
        return completedQueue;
    }

    public void printCompletedueue(){
        if(completedQueue.isEmpty()){
            System.out.println("PENDING QUEUE IS EMPTY");
        }else{
            System.out.println("SIZE OF PENDINGQUEUE: " + completedQueue.size());
            for (DeliveryRequisition order: completedQueue){
                System.out.println(order);
            }
        }
    }
}
