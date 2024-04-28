package com.example.hospitaldeliveryinterface.model;
import javafx.scene.layout.HBox;

import java.util.HashSet;
import java.util.Set;

public class ToggleTracking {

    private static Set<DeliveryRequisition> selectedOrders = new HashSet<>();

    public static void selectNode(HBox node) {
        DeliveryRequisition req = (DeliveryRequisition) node.getUserData();
        node.setStyle("-fx-background-color: #98FF98; -fx-border-color: #22aae1; -fx-border-width: 2;");
        selectedOrders.add(req);
        selectedCardOrderNum = req.getOrderNumberDisplay(); // Ensure this is the right method to get the display number
        System.out.println("Selected order: " + selectedCardOrderNum);
    }

    public static void deselectNode(HBox node) {
        DeliveryRequisition req = (DeliveryRequisition) node.getUserData();
        node.setStyle("-fx-background-color: transparent; -fx-border-color: #22aae1; -fx-border-width: 2;");
        selectedOrders.remove(req);
        if (selectedCardOrderNum != null && selectedCardOrderNum.equals(req.getOrderNumberDisplay())) {
            selectedCardOrderNum = null; // Reset selected card order number if it's the deselected one
        }
        System.out.println("Deselected order: " + req.getOrderNumberDisplay());
    }

    public static void clearOrders(){
        selectedOrders.clear();
    }
    public static Set<DeliveryRequisition> getSelectedOrders() {
        return selectedOrders;
    }

    private static boolean isEdit;
    private static boolean isNewDelivery;
    private static boolean isAddNote;
    private static  boolean isCreateUser;

    public static boolean getIsCreateUser(){
        return isCreateUser;
    }
    public  static boolean getIsAddNote(){
        return  isAddNote;
    }
    public static boolean getIsEdit() {
        return isEdit;
    }
    public static boolean getIsNewDelivery() {
        return isNewDelivery;
    }

    private static String currentTab;
    private static String languageTrack;
    private static String selectedCardOrderNum;

    public static String getSelectedCardOrderNum(){
        return selectedCardOrderNum;
    }
    public static String getCurrentTab() {
        return currentTab;
    }
    public static String getLanguageTrack() {
        return languageTrack;
    }

    public static void setIsEdit(boolean currentState) {
        isEdit = currentState;
    }
    public static void setIsNewDelivery(boolean currentState){
        isNewDelivery = currentState;
    }
    public static void setCurrentTab(String currentState){
        currentTab = currentState;
    }
    public static void setIsAddNote(boolean currentState){
        isAddNote = currentState;
    }
    public static void setisCreateUser(boolean currentState){
        isCreateUser = currentState;
    }
    public static void setSelectedCardOrderNum(String currentOrderNum){
        selectedCardOrderNum = currentOrderNum;
    }
    public static void setLanguageTrack(String languageTrack) {
        ToggleTracking.languageTrack = languageTrack;
    }
}
