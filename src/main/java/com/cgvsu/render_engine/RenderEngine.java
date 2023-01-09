package com.cgvsu.render_engine;

import com.cgvsu.math.vectors.vectorFloat.Vector3f;
import com.cgvsu.model.ChangedModel;
import com.cgvsu.rasterization.DrawUtilsJavaFX;
import com.cgvsu.rasterization.GraphicsUtils;
import com.cgvsu.rasterization.MyColor;
import com.cgvsu.rasterization.Rasterization;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point2f;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.cgvsu.render_engine.GraphicConveyor.*;
import static java.lang.Math.PI;
import static java.lang.Math.sin;

public class RenderEngine {

    static int velocity = 0;

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final ChangedModel mesh,
            final int width,
            final int height) throws IOException {
        Matrix4f modelMatrix = rotateScaleTranslate(mesh.getRotate(),
                mesh.getScale(),
                mesh.getTranslate());

        if (true) {
            float vel = (float) sin((velocity++)*PI/180)*20;
            javax.vecmath.Vector3f translateVector = new javax.vecmath.Vector3f(mesh.getTranslate().x, mesh.getTranslate().y+vel, mesh.getTranslate().z);
            modelMatrix = rotateScaleTranslate(mesh.getRotate(),
                    mesh.getScale(),
                    translateVector);
        }
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        Matrix4f modelViewProjectionMatrix = new Matrix4f(modelMatrix);
        modelViewProjectionMatrix.mul(viewMatrix);
        modelViewProjectionMatrix.mul(projectionMatrix);

        final int nPolygons = mesh.getPolygons().size();
        Double[][] zBuffer = new Double[width][height];
        Double[][] zBufferForPolygonalGrid = new Double[width][height];

        Point2f minPoint = new Point2f(10000, 10000);
        Point2f maxPoint = new Point2f();

        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = mesh.getPolygons().get(polygonInd).getVertexIndices().size();
            List<Double> pointsZ = new ArrayList<>();

            ArrayList<Point2f> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                com.cgvsu.math.Vector3f vertex = mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(vertexInPolygonInd));

                javax.vecmath.Vector3f vertexVecmath = new javax.vecmath.Vector3f(vertex.getX(), vertex.getY(), vertex.getZ());
                pointsZ.add((double) vertex.getZ());

                Point2f resultPoint = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertexVecmath), width, height);
                //вот здесь можно находить максимальную и минимальную координату
                //мб придумаю что-нибудь потом другое, но пока так
                if (minPoint.x > resultPoint.x) {
                    minPoint.x = resultPoint.x;
                }
                if (minPoint.y > resultPoint.y) {
                    minPoint.y = resultPoint.y;
                }
                if (maxPoint.x < resultPoint.x) {
                    maxPoint.x = resultPoint.x;
                }
                if (maxPoint.y < resultPoint.y) {
                    maxPoint.y = resultPoint.y;
                }
                resultPoints.add(resultPoint);
            }
            GraphicsUtils gr = new DrawUtilsJavaFX(graphicsContext.getCanvas());

            if (mesh.isGridLoaded()) {
                for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                        graphicsContext.strokeLine(
                                resultPoints.get(vertexInPolygonInd - 1).x,
                                resultPoints.get(vertexInPolygonInd - 1).y,
                                resultPoints.get(vertexInPolygonInd).x,
                                resultPoints.get(vertexInPolygonInd).y);
                }
                if (nVerticesInPolygon > 0 ) {
                    graphicsContext.strokeLine(
                            resultPoints.get(nVerticesInPolygon - 1).x,
                            resultPoints.get(nVerticesInPolygon - 1).y,
                            resultPoints.get(0).x,
                            resultPoints.get(0).y);
                }
            }

            if (mesh.isRasterized() && !mesh.isTextureLoaded()) {
                Vector3f vector3f1 = new Vector3f(mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(0)).getX(),mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(0)).getY(),mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(0)).getZ());
                Vector3f vector3f2 = new Vector3f(mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(1)).getX(),mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(1)).getY(),mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(1)).getZ());
                Vector3f vector3f3 = new Vector3f(mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(2)).getX(),mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(2)).getY(),mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(2)).getZ());
                Rasterization.fillTriangle(gr, new Vector3f(resultPoints.get(0).x, resultPoints.get(0).y, pointsZ.get(0).floatValue()),
                        new Vector3f(resultPoints.get(1).x, resultPoints.get(1).y, pointsZ.get(1).floatValue()),
                        new Vector3f(resultPoints.get(2).x, resultPoints.get(2).y, pointsZ.get(2).floatValue()),
                        new MyColor(1, 0, 0),
                        new MyColor(0, 1, 0),
                        new MyColor(0, 0, 1),
                        zBuffer, camera,
                        vector3f1,vector3f2,vector3f3,
                        mesh);
            }
            else if (mesh.isTextureLoaded()) {
                Vector3f vector3f1 = new Vector3f(mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(0)).getX(),mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(0)).getY(),mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(0)).getZ());
                Vector3f vector3f2 = new Vector3f(mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(1)).getX(),mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(1)).getY(),mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(1)).getZ());
                Vector3f vector3f3 = new Vector3f(mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(2)).getX(),mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(2)).getY(),mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(2)).getZ());
                    Rasterization.fillTriangleWithTexture(gr,
                            resultPoints.get(0).x, resultPoints.get(0).y, pointsZ.get(0),
                            resultPoints.get(1).x, resultPoints.get(1).y, pointsZ.get(1),
                            resultPoints.get(2).x, resultPoints.get(2).y, pointsZ.get(2),
                            MyColor.RED, MyColor.RED, MyColor.RED, zBuffer, camera, mesh.image,
                            mesh.getTextureVertices().get(mesh.getPolygons().get(polygonInd).getTextureVertexIndices().get(0)),
                            mesh.getTextureVertices().get(mesh.getPolygons().get(polygonInd).getTextureVertexIndices().get(1)),
                            mesh.getTextureVertices().get(mesh.getPolygons().get(polygonInd).getTextureVertexIndices().get(2)),
                            vector3f1,
                            vector3f2,
                            vector3f3, mesh);
            }
        }

        mesh.setMinPoint2f(minPoint);
        mesh.setMaxPoint2f(maxPoint);
    }
}