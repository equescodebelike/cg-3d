package com.cgvsu.render_engine;

import java.util.ArrayList;

import com.cgvsu.math.Vector3f;
import javafx.scene.canvas.GraphicsContext;
import javax.vecmath.*;
import com.cgvsu.model.Model;
import static com.cgvsu.render_engine.GraphicConveyor.*;

public class RenderEngine {

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height)
    {
        Matrix4f modelMatrix = rotateScaleTranslate();
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

                javax.vecmath.Vector3f vertexVecmath = new javax.vecmath.Vector3f(vertex.getX(), vertex.getY(), vertex.getZ());

                Point2f resultPoint = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertexVecmath), width, height);
                //вот здесь можно находить максимальную и минимальную координату
                //мб придумаю что-нибудь потом другое, но пока - так
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



            for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                graphicsContext.strokeLine(
                        resultPoints.get(vertexInPolygonInd - 1).x,
                        resultPoints.get(vertexInPolygonInd - 1).y,
                        resultPoints.get(vertexInPolygonInd).x,
                        resultPoints.get(vertexInPolygonInd).y);
            }

            if (nVerticesInPolygon > 0)
                graphicsContext.strokeLine(
                        resultPoints.get(nVerticesInPolygon - 1).x,
                        resultPoints.get(nVerticesInPolygon - 1).y,
                        resultPoints.get(0).x,
                        resultPoints.get(0).y);
        }

        mesh.setMinPoint2f(minPoint);
        mesh.setMaxPoint2f(maxPoint);
    }
}