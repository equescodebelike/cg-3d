package com.cgvsu.math.matrix.floatMatrix;

import com.cgvsu.math.matrix.Matrix3;

import java.util.Arrays;
import java.util.List;

public class Matrix3D extends Matrix3<Double> {
    public Matrix3D(List<Double> matrix) {
        super(matrix);
    }

    public Matrix3D(double[] matrix) {
        super(Arrays.stream(matrix).boxed().toArray(Double[]::new));
    }

    public Matrix3D(Double value) {
        super(value);
    }
}
