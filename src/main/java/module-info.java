module com.example.electrongun {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.electrongun to javafx.fxml;
    exports com.electrongun;
}