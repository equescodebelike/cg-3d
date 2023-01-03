package com.cgvsu.model;

import java.util.ArrayList;
import java.util.List;

public class Polygon {

    private List<Integer> vertexIndices;
    private List<Integer> textureVertexIndices;
    private List<Integer> normalIndices;

    public Polygon(final List<Integer> vertexIndices, final List<Integer> textureVertexIndices, final List<Integer> normalIndices) {
        this.vertexIndices = vertexIndices;
        this.textureVertexIndices = textureVertexIndices;
        this.normalIndices = normalIndices;
    }

    public Polygon() {
        vertexIndices = new ArrayList<>();
        textureVertexIndices = new ArrayList<>();
        normalIndices = new ArrayList<>();
    }


    public void addVertexIndex(final int vertexIndex) {
        this.vertexIndices.add(vertexIndex);
    }

    public void addTextureVertexIndex(final int textureVertexIndex) {
        this.textureVertexIndices.add(textureVertexIndex);
    }

    public void addNormalIndex(final int normalIndex) {
        this.normalIndices.add(normalIndex);
    }

    public void setVertexIndices(ArrayList<Integer> vertexIndices) {
        assert vertexIndices.size() >= 3;
        this.vertexIndices = vertexIndices;
    }

    public void setTextureVertexIndices(ArrayList<Integer> textureVertexIndices) {
        assert textureVertexIndices.size() >= 3;
        this.textureVertexIndices = textureVertexIndices;
    }

    public void setNormalIndices(ArrayList<Integer> normalIndices) {
        assert normalIndices.size() >= 3;
        this.normalIndices = normalIndices;
    }

    public int getSizePolygonsVertexIndices() {
        return vertexIndices.size();
    }

    public int getSizePolygonsTextureVertexIndices() {
        return textureVertexIndices.size();
    }

    public int getSizePolygonsNormalIndices() {
        return normalIndices.size();
    }

    public List<Integer> getVertexIndices() {
        return vertexIndices;
    }

    public List<Integer> getTextureVertexIndices() {
        return textureVertexIndices;
    }

    public List<Integer> getNormalIndices() {
        return normalIndices;
    }
}
