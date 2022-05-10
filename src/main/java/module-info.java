module com.example.csan {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

requires commons.io;
    opens com.example.csan to javafx.fxml;
    exports com.example.csan;
}