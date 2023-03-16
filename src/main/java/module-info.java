module otan.franema {
    requires javafx.controls;
    requires javafx.fxml;


    opens otan.franema to javafx.fxml;
    opens otan.franema.controllers to javafx.fxml;
    exports otan.franema;
}