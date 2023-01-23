package com.cgvsu.math;

import com.cgvsu.render_engine.Camera;
import javafx.collections.ObservableList;
import javax.vecmath.Vector3f;


public class Utils {

//    private static Vector3f cross(Vector3f v1, Vector3f v2) {
//        float x = sumToFloat(
//                multiplyToFloat(v1.y, v2.z),
//                -multiplyToFloat(v1.z, v2.y
//        );
//        float y = sumToFloat(
//                -multiplyToFloat(v1.x, v2.z),
//                multiplyToFloat(v1.z, v2.x)
//        );
//        float z =  v1.x + v2.z -(v1.z + v2.x);
//        return new Vector3f(x, y, z);
//    }
    public static Vector3f getNormal(Vector3f p1, Vector3f p2, Vector3f p3) {
        Vector3f p1p2 = new Vector3f(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
        Vector3f p1p3 = new Vector3f(p3.x - p1.x, p3.y - p1.y, p3.z - p1.z);
        p1p2.cross(p1p2,p1p3);
        return p1p2;
    }

    public static double getZ(Vector3f p1, Vector3f p2, Vector3f p3, double x, double y) {
        Vector3f normal = getNormal(p1, p2, p3);
        return (-normal.x * (x - p1.x) - normal.y * (y - p1.y)) / normal.z + p1.z;
    }

    public static double getCosLight(Camera camera, Vector3f p1, Vector3f p2, Vector3f p3) {
        Vector3f normal = getNormal(p1, p2, p3);
        normal.normalize();
        Vector3f target = Utils.getNormalizedVector(Utils.minus(new Vector3f(camera.getPositionLight().x,camera.getPositionLight().y,camera.getPositionLight().z) ,new Vector3f(camera.getTarget().x,camera.getTarget().y,camera.getTarget().z)));
        return Math.abs(dotProduct(normal, target));
    }

    public static Vector3f getNormalizedVector(Vector3f vector) {
        float length = (float)Math.sqrt(vector.x * vector.x + vector.y * vector.y + vector.z * vector.z);
        if (length != 0)
            return new Vector3f(vector.x / length, vector.y / length, vector.z / length);
        return new Vector3f(0,0,0);
    }

    public static String vector3ftoString(javax.vecmath.Vector3f vector3f) {
        return "x:" + String.format(String.valueOf(vector3f.x), 3) + " y:" +
                String.format(String.valueOf(vector3f.y), 3) + " z:" + String.format(String.valueOf(vector3f.z), 3);
    }

    public static void recalculateIndexes(ObservableList<Camera> cameras, ObservableList<String> listOfCameras) {
        for (int i = 0; i < cameras.size(); i++) {
            listOfCameras.add(i + Utils.vector3ftoString(cameras.get(i).getPosition()));
            listOfCameras.remove(0);
        }
    }

    public static double dotProduct(Vector3f vector1, Vector3f vector2) {
        return vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z;
    }

    public static Vector3f minus(Vector3f vector1 , Vector3f vector2){
        return new Vector3f(vector2.x - vector1.x , vector2.y - vector1.y, vector2.z - vector1.z);
    }
}