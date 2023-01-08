package com.cgvsu.math.matrix;


import com.cgvsu.math.vectors.Vector;
import com.cgvsu.math.vectors.Vector2;
import com.cgvsu.math.vectors.Vector3;
import com.cgvsu.math.vectors.Vector4;

import java.util.*;

import static com.cgvsu.math.matrix.MatrixUtils.*;

//import static matrix.MatrixUtils.transposition;

public class Matrix<T extends Number> {
    protected List<T> values;
    protected final int rows;
    protected final int cols;


    public Matrix(List<T> values, int rows, int cols) {
        this.values = values;
        this.rows = rows;
        this.cols = cols;
    }

    public Matrix(T[] values, int rows, int cols) {
        this(Arrays.asList(values), rows, cols);
    }

    //https://www.youtube.com/live/9yt4SiA7OiU?feature=share&t=5321
    public Matrix(int rows, int cols, T value) {
        this.rows = rows;
        this.cols = cols;
        values = new ArrayList<>();
        fill(value);
    }

    public Matrix(Matrix<T> matrix) {
        this(matrix.values, matrix.rows, matrix.cols);
    }

    private void fill(T number) {
        for (int i = 0; i < rows * cols; i++) {
            this.values.add(number);
        }

    }

    public List<T> getArray() {
        return values;
    }


    public void setElement(int row, int col, T value) {
        MatrixUtils.setElement(this.values, cols, row, col, value);
    }


//    public static <T extends Number> Matrix transposition(Matrix<T> matrix) {
//        List<T> matrix1F = matrix.getMatrix();
//        List<T> newMatrix = new float[matrix.getCols()];
//        for (int row = 0; row < matrix.getRows(); row++) {
//            for (int col = 0; col < matrix.getCols(); col++) {
//                setElement(newMatrix, matrix.getCols(), row, col, matrix.getElement(col, row));
//            }
//        }
//        return new Matrix(matrix.getMatrix(), matrix.getRows(), matrix.getCols());
//    }
//
//    public static Matrix<? extends Number> createIdentityMatrix(int rows, int cols) {
//        float[] newMatrix = new float[rows * cols];
//        for (int i = 0; i < cols; i++) {
//            newMatrix[cols * i + i] = 1;
//        }
//        return new Matrix<Number>(newMatrix, rows, cols);
//    }




    public List<T> getValues() {
        return values;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    //
    public T getElement(int row, int col) {
        return values.get(cols * row + col);
    }

    //
//    public Matrix<T> add(Matrix<T> matrix1) {
//        return addition(this, matrix1);
//    }
    public void add(Matrix<T> matrix) {
        Matrix<T> a = addition(this, matrix);
        values = a.values;
    }

    //? extends number
    public Matrix<T> sub(Matrix<T> matrix1) {
        return subtraction(this, matrix1);
    }

    public Matrix<T> mul(Matrix<T> matrix1) {
        return multiplication(this, matrix1);
    }

    public Matrix<T> mul(T c) {
        return multiplication(this, c);
    }


    public Matrix<T> transpose() {
        return transposition(this);
    }

    //utils

    public Matrix3<T> toMatrix3() {
        return new Matrix3<>(values);
    }

    public Matrix4<T> toMatrix4() {
        return new Matrix4<>(values);
    }

    public Vector<T> toVector() {
        return new Vector2<T>(values);
    }

    public Vector2<T> toVector2() {
        return new Vector2<>(values);
    }

    public Vector3<T> toVector3() {
        return new Vector3<>(values);
    }

    public Vector4<T> toVector4() {
        return new Vector4<>(values);
    }


    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix<?> matrix1 = (Matrix<?>) o;
        return rows == matrix1.rows && cols == matrix1.cols && Objects.equals(values, matrix1.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values, rows, cols);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                stringBuilder.append(getElement(row, col)).append(" ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();

    }
}
