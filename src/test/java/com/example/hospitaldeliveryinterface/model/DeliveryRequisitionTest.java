package com.example.hospitaldeliveryinterface.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryRequisitionTest {
    private DeliveryRequisition deliveryRequisition;
    @BeforeEach
    void setUp() {
        deliveryRequisition = new DeliveryRequisition();
    }

    @Test
    void getOrderStatusHistoryNullTest() {
        assertEquals(deliveryRequisition.getOrderStatusHistory(), null);
    }
    @Test
    void getOrderStatusHistoryNotNullTest() {
        OrderHistory orderHistory = new OrderHistory("statusMessage", "notes");
        ArrayList<OrderHistory> orderHistories = new ArrayList<>();
        orderHistories.add(orderHistory);

        deliveryRequisition.setOrderStatusHistory(orderHistories);
        assertEquals(deliveryRequisition.getOrderStatusHistory(), orderHistories);
    }

    @Test
    void getStatusNullTest() {
        assertEquals(deliveryRequisition.getStatus(), null);
    }
    @Test
    void getStatusNotNullTest() {
        deliveryRequisition.setStatus("status");
        assertEquals(deliveryRequisition.getStatus(), "status");
    }
    @Test
    void currentDateTimeTest() {
        LocalDateTime timeNow = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM dd, yyyy - hh:mm a");

        assertEquals(deliveryRequisition.currentDateTime(), timeNow.format(format));
    }
}