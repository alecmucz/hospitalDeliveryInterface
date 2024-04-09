package com.example.hospitaldeliveryinterface.controllers;

import com.example.hospitaldeliveryinterface.firebase.FirebaseListener;
import com.example.hospitaldeliveryinterface.model.NotifyMessg;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.Queue;

public class NotifyMessageController {

    @FXML
    private AnchorPane notifyBox;

    @FXML
    private Label notifyDatetime;

    @FXML
    private Label notifyMess;


    private  boolean beginNotify;
    public void initialize(){

        beginNotify = false;
        notifyBox.setVisible(false);

        notifyMess.setVisible(false);
        notifyBox.setVisible(false);
        notifyDatetime.setVisible(false);
    }

    public void displayNotfications(){

        if(!beginNotify){

            beginNotify = true;

            new Thread(()->{//allows to run independently from the main applicaiton flow

                Queue<NotifyMessg> retrieveMessgQueue = NotifyMessg.getMessgQueue();

                while(!retrieveMessgQueue.isEmpty()){
                    try {
                        NotifyMessg selectedNotify = NotifyMessg.removeMessg();

                        if(selectedNotify == null){break;}

                        Platform.runLater(()->{

                            notifyMess.setText("");
                            notifyDatetime.setText("");

                            notifyBox.setVisible(true);
                            notifyMess.setVisible(true);
                            notifyDatetime.setVisible(true);

                            FadeTransition fade1 = new FadeTransition(Duration.millis(3000), notifyBox);
                            FadeTransition fade2 = new FadeTransition(Duration.millis(3000), notifyBox);
                            FadeTransition fade3 = new FadeTransition(Duration.millis(3000), notifyBox);

                            fade1.setFromValue(1.0);
                            fade1.setToValue(0.0);
                            fade2.setFromValue(1.0);
                            fade2.setToValue(0.0);
                            fade3.setFromValue(1.0);
                            fade3.setToValue(0.0);

                            // Play all fade transitions
                            fade1.play();
                            fade2.play();
                            fade3.play();

                            notifyMess.setText(selectedNotify.getMessage());
                            notifyDatetime.setText(selectedNotify.getMssgDate()+" - "+selectedNotify.getMssgTime());
                        });
                        retrieveMessgQueue = NotifyMessg.getMessgQueue();
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                beginNotify = false;
                notifyBox.setVisible(false);
                notifyMess.setVisible(false);
                notifyDatetime.setVisible(false);
            }).start();


        }

    }

}
