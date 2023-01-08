package com.cgvsu.math.vectors.vectorFloat;

import com.cgvsu.math.vectors.Vector3;

import java.util.List;

public class Vector3f extends Vector3<Float> {

    public Vector3f(List<Float> matrix) {
        super(matrix);
    }

    public Vector3f(float x, float y, float z) {
        super(x, y, z);
    }

    public Vector3f(float value) {
        super(value);
    }

    public Vector3f() {
        super(0f);
    }
}
