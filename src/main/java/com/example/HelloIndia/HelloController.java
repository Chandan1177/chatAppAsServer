package com.example.HelloIndia;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class HelloController implements Initializable {
    static Socket s;
    static DataOutputStream dos;
    static DataInputStream dis;
    public TextField textField;
    public ScrollPane msgArea;
    @FXML
    public VBox vbox;

    public void onSend(MouseEvent mouseEvent) throws IOException {
        String msg=textField.getText();
        sendHbox(msg);
        dos.writeUTF(msg);
        textField.setText("");
    }

    public void sendHbox(String msg){
        HBox hBox=new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(5,5,5,10));
        Text text=new Text(msg);
        TextFlow textFlow=new TextFlow(text);
        textFlow.setStyle("-fx-background-color:#D9FDD3");
        hBox.getChildren().add(textFlow);
        vbox.getChildren().add(hBox);
    }

    public void receiveHbox(){
        Thread tr=new Thread(()->{
            System.out.println("start"+dos);
            while(true){
                String s="";
                try {
                    if(!(s=dis.readUTF()).equals(null)){
                        System.out.println(s);
                        PrintChar(s);
                        s="";
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });tr.start();
    }

    private synchronized void PrintChar(String str)
    {
            HBox hBox1=new HBox();
            hBox1.setAlignment(Pos.CENTER_LEFT);
            hBox1.setPadding(new Insets(5,5,5,10));
            Text text=new Text(str);
            TextFlow textFlow=new TextFlow(text);
            textFlow.setStyle("-fx-background-color:#FFFFFF");
            hBox1.getChildren().add(textFlow);
            Platform.runLater(() -> {
                vbox.getChildren().add(hBox1);
            });
        }

    public static void  connect(){
        try {
            s = new Socket("10.68.98.125", 9091);
            dos=new DataOutputStream(s.getOutputStream());
            dis=new DataInputStream(s.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void onBackSpace(MouseEvent mouseEvent) {
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connect();
        receiveHbox();
    }
}