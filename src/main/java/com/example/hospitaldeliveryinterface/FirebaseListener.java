package com.example.hospitaldeliveryinterface;

import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import javax.annotation.Nullable;
import java.util.Queue;

public class FirebaseListener {

    private Firestore firestore;
    private HomepageController controller;
    private String currentpage;

    public FirebaseListener(HomepageController homeController, String currPage){
        this.controller = homeController;
        this.firestore = FirestoreClient.getFirestore();
        this.currentpage = currPage;

        //this listen to changes in the pendingDeliveries Colllection
        firestore.collection("pendingDeliveries").addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                }

                if (currentpage.equals("Pending")) {
                    System.out.println("PendingDeliveries Collection has been UPDATED");
                    onDataDisplay("pendingDeliveries");
                }
            }

        });

        // Listen to changes in another collection (example: completedDeliveries)
        firestore.collection("completedDeliveries").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                }

                if (currentpage.equals("Completed")) {
                    System.out.println("CompletedDeliveries Collection has been UPDATED");
                    onDataDisplay("completedDeliveries");
                }
            }
        });
    }

    public void onDataDisplay(String collectionName) {
        try {
            controller.displayQueue(DataBaseMgmt.buildQueue(collectionName), collectionName);
        } catch (Exception e) {
            System.err.println("Error displaying data from collection " + collectionName + ": " + e.getMessage());
        }
    }

}
