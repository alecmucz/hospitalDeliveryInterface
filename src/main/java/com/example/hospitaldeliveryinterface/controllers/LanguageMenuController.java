package com.example.hospitaldeliveryinterface.controllers;

import com.example.hospitaldeliveryinterface.firebase.FirebaseListener;
import com.example.hospitaldeliveryinterface.model.MitchTextTranslate;
import com.example.hospitaldeliveryinterface.model.ToggleTracking;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.HashMap;

public class LanguageMenuController {

    @FXML
    private GridPane languageGrid;

    @FXML
    private AnchorPane languagePane;

    private Button prevButton;

    private Button languageButton;


    private HomepageController homeController;



    public void initialize() {

        for (Node node : languageGrid.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setOnAction(this::onLanguageButtonClick);

                if(button.getText().equals("English")){
                    prevButton = button;
                    buttonToggle(button);
                }
            }
        }

        languagePane.getStylesheets().clear();
    }
    public void setButton(Button button) {
        languageButton = button;
    }


    public void setHomeController(HomepageController homeController) {
        this.homeController = new HomepageController(homeController);
    }

    private void onLanguageButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        System.out.println("Language Selected: " + clickedButton.getText());
        buttonToggle(clickedButton);
        languageButton.setText("Language: " + clickedButton.getText());

        HashMap<String,String[]> checkStoredLang = MitchTextTranslate.getStoredLang();

        if(checkStoredLang.containsKey(clickedButton.getText()) && !clickedButton.getText().equals(prevButton.getText())){

            ToggleTracking.setLanguageTrack(clickedButton.getText());
           String[] retrieveTranslatedText = checkStoredLang.get(clickedButton.getText());
           homeController.setLangToggleBtn(retrieveTranslatedText);
            FirebaseListener.navBarDataDisplay(ToggleTracking.getCurrentTab());
            buttonNotToggle(prevButton);
            prevButton = clickedButton;
        }

    }
    public void buttonToggle(Button button) {
        button.setStyle("-fx-background-color: white; -fx-text-fill: #22aae1;");
    }

    public void buttonNotToggle(Button button) {
        button.setStyle("-fx-background-color: transparent;-fx-text-fill:white;");
    }

}