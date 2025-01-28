package lk.ijse.chatapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ClientController.class.getResource("clientForm.fxml"));
        Scene clientScene = new Scene(fxmlLoader.load());
        Stage clientStage = new Stage();
        clientStage.setTitle("Client");
        clientStage.setScene(clientScene);
        clientStage.show();

        FXMLLoader fxmlLoader2 = new FXMLLoader(ServerController.class.getResource("serverForm.fxml"));
        Scene serverScene = new Scene(fxmlLoader2.load());
        Stage serverStage = new Stage();
        serverStage.setTitle("Server");
        serverStage.setScene(serverScene);
        serverStage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}