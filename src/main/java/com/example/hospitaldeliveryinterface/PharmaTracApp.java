package com.example.hospitaldeliveryinterface;

import com.algolia.search.SearchClient;
import com.example.hospitaldeliveryinterface.Algolia.AlgoliaContext;
import com.example.hospitaldeliveryinterface.firebase.DataBaseMgmt;
import com.example.hospitaldeliveryinterface.firebase.FirestoreContext;
import com.example.hospitaldeliveryinterface.model.Employee;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.suuft.libretranslate.Translator;


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
        FXMLLoader fxmlloader = new FXMLLoader(PharmaTracApp.class.getResource("Homepage.fxml"));

        scene = new Scene(fxmlloader.load(),929,706);
        stage.setTitle("PharmaTrac");
        stage.setScene(scene);
        scene.getStylesheets().add(PharmaTracApp.class.getResource("lightMode.css").toExternalForm());
        stage.show();

        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Update login status to "False" for the current user
            if (Employee.getCurrentLogin() != null) {
                try {
                    DataBaseMgmt.updateLoginStatus(Employee.getCurrentLogin(),"False");
                } catch (Exception e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            }
        }));
    }

    public static Scene getScene(){
        return scene;
    }

    public static Stage getStage(){
        return stage;
    }

    public static void main(String[] args) {
        System.out.println("PharmaTrac is loading up.");
        String textTranslate = Translator.translate("en","es", "PharmaTrac is loading up.");
        System.out.println("Spanish: " + textTranslate);
        launch();

    }
}