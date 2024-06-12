module com.example.electrongun {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.electrongun to javafx.fxml;
    exports com.electrongun;
}