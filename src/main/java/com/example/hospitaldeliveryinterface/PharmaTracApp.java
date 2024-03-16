package com.example.hospitaldeliveryinterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PharmaTracApp extends Application {

    private static Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage theStage) throws IOException {

        stage = theStage;
        FXMLLoader fxmlLoader = new FXMLLoader(PharmaTracApp.class.getResource("Homepage.fxml"));

        scene = new Scene(fxmlLoader.load(),885,678);
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