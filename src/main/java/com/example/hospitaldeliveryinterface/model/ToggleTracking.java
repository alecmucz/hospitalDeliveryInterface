package com.example.hospitaldeliveryinterface.model;
import com.example.hospitaldeliveryinterface.controllers.HomepageController;
import javafx.scene.layout.HBox;

import java.util.HashSet;
import java.util.Set;

public class ToggleTracking {



    private static Set<DeliveryRequisition> selectedOrders = new HashSet<>();
    private static HomepageController homepageController;
    public static Set<DeliveryRequisition> getSelectedOrders() {
        return selectedOrders;
    }


    public static void setHomepageController(HomepageController controller) {
        homepageController = controller;
    }

    public static void selectNode(HBox node) {
        DeliveryRequisition req = (DeliveryRequisition) node.getUserData();
        node.setStyle("-fx-background-color: #98FF98; -fx-border-color: #22aae1; -fx-border-width: 2;");
        selectedOrders.add(req);
        setSelectedCardOrderNum(req.getOrderNumberDisplay());
        setIsEdit(false);
        homepageController.toggleNewDelivery();
        System.out.println("Selected order: " + req.getOrderNumberDisplay());
    }

    public static void deselectNode(HBox node) {
        DeliveryRequisition req = (DeliveryRequisition) node.getUserData();
        node.setStyle("-fx-background-color: transparent; -fx-border-color: #22aae1; -fx-border-width: 2;");
        selectedOrders.remove(req);
        if (req.getOrderNumberDisplay().equals(selectedCardOrderNum)) {
            if (selectedOrders.isEmpty()) {
                setSelectedCardOrderNum(null);
            } else {
                setSelectedCardOrderNum(selectedOrders.iterator().next().getOrderNumberDisplay());
            }
        }
        System.out.println("Deselected order: " + req.getOrderNumberDisplay());
        if (selectedOrders.isEmpty()) {
            setIsEdit(false);
        }
        homepageController.toggleNewDelivery();
    }

    public static void clearOrders(){
        selectedOrders.clear();
        selectedCardOrderNum = null;
        homepageController.toggleNewDelivery();
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
