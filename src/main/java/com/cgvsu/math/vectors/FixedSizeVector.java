package com.cgvsu.math.vectors;


import com.cgvsu.math.matrix.MatrixException;

import java.util.List;

public class FixedSizeVector<T extends Number> extends Vector<T> {
    public FixedSizeVector(List<T> matrix, int size) {
        super(matrix);
        if (matrix.size() != size) {
            throw new MatrixException("matrix size isn't equal to required size" +
                    "\n matrix size: " + matrix.size() + "\t required size: " + size);
        }
    }

    public FixedSizeVector(T[] matrix, int size) {
        super(matrix);
        if (matrix.length != size) {
            throw new MatrixException("matrix size isn't equal to required size" +
                    "\n matrix size: " + matrix.length + "\t required size: " + size);
        }
    }

    public FixedSizeVector(int size, T value) {
        super(size, value);
    }
}
