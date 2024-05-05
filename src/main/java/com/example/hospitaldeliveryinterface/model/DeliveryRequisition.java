package com.example.hospitaldeliveryinterface.model;

import com.algolia.search.com.fasterxml.jackson.annotation.JsonProperty;
import com.example.hospitaldeliveryinterface.PharmaTracApp;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.WriteResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ArrayList;

public class DeliveryRequisition {
    private String patientName;
    private String patientLocation; // floor and room number
    private String medication; // name and strength
    private String dose;
    private String numDoses;
    private String dateTime;

    private String deliveryInfo; // tracks which staff member signed off on the package
    private String orderCreationRecord; // tracks which staff created the delivery/order
    @JsonProperty("objectID")
    private  String orderNumberDisplay;
    private static int orderNumCount;
    private String status;

    private String patientMrn;

    private ArrayList<OrderHistory> orderStatusHistory;


    public DeliveryRequisition() {

    }

    public DeliveryRequisition(String orderNumber, String dateTime, String patientMRN, String patientName, String patientLocation, String medication, String dose, String numDoses, String deliveryInfo, String createdBy, ArrayList<OrderHistory> orderStatusHistory) {
        this.patientMrn = patientMRN;
        this.patientName = patientName;
        this.patientLocation = patientLocation;
        this.medication = medication;
        this.dose = dose;
        this.numDoses = numDoses;
        this.dateTime = dateTime;
        this.orderNumberDisplay = orderNumber;
        this.deliveryInfo = deliveryInfo;
        this.orderCreationRecord = createdBy;
        this.orderStatusHistory = orderStatusHistory;
    }

    public void setOrderStatusHistory(ArrayList<OrderHistory> orderStatusHistory) {
        this.orderStatusHistory = orderStatusHistory;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
    public void setPatientLocation(String patientLocation) {
        this.patientLocation = patientLocation;
    }
    public void setMedication(String medication) {
        this.medication = medication;
    }
    public void setDose(String dose) {
        this.dose= dose;
    }
    public void setNumDoses(String numDoses) {
        this.numDoses = numDoses;
    }

    public void setPatientMrn(String patientMrn) {
        this.patientMrn = patientMrn;
    }

    public static void setOrderNumCount(int DBcount) {
        orderNumCount = DBcount;
    }

    public void setOrderCreationRecord(String orderCreationRecord) {
        this.orderCreationRecord = orderCreationRecord;
    }

    public ArrayList<OrderHistory> getOrderStatusHistory() {
        return orderStatusHistory;
    }

    public String getPatientMrn() {
        return patientMrn;
    }

    public String getPatientName(){
        return patientName;
    }
    public String getPatientLocation() {
        return patientLocation;
    }
    public String getMedication() {
        return medication;
    }
    public String getDose() {
        return dose;
    }
    public String getNumDoses() {
        return numDoses;
    }

    public String getOrderNumberDisplay(){
        return orderNumberDisplay;
    }

    public String getDateTime(){
        return dateTime;
    }

    public String getStatus(){
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(String deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public void setOrderNumberDisplay(String orderNumberDisplay) {
        this.orderNumberDisplay = orderNumberDisplay;
    }

    public String getOrderCreationRecord() {
        return orderCreationRecord;
    }
    public static int getOrderNumCount() {
        return orderNumCount;
    }

    public static String generateOrderNum(){
        orderNumCount++;
        incrementNumOrders();
        System.out.println("Current OrderNumCount: "+ orderNumCount);
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateString = currentTime.format(formatter);
        String counterString = String.format("%03d", orderNumCount);

        return dateString + counterString;
    }

    public static String currentDateTime(){
        LocalDateTime timeNow = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM dd, yyyy - hh:mm a");
        return timeNow.format(format);
    }



    @Override
    public String toString() {
        return "DeliveryRequisition{" +
                "patientName='" + patientName + '\'' +
                ", patientLocation='" + patientLocation + '\'' +
                ", medication='" + medication + '\'' +
                ", dose='" + dose + '\'' +
                ", numDoses=" + numDoses +
                ", dateTime=" + dateTime +
                ", orderNumberDisplay='" + orderNumberDisplay + '\'' +
                '}';
    }

    /**
     * increases the tracker in DB for number of orders
     */
    public static void incrementNumOrders(){
        DocumentReference docRef = PharmaTracApp.fstore.collection("statistics").document("numOrders");
        final ApiFuture<WriteResult> updateFuture =
                docRef.update("totalNumOrders", FieldValue.increment(1));
    }


}
