package com.cgvsu.math;


import com.cgvsu.rasterization.MyPoint3D;

import javax.vecmath.Vector3d;

public class SomeVectorMath {

    public static double getZ(MyPoint3D p1, MyPoint3D p2, MyPoint3D p3, float x, float y) {
        Vector3d normal = getNormal(p1, p2, p3);
        return (-normal.x * (x - p1.getX()) - normal.y * (y - p1.getY())) / normal.z + p1.getZ();
    }

    public static Vector3d getNormal(MyPoint3D p1, MyPoint3D p2, MyPoint3D p3) {
        Vector3d p1p2 = new Vector3d(p2.getX() - p1.getX(), p2.getY() - p1.getY(), p2.getZ() - p1.getZ());
        Vector3d p1p3 = new Vector3d(p3.getX() - p1.getX(), p3.getY() - p1.getY(), p3.getZ() - p1.getZ());
        double A = p1p2.y * p1p3.z - p1p2.z * p1p3.y;
        double B = -(p1p2.x * p1p3.z - p1p2.z * p1p3.x);
        double C = p1p2.x * p1p3.y - p1p2.y * p1p3.x;
        double normalLength = Math.sqrt(A * A + B * B + C * C);
        return new Vector3d(A/normalLength, B/normalLength, C/normalLength);
    }
}
