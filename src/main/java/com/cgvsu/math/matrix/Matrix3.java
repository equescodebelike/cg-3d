package com.cgvsu.math.matrix;

import java.util.List;

public class Matrix3<T extends Number> extends SquareMatrix<T> {


    public Matrix3(List<T> matrix) {
        super(matrix, 3);
    }

    public Matrix3(T[] matrix) {
        super(matrix, 3);
    }

    public Matrix3(Matrix<T> matrix) {
        super(matrix);
    }

    public Matrix3(T value) {
        super(3, value);
    }

}
