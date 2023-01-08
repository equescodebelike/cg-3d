package com.cgvsu.ui;

import com.cgvsu.math.vectors.vectorFloat.Vector3f;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


public class ModelSettings {
    private final TranslateTransition translateTransition;
    AnchorPane settings;
    SimpleObjectProperty<UIModel> currentModel = new SimpleObjectProperty<>(this, "currentModel");

    ModelTransform rotateTransform;
    ModelTransform scaleTransform;
    ModelTransform translateTransform;

    private final double defaultX;
    private final double movedX;


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
            modelTransformByX.currentValue.addListener((observableValue, number, t1) -> vector.get().setX(t1.floatValue()));

            BorderPane borderPaneY = (BorderPane) vBox.getChildren().get(2);
            modelTransformByY = new ModelTransformByAxis(borderPaneY);
            modelTransformByY.currentValue.addListener((observableValue, number, t1) -> vector.get().setY(t1.floatValue()));

            BorderPane borderPaneZ = (BorderPane) vBox.getChildren().get(3);
            modelTransformByZ = new ModelTransformByAxis(borderPaneZ);
            modelTransformByZ.currentValue.addListener((observableValue, number, t1) -> vector.get().setZ(t1.floatValue()));

            vector.addListener(new ChangeListener<Vector3f>() {
                @Override
                public void changed(ObservableValue<? extends Vector3f> observableValue, Vector3f vector3f, Vector3f t1) {
                    modelTransformByX.textField.setText((int) (float)t1.getX() + "");
                    modelTransformByY.textField.setText((int) (float)t1.getY() + "");
                    modelTransformByZ.textField.setText((int) (float)t1.getZ() + "");
                    System.out.println("I picked another model");
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

        defaultX = vBox.getLayoutX();

        movedX = vBox.getWidth();

        rotateTransform = new ModelTransform((VBox) vBox.getChildren().get(0));

        scaleTransform = new ModelTransform((VBox) vBox.getChildren().get(1));

        translateTransform = new ModelTransform((VBox) vBox.getChildren().get(2));


        currentModel.addListener(new ChangeListener<UIModel>() {
            @Override
            public void changed(ObservableValue<? extends UIModel> observableValue, UIModel uiModel, UIModel t1) {
                if (t1 != null) /**/{
                    rotateTransform.setVector(t1.model.getRotate());
                    scaleTransform.setVector(t1.model.getScale());
                    translateTransform.setVector(t1.model.getTranslate());
                    if (uiModel != null) {
                        translateTransition.setFromX(0);
                        translateTransition.setToX(0);
                        System.out.println("\n\n\n!!!!!!!\n\n\n");
                    } else {
                        translateTransition.setFromX(175);
                        translateTransition.setToX(0);
                    }
                    currentModel.get().model.setRotate(rotateTransform.vector.get());
                    currentModel.get().model.setScale(scaleTransform.vector.get());
                    currentModel.get().model.setTranslate(translateTransform.vector.get());
                } else {
                    System.out.println("Модели нет");
                    if (uiModel != null){

                        translateTransition.setFromX(0);
                        translateTransition.setToX(175);
                        System.out.println("но прошлая была");
                    }
                    else {
                        translateTransition.setFromX(0);
                        translateTransition.setToX(0);
                        System.out.println("но прошлой не было");
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
