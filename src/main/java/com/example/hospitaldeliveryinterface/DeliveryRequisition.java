package com.example.hospitaldeliveryinterface;

import java.time.LocalDateTime;
import java.util.Date;

public class DeliveryRequisition {
    private String patientName;
    private String patientLocation; // floor and room number
    private String medication; // name and strength
    private String dose;
    private int numDoses;
    LocalDateTime dateTime;

    public DeliveryRequisition(String patientName, String patientLocation, String medication, String dose, int numDoses) {
        this.patientName = patientName;
        this.patientLocation = patientLocation;
        this.medication = medication;
        this.dose = dose;
        this.numDoses = numDoses;
        this.dateTime = java.time.LocalDateTime.now();
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
}
