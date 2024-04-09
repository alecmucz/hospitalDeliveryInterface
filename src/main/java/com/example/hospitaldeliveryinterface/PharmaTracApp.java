package com.example.hospitaldeliveryinterface;

import com.algolia.search.SearchClient;
import com.example.hospitaldeliveryinterface.Algolia.AlgoliaContext;
import com.example.hospitaldeliveryinterface.firebase.FirestoreContext;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PharmaTracApp extends Application {

    public static Firestore fstore;
    public static FirebaseAuth fauth;
    private final FirestoreContext contxtFirebase = new FirestoreContext();
    private static Scene scene;
    private static Stage stage;
    public static SearchClient aClient;
    private static AlgoliaContext client = new AlgoliaContext();

    @Override
    public void start(Stage theStage) throws IOException {

        fstore = contxtFirebase.firebase();
        fauth = FirebaseAuth.getInstance();
        aClient = client.algoliaClient();

        stage = theStage;
        FXMLLoader fxmlLoader = new FXMLLoader(PharmaTracApp.class.getResource("Homepage.fxml"));

        scene = new Scene(fxmlLoader.load(),900,500);
        stage.setTitle("PharmaTrac");
        stage.setScene(scene);
        stage.show();
    }

    static Scene getScene(){
        return scene;
    }

    static Stage getStage(){
        return stage;
    }

    public static void main(String[] args) {
        launch();
    }
}