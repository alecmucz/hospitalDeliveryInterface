package com.example.hospitaldeliveryinterface.firebase;

import com.example.hospitaldeliveryinterface.PharmaTracApp;
import com.example.hospitaldeliveryinterface.controllers.HomepageController;
import com.example.hospitaldeliveryinterface.firebase.DataBaseMgmt;
import com.example.hospitaldeliveryinterface.model.NotifyMessg;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import javax.annotation.Nullable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                    onDataDisplay("pendingDeliveries");

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
                    onDataDisplay("completedDeliveries");

            }
        });
    }

    public static void listenToNotifyHistory() {
        System.out.println("Notfication listener has been called");
        PharmaTracApp.fstore.collection("notifyHistory").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                }

                if (queryDocumentSnapshots != null && initializeNotify) {
                    System.out.println("Notfication listener is ACTIVE");
                    System.out.println("Notify History Collection has been UPDATED");
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            DocumentSnapshot documentSnapshot = documentChange.getDocument();
                            // Process the newly added document
                            System.out.println("New MESSAGE HAS BEEN RETRIEVED: " + documentSnapshot.getId());
                            processAddedDocument(documentSnapshot);
                        }
                    }

                }

                initializeNotify = true;
            }
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
    public static void onDataDisplay(String collectionName) {
        try {
            controller.displayQueue(DataBaseMgmt.buildQueue(collectionName), collectionName);
        } catch (Exception e) {
            System.err.println("Error displaying data from collection " + collectionName + ": " + e.getMessage());
        }
    }

}
