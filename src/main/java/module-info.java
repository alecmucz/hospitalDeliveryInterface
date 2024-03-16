module com.example.hospitaldeliveryinterface {
    requires javafx.controls;
    requires javafx.fxml;
    requires firebase.admin;


    opens com.example.hospitaldeliveryinterface to javafx.fxml;
    exports com.example.hospitaldeliveryinterface;
}