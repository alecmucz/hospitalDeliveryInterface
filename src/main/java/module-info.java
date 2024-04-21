module com.example.hospitaldeliveryinterface {
    requires javafx.controls;
    requires javafx.fxml;
    requires google.cloud.firestore;
    requires firebase.admin;
    requires com.google.auth.oauth2;
    requires java.logging;
    requires com.google.api.apicommon;
    requires com.google.auth;
    requires google.cloud.core;
    requires jsr305;
    requires algoliasearch.apache.uber;


    requires java.net.http;
    requires libretranslate.java;
    requires java.desktop;
    //requires google.api.translate.java;
    //requires com.google.firebase:firebase-auth


    opens com.example.hospitaldeliveryinterface to javafx.fxml;
    exports com.example.hospitaldeliveryinterface;
    exports com.example.hospitaldeliveryinterface.controllers;
    opens com.example.hospitaldeliveryinterface.controllers to javafx.fxml;
    exports com.example.hospitaldeliveryinterface.firebase;
    opens com.example.hospitaldeliveryinterface.firebase to javafx.fxml;
    exports com.example.hospitaldeliveryinterface.model;
    opens com.example.hospitaldeliveryinterface.model to javafx.fxml, algoliasearch.apache.uber, google.cloud.firestore;
}