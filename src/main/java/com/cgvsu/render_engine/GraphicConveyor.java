package com.cgvsu.render_engine;


import com.cgvsu.math.matrix.floatMatrix.Matrix4f;
import com.cgvsu.math.vectors.vectorFloat.Vector3f;
import javax.vecmath.Point2f;

import static java.lang.Math.*;

public class GraphicConveyor {

    public static Matrix4f rotateScaleTranslate(final Vector3f rotateVector, final Vector3f scaleVector, final Vector3f translationVector) {
        float[] matrix = new float[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };
        Matrix4f matrix4f = new Matrix4f(matrix);
        //Angles GRAD -> RAD
        float rotateAngleXRAD = (float) ((rotateVector.getX()) * PI / 180);
        float rotateAngleYRAD = (float) ((rotateVector.getY()) * PI / 180);
        float rotateAngleZRAD = (float) ((rotateVector.getZ()) * PI / 180);

        //Rotation
        //x
        matrix4f = new Matrix4f(matrix4f.mul(new Matrix4f(new float[]{
                1, 0, 0, 0,
                0, (float) cos(rotateAngleXRAD), (float) sin(rotateAngleXRAD), 0,
                0, (float) -sin(rotateAngleXRAD), (float) cos(rotateAngleXRAD), 0,
                0, 0, 0, 1
        })));
        //y
        matrix4f = new Matrix4f(matrix4f.mul(new Matrix4f(new float[]{
                (float) cos(rotateAngleYRAD), 0, (float) -sin(rotateAngleYRAD), 0,
                0, 1, 0, 0,
                (float) sin(rotateAngleYRAD), 0, (float) cos(rotateAngleYRAD), 0,
                0, 0, 0, 1
        })));
        //z
        matrix4f = new Matrix4f(matrix4f.mul(new Matrix4f(new float[]{
                (float) cos(rotateAngleZRAD), (float) sin(rotateAngleZRAD), 0, 0,
                -(float) sin(rotateAngleZRAD), (float) cos(rotateAngleZRAD), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        })));


        //Scaling
//        matrix4f.getElement(0,0) = 1F;
        matrix4f.setElement(0,0, matrix4f.getElement(0, 0) * scaleVector.getX());
        matrix4f.setElement(1,1, matrix4f.getElement(1, 1) * scaleVector.getY());
        matrix4f.setElement(2,2, matrix4f.getElement(2, 2) * scaleVector.getZ());
//        matrix4f.m00 *= scaleVector.x;
//        matrix4f.m11 *= scaleVector.y;
//        matrix4f.m22 *= scaleVector.z;
        //Translation
//        matrix4f.m30 = translationVector.x;
//        matrix4f.m31 = translationVector.y;
//        matrix4f.m32 = translationVector.z;
        matrix4f.setElement(3,0, translationVector.getX());
        matrix4f.setElement(3,1, translationVector.getY());
        matrix4f.setElement(3,2, translationVector.getZ());


        return matrix4f;
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target) {
        return lookAt(eye, target, new Vector3f(0F, 1.0F, 0F));
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target, Vector3f up) {
        Vector3f resultX = new Vector3f();
        Vector3f resultY = new Vector3f();
        Vector3f resultZ = new Vector3f();

        resultZ = new Vector3f(target.sub(eye).getValues());
        resultX = new Vector3f(up.cross(resultZ).getValues());
        resultY = new Vector3f(resultZ.cross(resultX).getValues());

        resultX.normalize();
        resultY.normalize();
        resultZ.normalize();

        float[] matrix = new float[]{
                resultX.getX(), resultY.getX(), resultZ.getX(), 0,
                resultX.getY(), resultY.getY(), resultZ.getY(), 0,
                resultX.getZ(), resultY.getZ(), resultZ.getZ(), 0,
                -resultX.dot(eye), -resultY.dot(eye), -resultZ.dot(eye), 1};
        return new Matrix4f(matrix);
    }

    public static Matrix4f perspective(
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        Matrix4f result = new Matrix4f();
        float tangentMinusOnDegree = (float) (1.0F / (Math.tan(fov * 0.5F)));
        result.setElement( 0,0,tangentMinusOnDegree / aspectRatio );
        result.setElement( 1,1,tangentMinusOnDegree );
        result.setElement( 2,2,(farPlane + nearPlane) / (farPlane - nearPlane) );
        result.setElement( 2,3,1.0F );
        result.setElement( 3,2,2 * (nearPlane * farPlane) / (nearPlane - farPlane) );

//        result.m00 = tangentMinusOnDegree / aspectRatio;
//        result.m11 = tangentMinusOnDegree;
//        result.m22 = (farPlane + nearPlane) / (farPlane - nearPlane);
//        result.m23 = 1.0F;
//        result.m32 = 2 * (nearPlane * farPlane) / (nearPlane - farPlane);


        return result;
    }

    public static Vector3f multiplyMatrix4ByVector3(final Matrix4f matrix, final Vector3f vertex) {
        final float x = (vertex.getX() * matrix.getElement(0,0)) + (vertex.getY() * matrix.getElement(1,0))
                + (vertex.getZ() * matrix.getElement(2,0)) + matrix.getElement(3,0);
        final float y = (vertex.getX() * matrix.getElement(0,1)) + (vertex.getY() * matrix.getElement(1,1))
                + (vertex.getZ() * matrix.getElement(2,1)) + matrix.getElement(3,1);
        final float z = (vertex.getX() * matrix.getElement(0,2)) + (vertex.getY() * matrix.getElement(1,2))
                + (vertex.getZ() * matrix.getElement(2,2)) + matrix.getElement(3,2);
        final float w = (vertex.getX() * matrix.getElement(0,3)) + (vertex.getY() * matrix.getElement(1,3))
                + (vertex.getZ() * matrix.getElement(2,3)) + matrix.getElement(3,3);
//        final float x = (vertex.x * matrix.m00) + (vertex.y * matrix.m10) + (vertex.z * matrix.m20) + matrix.m30;
//        final float y = (vertex.x * matrix.m01) + (vertex.y * matrix.m11) + (vertex.z * matrix.m21) + matrix.m31;
//        final float z = (vertex.x * matrix.m02) + (vertex.y * matrix.m12) + (vertex.z * matrix.m22) + matrix.m32;
//        final float w = (vertex.x * matrix.m03) + (vertex.y * matrix.m13) + (vertex.z * matrix.m23) + matrix.m33;
        return new Vector3f(x / w, y / w, z / w);
    }

    public static Point2f vertexToPoint(final Vector3f vertex, final int width, final int height) {
        return new Point2f(vertex.getX() * width + width / 2.0F, -vertex.getY() * height + height / 2.0F);
    }
}
