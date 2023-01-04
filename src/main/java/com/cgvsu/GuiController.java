package com.cgvsu;

import com.cgvsu.model.ChangedModel;
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
    private Scene scene = new Scene();

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    // private final List<Model> mesh = new ArrayList<>();
    // private Model mesh = null;


    private int numberCamera = 0;
    private int numberMesh = 0;

    private Timeline timeline;

   /* private List<Camera> camera = new ArrayList<>(List.of(new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100)));*/

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        anchorPane.setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY();
            //todo: scene
            if (deltaY > 0) {
                scene.getCamera().get(numberCamera).movePosition(new Vector3f(0, 0, -TRANSLATION));
            } else {
                scene.getCamera().get(numberCamera).movePosition(new Vector3f(0, 0, TRANSLATION));
            }
        });

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            scene.getCamera().get(numberCamera).setAspectRatio((float) (width / height));

            //todo: HashMap, change model

            // scene.getLoadedModels().get(scene.currentModel).setRotate(new com.cgvsu.math.Vector3f(Double.parseDouble()));

            if (scene.mesh.size() != 0) {
                RenderEngine.render(canvas.getGraphicsContext2D(), scene.getCamera().get(numberCamera), scene.mesh.get(numberMesh), (int) width, (int) height);
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
            scene.mesh.add(ObjReader.read(fileContent, writeToConsole));
            /* String fileContent = Files.readString(fileName);
            Model model = ObjReader.read(fileContent, writeToConsole);
            ArrayList<Polygon> triangles = Triangle.triangulatePolygon(model.getPolygons());
            model.setPolygons(triangles);
            scene.getLoadedModels().put(file.getName(), new ChangedModel(model));
            scene.currentModel = file.getName(); */

            // todo: обработка ошибок
        } catch (IOException exception) {

        }
        ArrayList<Polygon> triangles = Triangle.triangulatePolygon(scene.mesh.get(numberMesh).getPolygons());
        scene.mesh.get(numberMesh).setPolygons(triangles);

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
            ArrayList<String> fileContent = ObjWriter.write(scene.mesh.get(numberMesh));
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
        scene.getCamera().add(new Camera(
                new Vector3f(0, 0, 100),
                new Vector3f(0, 0, 0),
                1.0F, 1, 0.01F, 100));
        numberCamera++;
    }

    @FXML
    public void deleteCamera() {
        if (scene.getCamera().size() > 1) {
            if (numberCamera == scene.getCamera().size() - 1) numberCamera--;
            scene.getCamera().remove(scene.getCamera().size() - 1);
        }
    }

    @FXML
    public void nextCamera() {
        if (numberCamera < scene.getCamera().size() - 1) numberCamera++;
        else numberCamera = 0;
    }

    @FXML
    public void nextModel() {
        if (numberMesh < scene.mesh.size() - 1) numberMesh++;
        else numberMesh = 0;
    }

    @FXML
    public void deleteMesh() {
        if (scene.mesh.size() > 1) {
            if (numberMesh == scene.mesh.size() - 1) numberMesh--;
           scene.mesh.remove(scene.mesh.size() - 1);
        }
    }

    @FXML
    private void displayConsole() {
        writeToConsole = !writeToConsole;
    }

    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        scene.getCamera().get(numberCamera).movePosition(new Vector3f(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        scene.getCamera().get(numberCamera).movePosition(new Vector3f(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        scene.getCamera().get(numberCamera).movePosition(new Vector3f(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        scene.getCamera().get(numberCamera).movePosition(new Vector3f(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        scene.getCamera().get(numberCamera).movePosition(new Vector3f(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        scene.getCamera().get(numberCamera).movePosition(new Vector3f(0, -TRANSLATION, 0));
    }
}