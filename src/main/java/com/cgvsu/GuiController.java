package com.cgvsu;

import com.cgvsu.misc.ToggleSwitch;
import com.cgvsu.model.ChangedModel;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.objwriter.ObjWriter;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.render_engine.RenderEngine;
import com.cgvsu.triangulation.Triangle;
import com.cgvsu.ui.Border;
import com.cgvsu.ui.DialogException;
import com.cgvsu.ui.ModelSettings;
import com.cgvsu.ui.UIModel;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point2f;
import javax.vecmath.Vector3f;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.cgvsu.render_engine.GraphicConveyor.multiplyMatrix4ByVector3;
import static com.cgvsu.render_engine.GraphicConveyor.rotateScaleTranslate;

public class GuiController {

    final private float TRANSLATION = 2F;
    private boolean writeToConsole = false;

    private final Scene scene = new Scene();

    List<UIModel> uiModels = new ArrayList<>();

    public static final float EPS = 1e-6f;

    private boolean isClickedOnModel = false;

    private final SimpleObjectProperty<UIModel> currentUIModel = new SimpleObjectProperty<>(this, "currentUIModel");

    ModelSettings settings;
    Boolean isClicked = false;
    int ticks =0;

    @FXML
    AnchorPane modelSettings;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private AnchorPane changeTheme;

    @FXML
    private Canvas canvas;

    @FXML
    private MenuButton menuButton;

    @FXML
    private ListView<UIModel> listView;

    private int numberCamera = 0;
    private int numberMesh = 0;

    private Timeline timeline;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
        settings = new ModelSettings(modelSettings);

        anchorPane.widthProperty().addListener((observableValue, number, t1) -> canvas.setWidth(t1.intValue()));
        anchorPane.heightProperty().addListener((observableValue, number, t1) -> canvas.setHeight(t1.intValue()));


        currentUIModel.addListener((observableValue, uiModel, t1) -> {
            isClickedOnModel = t1 != null;
            settings.setCurrentModel(t1);
        });

        canvas.setOnMouseReleased(mouseEvent -> {
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();
            Point2f point2f = new Point2f((float) x, (float) y);
            for (int i = 0, uiModelsSize = uiModels.size(); i < uiModelsSize; i++) {
                UIModel uiModel = uiModels.get(i);
                if (uiModel.getBorder().isInBorder(point2f)) {
                    currentUIModel.set(uiModel);
                    numberMesh = i;
                    return;
                }
            }
            currentUIModel.set(null);
        });

        // FileInputStream input = null;
        InputStream is = getClass().getResourceAsStream("fxml/image/ico.png");
        Image image = new Image(is);
        ImageView imageView = new ImageView(image);
        menuButton.setGraphic(imageView);

        menuButton.setStyle("-fx-mark-color: transparent");
        menuButton.setShape(new Circle());

