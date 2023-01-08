package com.cgvsu.math;


import com.cgvsu.rasterization.MyPoint3D;
import com.cgvsu.render_engine.Camera;

import javax.vecmath.Vector3f;

public class SomeVectorMath {
    public static Vector3f getNormal(MyPoint3D p1, MyPoint3D p2, MyPoint3D p3) {
        Vector3f p1p2 = new Vector3f((float) (p2.getX() - p1.getX()), (float) (p2.getY() - p1.getY()), (float) (p2.getZ() - p1.getZ()));
        Vector3f p1p3 = new Vector3f((float) (p3.getX() - p1.getX()), (float) (p3.getY() - p1.getY()), (float) (p3.getZ() - p1.getZ()));
        double A = p1p2.y * p1p3.z - p1p2.z * p1p3.y;
        double B = -(p1p2.x * p1p3.z - p1p2.z * p1p3.x);
        double C = p1p2.x * p1p3.y - p1p2.y * p1p3.x;
        double normalLength = Math.sqrt(A * A + B * B + C * C);
        return new Vector3f((float) (A / normalLength), (float) (B / normalLength), (float) (C / normalLength));
    }

    public static double getZ(MyPoint3D p1, MyPoint3D p2, MyPoint3D p3, double x, double y) {
        Vector3f normal = getNormal(p1, p2, p3);
        return (-normal.x * (x - p1.getX()) - normal.y * (y - p1.getY())) / normal.z + p1.getZ();
    }

    public static double getCosLight(Camera camera, MyPoint3D p1, MyPoint3D p2, MyPoint3D p3) {
        Vector3f normal = getNormal(p1, p2, p3);
        Vector3f normalCamera = new Vector3f(camera.getTarget().x - camera.getPosition().x,
                camera.getTarget().y - camera.getPosition().y,
                camera.getTarget().z - camera.getPosition().z);
        Vector3f normalCameraN = new Vector3f(
                normalCamera.x / normalCamera.length(),
                normalCamera.y / normalCamera.length(),
                normalCamera.z / normalCamera.length());
        double numerator = normalCameraN.x * normal.x + normalCameraN.y * normal.y +
                normalCameraN.z * normal.z;

        double denominator = normal.length() * normalCameraN.length();
        return Math.abs(numerator / denominator);
    }
}