package com.cgvsu.ui;

import com.cgvsu.math.Vector2f;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import javax.vecmath.Point2f;

public class UIModel extends StackPane{

    ObjectProperty<MyRectangle> myRectangle = new SimpleObjectProperty<>();

    private int x;
    private int y;
    private int width;
    private int height;


/*    public UIModel(Point2f minPoint2f, Point2f maxPoint2f) {
        setSize(minPoint2f,maxPoint2f);
//        MyRectangle shape = new Rectangle(x,y,width,height);
//        shape.setFill(Color.BLACK);
//        myRectangle.set(shape);
//        setShape(myRectangle.get());
    }*/

    public UIModel() {
        MyRectangle shape = new MyRectangle();
        shape.setFill(Color.BLACK);
        myRectangle.set(shape);
        setShape(myRectangle.get());

    }

    public void setSize(Point2f minPoint2f, Point2f maxPoint2f){
        this.x = (int) minPoint2f.x;
        this.y = (int) maxPoint2f.y;
        width = (int) (maxPoint2f.x - minPoint2f.x);
        height = (int) (maxPoint2f.y - minPoint2f.y);
        MyRectangle myRectangle = new MyRectangle(minPoint2f,maxPoint2f);
        myRectangle.setFill(Color.BLACK);
        this.myRectangle.set(myRectangle);
    }

    public MyRectangle getMyRectangle() {
        return myRectangle.get();
    }

    public ObjectProperty<MyRectangle> myRectangleProperty() {
        return myRectangle;
    }

    public void setMinPoint2f(Point2f minPoint2f) {
//        this.minPoint2f = minPoint2f;
    }

    public void setMaxPoint2f(Point2f maxPoint2f) {
//        this.maxPoint2f = maxPoint2f;
    }
}
