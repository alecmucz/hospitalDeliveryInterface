package com.example.hospitaldeliveryinterface.model;

public class ToggleTracking {

    private static boolean isEdit;
    private static boolean isNewDelivery;

    private static boolean isAddNote;
    private static String currentTab;

    private static  boolean isCreateUser;


    private static String selectedCardOrderNum;

    public static boolean getIsCreateUser(){
        return isCreateUser;
    }
    public static String getSelectedCardOrderNum(){
        return selectedCardOrderNum;
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

    public static String getCurrentTab() {
        return currentTab;
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


}
