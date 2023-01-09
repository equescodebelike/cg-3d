package com.cgvsu.ui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class DialogException {
    //todo: make static and create method alert
    public DialogException(String s) {
        Platform.runLater(() -> {
            Alert dialog = new Alert(Alert.AlertType.ERROR, s, ButtonType.OK);
            dialog.showAndWait();
        });
    }
}
