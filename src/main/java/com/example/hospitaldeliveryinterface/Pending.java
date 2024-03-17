package com.example.hospitaldeliveryinterface;

import java.security.cert.PolicyNode;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Pending {

    private Queue<DeliveryRequisition> pendingQueue = new LinkedList<>();
    private static Pending instance = new Pending();

    public static Pending getInstance() {
        return instance;
    }

    public void addOrders(DeliveryRequisition order){
        System.out.println("ORDER HAS BEEN ADDED TO PENDING QUEUE");
        pendingQueue.add(order);
        printPendingQueue();
    }

    public DeliveryRequisition getPendingSingleData(){
        return pendingQueue.poll();
    }

    public Queue<DeliveryRequisition> getPendingQueue(){
        return pendingQueue;
    }

    public DeliveryRequisition getPendingOrderByOrderNumber(String orderNumber) {
        for (DeliveryRequisition order : pendingQueue) {
            if (order.getOrderNumberDisplay().equals(orderNumber)) {
                return order;
            }
        }
        return null;
    }

    public void getRemoveOrderByOrderNumber(String orderNumber){
        Iterator<DeliveryRequisition> iterator = pendingQueue.iterator();
        while (iterator.hasNext()) {
            DeliveryRequisition order = iterator.next();
            if (order.getOrderNumberDisplay().equals(orderNumber)) {
                iterator.remove();
                System.out.println("Order with order number " + orderNumber + " removed from pending queue.");
                return;
            }
        }
        System.out.println("Order with order number " + orderNumber + " not found in pending queue.");
    }



    public void printPendingQueue(){
        if(pendingQueue.isEmpty()){
            System.out.println("PENDING QUEUE IS EMPTY");
        }else{
            System.out.println("SIZE OF PENDINGQUEUE: " + pendingQueue.size());
            for (DeliveryRequisition order: pendingQueue){
                System.out.println(order);
            }
        }
    }

}
