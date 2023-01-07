package com.cgvsu.ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.vecmath.Point2f;

public class UIModel{

    Border border;

    public UIModel() {
        border  = new Border();
    }

    public void setSize(Point2f minPoint2f, Point2f maxPoint2f){
           border.setScale(minPoint2f);
           border.setRightTop(maxPoint2f);
    }

    public Border getBorder() {
        return border;
    }


    public void setMinPoint2f(Point2f minPoint2f) {
//        this.minPoint2f = minPoint2f;
    }

    public void setMaxPoint2f(Point2f maxPoint2f) {
//        this.maxPoint2f = maxPoint2f;
    }
}
