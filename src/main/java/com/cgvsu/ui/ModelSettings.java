package com.cgvsu.ui;

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

import javax.vecmath.Vector3f;

public class ModelSettings {
    private final TranslateTransition translateTransition;
    AnchorPane settings;
    SimpleObjectProperty<UIModel> currentModel = new SimpleObjectProperty<>(this, "currentModel");

    ModelTransform rotateTransform;
    ModelTransform scaleTransform;
    ModelTransform translateTransform;

    private final double defaultX;
    private final double movedX;


    public class  ModelTransform{
        Label nameOfTransform;
        ModelTransformByAxis modelTransformByX;
        ModelTransformByAxis modelTransformByY;
        ModelTransformByAxis modelTransformByZ;

        Vector3f vector = new Vector3f();
        private class ModelTransformByAxis{
            Label axis;
            TextField textField;
            Slider slider;
            SimpleIntegerProperty currentValue = new SimpleIntegerProperty(this, "currentValue");

            public ModelTransformByAxis(BorderPane borderPane) {

                this.axis = (Label) borderPane.getLeft();
                this.textField = (TextField) borderPane.getCenter();
                this.slider = (Slider) borderPane.getBottom();
                textField.textProperty().addListener((observableValue, s, t1) -> {
                    if(t1.isEmpty()){
                        currentValue.set(0);
                    }
                    else {
                        currentValue.set(Integer.parseInt(textField.getText()));
                        textField.setText(t1.replaceAll("[^\\d]", ""));
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
            modelTransformByX  = new ModelTransformByAxis(borderPaneX);
            modelTransformByX.currentValue.addListener((observableValue, number, t1) -> vector.x = t1.floatValue());

            BorderPane borderPaneY = (BorderPane) vBox.getChildren().get(2);
            modelTransformByY  = new ModelTransformByAxis(borderPaneY);
            modelTransformByY.currentValue.addListener((observableValue, number, t1) -> vector.y = t1.floatValue());

            BorderPane borderPaneZ = (BorderPane) vBox.getChildren().get(3);
            modelTransformByZ  = new ModelTransformByAxis(borderPaneZ);
            modelTransformByZ.currentValue.addListener((observableValue, number, t1) -> vector.z = t1.floatValue());
        }

        public Vector3f getVector(){
            return vector;
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
                if (t1 != null){
                    translateTransition.setByX(-175);
                    currentModel.get().model.setRotate(rotateTransform.vector);
                    currentModel.get().model.setScale(scaleTransform.vector);
                    currentModel.get().model.setTranslate(translateTransform.vector);

                }
                else {
                    translateTransition.setByX(175);
                }
                translateTransition.play();
            }
        });

        translateTransition = new TranslateTransition();
        translateTransition.setNode(settings);
        translateTransition.setByX(175);
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
