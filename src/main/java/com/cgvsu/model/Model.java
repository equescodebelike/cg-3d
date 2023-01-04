package com.cgvsu.model;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.objreader.ObjReaderExceptions;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private List<Vector3f> vertices;
    private List<Vector2f> textureVertices;
    private List<Vector3f> normals;
    private List<Polygon> polygons;
    private List<Polygon> trianglePolygons;

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
}
