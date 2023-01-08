package com.cgvsu.model;


import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import javafx.scene.image.Image;

import java.util.Objects;

public class ChangedModel extends Model {
    private Vector3f rotate;
    private Vector3f scale;
    private Vector3f translate;
    private boolean isRasterized = false;
    private boolean isZBuffered = false;
    private boolean isLighted = false;
    private boolean isTextureLoaded = false;
    private boolean isGridLoaded = true;

    public Image image;

    public ChangedModel(Model model) {
        super(model);
        rotate = new Vector3f(0, 0, 0);
        scale = new Vector3f(1, 1, 1);
        translate = new Vector3f(0, 0, 0);
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
        return rotate;
    }

    public void setRotate(Vector3f rotate) {
        this.rotate = rotate;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public Vector3f getTranslate() {
        return translate;
    }

    public void setTranslate(Vector3f translate) {
        this.translate = translate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChangedModel that = (ChangedModel) o;
        return Objects.equals(rotate, that.rotate) && Objects.equals(scale, that.scale) && Objects.equals(translate, that.translate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rotate, scale, translate);
    }
}
