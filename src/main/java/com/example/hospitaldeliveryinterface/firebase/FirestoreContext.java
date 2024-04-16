package com.example.hospitaldeliveryinterface.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;

public class FirestoreContext {

    public Firestore firebase() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("key.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        }catch(IOException e) {
            e.printStackTrace();
        }
        return FirestoreClient.getFirestore();
    }

}