module lk.ijse.chatapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens lk.ijse.chatapp to javafx.fxml;
    exports lk.ijse.chatapp;
}