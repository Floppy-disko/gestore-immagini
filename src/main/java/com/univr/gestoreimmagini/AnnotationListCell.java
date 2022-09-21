package com.univr.gestoreimmagini;

import com.univr.gestoreimmagini.modello.Annotazione;
import com.univr.gestoreimmagini.modello.Tag;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AnnotationListCell extends ListCell<Annotazione> {

    @FXML
    private VBox content;
    @FXML
    private Label label;
    @FXML
    private Button button;
    @FXML
    private DialogPane value;

    public AnnotationListCell() {  //la cella sarà formata da un label ed un bottone per eliminarla
        super();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("annotationListCell.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    protected void updateItem(Annotazione item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) { // <== test for null item and empty parameter
            label.setText(item.getTag().toString());
            value.setContentText(item.getValue());
            button.setText("x");
            setPrefHeight(content.getMinHeight());
            //button.setId(item.toString() + "Button"); //setto un id al bottone così lo posso identificare
            button.getStyleClass().add("tagButton");
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }

    public void setButtonOnAction(EventHandler<ActionEvent> eventHandler) {
        button.setOnAction(eventHandler);
    }
}
