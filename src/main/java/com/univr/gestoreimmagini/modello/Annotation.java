package com.univr.gestoreimmagini.modello;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.image.Image;

public class Annotation {

    private Model modello = Model.getModel();

    private Tag tag;

    private SimpleDoubleProperty X = new SimpleDoubleProperty();

    private SimpleDoubleProperty Y = new SimpleDoubleProperty();

    private SimpleDoubleProperty width = new SimpleDoubleProperty();

    private SimpleDoubleProperty height = new SimpleDoubleProperty();

    private SimpleIntegerProperty number = new SimpleIntegerProperty();

    private AnnotatedImage annotatedImage;

    private String value;

    public Annotation(AnnotatedImage annotatedImage, double X, double Y, double width, double height, Tag tag, String value){
        this.annotatedImage = annotatedImage;   //mantengo un riferimento a chi annoto
        this.X.set(X);
        this.Y.set(Y);
        this.width.set(width);
        this.height.set(height);

        this.tag = tag;
        this.value = value;

        this.X.addListener((c)-> {
            annotatedImage.updateMemory();
        });

        this.Y.addListener((c)-> {
            annotatedImage.updateMemory();
        });

        this.width.addListener((c)-> {
            annotatedImage.updateMemory();
        });

        this.height.addListener((c)-> {
            annotatedImage.updateMemory();
        });



        annotatedImage.getAnnotazioni().addListener((ListChangeListener<Annotation>) c -> {  //binding tra la lista di immagini nel modello e i figli di ImageGrid
            while (c.next()) {
                number.set(getIndexInList() + 1);
            }

        });
    }

    private int getIndexInList(){
        return annotatedImage.getAnnotazioni().indexOf(this);
    }

    public Image getImage(){
        return annotatedImage.getImage();
    }

    public double getX() {
        return X.get();
    }

    public SimpleDoubleProperty xProperty() {
        return X;
    }

    public void setX(double x) {
        this.X.set(x);
    }

    public double getY() {
        return Y.get();
    }

    public SimpleDoubleProperty yProperty() {
        return Y;
    }

    public void setY(double y) {
        this.Y.set(y);
    }

    public double getWidth() {
        return width.get();
    }

    public SimpleDoubleProperty widthProperty() {
        return width;
    }

    public void setWidth(double width) {
        this.width.set(width);
    }

    public double getHeight() {
        return height.get();
    }

    public SimpleDoubleProperty heightProperty() {
        return height;
    }

    public void setHeight(double height) {
        this.height.set(height);
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getNumber() {
        return number.get();
    }

    public SimpleIntegerProperty numberProperty() {
        return number;
    }

    public void setNumber(int number) {
        this.number.set(number);
    }
}
