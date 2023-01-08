package com.cgvsu.math.vectors.vectorFloat;

import com.cgvsu.math.vectors.Vector4;

public class Vector4f extends Vector4<Float> {
    public Vector4f(float x, float y, float z, float w) {
        super(x, y, z, w);
    }

    public Vector4f(float value) {
        super(value);
    }

    public Vector4f() {
        super(0f);
    }
}
