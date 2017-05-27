package com.journaldev.customlistview;

import java.util.Date;

/**
 * Created by anupamchugh on 09/02/16.
 */
public class Vehicule {

    String matricule;
    String nomProprietaire;
    //String prenomProprietaire;
    String permis;
    String proprietaireNouveauPermis;
    String marque;
    String modele;

    public Vehicule() {
    }

    public Vehicule(String matricule, String nomProprietaire, /*String prenomProprietaire,*/ String permis, String proprietaireNouveauPermis, String marque, String modele) {
        this.matricule = matricule;
        this.nomProprietaire = nomProprietaire;
        //this.prenomProprietaire = prenomProprietaire;
        this.permis = permis;
        this.proprietaireNouveauPermis = proprietaireNouveauPermis;
        this.marque = marque;
        this.modele = modele;
    }

    public String getPermis() {
        return permis;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public void setPermis(String permis) {
        this.permis = permis;
    }

    public String getMatricule() {
        return matricule;
    }

    public String getNomProprietaire() {
        return nomProprietaire;
    }

    /*public String getPrenomProprietaire() {
        return prenomProprietaire;
    }*/

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getProprietaireNouveauPermis() {
        return proprietaireNouveauPermis;
    }

    public void setProprietaireNouveauPermis(String proprietaireNouveauPermis) {
        this.proprietaireNouveauPermis = proprietaireNouveauPermis;
    }

    public void setNomProprietaire(String nomProprietaire) {
        this.nomProprietaire = nomProprietaire;

    }

    /*public void setPrenomProprietaire(String prenomProprietaire) {
        this.prenomProprietaire = prenomProprietaire;
    }*/

    public void setMarque(String marque) {
        this.marque = marque;
    }


    public String getMarque() {
        return marque;

    }
}

