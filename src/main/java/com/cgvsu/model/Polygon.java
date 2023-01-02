package com.cgvsu.model;

import java.util.ArrayList;

public class Polygon {

    private ArrayList<Integer> vertexIndices;
    private ArrayList<Integer> textureVertexIndices;
    private ArrayList<Integer> normalIndices;


    public Polygon() {
        vertexIndices = new ArrayList<Integer>();
        textureVertexIndices = new ArrayList<Integer>();
        normalIndices = new ArrayList<Integer>();
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

    public ArrayList<Integer> getVertexIndices() {
        return vertexIndices;
    }

    public ArrayList<Integer> getTextureVertexIndices() {
        return textureVertexIndices;
    }

    public ArrayList<Integer> getNormalIndices() {
        return normalIndices;
    }
}
