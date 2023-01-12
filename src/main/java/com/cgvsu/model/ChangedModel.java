package com.cgvsu.model;


import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import com.cgvsu.render_engine.GraphicConveyor;
import com.cgvsu.render_engine.Transform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;

import java.util.Objects;

public class ChangedModel extends Model {
    private final SimpleObjectProperty<Vector3f> rotate = new SimpleObjectProperty<>(this, "rotate");
    private final SimpleObjectProperty<Vector3f> scale = new SimpleObjectProperty<>(this, "scale");
    private final SimpleObjectProperty<Vector3f> translate = new SimpleObjectProperty<>(this, "translate");
    private boolean isRasterized = false;
    private boolean isZBuffered = false;
    private boolean isLighted = false;
    private boolean isTextureLoaded = false;
    private boolean isGridLoaded = true;

    private final SimpleObjectProperty<Matrix4f> transform = new SimpleObjectProperty<>(this, "transform");

    public Image image;

    public ChangedModel(Model model) {
        super(model);
        rotate.set(new Vector3f(0, 0, 0));
        scale.set(new Vector3f(1, 1, 1));
        translate.set(new Vector3f(0, 0, 0));


        rotate.addListener(this::changed);
        scale.addListener(this::changed);
        translate.addListener(this::changed);
    }
    public void initialize(){
        transform.set(GraphicConveyor.rotateScaleTranslate(rotate.get(), scale.get(), translate.get()));
    }
    public boolean isZBuffered() {
        return isZBuffered;
    }

    public void setZBuffered(boolean ZBuffered) {
        isZBuffered = ZBuffered;
    }

    public boolean isLighted() {
        return isLighted;
    }

    public void setLighted(boolean lighted) {
        isLighted = lighted;
    }

    public boolean isTextureLoaded() {
        return isTextureLoaded;
    }

    public void setTextureLoaded(boolean textureLoaded) {
        isTextureLoaded = textureLoaded;
    }

    public boolean isGridLoaded() {
        return isGridLoaded;
    }

    public void setGridLoaded(boolean gridLoaded) {
        isGridLoaded = gridLoaded;
    }

    public boolean isRasterized() {
        return isRasterized;
    }

    public void setRasterized(boolean rasterized) {
        isRasterized = rasterized;
    }

    public Model getModel() {
        return this;
    }

    public Matrix4f getModelMatrix() {
        return null;
    }

    public Vector3f getRotate() {
        return rotate.get();
    }

    public void setRotate(Vector3f rotate) {
        this.rotate.set(rotate);
    }

    public Vector3f getScale() {
        return scale.get();
    }

    public void setScale(Vector3f scale) {
        this.scale.set(scale);
    }

    public Vector3f getTranslate() {
        return translate.get();
    }

    public void setTranslate(Vector3f translate) {
        this.translate.set(translate);
    }

    public Matrix4f getTransform() {
        return transform.get();
    }

    public SimpleObjectProperty<Matrix4f> transformProperty() {
        return transform;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChangedModel that = (ChangedModel) o;
        return Objects.equals(rotate.get(), that.rotate.get()) && Objects.equals(scale.get(), that.scale.get()) && Objects.equals(translate.get(), that.translate.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(rotate.get(), scale.get(), translate.get());
    }

    private void changed(ObservableValue<? extends Vector3f> observableValue, Vector3f vector3f, Vector3f t1) {
        transform.set(GraphicConveyor.rotateScaleTranslate(rotate.get(), scale.get(), translate.get()));
    }
}
