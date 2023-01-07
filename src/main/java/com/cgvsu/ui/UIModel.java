package com.cgvsu.ui;

import com.cgvsu.model.ChangedModel;
import com.cgvsu.model.Model;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.vecmath.Point2f;
import java.util.Objects;

public class UIModel{

    Border border;

    ChangedModel model;

    public UIModel(ChangedModel changedModel) {
        model = changedModel;
        border  = new Border();
    }

    public void setSize(Point2f minPoint2f, Point2f maxPoint2f){
           border.setScale(minPoint2f);
           border.setRightTop(maxPoint2f);
    }

    public Border getBorder() {
        return border;
    }

    public ChangedModel getModel() {
        return model;
    }

    public void setMinPoint2f(Point2f minPoint2f) {
//        this.minPoint2f = minPoint2f;
    }

    public void setMaxPoint2f(Point2f maxPoint2f) {
//        this.maxPoint2f = maxPoint2f;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UIModel uiModel = (UIModel) o;
        return Objects.equals(border, uiModel.border) && Objects.equals(model, uiModel.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(border, model);
    }
}
