package com.cgvsu.objwriter;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.util.ArrayList;
import java.util.List;

public class ObjWriter {
    private static final String OBJ_VERTEX_TOKEN = "v";
    private static final String OBJ_TEXTURE_TOKEN = "vt";
    private static final String OBJ_NORMAL_TOKEN = "vn";
    private static final String OBJ_FACE_TOKEN = "f";

    public static ArrayList<String> write(Model mesh) {
        ArrayList<String> listFileContent = new ArrayList<>();

        writeVertices(mesh.getVertices(), listFileContent);
        listFileContent.add("# " + mesh.getVertices().size() + " vertices");

        writeTextureVertices(mesh.getTextureVertices(), listFileContent);
        listFileContent.add("# " + mesh.getTextureVertices().size() + " texture coordinates");

        writeNormals(mesh.getNormals(), listFileContent);
        listFileContent.add("# " + mesh.getNormals().size() + " vertices");

        writePolygons(mesh.getPolygons(), listFileContent);
        listFileContent.add("# " + mesh.getPolygons().size() + " polygons");
        return listFileContent;
    }

    protected static void writeVertices(final List<Vector3f> vertices, ArrayList<String> outListFileContent) { //try? exception?
        for (Vector3f vertex : vertices) {
            outListFileContent.add(OBJ_VERTEX_TOKEN + " " + vertex.getX()
                    + " " + vertex.getY() + " " + vertex.getZ());
        }

    }

    protected static void writeTextureVertices(final List<Vector2f> textureVertices, ArrayList<String> outListFileContent) {
        for (Vector2f textureVertex : textureVertices) {
            outListFileContent.add(OBJ_TEXTURE_TOKEN + " " + textureVertex.getX()
                    + " " + textureVertex.getY());
        }
    }

    protected static void writeNormals(final List<Vector3f> normals, ArrayList<String> outListFileContent) {
        for (Vector3f normal : normals) {
            outListFileContent.add(OBJ_NORMAL_TOKEN + " " + normal.getX()
                    + " " + normal.getY() + " " + normal.getZ());
        }
    }

    protected static void writePolygons(final List<Polygon> polygons, ArrayList<String> outListFileContent) {
        for (Polygon polygon : polygons) {
            writeOnePolygon(polygon, outListFileContent);
        }
    }

    protected static void writeOnePolygon(final Polygon polygon, List<String> outListFileContent) {

        // f 1/2/3 v/vt/vn

        StringBuilder strPolygon = new StringBuilder();
        strPolygon.append(OBJ_FACE_TOKEN + " ");

        try {
            if (polygon.getSizePolygonsTextureVertexIndices() == 0 &&
                    polygon.getSizePolygonsNormalIndices() == 0) {
                for (int j = 0; j < polygon.getSizePolygonsVertexIndices(); j++) {
                    strPolygon.append(polygon.getVertexIndices().get(j) + 1).append(" ");
                }
            } else if (polygon.getSizePolygonsTextureVertexIndices() != 0 &&
                    polygon.getSizePolygonsNormalIndices() == 0) {
                for (int j = 0; j < polygon.getSizePolygonsTextureVertexIndices(); j++) {
                    strPolygon.append(polygon.getVertexIndices().get(j) + 1).append("/").
                            append(polygon.getTextureVertexIndices().get(j) + 1).append(" ");
                }
            } else if (polygon.getSizePolygonsTextureVertexIndices() == 0 &&
                    polygon.getSizePolygonsNormalIndices() != 0) {
                for (int j = 0; j < polygon.getSizePolygonsNormalIndices(); j++) {
                    strPolygon.append(polygon.getVertexIndices().get(j) + 1).append("//").
                            append(polygon.getNormalIndices().get(j) + 1).append(" ");
                }
            } else {
                for (int j = 0; j < polygon.getSizePolygonsTextureVertexIndices(); j++) {
                    strPolygon.append(polygon.getVertexIndices().get(j) + 1).append("/").append(
                            polygon.getTextureVertexIndices().get(j) + 1).append("/").append(
                            polygon.getNormalIndices().get(j) + 1).append(" ");
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new ObjWriterExceptions("Too few vertex arguments");
        }
        outListFileContent.add(String.valueOf(strPolygon));
    }
}
