package com.cgvsu.ui;

import javafx.scene.shape.Rectangle;

import javax.vecmath.Point2f;

public class MyRectangle extends Rectangle {
    public MyRectangle(Point2f minP, Point2f maxP) {
        super(minP.x, minP.y, maxP.x- minP.x, maxP.y- minP.y);
    }

    public MyRectangle() {
    }
}
