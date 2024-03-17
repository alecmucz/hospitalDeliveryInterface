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


    opens com.example.hospitaldeliveryinterface to javafx.fxml;
    exports com.example.hospitaldeliveryinterface;
}