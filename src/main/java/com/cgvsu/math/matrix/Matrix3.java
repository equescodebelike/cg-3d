package com.cgvsu.math.matrix;

import java.util.ArrayList;
import java.util.Arrays;
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

    public static void main(String[] args) {
        Matrix<Integer> a = new Matrix<>(new ArrayList<>(Arrays.asList(
                1, 2,
                4, 5,
                7, 8
        )), 3, 2);
        Matrix<Integer> b = new Matrix<Integer>(new ArrayList<>(Arrays.asList(
                10, 20,
                30, 40
        )), 2, 2);

//        b = b.add(a);
        System.out.println(a.mul(b));
//        Matrix matrix3 = new Matrix(new float[]{},1,2);
    }


}
