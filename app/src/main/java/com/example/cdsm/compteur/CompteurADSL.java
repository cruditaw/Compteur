package com.example.cdsm.compteur;

import android.util.Log;

/**
 * Created by cdsm on 2/14/18.
 */

public class CompteurADSL {

    private int limit;
    private int current;
    private Digit[] digitsTab;// = new Digit[]{new Digit(), new Digit(), new Digit(), new Digit()};

    public CompteurADSL() {
        this.limit = 100;
        this.current = 0;
        digitsTab = new Digit[]{new Digit(), new Digit(), new Digit(), new Digit()};
    }

    public CompteurADSL(int limit) {
        this.limit = limit;
        this.current = 0;
        digitsTab = new Digit[]{new Digit(), new Digit(), new Digit(), new Digit()};
    }

    public CompteurADSL(int limit, int current) {
        this.limit = limit;
        this.current = current;
        int size = String.valueOf(current).length();
        int[] values = new int[]{0,0,0,0};
        int i = size-1;
        for (char c : String.valueOf(current).toCharArray()) {
            values[i] = Integer.valueOf(String.valueOf(c));
            System.out.println("Values["+i+"] : "+values[i]);
            i-=1;
        }
        digitsTab = new Digit[]{new Digit(values[0]), new Digit(values[1]), new Digit(values[2]), new Digit(values[3])};
    }

    public void reset() {
        current = 0;
        digitsTab = new Digit[]{new Digit(), new Digit(), new Digit(), new Digit()};
    }

    public boolean increment() {
        if (current < limit) {
            boolean isMax = digitsTab[0].increment();

            if (isMax) {
                //idx +=1;
                isMax = digitsTab[1].increment();
                if (isMax) {
                    isMax = digitsTab[2].increment();
                    if (isMax) {
                        isMax = digitsTab[3].increment();
                    }
                }
            }
            System.out.println("digitsTab[0].getValue() : "+digitsTab[0].getValue());
            System.out.println("digitsTab[1].getValue() : "+digitsTab[1].getValue());
            System.out.println("digitsTab[2].getValue() : "+digitsTab[2].getValue());
            System.out.println("digitsTab[3].getValue() : "+digitsTab[3].getValue());
            current = (digitsTab[3].getValue()*1000) + (digitsTab[2].getValue()*100) + (digitsTab[1].getValue()*10) + digitsTab[0].getValue();
            //System.out.println("Current "+current);
            Log.w("Increment", "current : "+current);
        }

        return (current == limit);
    }

    public int getLimit() {
        return limit;
    }

    public Digit[] getDigitsTab() {
        return digitsTab;
    }

    public int getCurrent() {
        return current;
    }
}
