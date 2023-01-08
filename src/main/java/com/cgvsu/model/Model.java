package com.cgvsu.model;

import com.cgvsu.objreader.ObjReaderExceptions;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;

import javax.vecmath.Point2f;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Model {

    private List<Vector3f> vertices;
    private List<Vector2f> textureVertices;
    private List<Vector3f> normals;
    private List<Polygon> polygons;
    private List<Polygon> trianglePolygons;

    private String name;

    private Point2f minPoint2f;
    private Point2f maxPoint2f;

    public Model(final List<Vector3f> vertices, final List<Vector2f> textureVertices, final List<Vector3f> normals, final List<Polygon> polygons, final List<Polygon> trianglePolygons) {
        this.vertices = vertices;
        this.textureVertices = textureVertices;
        this.normals = normals;
        this.polygons = polygons;
        this.trianglePolygons = trianglePolygons;
    }

    public Model(final List<Vector3f> vertices, final List<Vector2f> textureVertices, final List<Vector3f> normals, final List<Polygon> polygons) { // tests
        this.vertices = vertices;
        this.textureVertices = textureVertices;
        this.normals = normals;
        this.polygons = polygons;
    }

    public Model() {
        vertices = new ArrayList<>();
        textureVertices = new ArrayList<>();
        normals = new ArrayList<>();
        polygons = new ArrayList<>();
        trianglePolygons = new ArrayList<>();
    }

    public Model(Model model) {
        this(model.vertices, model.textureVertices, model.normals, model.polygons, model.trianglePolygons);
        setName(model.name);
    }


    public void setMinPoint2f(Point2f minPoint2f) {
        this.minPoint2f = minPoint2f;
    }

    public void setMaxPoint2f(Point2f maxPoint2f) {
        this.maxPoint2f = maxPoint2f;
    }

    public Point2f getMinPoint2f() {
        return minPoint2f;
    }

    public Point2f getMaxPoint2f() {
        return maxPoint2f;
    }

    public List<Polygon> getTrianglePolygons() {
        return trianglePolygons;
    }

    public void setTrianglePolygons(List<Polygon> trianglePolygons) {
        this.trianglePolygons = trianglePolygons;
    }

    public List<Vector3f> getVertices() {
        return vertices;
    }

    public List<Vector2f> getTextureVertices() {
        return textureVertices;
    }

    public List<Vector3f> getNormals() {
        return normals;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }

    public void setVertices(List<Vector3f> vertices) {
        this.vertices = vertices;
    }

    public void setTextureVertices(List<Vector2f> textureVertices) {
        this.textureVertices = textureVertices;
    }

    public void setNormals(List<Vector3f> normals) {
        this.normals = normals;
    }

    public void setPolygons(List<Polygon> polygons) {
        this.polygons = polygons;
    }

    public boolean checkConsistency() {
        for (int i = 0; i < polygons.size(); i++) {
            List<Integer> vertexIndices = polygons.get(i).getVertexIndices();
            List<Integer> textureVertexIndices = polygons.get(i).getTextureVertexIndices();
            List<Integer> normalIndices = polygons.get(i).getNormalIndices();
            if (vertexIndices.size() != textureVertexIndices.size()
                    && vertexIndices.size() != 0 && textureVertexIndices.size() != 0) {
                throw new ObjReaderExceptions.NotDefinedUniformFormatException(
                        "The unified format for specifying polygon descriptions is not defined.");
            }
            if (vertexIndices.size() != normalIndices.size()
                    && vertexIndices.size() != 0 && normalIndices.size() != 0) {
                throw new ObjReaderExceptions.NotDefinedUniformFormatException(
                        "The unified format for specifying polygon descriptions is not defined.");
            }
            if (normalIndices.size() != textureVertexIndices.size()
                    && normalIndices.size() != 0 && textureVertexIndices.size() != 0) {
                throw new ObjReaderExceptions.NotDefinedUniformFormatException(
                        "The unified format for specifying polygon descriptions is not defined.");
            }
            for (int j = 0; j < vertexIndices.size(); j++) {
                if (vertexIndices.get(j) >= vertices.size()) {
                    throw new ObjReaderExceptions.FaceException(
                            "Polygon description is wrong.", i + 1);
                }
            }
            for (int j = 0; j < textureVertexIndices.size(); j++) {
                if (textureVertexIndices.get(j) >= textureVertices.size()) {
                    throw new ObjReaderExceptions.FaceException(
                            "Polygon description is wrong.", i + 1);
                }
            }
            for (int j = 0; j < normalIndices.size(); j++) {
                if (normalIndices.get(j) >= normals.size()) {
                    throw new ObjReaderExceptions.FaceException(
                            "Polygon description is wrong.", i + 1);
                }
            }
        }
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Model model = (Model) o;
        return Objects.equals(vertices, model.vertices) && Objects.equals(textureVertices, model.textureVertices) && Objects.equals(normals, model.normals) && Objects.equals(polygons, model.polygons) && Objects.equals(trianglePolygons, model.trianglePolygons) && Objects.equals(minPoint2f, model.minPoint2f) && Objects.equals(maxPoint2f, model.maxPoint2f);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices, textureVertices, normals, polygons, trianglePolygons, minPoint2f, maxPoint2f);
    }

    @Override
    public String toString() {
        return name;
    }
}
