package lk.ijse.chatapp;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController {

    @FXML
    public ImageView imageView;
    public Button btnSendImage;
    @FXML
    private Button btnSend;

    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtMessage;

    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public void initialize(){
        new Thread(()->{
            try {
                serverSocket = new ServerSocket(3000);
                socket=serverSocket.accept();
                System.out.println("\nServer Connected");

                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                listenMassage();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void listenMassage() {
        while (true) {
            try {

                String messageType = dataInputStream.readUTF();
                if ("text".equals(messageType)){
                    handleTextMessage();
                }else if ("image".equals(messageType)){
                    handleFileMessage();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleFileMessage() throws IOException {
        int length = dataInputStream.readInt();
        if (length>0){
            byte[] imageBytes = new byte[length];
            dataInputStream.readFully(imageBytes);
            Image image = new Image(new ByteArrayInputStream(imageBytes));
            imageView.setImage(image);
            txtArea.appendText("\nsent image");

        }
    }

    private void handleTextMessage() throws IOException {
        String txtMessage = dataInputStream.readUTF();
        txtArea.appendText("\nClient : "+txtMessage);
    }

    @FXML
    void btnSendOnAction(ActionEvent event) throws IOException {
        String messageToSend=txtMessage.getText().trim();
        if (!messageToSend.isEmpty()) {
            dataOutputStream.writeUTF("text");
            dataOutputStream.writeUTF(messageToSend);
            dataOutputStream.flush();
            txtArea.appendText("\nMe Send : "+messageToSend);


        }

    }

    public void btnSendImageOnAction(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image","*.png","*.jpg","*.jpeg","*.gif"));
        File file=fileChooser.showOpenDialog(new Stage());
        if (file!=null) {
            sendImage(file);
        }

    }

    private void sendImage(File file) throws IOException {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] byteArray = new byte[(int) file.length()];
            fileInputStream.read(byteArray);
            dataOutputStream.writeUTF("image");
            dataOutputStream.writeInt(byteArray.length);
            dataOutputStream.write(byteArray);
            dataOutputStream.flush();
            txtArea.appendText("Sent image :" +file.getName());

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
