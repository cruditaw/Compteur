package com.example.cdsm.compteur;

/**
 * Created by cdsm on 2/14/18.
 */

public class Lampe {

    private boolean etatLampe;

    public Lampe() {
        etatLampe = false;
    }

    public Lampe(boolean etatLampe) {
        this.etatLampe = etatLampe;
    }

    public boolean donneEtat() {
        return etatLampe;
    }

    public void setEtatLampe(boolean etatLampe) {
        this.etatLampe = etatLampe;
    }
}
