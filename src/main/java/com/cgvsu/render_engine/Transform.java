package com.cgvsu.render_engine;


import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import static java.lang.Math.*;

public class Transform {
    Matrix4f transform;

    public Transform() {
        float[] matrix = new float[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };
        transform = new Matrix4f(matrix);
    }


    public Matrix4f get() {
        return transform;
    }

    private void setTransform(final Vector3f rotateVector, final Vector3f scaleVector, final Vector3f translationVector) {
        rotate(rotateVector);
        scale(scaleVector);
        translate(rotateVector);
    }



    private void rotate(Vector3f rotateVector){
        //Angles GRAD -> RAD
        float rotateAngleXRAD = (float) ((rotateVector.x) * PI / 180);
        float rotateAngleYRAD = (float) ((rotateVector.y) * PI / 180);
        float rotateAngleZRAD = (float) ((rotateVector.z) * PI / 180);

        //Rotation
        //x
        transform.mul(new Matrix4f(new float[]{
                1, 0, 0, 0,
                0, (float) cos(rotateAngleXRAD), (float) sin(rotateAngleXRAD), 0,
                0, (float) -sin(rotateAngleXRAD), (float) cos(rotateAngleXRAD), 0,
                0, 0, 0, 1
        }));
        //y
        transform.mul(new Matrix4f(new float[]{
                (float) cos(rotateAngleYRAD), 0, (float) -sin(rotateAngleYRAD), 0,
                0, 1, 0, 0,
                (float) sin(rotateAngleYRAD), 0, (float) cos(rotateAngleYRAD), 0,
                0, 0, 0, 1
        }));
        //z
        transform.mul(new Matrix4f(new float[]{
                (float) cos(rotateAngleZRAD), (float) sin(rotateAngleZRAD), 0, 0,
                -(float) sin(rotateAngleZRAD), (float) cos(rotateAngleZRAD), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        }));
    }

    private void scale(Vector3f scaleVector){
        transform.m00 *= scaleVector.x;
        transform.m11 *= scaleVector.y;
        transform.m22 *= scaleVector.z;
    }
    private void translate(Vector3f translationVector){
        transform.m00 *= translationVector.x;
        transform.m11 *= translationVector.y;
        transform.m22 *= translationVector.z;
    }
}
