package com.cgvsu.math.matrix.floatMatrix;

import com.cgvsu.math.matrix.Matrix;
import com.cgvsu.math.matrix.Matrix3;

import java.util.List;

import static com.cgvsu.math.matrix.MatrixUtils.toFloatArray;

public class Matrix3f extends Matrix3<Float> {
    public Matrix3f(List<Float> matrix) {
        super(matrix);
    }

    public Matrix3f(float[] matrix) {
        super(toFloatArray(matrix));
    }

    public Matrix3f(Matrix<Float> matrix) {
        super(matrix);
    }

    public Matrix3f() {
        super(0f);
    }


    public Matrix3f(int size, Float value) {
        super(value);
    }
}
