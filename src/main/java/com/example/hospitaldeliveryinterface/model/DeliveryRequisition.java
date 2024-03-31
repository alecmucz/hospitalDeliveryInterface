package com.example.hospitaldeliveryinterface.model;

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

public class DeliveryRequisition {
    private String patientName;
    private String patientLocation; // floor and room number
    private String medication; // name and strength
    private String dose;
    private String numDoses;
    private String dateTime;
    private String notes;
    private String deliveredBy; // tracks which staff member signed off on the package
    private String createdBy; // tracks which staff created the delivery/order
    private String updatedBy;

    private  String orderNumberDisplay;
    private static int orderNumCount;

    public DeliveryRequisition(String orderNumber, String dateTime, String patientName, String patientLocation, String medication, String dose, String numDoses, String notes, String deliveredBy, String createdBy, String updatedBy) {
        this.patientName = patientName;
        this.patientLocation = patientLocation;
        this.medication = medication;
        this.dose = dose;
        this.numDoses = numDoses;
        this.dateTime = dateTime;
        this.orderNumberDisplay = orderNumber;
        this.notes = notes;
        this.deliveredBy = deliveredBy;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
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

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public static void setOrderNumCount(int DBcount) {
        orderNumCount = DBcount;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
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
    public String getNotes() {
        return notes;
    }

    public String getDeliveredBy() {
        return deliveredBy;
    }

    public void setDeliveredBy(String deliveredBy) {
        this.deliveredBy = deliveredBy;
    }

    public String getCreatedBy() {
        return createdBy;
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

    public static Map<String, Object> orderToMap(DeliveryRequisition order) {
        Map<String, Object> data = new HashMap<>();
        data.put("patientName", order.getPatientName());
        data.put("patientLocation", order.getPatientLocation());
        data.put("medication", order.getMedication());
        data.put("dose", order.getDose());
        data.put("numDoses", order.getNumDoses());
        data.put("dateTime", order.getDateTime());
        data.put("notes", order.getNotes());
        data.put("deliveredBy", order.getDeliveredBy());
        data.put("createdBy", order.getCreatedBy());
        data.put("updatedBy", order.getUpdatedBy());
        return data;
    }

}
