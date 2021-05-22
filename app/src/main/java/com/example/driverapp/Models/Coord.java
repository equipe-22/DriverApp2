package com.example.driverapp.Models;

public class Coord {
    public static double lat;   //variable statique qui contient le lattitude
    public static double lng;   //variable dynamique qui contient le longitude

    public void recup_coord(String contenu_sms){       //Methode qui recupére les 2 coords
        String tmp[] = contenu_sms.split(",");  //séparé le msg reçu en 2 String (délimitateur est la virgule)
        this.lat=Double.parseDouble(tmp[0]);    //1er string est lat
        this.lng=Double.parseDouble(tmp[1]);    //2eme est lng
    }
}
