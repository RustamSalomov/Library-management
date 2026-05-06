module org.example {

    requires javafx.controls;
    requires javafx.fxml;

    opens org.example to javafx.fxml;
    opens org.example.models to javafx.base;

    exports org.example;
    exports org.example.ui;
    exports org.example.models;
}
