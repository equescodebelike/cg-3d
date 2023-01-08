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


    public static void main(String[] args) {
        Vector3<Double> v1 = new Vector3<>(10.0);
        Vector3<Double> v2 = new Vector3<>(1.0, 2.0, 3.0);
//        System.out.println(v2);
        v1 = v1.cross(v2);
        System.out.println(v1);
//        System.out.println(v2.transpose());

    }
}
