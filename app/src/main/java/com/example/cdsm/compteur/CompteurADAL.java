package com.example.cdsm.compteur;

public class CompteurADAL {

    private CompteurADSL compteur;
    private Lampe lampe;

    public CompteurADAL() {
        compteur = new CompteurADSL();
        lampe = new Lampe();
    }

    public CompteurADAL(int limit) {
        compteur = new CompteurADSL(limit);
        lampe = new Lampe();
    }

    public CompteurADAL(int limit, int start) {
        compteur = new CompteurADSL(limit, start);
        lampe = new Lampe();
    }

    public void resetCompteur() {
        lampe.eteintLampe();
        compteur.reset();
    }

    public Lampe getLampe() {
        return lampe;
    }

    public CompteurADSL getCompteur() {
        return compteur;
    }

}
