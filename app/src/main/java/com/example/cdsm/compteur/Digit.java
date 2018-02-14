package com.example.cdsm.compteur;

/**
 * Created by cdsm on 2/14/18.
 */

public class Digit {

    private int value;

    public Digit() {
        this.value = 0;
    }

    public Digit(int value) {
        this.value = (value >= 0 && value <10) ? value: 0;
    }

    public boolean increment() {
        if (value == 9) {
            value = 0;
            return true;

        } else {
            value+=1;
            return false;
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
