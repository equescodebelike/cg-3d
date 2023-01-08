package com.cgvsu.objwriter;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Polygon;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ObjWriterTest {

    @Test
    void writeVertices1() {
        Vector3f v1 = new Vector3f(1.01f, 2.02f, 3.03f);
        ArrayList<Vector3f> list1 = new ArrayList<>(List.of(v1));
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> expected = new ArrayList<>(List.of("v" + " " + "1.01" + " " + "2.02" + " " + "3.03"));
        ObjWriter.writeVertices(list1, result);
        assertEquals(expected, result);
    }

    @Test
    void writeVertices2() {
        Vector3f v1 = new Vector3f(1.01f, 2.02f, 3.03f);
        ArrayList<Vector3f> list1 = new ArrayList<>(List.of(v1));
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> expected = new ArrayList<>(List.of("v" + " " + "1.02" + " " + "2.03" + " " + "3.04"));
        ObjWriter.writeVertices(list1, result);
        assertNotEquals(expected, result);
    }


    @Test
    void writeTextureVertices1() {
        Vector2f v1 = new Vector2f(1.01f, 2.02f);
        ArrayList<Vector2f> list1 = new ArrayList<>(List.of(v1));
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> expected = new ArrayList<>(List.of("vt" + " " + "1.01" + " " + "2.02"));
        ObjWriter.writeTextureVertices(list1, result);
        assertEquals(expected, result);
    }

    @Test
    void writeTextureVertices2() {
        Vector2f v1 = new Vector2f(1.02f, 2.03f);
        ArrayList<Vector2f> list1 = new ArrayList<>(List.of(v1));
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> expected = new ArrayList<>(List.of("vt" + " " + "1.01" + " " + "2.02"));
        ObjWriter.writeTextureVertices(list1, result);
        assertNotEquals(expected, result);
    }

    @Test
    void writeNormals1() {
        Vector3f v1 = new Vector3f(1.01f, 2.02f, 3.03f);
        ArrayList<Vector3f> list1 = new ArrayList<>(List.of(v1));
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> expected = new ArrayList<>(List.of("vn" + " " + "1.01" + " " + "2.02" + " " + "3.03"));
        ObjWriter.writeNormals(list1, result);
        assertEquals(expected, result);
    }

    @Test
    void writeNormals2() {
        Vector3f v1 = new Vector3f(1.01f, 2.02f, 3.03f);
        ArrayList<Vector3f> list1 = new ArrayList<>(List.of(v1));
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> expected = new ArrayList<>(List.of("vn" + " " + "1.02" + " " + "2.03" + " " + "3.03"));
        ObjWriter.writeNormals(list1, result);
        assertNotEquals(expected, result);
    }

    @Test
    void writePolygons1() {
        ArrayList<Polygon> polygons = new ArrayList<>();
        Polygon p1 = new Polygon();
        p1.addVertexIndex(0);
        p1.addVertexIndex(1);
        p1.addVertexIndex(2);
        polygons.add(p1);

        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> expected = new ArrayList<>(List.of("f" + " " + "1" + " " + "2" + " " + "3" + " "));
        ObjWriter.writePolygons(polygons, result);
        assertEquals(expected, result);
    }

    @Test
    void writePolygons2() {
        ArrayList<Polygon> polygons = new ArrayList<>();
        Polygon p1 = new Polygon();
        p1.addVertexIndex(0);
        p1.addVertexIndex(1);
        p1.addVertexIndex(2);
        p1.addNormalIndex(0);
        p1.addNormalIndex(1);
        p1.addNormalIndex(2);
        polygons.add(p1);

        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> expected = new ArrayList<>(List.of("f" + " " + "1//1" + " " + "2//2" + " " + "3//3" + " "));
        ObjWriter.writePolygons(polygons, result);
        assertEquals(expected, result);
    }

    @Test
    void writePolygons3() {
        ArrayList<Polygon> polygons = new ArrayList<>();
        Polygon p1 = new Polygon();
        p1.addVertexIndex(0);
        p1.addVertexIndex(1);
        p1.addVertexIndex(2);
        p1.addNormalIndex(0);
        p1.addNormalIndex(1);
        p1.addNormalIndex(2);
        p1.addTextureVertexIndex(0);
        p1.addTextureVertexIndex(1);
        p1.addTextureVertexIndex(2);
        polygons.add(p1);

        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> expected = new ArrayList<>(List.of("f" + " " + "1/1/1" + " " + "2/2/2" + " " + "3/3/3" + " "));
        ObjWriter.writePolygons(polygons, result);
        assertEquals(expected, result);
    }

    @Test
    void writePolygons4() {
        ArrayList<Polygon> polygons = new ArrayList<>();
        Polygon p1 = new Polygon();
        p1.addVertexIndex(0);
        p1.addVertexIndex(1);
        p1.addVertexIndex(2);
        p1.addTextureVertexIndex(0);
        p1.addTextureVertexIndex(1);
        p1.addTextureVertexIndex(2);
        polygons.add(p1);

        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> expected = new ArrayList<>(List.of("f" + " " + "1/1" + " " + "2/2" + " " + "3/3" + " "));
        ObjWriter.writePolygons(polygons, result);
        assertEquals(expected, result);
    }
}