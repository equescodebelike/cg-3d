package com.cgvsu;

import com.cgvsu.misc.ToggleSwitch;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.objwriter.ObjWriter;
import com.cgvsu.render_engine.RenderEngine;
import com.cgvsu.triangulation.Triangle;
import com.cgvsu.ui.Border;
import com.cgvsu.ui.MyRectangle;
import com.cgvsu.ui.UIModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
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
import java.util.List;
import javax.vecmath.Point2f;
import javax.vecmath.Vector3f;

import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

public class GuiController {

    final private float TRANSLATION = 2F;
    // todo: comments below
    // private boolean isStructure = false; mesh
    // public static boolean isLight = true; light
    // private boolean isTexture = false; texture
    private boolean writeToConsole = true;
    private final Scene scene = new Scene();

    List<UIModel> uiModels = new ArrayList<>();

    private static final float EPS = 1e-6f;

    private boolean isClickedOnModel = false;
    private UIModel currentUIModel;

    @FXML
    AnchorPane anchorPane;
    @FXML
    AnchorPane changeTheme;

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

        /*checkMenuItem.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                scene.getStyleSheets().add("dark-theme.css");
            } else {
                scene.getStyleSheets().remove("dark-theme.css");
            }
        });*/

        canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double x = mouseEvent.getX();
                double y = mouseEvent.getY();
                Point2f point2f = new Point2f((float) x, (float) y);
                for (UIModel uiModel : uiModels) {
                    if (uiModel.getBorder().isInBorder(point2f)){
                        isClickedOnModel = true;
                        currentUIModel = uiModel;
                        return;
                    }
                }
                isClickedOnModel = false;
                currentUIModel = null;
            }
        });

        ToggleSwitch button = new ToggleSwitch();
        SimpleBooleanProperty turn = button.switchOnProperty();
        turn.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                button.getScene().getRoot().getStylesheets().add(getClass().getResource("fxml/style.css").toString());
            } else {
                button.getScene().getRoot().getStylesheets().remove(getClass().getResource("fxml/style.css").toString());
            }

        });
        changeTheme.getChildren().add(button);

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
        //It's better to change. Updating every 15 millis aren't well.
        //I'll do it by myself @Nikitos
        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            scene.getCamera().get(numberCamera).setAspectRatio((float) (width / height));

            //todo: HashMap, change model

            // scene.getLoadedModels().get(scene.currentModel).setRotate(new com.cgvsu.math.Vector3f(Double.parseDouble()));
            if (scene.mesh.size() != 0) {
                canvas.setOnMousePressed(this::handleMousePressed);
                RenderEngine.render(
                        canvas.getGraphicsContext2D(),
                        scene.getCamera().get(numberCamera),
                        scene.mesh.get(numberMesh),
                        (int) width,
                        (int) height
                );
                UIModel a = uiModels.get(numberMesh);
                Model model = scene.mesh.get(numberMesh);
                Point2f minP = model.getMinPoint2f();
                Point2f maxP = model.getMaxPoint2f();
                a.setSize(minP,maxP);
//                canvas.getGraphicsContext2D().fillRect(minP.x,minP.y,maxP.x - minP.x,maxP.y - minP.y);
//                Border b = a.getBorder();
/*                canvas.getGraphicsContext2D().fillRect(
                        b.getScale().x,
                        b.getScale().y,
                        b.getWidth(),
                        b.getHeight()
                );*/

                if (isClickedOnModel){
                    Border b = currentUIModel.getBorder();
                    canvas.getGraphicsContext2D().strokeRect(
                            b.getScale().x,
                            b.getScale().y,
                            b.getWidth(),
                            b.getHeight()
                    );
                }


            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
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

            /* String fileContent = Files.readString(fileName);
            Model model = ObjReader.read(fileContent, writeToConsole);
            ArrayList<Polygon> triangles = Triangle.triangulatePolygon(model.getPolygons());
            model.setPolygons(triangles);
            scene.getLoadedModels().put(file.getName(), new ChangedModel(model));
            scene.currentModel = file.getName(); */

            // todo: обработка ошибок
        } catch (IOException exception) {

        }

        Model model = ObjReader.read(fileContent, writeToConsole);
        scene.mesh.add(model);

        uiModels.add(new UIModel());

        ArrayList<Polygon> triangles = Triangle.triangulatePolygon(scene.mesh.get(numberMesh).getPolygons());
        scene.mesh.get(numberMesh).setPolygons(triangles);
        if (scene.mesh.size() > 1) {
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