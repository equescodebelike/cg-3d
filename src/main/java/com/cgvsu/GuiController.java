package com.cgvsu;

import com.cgvsu.model.Polygon;
import com.cgvsu.objwriter.ObjWriter;
import com.cgvsu.render_engine.RenderEngine;
import com.cgvsu.triangulation.Triangle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.vecmath.Vector3f;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

public class GuiController {

    final private float TRANSLATION = 2F;
    // private boolean isStructure = false; mesh
    // public static boolean isLight = true; light
    // private boolean isTexture = false; texture
    private boolean writeToConsole = true;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    private final List<Model> mesh = new ArrayList<>();

    private int numberCamera = 0;
    private int numberMesh = 0;

    private Timeline timeline;

    private List<Camera> camera = new ArrayList<>(List.of(new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100)));

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        anchorPane.setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY();
            //todo: scene
            if (deltaY > 0) {
                camera.get(numberCamera).movePosition(new Vector3f(0, 0, -TRANSLATION));
            } else {
                camera.get(numberCamera).movePosition(new Vector3f(0, 0, TRANSLATION));
            }
        });

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.get(numberCamera).setAspectRatio((float) (width / height));

            if (mesh.size() != 0) {
                RenderEngine.render(canvas.getGraphicsContext2D(), camera.get(numberCamera), mesh.get(numberMesh), (int) width, (int) height);
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    @FXML
    private void openModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            mesh.add(ObjReader.read(fileContent, writeToConsole));
            // todo: обработка ошибок
        } catch (IOException exception) {

        }
        ArrayList<Polygon> triangles = Triangle.triangulatePolygon(mesh.get(numberMesh).getPolygons());
        mesh.get(numberMesh).setPolygons(triangles);
    }

    @FXML
    private void saveModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Save Model");
        fileChooser.setInitialFileName("NewFileOBJ");

        File file = fileChooser.showSaveDialog((Stage) canvas.getScene().getWindow());

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            ArrayList<String> fileContent = ObjWriter.write(mesh.get(numberMesh));
            FileWriter writer = new FileWriter(fileName.toFile());
            for (String s : fileContent) {
                writer.write(s + "\n");

            }
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            // Handle exception
        }

    }

    @FXML
    public void addCamera() {
        camera.add(new Camera(
                new Vector3f(0, 0, 100),
                new Vector3f(0, 0, 0),
                1.0F, 1, 0.01F, 100));
        numberCamera++;
    }

    @FXML
    public void deleteCamera() {
        if (camera.size() > 1) {
            if (numberCamera == camera.size() - 1) numberCamera--;
            camera.remove(camera.size() - 1);
        }
    }

    @FXML
    public void nextCamera() {
        if (numberCamera < camera.size() - 1) numberCamera++;
        else numberCamera = 0;
    }

    @FXML
    public void nextModel() {
        if (numberMesh < mesh.size() - 1) numberMesh++;
        else numberMesh = 0;
    }

    @FXML
    public void deleteMesh() {
        if (mesh.size() > 1) {
            if (numberMesh == mesh.size() - 1) numberMesh--;
            mesh.remove(mesh.size() - 1);
        }
    }

    @FXML
    private void displayConsole() {
        writeToConsole = !writeToConsole;
    }

    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        camera.get(numberCamera).movePosition(new Vector3f(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        camera.get(numberCamera).movePosition(new Vector3f(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        camera.get(numberCamera).movePosition(new Vector3f(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        camera.get(numberCamera).movePosition(new Vector3f(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        camera.get(numberCamera).movePosition(new Vector3f(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.get(numberCamera).movePosition(new Vector3f(0, -TRANSLATION, 0));
    }
}