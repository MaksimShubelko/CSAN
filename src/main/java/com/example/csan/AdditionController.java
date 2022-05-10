package com.example.csan;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class AdditionController implements Initializable {

    @FXML
    private TextArea textAreaInformation = new TextArea();

    public void openAbout() {
        textAreaInformation.setText("fff");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textAreaInformation = new TextArea();
    }
}
