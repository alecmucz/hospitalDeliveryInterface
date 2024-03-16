package com.example.hospitaldeliveryinterface;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DeliveryRequisition {
    private String patientName;
    private String patientLocation; // floor and room number
    private String medication; // name and strength
    private String dose;
    private int numDoses;
    private String dateTime;

    private  String orderNumberDisplay;
    private static int orderNumCount = 1;

    public DeliveryRequisition(String patientName, String patientLocation, String medication, String dose, int numDoses) {
        this.patientName = patientName;
        this.patientLocation = patientLocation;
        this.medication = medication;
        this.dose = dose;
        this.numDoses = numDoses;
        this.dateTime = currentDateTime();
        this.orderNumberDisplay = generateOrderNum();
    }
    public DeliveryRequisition(String orderNumber, String patientName, String patientLocation, String medication, String dose, int numDoses, String dateTime) {
        this.patientName = patientName;
        this.patientLocation = patientLocation;
        this.medication = medication;
        this.dose = dose;
        this.numDoses = numDoses;
        this.dateTime = dateTime;
        this.orderNumberDisplay = orderNumber;

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
    public void setNumDoses(int numDoses) {
        this.numDoses = numDoses;
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
    public int getNumDoses() {
        return numDoses;
    }

    public String getOrderNumberDisplay(){
        return orderNumberDisplay;
    }

    public String getDateTime(){
        return dateTime;
    }

    public String generateOrderNum(){
        System.out.println("Current OrderNumCount: "+ orderNumCount);
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateString = currentTime.format(formatter);
        String counterString = String.format("%03d", orderNumCount);
        orderNumCount++;
        String orderNumber = dateString + counterString;
        return orderNumber;
    }

    public String currentDateTime(){

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
}
