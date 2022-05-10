package com.example.csan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdditionStage  {
    private TextArea textArea;
    private AnchorPane load = new AnchorPane();
    private Stage stage = new Stage();

    public void start(javafx.stage.Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(AdditionStage.class.getResource("help.fxml"));
        load = (AnchorPane) loader.load();
        textArea = new TextArea();
        textArea.setPrefWidth(602);
        textArea.setPrefHeight(500);
        textArea.setId("textAreaInformation");
        load.getChildren().addAll(textArea);
        Scene scene = new Scene(load);
        stage.setTitle("Ping");
        stage.setScene(scene);
        stage.show();
    }

    public void showStage() throws IOException {
        start(stage);

    }

    public void setTextAreaText(String text) {
        textArea.setText(text);
    }
}
