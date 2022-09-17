package com.univr.gestoreimmagini;

import com.univr.gestoreimmagini.modello.Model;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WorkingImageController implements Initializable {

    @FXML
    private Button button;

    @FXML
    private ImageView mainImage;
    @FXML
    private ImageView leftImage;
    @FXML
    private ImageView rightImage;
    @FXML
    private Button leftButton;
    @FXML
    private Button rightButton;
    @FXML
    private ImageView fullImage;
    @FXML
    private ImageView zoomImage;
    @FXML
    private Button zoomin;
    @FXML
    private Button zoomout;

    private Model modello = Model.getModel();

    private SimpleIntegerProperty selectedImageIndex = new SimpleIntegerProperty();

    private Image voidImage;

    private SimpleObjectProperty<Rectangle2D> viewPort = new SimpleObjectProperty<>(this, "viewPort");

    private double zoomLevel = 1;  //quanto sono zoommatop
    private double offsetX = 0; //di quanto sono spostato a dx
    private double offsetY = 0; //di quanto sono spostato a sx

    public WorkingImageController(){
        try {
            voidImage = new Image(getClass().getResource("void.png").openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fullImage.imageProperty().bind(mainImage.imageProperty());
        zoomImage.imageProperty().bind(mainImage.imageProperty());

        zoomImage.viewportProperty().bind(viewPort);

        updateImages(0);

        selectedImageIndex.addListener(this::changed);

        zoomout.setDisable(true); //all'inizio l'immagine non può essere zoommata
    }

    private void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
        updateImages(newValue.intValue());
    }

    private void updateImages(int newValue){
        Image nextMainImage = getNextImage(newValue);
        Image nextLeftImage = getNextImage(getLeftIndex(newValue));
        Image nextRightImage = getNextImage(getRightIndex(newValue));

        mainImage.setImage(nextMainImage);

        resetViewPort();

        int size = modello.getImages().getSize();

        if(size==1) {  //Se ho solo un immagine mostro solo quella al centro
            leftButton.setDisable(true);
            leftImage.setImage(voidImage);

            rightButton.setDisable(true);
            rightImage.setImage(voidImage);
        }

        else if(size==2){
            if(newValue==1){  //se l'immagini principale è l'ultima della lista mostro solo l'immagine alla sua sinistra
                leftButton.setDisable(false);
                leftImage.setImage(nextLeftImage);

                rightButton.setDisable(true);
                rightImage.setImage(voidImage);
            }

            else{  //se invece è la prima della lista mostro solo quella alla sua destra
                leftButton.setDisable(true);
                leftImage.setImage(voidImage);

                rightButton.setDisable(false);
                rightImage.setImage(nextRightImage);
            }
        }

        else{
            leftImage.setImage(nextLeftImage);
            rightImage.setImage(nextRightImage);
        }

    }

    private void resetViewPort(){
        setViewPort(mainImage.getViewport());  //Resetto il viewPort quando cambio immagine
        zoomLevel = 1;
        applyZoom();
    }

    private Image getNextImage(int newValue){
        return modello.getImages().getRisorsa(newValue).getImage();
    }

    public int getSelectedImageIndex() {
        return selectedImageIndex.get();
    }

    public SimpleIntegerProperty selectedImageIndexProperty() {
        return selectedImageIndex;
    }

    public void setSelectedImageIndex(int selectedImageIndex) {
        this.selectedImageIndex.set(selectedImageIndex);
    }

    public Rectangle2D getViewPort() {
        return viewPort.get();
    }

    public SimpleObjectProperty<Rectangle2D> viewPortProperty() {
        return viewPort;
    }

    public void setViewPort(Rectangle2D viewPort) {
        this.viewPort.set(viewPort);
    }

    @FXML
    private void scrollRight(ActionEvent actionEvent) {
        setSelectedImageIndex(getRightIndex(getSelectedImageIndex()));
    }

    @FXML
    private void scrollLeft(ActionEvent actionEvent) {
        setSelectedImageIndex(getLeftIndex(getSelectedImageIndex()));
    }

    private int getRightIndex(int index){
        int size = modello.getImages().getSize();

        int rightIndex = (getSelectedImageIndex()+1) % size; //Aumento di uno ma se arrivo alla fine riparto dall'inizio (effetto PacMan)

        return rightIndex;
    }

    private int getLeftIndex(int index){
        int size = modello.getImages().getSize();

        int leftIndex = (index-1+size) % size;  //per evitare di avere resto negativo sommo sempre il valore del numero di immagini (size), così da rendere l'operazione modulo e non resto

        return leftIndex;
    }

    @FXML
    private void zoomIn(ActionEvent actionEvent) {

        if (zoomLevel <= 1) {
            zoomout.setDisable(false);  //Riattivo la possibilitò di dezoommare se aumento il valore onltre minZoom
        }

        zoomLevel += 0.2;

        applyZoom();

        if(zoomLevel >= 4)
            zoomin.setDisable(true);  //Se ho raggiunto maxZoom spengo il bottone
    }

    @FXML
    private void zoomOut(ActionEvent actionEvent) {

        if (zoomLevel >= 4) {
            zoomin.setDisable(false);  //Riattivo la possibilitò di zoommare se diminuisco il valore sotto a mazZoom
        }

        zoomLevel -= 0.2;

        applyZoom();

        if(zoomLevel <= 1)
            zoomout.setDisable(true);  //Se ho raggiunto minZoom spengo il bottone
    }

    private void applyZoom(){
        double newWidth = mainImage.getImage().getWidth()/zoomLevel;
        double newHeight = mainImage.getImage().getHeight()/zoomLevel;

        Rectangle2D newViewPort = new Rectangle2D(offsetX, offsetY, newWidth, newHeight);
        setViewPort(newViewPort);
    }

    @FXML
    private void switchToImageEditorView(ActionEvent actionEvent) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("ImageManagerView.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root, 1280, 720);
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Image editor");
        stage.setScene(scene);
        stage.show();

        if(modello.getImages().resourceFileExists(getSelectedImageIndex()) == false)
            modello.getImages().restoreImage(getSelectedImageIndex());
    }

}
