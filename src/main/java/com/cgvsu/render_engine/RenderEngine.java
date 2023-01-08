package com.cgvsu.render_engine;

import java.util.ArrayList;

import com.cgvsu.math.matrix.floatMatrix.Matrix4f;
import com.cgvsu.math.vectors.vectorFloat.Vector3f;
import com.cgvsu.model.ChangedModel;
import com.cgvsu.rasterization.DrawUtilsJavaFX;
import com.cgvsu.rasterization.GraphicsUtils;
import javafx.scene.canvas.GraphicsContext;


import javax.vecmath.Point2f;

import static com.cgvsu.render_engine.GraphicConveyor.*;

public class RenderEngine {

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final ChangedModel mesh,
            final int width,
            final int height) {
        Matrix4f modelMatrix = rotateScaleTranslate(mesh.getRotate(),
                mesh.getScale(),
                mesh.getTranslate());

        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        Matrix4f modelViewProjectionMatrix = new Matrix4f(modelMatrix);
        modelViewProjectionMatrix.mul(viewMatrix);
        modelViewProjectionMatrix.mul(projectionMatrix);


        final int nPolygons = mesh.getPolygons().size();

        Point2f minPoint = new Point2f(10000,10000);
        Point2f maxPoint = new Point2f();


        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = mesh.getPolygons().get(polygonInd).getVertexIndices().size();

            ArrayList<Point2f> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3f vertex = mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(vertexInPolygonInd));

                Vector3f vertexVecmath = new Vector3f(vertex.getX(), vertex.getY(), vertex.getZ());

                Point2f resultPoint = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertexVecmath), width, height);
                //вот здесь можно находить максимальную и минимальную координату
                //мб придумаю что-нибудь потом другое, но пока так
                if (minPoint.x > resultPoint.x){
                    minPoint.x = resultPoint.x;
                }
                if (minPoint.y > resultPoint.y){
                    minPoint.y = resultPoint.y;
                }
                if (maxPoint.x < resultPoint.x){
                    maxPoint.x = resultPoint.x;
                }
                if (maxPoint.y < resultPoint.y){
                    maxPoint.y = resultPoint.y;
                }
                resultPoints.add(resultPoint);
            }
            GraphicsUtils gr = new DrawUtilsJavaFX(graphicsContext.getCanvas());



            for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                graphicsContext.strokeLine(
                        resultPoints.get(vertexInPolygonInd - 1).x,
                        resultPoints.get(vertexInPolygonInd - 1).y,
                        resultPoints.get(vertexInPolygonInd).x,
                        resultPoints.get(vertexInPolygonInd).y);
                //Rasterization
//                if (vertexInPolygonInd + 1 < nVerticesInPolygon)
//                    Rasterization.fillTriangle(gr, resultPoints.get(vertexInPolygonInd - 1).x, resultPoints.get(vertexInPolygonInd - 1).y,
//                            resultPoints.get(vertexInPolygonInd).x, resultPoints.get(vertexInPolygonInd).y,
//                            resultPoints.get(vertexInPolygonInd + 1).x, resultPoints.get(vertexInPolygonInd + 1).y,
//                            MyColor.GREEN, MyColor.BLUE, MyColor.RED);
            }
            if (nVerticesInPolygon > 0) {
                graphicsContext.strokeLine(
                        resultPoints.get(nVerticesInPolygon - 1).x,
                        resultPoints.get(nVerticesInPolygon - 1).y,
                        resultPoints.get(0).x,
                        resultPoints.get(0).y);
            }
        }

        mesh.setMinPoint2f(minPoint);
        mesh.setMaxPoint2f(maxPoint);
    }
}