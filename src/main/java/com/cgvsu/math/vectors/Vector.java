package com.cgvsu.math.vectors;


import com.cgvsu.math.matrix.Matrix;

import java.util.ArrayList;
import java.util.List;

import static com.cgvsu.math.matrix.MatrixUtils.multiplyToFloat;


public class Vector<T extends Number> extends Matrix<T> {

    public Vector(List<T> matrix) {
        super(matrix, 1, matrix.size());
    }

    public Vector(T[] matrix) {
        super(matrix, 1, matrix.length);
    }

    public Vector(int size, T value) {
        super(1, size, value);
    }

    public int size(){
        return values.size();
    }
    public static<T extends Number> float length(Vector<T> vec) {
        float temp = 0;
        for(int i = 0; i < vec.size(); i++) {
            temp += Math.pow(vec.values.get(i).doubleValue(), 2);
        }
        return (float) Math.sqrt(temp);
    }

    public static<T extends Number> Vector<T> normalize(Vector<T> vec) {
        List<T> values = new ArrayList<>();
        float length = length(vec);
        if(Double.compare(length,0.0) ==0){
            return new Vector<T>(vec.size(),(T) Float.valueOf(0));
        }
        for(int i = 0; i < vec.size(); i++) {
            values.add((T) Float.valueOf(vec.values.get(i).floatValue()/length));
        }
        return new Vector<T>(values);
    }

    public static<T extends Number> Vector<T> divideByScalar(Vector<T> vec, double s) {
        if (Double.compare(s,0) == 0){
            throw new VectorException("zero division error");
        }
        for (int i = 0; i < vec.size(); i++) {
            vec.values.set(i,(T) Double.valueOf(vec.values.get(i).doubleValue()/s));
        }
        return vec;
    }

    public static<T extends Number> T dot(Vector<T> vec1, Vector<T> vec2) throws VectorException {
        float scalar = 0;
        if(vec1.size() == vec2.size()) {
            for (int i = 0; i < vec1.size(); i++) {
                scalar += multiplyToFloat(vec1.values.get(i), vec2.values.get(i));
            }
        } else {
            throw new VectorException("vectors of different sizes");
        }
       return (T) Float.valueOf(scalar);
    }

    public static<T extends Number> void printVector(Vector<T> vector) {
        for(T i: vector.values) {
            System.out.print(i + " ");
        }
    }
    public T dot(Vector<T> vec){
        return dot(this,vec);
    }
    public void normalize(){
        Vector<T>  a = normalize(this);
        values = a.values;
    }
}
