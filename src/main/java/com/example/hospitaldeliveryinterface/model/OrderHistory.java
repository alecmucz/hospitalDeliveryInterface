package com.example.hospitaldeliveryinterface.model;

public class OrderHistory {

    private String statusMessage;
    private String notes;

    public OrderHistory(){
    }
    public OrderHistory(String statusMessage, String notes){
        this.statusMessage = statusMessage;
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
