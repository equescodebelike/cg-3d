package com.cgvsu.objwriter;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.util.ArrayList;

public class ObjWriter {
    private static final String OBJ_VERTEX_TOKEN = "v";
    private static final String OBJ_TEXTURE_TOKEN = "vt";
    private static final String OBJ_NORMAL_TOKEN = "vn";
    private static final String OBJ_FACE_TOKEN = "f";

    public static ArrayList<String> write(Model mesh) {
        ArrayList<String> listFileContent = new ArrayList<>();

        writeVertices(mesh.vertices, listFileContent);
        listFileContent.add("# " + mesh.vertices.size() + " vertices");

        writeTextureVertices(mesh.textureVertices, listFileContent);
        listFileContent.add("# " + mesh.textureVertices.size() + " texture coordinates");

        writeNormals(mesh.normals, listFileContent);
        listFileContent.add("# " + mesh.normals.size() + " vertices");

        writePolygons(mesh.polygons, listFileContent);
        listFileContent.add("# " + mesh.polygons.size() + " polygons");
        return listFileContent;
    }

    protected static void writeVertices(final ArrayList<Vector3f> vertices, ArrayList<String> outListFileContent) { //try? exception?
        for (Vector3f vertex : vertices) {
            outListFileContent.add(OBJ_VERTEX_TOKEN + " " + vertex.getX()
                    + " " + vertex.getY() + " " + vertex.getZ());
        }

    }

    protected static void writeTextureVertices(final ArrayList<Vector2f> textureVertices, ArrayList<String> outListFileContent) {
        for (Vector2f textureVertex : textureVertices) {
            outListFileContent.add(OBJ_TEXTURE_TOKEN + " " + textureVertex.getX()
                    + " " + textureVertex.getY());
        }
    }

    protected static void writeNormals(final ArrayList<Vector3f> normals, ArrayList<String> outListFileContent) {
        for (Vector3f normal : normals) {
            outListFileContent.add(OBJ_NORMAL_TOKEN + " " + normal.getX()
                    + " " + normal.getY() + " " + normal.getZ());
        }
    }

    protected static void writePolygons(final ArrayList<Polygon> polygons, ArrayList<String> outListFileContent) {
        for (int i = 0; i < polygons.size(); i++) {
            writeOnePolygon(polygons, i, outListFileContent);
        }
    }

    protected static void writeOnePolygon(final ArrayList<Polygon> polygons, int i, ArrayList<String> outListFileContent) {

        // f 1/2/3 v/vt/vn

        StringBuilder strPolygon = new StringBuilder();
        strPolygon.append(OBJ_FACE_TOKEN + " ");

        try {
            if (polygons.get(i).getSizePolygonsTextureVertexIndices() == 0 &&
                    polygons.get(i).getSizePolygonsNormalIndices() == 0) {
                for (int j = 0; j < polygons.get(i).getSizePolygonsVertexIndices(); j++) {
                    strPolygon.append(polygons.get(i).getVertexIndices().get(j) + 1).append(" ");
                }
            } else if (polygons.get(i).getSizePolygonsTextureVertexIndices() != 0 &&
                    polygons.get(i).getSizePolygonsNormalIndices() == 0) {
                for (int j = 0; j < polygons.get(i).getSizePolygonsTextureVertexIndices(); j++) {
                    strPolygon.append(polygons.get(i).getVertexIndices().get(j) + 1).append("/").
                            append(polygons.get(i).getTextureVertexIndices().get(j) + 1).append(" ");
                }
            } else if (polygons.get(i).getSizePolygonsTextureVertexIndices() == 0 &&
                    polygons.get(i).getSizePolygonsNormalIndices() != 0) {
                for (int j = 0; j < polygons.get(i).getSizePolygonsNormalIndices(); j++) {
                    strPolygon.append(polygons.get(i).getVertexIndices().get(j) + 1).append("//").
                            append(polygons.get(i).getNormalIndices().get(j) + 1).append(" ");
                }
            } else {
                for (int j = 0; j < polygons.get(i).getSizePolygonsTextureVertexIndices(); j++) {
                    strPolygon.append(polygons.get(i).getVertexIndices().get(j) + 1).append("/").append(
                            polygons.get(i).getTextureVertexIndices().get(j) + 1).append("/").append(
                            polygons.get(i).getNormalIndices().get(j) + 1).append(" ");
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new ObjWriterException("Too few vertex arguments");
        }
        outListFileContent.add(String.valueOf(strPolygon));
    }
}
