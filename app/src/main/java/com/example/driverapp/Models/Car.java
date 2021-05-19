package com.example.driverapp.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity (tableName = "car_table")
public class Car implements Serializable {
    @PrimaryKey (autoGenerate = true)
    private int id;

    private String marque;
    private String modele;
    private String matricule;
    private String codeSecret;
    private String numTele;

    public Car(String marque, String modele, String matricule, String codeSecret, String numTele) {
        this.marque = marque;
        this.modele = modele;
        this.matricule = matricule;
        this.codeSecret = codeSecret;
        this.numTele = numTele;
    }

    public Car(String [] carData) {
        this.marque = carData[0];
        this.modele = carData[1];
        this.matricule = carData[2];
        this.codeSecret = carData[3];
        this.numTele = carData[4];
    }

    @Override
    public String toString() {
        return "Car{" +
                ", marque='" + marque + '\'' +
                ", modele='" + modele + '\'' +
                ", matricule='" + matricule + '\'' +
                ", codeSecret='" + codeSecret + '\'' +
                ", numTele='" + numTele + '\'' +
                '}';
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getCodeSecret() {
        return codeSecret;
    }

    public void setCodeSecret(String codeSecret) {
        this.codeSecret = codeSecret;
    }

    public String getNumTele() {
        return numTele;
    }

    public void setNumTele(String numTele) {
        this.numTele = numTele;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
