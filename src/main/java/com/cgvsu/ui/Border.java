package com.cgvsu.ui;

import javax.vecmath.Point2f;

public class Border {
    Point2f leftBottom;
    Point2f rightTop;

    public boolean isInBorder(Point2f p) {
        return p.x > leftBottom.x && p.x < rightTop.x && p.y > leftBottom.y && p.y < rightTop.y;
    }

    public Point2f getScale() {
        return leftBottom;
    }

    public double getWidth() {
        return rightTop.x - leftBottom.x;
    }

    public double getHeight() {
        return rightTop.y - leftBottom.y;
    }

    public void setScale(Point2f leftBottom) {
        this.leftBottom = leftBottom;
    }

    public void setRightTop(Point2f rightTop) {
        this.rightTop = rightTop;
    }
}
