package com.cgvsu.math.vectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Vector4<T extends Number> extends FixedSizeVector<T> {
    public Vector4(List<T> matrix) {
        super(matrix, matrix.size());
    }

    public Vector4(T x, T y, T z, T w) {
        super(new ArrayList<>(Arrays.asList(x, y, z, w)), 4);
    }

    public Vector4(T value) {
        super(4, value);
    }
}
