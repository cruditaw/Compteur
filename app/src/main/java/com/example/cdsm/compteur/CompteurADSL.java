package com.example.cdsm.compteur;

public class CompteurADSL {

    private int limit;
    private int start;
    private int current;
    private Digit[] digitsTab;

    public CompteurADSL() {
        this.limit = 100;
        this.start = 0;
        digitsTab = new Digit[]{new Digit(), new Digit(), new Digit(), new Digit()};
    }

    public CompteurADSL(int limit) {
        this.limit = limit;
        this.start = 0;
        digitsTab = new Digit[]{new Digit(), new Digit(), new Digit(), new Digit()};
    }

    public CompteurADSL(int limit, int start) {
        this.limit = limit;
        this.start = start;
        int size = String.valueOf(start).length();
        int[] values = makeDigitTabValues(start, size);
        digitsTab = new Digit[]{new Digit(values[0]), new Digit(values[1]), new Digit(values[2]), new Digit(values[3])};
    }

    private int[] makeDigitTabValues(int start, int size) {
        int[] values = new int[]{0,0,0,0};
        int i = size-1;
        for (char c : String.valueOf(start).toCharArray()) {
            values[i] = Integer.valueOf(String.valueOf(c));
            i-=1;
        }
        return values;
    }

    public void pause() {
        start = current;
    }

    public void reset() {
        start = 0;
        digitsTab = new Digit[]{new Digit(), new Digit(), new Digit(), new Digit()};
    }

    public boolean increment() {
        handleDigits();
        current = (digitsTab[3].getValue()*1000) + (digitsTab[2].getValue()*100) + (digitsTab[1].getValue()*10) + digitsTab[0].getValue();
        return (current == limit);
    }

    private void handleDigits() {
        boolean isMax = digitsTab[0].increment();
        if (isMax) {
            isMax = digitsTab[1].increment();
            if (isMax) {
                isMax = digitsTab[2].increment();
                if (isMax) {
                    digitsTab[3].increment();
                }
            }
        }
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

    public int getStart() {
        return start;
    }
}
