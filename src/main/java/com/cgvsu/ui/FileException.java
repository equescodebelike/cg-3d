package com.cgvsu.ui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.FileNotFoundException;

public class FileException  {
    //make static and create method alert
    public FileException(String s) {
        Platform.runLater(() -> {
            Alert dialog = new Alert(Alert.AlertType.ERROR, s, ButtonType.OK);
            dialog.show();
        });
    }
}
