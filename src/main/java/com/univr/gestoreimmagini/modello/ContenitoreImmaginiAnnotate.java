package com.univr.gestoreimmagini.modello;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import org.apache.commons.io.FilenameUtils;


public class ContenitoreImmaginiAnnotate extends ContenitoreRisorse<ImmagineAnnotata> {

    protected ContenitoreImmaginiAnnotate() {
        super();
    }

    public void addRisorsa(Image immagine, String nome, String extension){  //così posso creare un Tag usando solo la stringa del nome
        addRisorsa(new ImmagineAnnotata(immagine, nome, extension));
    }

    @Override
    protected void addToMemory(ImmagineAnnotata r) {

        String imagePath = resourcesDir.getPath() + "/" + r.toString();

        BufferedImage convertedImage = SwingFXUtils.fromFXImage(r.getImage(), null);
        try {
            ImageIO.write(convertedImage, r.getExtension(), new File(imagePath));  //Image di javafx non sappiamo come scriverla
        } catch(Exception e) {
            System.err.printf("\nError saving %s as file\n", imagePath);
        }

        updateMemory();
    }

    @Override
    protected void removeFromMemory(String fullName) {

        File imageFile = new File(resourcesDir.getPath() + "/" + fullName);

        imageFile.delete();

        updateMemory();
    }

    @Override
    protected void loadFromMemory(){

        super.loadFromMemory();

        for(String fileFullName: getNomiRisorse()){

            String name =  FilenameUtils.getBaseName(fileFullName);
            String extension = FilenameUtils.getExtension(fileFullName);

            String imagePath = resourcesDir.getPath() + "/" + fileFullName;

            Image immagine = null;

            try(FileInputStream stream = new FileInputStream(imagePath)) {
                immagine = new Image(stream);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            getRisorse().add(new ImmagineAnnotata(immagine, name, extension)); //devo aggiornare le liste senza chiamare addRisorsa visto che essa chiama addToMemory
        }
    }

    @Override
    public boolean nomeInLista(String nome) {
        for(String nomeRisorsa : getNomiRisorse()){   //cerca se c'è una risorsa con lo stesso nome
            if(nome.equals(FilenameUtils.getBaseName(nomeRisorsa)))  //Controllo se il nome è uguale togliendo l'estensione
                return true;
        }

        return false;
    }
}
