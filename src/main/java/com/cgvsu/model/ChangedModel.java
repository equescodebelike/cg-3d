package com.cgvsu.model;

import com.cgvsu.math.Vector3f;

import javax.vecmath.Matrix4f;

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
}
