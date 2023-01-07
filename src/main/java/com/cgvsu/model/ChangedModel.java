package com.cgvsu.model;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.util.Objects;

public class ChangedModel extends Model {
    private Vector3f rotate;
    private Vector3f scale;
    private Vector3f translate;

    public ChangedModel(Model model) {
        super(model);
        rotate = new Vector3f(0, 0, 0);
        scale = new Vector3f(1, 1, 1);
        translate = new Vector3f(0, 0, 0);
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