        ToggleSwitch button = new ToggleSwitch();
        SimpleBooleanProperty turn = button.switchOnProperty();
        turn.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                button.getScene().getRoot().getStylesheets().add(Objects.requireNonNull(getClass().getResource("fxml/styles/style.css")).toString());
            } else {
                button.getScene().getRoot().getStylesheets().remove(Objects.requireNonNull(getClass().getResource("fxml/styles/style.css")).toString());
            }

        });

        changeTheme.getChildren().add(button);
        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        //It's better to change. Updating every 15 millis aren't well.
        //I'll do it by myself @Nikitos
        KeyFrame frame = new KeyFrame(Duration.millis(300), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();
            if(isClicked){
                ticks++;
                scene.getCamera().get(numberCamera).setPositionLight(ticks);
            }
            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            scene.getCamera().get(numberCamera).setAspectRatio((float) (width / height));

            for (int i = 0; i < scene.loadedMeshes.size(); i++) {

                canvas.setOnMousePressed(this::handleMousePressed);
                handleWheelScroll();
                try {
                    RenderEngine.render(canvas.getGraphicsContext2D(), scene.getCamera().get(numberCamera), scene.loadedMeshes.get(i), (int) width, (int) height);
                } catch (IOException e) {
                    new DialogException("Error with rendering!");
                }
                UIModel a = uiModels.get(i);
                Model model = scene.loadedMeshes.get(i);
                Point2f minP = model.getMinPoint2f();
                Point2f maxP = model.getMaxPoint2f();
                a.setSize(minP, maxP);
            }
            if (isClickedOnModel) {
                Border b = currentUIModel.get().getBorder();
                canvas.getGraphicsContext2D().strokeRect(
                        b.getScale().x,
                        b.getScale().y,
                        b.getWidth(),
                        b.getHeight()
                );
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    private void handleWheelScroll() {
        anchorPane.setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY();
            if (deltaY > 0) {
                scene.getCamera().get(numberCamera).movePosition(new Vector3f(0, 0, -TRANSLATION));
            } else {
                scene.getCamera().get(numberCamera).movePosition(new Vector3f(0, 0, TRANSLATION));
            }
        });
    }

    private void handleMousePressed(javafx.scene.input.MouseEvent event) {
        var ref = new Object() {
            float prevX = (float) event.getX();
            float prevY = (float) event.getY();
        };
        canvas.setOnMouseDragged(mouseEvent -> {
            final float actualX = (float) mouseEvent.getX();
            final float actualY = (float) mouseEvent.getY();
            float dx = ref.prevX - actualX;
            final float dy = actualY - ref.prevY;
            final float dxy = Math.abs(dx) - Math.abs(dy);
            float dz = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

            if (dxy >= EPS && (scene.getCamera().get(numberCamera).getPosition().x <= EPS && dx < 0 ||
                    scene.getCamera().get(numberCamera).getPosition().x > EPS && dx > 0)) {
                dz *= -1;
            } else if (dxy < EPS) { //если больше перемещаем по y, то по z не перемещаем
                dz = 0;
            }
            if (scene.getCamera().get(numberCamera).getPosition().z <= EPS) {
                dx *= -1;
            }

            ref.prevX = actualX;
            ref.prevY = actualY;
            scene.getCamera().get(numberCamera).movePosition(new Vector3f(new float[]{dx * 0.1f, dy * 0.1f, dz * 0.1f}));
        });
    }

    @FXML
    private void openModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());
        String fileContent = "";
        try {
            fileContent = Files.readString(fileName);

        } catch (IOException exception) {
            new DialogException("Error with reading model!");
        }
        if (scene.loadedMeshes.size() >= 1) {
            numberMesh++;
            addCamera();
        }
        Model model = ObjReader.read(fileContent, writeToConsole);
        model.setName(file.getName());
        ChangedModel changedModel = new ChangedModel(model);

        scene.loadedMeshes.add(changedModel);
        UIModel a = new UIModel(changedModel);
        uiModels.add(a);

        ArrayList<Polygon> triangles = Triangle.triangulatePolygon(scene.loadedMeshes.get(numberMesh).getPolygons());
        scene.loadedMeshes.get(numberMesh).setPolygons(triangles);
        listView.getItems().add(a);
        listView.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            currentUIModel.set(t1);
        });

    }

    @FXML
    private void saveModelMenuItemClick() {
        Vector3f defaultRotate = new Vector3f(0, 0, 0);
        Vector3f defaultScale = new Vector3f(1, 1, 1);
        Vector3f defaultTranslate = new Vector3f(0, 0, 0);

        try {
            if (!Objects.equals(currentUIModel.get().getModel().getRotate(), defaultRotate) || !Objects.equals(currentUIModel.get().getModel().getScale(), defaultScale) ||
                    !Objects.equals(currentUIModel.get().getModel().getTranslate(), defaultTranslate)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Save model with changes?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    saveChangedModelMenuItemClick();
                }

                if (alert.getResult() == ButtonType.NO) {
                    saveBasicModelMenuItemClick();
                }
            } else {
                saveBasicModelMenuItemClick();
            }
        } catch (NullPointerException e) {
            new DialogException("Select model before saving!");
        }
    }

    private void saveBasicModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Save Model");
        fileChooser.setInitialFileName("NewFileOBJ");
        File file = fileChooser.showSaveDialog(canvas.getScene().getWindow());

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            ArrayList<String> fileContent = ObjWriter.write(scene.loadedMeshes.get(numberMesh));
            FileWriter writer = new FileWriter(fileName.toFile());
            for (String s : fileContent) {
                writer.write(s + "\n");

            }
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            new DialogException("Error with saving model!");
        }
    }

    private void saveChangedModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Save Changed Model");
        fileChooser.setInitialFileName("NewChangedFileOBJ");
        File file = fileChooser.showSaveDialog(canvas.getScene().getWindow());

        Path fileName = Path.of(file.getAbsolutePath());
        ChangedModel model = currentUIModel.get().getModel();
        Matrix4f rst = rotateScaleTranslate(model.getRotate(),
                model.getScale(),
                model.getTranslate());
        Model model1 = new Model(model);
        model1.setVertices(new ArrayList<>());
        for (com.cgvsu.math.Vector3f vertex : model.getVertices()) {
            Vector3f vM = new Vector3f(vertex.getX(), vertex.getY(), vertex.getZ());
            Vector3f resultVector = multiplyMatrix4ByVector3(rst, vM);
            model1.getVertices().add(new com.cgvsu.math.Vector3f(
                    resultVector.x,
                    resultVector.y,
                    resultVector.z
            ));

        }
        try {
            ArrayList<String> fileContent = ObjWriter.write(model1);
            FileWriter writer = new FileWriter(fileName.toFile());
            for (String s : fileContent) {
                writer.write(s + "\n");

            }
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            new DialogException("Error with saving changed model!");
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
    public void deleteMesh() {
        if (scene.loadedMeshes.size() >= 1) {
            listView.getItems().remove(uiModels.get(numberMesh));
            uiModels.remove(numberMesh);
            scene.loadedMeshes.remove(numberMesh);
            numberMesh--;
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

    public void doScene(ActionEvent actionEvent) {
        isClicked = true;

    }
}