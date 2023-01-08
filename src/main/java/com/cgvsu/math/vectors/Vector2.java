package com.cgvsu.math.vectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Vector2<T extends Number> extends FixedSizeVector<T> {

    public Vector2(List<T> matrix) {
        super(matrix, matrix.size());
    }

    public Vector2(T x, T y) {
        super(new ArrayList<>(Arrays.asList(x, y)), 2);
    }

    public Vector2(T value) {
        super(2, value);
    }

    public T getX() {
        return values.get(0);
    }

    public T getY() {
        return values.get(1);
    }
}
