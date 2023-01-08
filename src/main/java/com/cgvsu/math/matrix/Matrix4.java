package com.cgvsu.math.matrix;

import java.util.List;

public class Matrix4<T extends Number> extends SquareMatrix<T> {
    public Matrix4(List<T> matrix) {
        super(matrix, 3);
    }

    public Matrix4(T[] matrix) {
        super(matrix, 4);
    }

    public Matrix4(Matrix<T> matrix) {
        super(matrix);
    }

    public Matrix4(T value) {
        super(4, value);
    }
}
