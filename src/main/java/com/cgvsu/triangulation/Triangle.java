package com.cgvsu.triangulation;

import com.cgvsu.model.Polygon;

import java.util.ArrayList;
import java.util.List;

public class Triangle {

    public static ArrayList<Polygon> triangulatePolygon(List<Polygon> polygons) {
        ArrayList<Polygon> triangles = new ArrayList<>();
        for (Polygon polygon : polygons) {
            while (polygon.getVertexIndices().size() > 2) { // циклическая проверка для триангуляции

                Polygon triangle = new Polygon();

                // добавляем вершины в треугольник
                triangle.getVertexIndices().add(
                        polygon.getVertexIndices().get(0));
                triangle.getVertexIndices().add(
                        polygon.getVertexIndices().get(1));
                triangle.getVertexIndices().add(
                        polygon.getVertexIndices().get(2));


                if (polygon.getTextureVertexIndices().size() != 0) {
                    triangle.getTextureVertexIndices().add(
                            polygon.getTextureVertexIndices().get(0));
                    triangle.getTextureVertexIndices().add(
                            polygon.getTextureVertexIndices().get(1));
                    triangle.getTextureVertexIndices().add(
                            polygon.getTextureVertexIndices().get(2));
                }

                if (polygon.getNormalIndices().size() != 0) {
                    triangle.getNormalIndices().add(
                            polygon.getNormalIndices().get(0));
                    triangle.getNormalIndices().add(
                            polygon.getNormalIndices().get(1));
                    triangle.getNormalIndices().add(
                            polygon.getNormalIndices().get(2));
                }


                polygon.getVertexIndices().remove(1);
                triangles.add(triangle);
            }
        }
        return triangles;
    }
}