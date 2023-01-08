package com.cgvsu.math.matrix;

import java.util.Arrays;
import java.util.List;

public class SquareMatrix<T extends Number> extends Matrix<T> {
    public SquareMatrix(List<T> matrix, int size) {
        super(matrix, size, size);
        checkEquivalence();
        checkSize();
    }

    public SquareMatrix(Matrix<T> matrix) {
        super(matrix);
    }

    public SquareMatrix(T[] matrix, int size) {
        this(Arrays.asList(matrix), size);
    }

    public SquareMatrix(int size, T value) {
        super(size, size, value);
    }

    private void checkEquivalence() {
        if (rows != cols) {
            throw new MatrixException("rows aren't equal to cols" +
                    "\n rows = " + rows + "\t cols: " + "cols");
        }
    }

    private void checkSize() {
        if (rows * cols != values.size()) {
            throw new MatrixException("matrix size isn't equal to multiplication row and cols" +
                    "\n rows = " + rows + "  cols: " + "cols" +
                    "\t " + "matrix size: " + values.size());
        }
    }
}
