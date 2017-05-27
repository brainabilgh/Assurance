package com.journaldev.customlistview;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class Post {
    Vehicule vehicule1;

    String date;
    String lieu;
    String infos;
    String type;
    Vehicule vehicule2;
    float mantant;
    String nomImage;
    String nomVideo;

    public Post(){

    }

    public Post(Vehicule vehicule1, String date, String lieu, String infos, String type, Vehicule vehicule2, float mantant, String nomImage, String nomVideo) {
        this.vehicule1 = vehicule1;
        this.date = date;
        this.lieu = lieu;
        this.infos = infos;
        this.nomImage = nomImage;
        this.nomVideo = nomVideo;
        this.type = type;
        this.vehicule2 = vehicule2;
        this.mantant = mantant;
    }
}