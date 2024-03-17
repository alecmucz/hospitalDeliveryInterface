package com.example.hospitaldeliveryinterface;

import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import javax.annotation.Nullable;
import java.util.Queue;

public class FirebaseListener {

    private Firestore firestore;
    private HomepageController controller;

    public FirebaseListener(HomepageController controller){
        this.controller = controller;
        this.firestore = FirestoreClient.getFirestore();

        //this listen to changes in the pendingDeliveries Colllection
        firestore.collection("pendingDeliveries").addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                }

                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    System.out.println("PendingDeliveries Collection has been UPDATED");
                    onDataDisplay("pendingDeliveries");
                } else {
                    System.out.println("No documents found in collection: pendingDeliveries.");
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

                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    System.out.println("CompletedDeliveries Collection has been UPDATED");
                    onDataDisplay("completedDeliveries");
                } else {
                    System.out.println("No documents found in collection: completedDeliveries.");
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
