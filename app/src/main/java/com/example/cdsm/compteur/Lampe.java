package com.example.cdsm.compteur;

public class Lampe {

    private boolean etatLampe;

    public Lampe() {
        etatLampe = false;
    }

    public boolean donneEtat() {
        return etatLampe;
    }

    public void allumeLampe() {
        etatLampe = true;
    }

    public void eteintLampe() {
        etatLampe = false;
    }
}
