package com.cgvsu.math;

import com.cgvsu.math.vectors.Vector3;
import com.cgvsu.math.vectors.vectorFloat.Vector3f;
import com.cgvsu.render_engine.Camera;
import javafx.collections.ObservableList;

import static com.cgvsu.math.matrix.MatrixUtils.multiplyToFloat;
import static com.cgvsu.math.matrix.MatrixUtils.sumToFloat;


public class Utils {

    private static Vector3f cross(Vector3f v1, Vector3f v2) {
        float x = sumToFloat(
                multiplyToFloat(v1.getY(), v2.getZ()),
                -multiplyToFloat(v1.getZ(), v2.getY())
        );
        float y = sumToFloat(
                -multiplyToFloat(v1.getX(), v2.getZ()),
                multiplyToFloat(v1.getZ(), v2.getX())
        );
        float z = sumToFloat(
                multiplyToFloat(v1.getX(), v2.getY()),
                -multiplyToFloat(v1.getY(), v2.getX())
        );
        return new Vector3f(x, y, z);
    }
    public static Vector3f getNormal(Vector3f p1, Vector3f p2, Vector3f p3) {
        Vector3f p1p2 = new Vector3f(p2.getX() - p1.getX(), p2.getY() - p1.getY(), p2.getZ() - p1.getZ());
        Vector3f p1p3 = new Vector3f(p3.getX() - p1.getX(), p3.getY() - p1.getY(), p3.getZ() - p1.getZ());
        Vector3f vector3f = cross(p1p2, p1p3);
        return vector3f;
    }

    public static double getZ(Vector3f p1, Vector3f p2, Vector3f p3, double x, double y) {
        Vector3f normal = getNormal(p1, p2, p3);
        return (-normal.getX() * (x - p1.getX()) - normal.getY() * (y - p1.getY())) / normal.getZ() + p1.getZ();
    }

    public static double getCosLight(Camera camera, Vector3f p1, Vector3f p2, Vector3f p3) {
        Vector3f normal = getNormal(p1, p2, p3);
        normal.normalize();
        Vector3f target = Utils.getNormalizedVector(Utils.minus(new Vector3f(camera.getPosition().x,camera.getPosition().y,camera.getPosition().z) ,new Vector3f(camera.getTarget().x,camera.getTarget().y,camera.getTarget().z)));
        return Math.abs(dotProduct(normal, target));
    }

    public static Vector3f getNormalizedVector(Vector3f vector) {
        float length = (float)Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY() + vector.getZ() * vector.getZ());
        if (length != 0)
            return new Vector3f(vector.getX() / length, vector.getY() / length, vector.getZ() / length);
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
        return vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY() + vector1.getZ() * vector2.getZ();
    }

    public static Vector3f minus(Vector3f vector1 , Vector3f vector2){
        return new Vector3f(vector2.getX() - vector1.getX() , vector2.getY() - vector1.getY(), vector2.getZ() - vector1.getZ());
    }
}