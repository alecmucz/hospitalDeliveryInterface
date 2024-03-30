package com.example.hospitaldeliveryinterface.firebase;

import com.example.hospitaldeliveryinterface.PharmaTracApp;
import com.example.hospitaldeliveryinterface.controllers.HomepageController;
import com.example.hospitaldeliveryinterface.firebase.DataBaseMgmt;
import com.example.hospitaldeliveryinterface.model.DeliveryRequisition;
import com.example.hospitaldeliveryinterface.model.NotifyMessg;
import com.example.hospitaldeliveryinterface.model.QueueSaves;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import javax.annotation.Nullable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class FirebaseListener {

    private static HomepageController controller;

    private static boolean initializeNotify = false;

    public static void setController(HomepageController control) {
        controller = control;
    }


    public static void listenToPendingDeliveries() {
        //this listen to changes in the pendingDeliveries Colllection
        PharmaTracApp.fstore.collection("pendingDeliveries").addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                }

                System.out.println("PendingDeliveries Collection has been UPDATED");
                onDataDisplay(queryDocumentSnapshots, "pendingDeliveries");

            }

        });
    }

    public static void listenToCompletedDeliveries() {
        // Listen to changes in another collection (example: completedDeliveries)
        PharmaTracApp.fstore.collection("completedDeliveries").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                }

                    System.out.println("CompletedDeliveries Collection has been UPDATED");
                    onDataDisplay(queryDocumentSnapshots, "completedDeliveries");

            }
        });
    }

    public static void listenToNotifyHistory() {
        System.out.println("Notfication listener has been called");
        PharmaTracApp.fstore.collection("notifyHistory").document("notifyMessage").addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
            }

            if (documentSnapshot != null && documentSnapshot.exists() && initializeNotify) {
                System.out.println("Notfication listener is ACTIVE");
                System.out.println("Notify History Collection has been UPDATED");

                System.out.println("New MESSAGE HAS BEEN RETRIEVED: " + documentSnapshot.getId());
                processAddedDocument(documentSnapshot);
            }

            initializeNotify = true;

        });

    }


    public static void processAddedDocument(DocumentSnapshot document){
        try{
            NotifyMessg createMessg = new NotifyMessg(
                    document.getString("date"),
                    document.getString("time"),
                    document.getString("message")
            );

            NotifyMessg.addMessg(createMessg);
            System.out.println("New document added: " + document.getId());

            controller.displayNotfications();

        }catch (Exception e){
            System.err.println("Error finding notification message for document ID " + document.getId() + ": " + e.getMessage());

        }
    }
    public static void onDataDisplay(QuerySnapshot querySnaps, String collectionName) {
        try {
            controller.displayQueue(DataBaseMgmt.buildQueue(querySnaps, collectionName), collectionName);
        } catch (Exception e) {
            System.err.println("Error displaying data from collection " + collectionName + ": " + e.getMessage());
        }
    }

    public static void navBarDataDisplay(String tabType){

        Queue<DeliveryRequisition> tempQueue = new LinkedList<>();
        String collectionName = "";

        if(tabType.equals("Completed")){
            tempQueue = QueueSaves.getCompletedLatest();
            collectionName = "completedDeliveries";
        }

        if(tabType.equals("Pending")){
            tempQueue = QueueSaves.getPendingLatest();
            collectionName = "pendingDeliveries";
        }

        controller.displayQueue(tempQueue, collectionName);
    }

}
