package com.cgvsu.rasterization;

import java.util.Objects;

public class MyPoint3D {
    private double x;
    private double y;
    private double z;

    public MyPoint3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyPoint3D myPoint3D = (MyPoint3D) o;
        return Double.compare(myPoint3D.x, x) == 0 && Double.compare(myPoint3D.y, y) == 0 && Double.compare(myPoint3D.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }


    @Override
    public String toString() {
        return "MyPoint2D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
