package com.journaldev.customlistview;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class Accident {
    //private int id;
    private Vehicule vehicule1;
    private String date;
    private String lieu;
    private String infos;
    private String type; // stationnement ou pas
    private Vehicule vehicule2;
    private float mantant;
    private String nomImage;
    private String nomVideo;
    private int etat=0;
    /*
    -2 refusé
    -1 ouvert
    0  envoyé
    1  traitement
    2  accepté
    */

    public Accident() {
    }

    public Accident(Vehicule vehicule1, String date, String lieu, String infos, String type, Vehicule vehicule2, float mantant, String nomImage, String nomVideo, int etat) {
        //this.id= id;
        this.vehicule1 = vehicule1;
        this.date = date;
        this.lieu = lieu;
        this.infos = infos;
        this.nomImage = nomImage;
        this.nomVideo = nomVideo;
        this.type = type;
        this.vehicule2 = vehicule2;
        this.mantant = mantant;
        this.etat = etat;
    }


    public Vehicule getVehicule1() {
        return vehicule1;
    }

    public void setVehicule1(Vehicule vehicule1) {
        this.vehicule1 = vehicule1;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getInfos() {
        return infos;
    }

    public void setInfos(String infos) {
        this.infos = infos;
    }

    public String getNomImage() {
        return nomImage;
    }

    public void setNomImage(String nomImage) {
        this.nomImage = nomImage;
    }

    public String getNomVideo() {
        return nomVideo;
    }

    public void setNomVideo(String nomVideo) {
        this.nomVideo = nomVideo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Vehicule getVehicule2() {
        return vehicule2;
    }

    public void setVehicule2(Vehicule vehicule2) {
        this.vehicule2 = vehicule2;
    }

    public float getMantant() {
        return mantant;
    }

    public void setMantant(float mantant) {
        this.mantant = mantant;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }
}
