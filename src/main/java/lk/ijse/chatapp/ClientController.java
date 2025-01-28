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
import java.net.Socket;

public class ClientController {

    @FXML
    public ImageView imageView;
    @FXML
    public Button btnSendImage;
    @FXML
    private Button btnSend;

    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtMessage;

    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    public void initialize(){

            new Thread(()->{
                try {
                    Thread.sleep(1000);
                    socket=new Socket("localhost",3000);
                    txtArea.appendText("\nServer Connected ");

                     dataInputStream = new DataInputStream(socket.getInputStream());
                     dataOutputStream=new DataOutputStream(socket.getOutputStream());
                     listenMessage();

                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
    }

    private void listenMessage() throws IOException {
        while (true){
            String messageType=dataInputStream.readUTF();
            if ("text".equals(messageType)){
                handleTextMessage();
            }else if ("image".equals(messageType)){
                handleImageMessage();
            }

        }
    }

    private void handleImageMessage() throws IOException {
        int length = dataInputStream.readInt();
        if (length>0){
            byte[] imageBytes = new byte[length];
            dataInputStream.readFully(imageBytes);
            System.out.println("image size"+imageBytes.length);
            Image image = new Image(new ByteArrayInputStream(imageBytes));
            imageView.setImage(image);
            txtArea.appendText("Sent image Successfully");


        }

    }

    private void handleTextMessage() throws IOException {
        String txtMessage=dataInputStream.readUTF();
        txtArea.setText("\nServer :" +txtMessage);

    }


    @FXML
    void btnSendOnAction(ActionEvent event) throws IOException {
        String massageToSend=txtMessage.getText().trim();
        if (!massageToSend.isEmpty()) {
            dataOutputStream.writeUTF("text");
            dataOutputStream.writeUTF(massageToSend);
            dataOutputStream.flush();
            txtArea.appendText("\nMe Send : "+massageToSend);

        }

    }

    public void btnSendImageOnAction(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image","*.png","*.jpg","*.jpeg","*.gif"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file!=null){
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
            txtArea.appendText("\nSent Image :"+file.getName());



        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}


