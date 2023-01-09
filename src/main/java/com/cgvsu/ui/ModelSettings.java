package com.cgvsu.ui;

import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.vecmath.Vector3f;
import java.io.File;


public class ModelSettings {
    private final TranslateTransition translateTransition;
    AnchorPane settings;
    SimpleObjectProperty<UIModel> currentModel = new SimpleObjectProperty<>(this, "currentModel");

    ModelTransform rotateTransform;
    ModelTransform scaleTransform;
    ModelTransform translateTransform;

    CheckBox rasterization;
    CheckBox zBuffer;
    CheckBox switchLights;
    CheckBox unloadTexture;
    CheckBox switchGrid;

    private void uploadTextureChanged(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
        if (currentModel != null) {
            if (t1 && currentModel.get().model.image == null){

                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model", "*.png","*.jpg"));
                fileChooser.setTitle("Load Model");

                File file = fileChooser.showOpenDialog(settings.getScene().getWindow());
                if (file == null) {
                    unloadTexture.setSelected(false);
                    return;
                }
                currentModel.get().model.image = new Image(file.toString());

            }
            unloadTexture.setSelected(t1);

            currentModel.get().getModel().setTextureLoaded(t1);

        }
    }


    public class ModelTransform {
        Label nameOfTransform;
        ModelTransformByAxis modelTransformByX;
        ModelTransformByAxis modelTransformByY;
        ModelTransformByAxis modelTransformByZ;

        SimpleObjectProperty<Vector3f> vector = new SimpleObjectProperty<>(this, "vector", new Vector3f());

        private class ModelTransformByAxis {
            Label axis;
            TextField textField;
            Slider slider;
            SimpleIntegerProperty currentValue = new SimpleIntegerProperty(this, "currentValue");

            public ModelTransformByAxis(BorderPane borderPane) {

                this.axis = (Label) borderPane.getLeft();
                this.textField = (TextField) borderPane.getCenter();
                this.slider = (Slider) borderPane.getBottom();
                textField.textProperty().addListener((observableValue, s, t1) -> {
                    if (t1.isEmpty() || t1.equals("-")) {
                        currentValue.set(0);
                    } else {
                        textField.setText(t1.replaceAll("[^-\\d]", ""));

                        currentValue.set(Integer.parseInt(textField.getText()));
                    }
                    slider.adjustValue(currentValue.get());
                });
                slider.valueProperty().addListener((observableValue, number, t1) -> {
                    currentValue.set(t1.intValue());
                    textField.setText(String.valueOf(t1.intValue()));
                });
            }
        }

        public ModelTransform(VBox vBox) {
            nameOfTransform = (Label) vBox.getChildren().get(0);

            BorderPane borderPaneX = (BorderPane) vBox.getChildren().get(1);
            modelTransformByX = new ModelTransformByAxis(borderPaneX);
            modelTransformByX.currentValue.addListener((observableValue, number, t1) -> vector.get().x = t1.floatValue());

            BorderPane borderPaneY = (BorderPane) vBox.getChildren().get(2);
            modelTransformByY = new ModelTransformByAxis(borderPaneY);
            modelTransformByY.currentValue.addListener((observableValue, number, t1) -> vector.get().y = t1.floatValue());

            BorderPane borderPaneZ = (BorderPane) vBox.getChildren().get(3);
            modelTransformByZ = new ModelTransformByAxis(borderPaneZ);
            modelTransformByZ.currentValue.addListener((observableValue, number, t1) -> vector.get().z = t1.floatValue());

            vector.addListener(new ChangeListener<Vector3f>() {
                @Override
                public void changed(ObservableValue<? extends Vector3f> observableValue, Vector3f vector3f, Vector3f t1) {
                    modelTransformByX.textField.setText((int) t1.x + "");
                    modelTransformByY.textField.setText((int) t1.y + "");
                    modelTransformByZ.textField.setText((int) t1.z + "");
                }
            });
        }

        public Vector3f getVector() {
            return vector.get();
        }

        public void setVector(Vector3f vector) {
            this.vector.set(vector);
        }
    }


    //it is assumed that it is the settings that are loaded from fxml. No other.
    public ModelSettings(AnchorPane settings) {

        this.settings = settings;

        VBox vBox = (VBox) settings.getChildren().get(0);

        rotateTransform = new ModelTransform((VBox) vBox.getChildren().get(0));

        scaleTransform = new ModelTransform((VBox) vBox.getChildren().get(1));

        translateTransform = new ModelTransform((VBox) vBox.getChildren().get(2));

        rasterization = (CheckBox) vBox.getChildren().get(3);
        rasterization.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if(currentModel != null){
                rasterization.setSelected(t1);
                currentModel.get().getModel().setRasterized(t1);
            }
        });
        zBuffer = (CheckBox) vBox.getChildren().get(4);
        zBuffer.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if(currentModel != null){
                zBuffer.setSelected(t1);
                currentModel.get().getModel().setZBuffered(t1);
            }
        });
        switchLights = (CheckBox) vBox.getChildren().get(5);
        switchLights.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if(currentModel != null){
                switchLights.setSelected(t1);
                currentModel.get().getModel().setLighted(t1);
            }
        });
        switchGrid = (CheckBox) vBox.getChildren().get(7);
        switchGrid.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if(currentModel != null){
                switchGrid.setSelected(t1);
                currentModel.get().getModel().setGridLoaded(t1);
            }
        });


        unloadTexture = (CheckBox) vBox.getChildren().get(6);
        unloadTexture.selectedProperty().addListener(this::uploadTextureChanged);
        currentModel.addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends UIModel> observableValue, UIModel uiModel, UIModel t1) {
                if (t1 != null) {
                    rotateTransform.setVector(t1.model.getRotate());
                    scaleTransform.setVector(t1.model.getScale());
                    translateTransform.setVector(t1.model.getTranslate());

                    rasterization.setSelected(t1.model.isRasterized());
                    zBuffer.setSelected(t1.model.isZBuffered());
                    switchLights.setSelected(t1.model.isLighted());
                    unloadTexture.setSelected(t1.model.isTextureLoaded());
                    switchGrid.setSelected(t1.model.isGridLoaded());

                    if (uiModel != null) {
                        translateTransition.setFromX(0);
                        translateTransition.setToX(0);
                    } else {
                        translateTransition.setFromX(175);
                        translateTransition.setToX(0);
                    }
                    currentModel.get().model.setRotate(rotateTransform.vector.get());
                    currentModel.get().model.setScale(scaleTransform.vector.get());
                    currentModel.get().model.setTranslate(translateTransform.vector.get());
                } else {
                    if (uiModel != null) {

                        translateTransition.setFromX(0);
                        translateTransition.setToX(175);
                    } else {
                        translateTransition.setFromX(0);
                        translateTransition.setToX(0);
                    }
                }
                translateTransition.play();
            }
        });

        translateTransition = new TranslateTransition();
        translateTransition.setNode(settings);
        translateTransition.setFromX(0);
        translateTransition.setToX(175);
        translateTransition.play();
    }

    public ModelTransform getRotateTransform() {
        return rotateTransform;
    }

    public ModelTransform getScaleTransform() {
        return scaleTransform;
    }

    public ModelTransform getTranslateTransform() {
        return translateTransform;
    }

    public void setCurrentModel(UIModel currentModel) {
        this.currentModel.set(currentModel);
    }
}
