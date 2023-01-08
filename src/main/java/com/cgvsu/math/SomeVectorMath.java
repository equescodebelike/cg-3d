package com.cgvsu.math;


import com.cgvsu.rasterization.MyPoint3D;
import com.cgvsu.render_engine.Camera;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

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

    public static double getCosLight(Camera camera, MyPoint3D p1, MyPoint3D p2, MyPoint3D p3) {
        Vector3d normal = getNormal(p1, p2, p3);
        Vector3d normalCamera = new Vector3d(camera.getTarget().getX() - camera.getPosition().getX(),
                camera.getTarget().getY() - camera.getPosition().getY(),
                camera.getTarget().getZ() - camera.getPosition().getZ());
        Vector3d normalCameraN = new Vector3d(
                normalCamera.x / normalCamera.length(),
                normalCamera.y / normalCamera.length(),
                normalCamera.z / normalCamera.length());
        double numerator = normalCameraN.x * normal.x + normalCameraN.y * normal.y +
                normalCameraN.z * normal.z;

        double denominator = normal.length() * normalCameraN.length();
        return Math.abs(numerator / denominator);
    }
}
