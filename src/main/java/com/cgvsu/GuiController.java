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
import com.cgvsu.ui.ModelSettings;
import com.cgvsu.ui.UIModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import javax.vecmath.Vector3f;
import java.awt.image.BufferedImage;
import java.io.*;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Point2f;

public class GuiController {

    final private float TRANSLATION = 2F;
    // todo: comments below
    // private boolean isStructure = false; mesh
    // public static boolean isLight = true; light
    // private boolean isTexture = false; texture
    private boolean writeToConsole = false;

    private final Scene scene = new Scene();

    List<UIModel> uiModels = new ArrayList<>();

    public static final float EPS = 1e-6f;

    private boolean isClickedOnModel = false;

    private SimpleObjectProperty<UIModel> currentUIModel = new SimpleObjectProperty<>(this, "currentUIModel");

    @FXML
    AnchorPane modelSettings;

    ModelSettings settings;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private AnchorPane changeTheme;

    @FXML
    private Canvas canvas;

    @FXML
    private MenuButton menuButton;

    @FXML
    private ListView listView;

    private Model mesh = null;

    private int numberCamera = 0;
    private int numberMesh = 0;

    private Timeline timeline;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
        settings = new ModelSettings(modelSettings);

        anchorPane.widthProperty().addListener((observableValue, number, t1) -> canvas.setWidth(t1.intValue() - 300));
        anchorPane.heightProperty().addListener((observableValue, number, t1) -> canvas.setHeight(t1.intValue() - 300));


        currentUIModel.addListener(new ChangeListener<UIModel>() {
            @Override
            public void changed(ObservableValue<? extends UIModel> observableValue, UIModel uiModel, UIModel t1) {
                if (t1 != null) {
                    isClickedOnModel = true;
                } else {
                    isClickedOnModel = false;
                }
                settings.setCurrentModel(t1);
            }
        });

        canvas.setOnMouseReleased(mouseEvent -> {
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();
            Point2f point2f = new Point2f((float) x, (float) y);
            for (UIModel uiModel : uiModels) {
                if (uiModel.getBorder().isInBorder(point2f)) {
                    currentUIModel.set(uiModel);
                    return;
                }
            }
            currentUIModel.set(null);
        });


        FileInputStream input = null;
        try {
            input = new FileInputStream("src/main/resources/com/cgvsu/fxml/image/ico.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(input);
        ImageView imageView = new ImageView(image);
        menuButton.setGraphic(imageView);
        menuButton.setStyle("-fx-mark-color: transparent");
        menuButton.setShape(new Circle());

        ToggleSwitch button = new ToggleSwitch();
        SimpleBooleanProperty turn = button.switchOnProperty();
        turn.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                button.getScene().getRoot().getStylesheets().add(getClass().getResource("fxml/styles/style.css").toString());
            } else {
                button.getScene().getRoot().getStylesheets().remove(getClass().getResource("fxml/styles/style.css").toString());
            }

        });
        changeTheme.getChildren().add(button);

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        //It's better to change. Updating every 15 millis aren't well.
        //I'll do it by myself @Nikitos
        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            scene.getCamera().get(numberCamera).setAspectRatio((float) (width / height));

            for (int i = 0; i < scene.loadedMeshes.size(); i++) {

                canvas.setOnMousePressed(this::handleMousePressed);
                handleWheelScroll();
                // if (no light, no texture)
                if (false)
                    try {
                        RenderEngine.render(canvas.getGraphicsContext2D(), scene.getCamera().get(numberCamera), scene.loadedMeshes.get(i), (int) width, (int) height, false, false, null);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    //if (light and texture)
                    //BufferedImage texture = new BufferedImage();
                    //LoadBufferedImage
                else
                    try {
                        RenderEngine.render(canvas.getGraphicsContext2D(), scene.getCamera().get(numberCamera), scene.loadedMeshes.get(i), (int) width, (int) height, true, true, null);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                UIModel a = uiModels.get(i);
                Model model = scene.loadedMeshes.get(i);
                Point2f minP = model.getMinPoint2f();
                Point2f maxP = model.getMaxPoint2f();
                a.setSize(minP, maxP);

                if (isClickedOnModel) {
                    Border b = currentUIModel.get().getBorder();
                    canvas.getGraphicsContext2D().strokeRect(
                            b.getScale().x,
                            b.getScale().y,
                            b.getWidth(),
                            b.getHeight()
                    );
                }

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

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());
        String fileContent = "";
        try {
            fileContent = Files.readString(fileName);

        } catch (IOException exception) {

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
            UIModel a1 = (UIModel) t1;
            currentUIModel.set(a1);
        });

        listView.scrollTo(scene.loadedMeshes.get(numberMesh));

        if (scene.loadedMeshes.size() > 1) {
            addCamera();
            nextModel();
        }

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
            ArrayList<String> fileContent = ObjWriter.write(scene.loadedMeshes.get(numberMesh));
            FileWriter writer = new FileWriter(fileName.toFile());
            for (String s : fileContent) {
                writer.write(s + "\n");

            }
            writer.flush();
            writer.close();
        } catch (IOException ex) {

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
        if (numberMesh < scene.loadedMeshes.size() - 1) numberMesh++;
        else numberMesh = 0;
        nextCamera();
    }

    @FXML
    public void deleteMesh() {
        if (scene.loadedMeshes.size() >= 1) {
            if (numberMesh == scene.loadedMeshes.size() - 1) numberMesh--;
            listView.getItems().remove(uiModels.get(scene.loadedMeshes.size() - 1));
            uiModels.remove(scene.loadedMeshes.size() - 1);
            scene.loadedMeshes.remove(scene.loadedMeshes.size() - 1);
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