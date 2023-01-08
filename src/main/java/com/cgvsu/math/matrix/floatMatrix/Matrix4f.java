package com.cgvsu.math.matrix.floatMatrix;

import com.cgvsu.math.matrix.Matrix;
import com.cgvsu.math.matrix.Matrix4;

import java.util.List;

import static com.cgvsu.math.matrix.MatrixUtils.toFloatArray;

public class Matrix4f extends Matrix4<Float> {
    public Matrix4f(List<Float> matrix) {
        super(matrix);
    }

    public Matrix4f(float[] matrix) {
        super(toFloatArray(matrix));
    }

    public Matrix4f(Matrix<Float> matrix) {
        super(matrix);
    }

    public Matrix4f() {
        super(0f);
    }

    public Matrix4f(float value) {
        super(value);
    }
}
