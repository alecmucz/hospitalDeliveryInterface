module com.example.hospitaldeliveryinterface {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.hospitaldeliveryinterface to javafx.fxml;
    exports com.example.hospitaldeliveryinterface;
}