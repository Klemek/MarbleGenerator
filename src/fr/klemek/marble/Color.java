package fr.klemek.marble;

import java.util.Random;

class Color {
    final byte r;
    final byte g;
    final byte b;

    private Color(byte r, byte g, byte b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    Color(int r, int g, int b) {
        this.r = (byte) r;
        this.g = (byte) g;
        this.b = (byte) b;
    }

    static Color fromRgb(int r, int g, int b) {
        return new Color(r + Byte.MIN_VALUE, g + Byte.MIN_VALUE, b + Byte.MIN_VALUE);
    }

    static Color random(Random rand) {
        return new Color(
                Utils.randomByte(rand),
                Utils.randomByte(rand),
                Utils.randomByte(rand));
    }

    static Color add(float[] factors, Color[] colors) {
        float r = 0f;
        float g = 0f;
        float b = 0f;
        for (int i = 0; i < factors.length; i++) {
            r += factors[i] * colors[i].r;
            g += factors[i] * colors[i].g;
            b += factors[i] * colors[i].b;
        }
        return new Color(Utils.bound(Math.round(r)), Utils.bound(Math.round(g)), Utils.bound(Math.round(b)));
    }

    static Color add(Color... colors) {
        float[] factors = new float[colors.length];
        for (int i = 0; i < colors.length; i++)
            factors[i] = 1f;
        return add(factors, colors);
    }

    Color diverge(Random rand) {
        return new Color(Utils.bound(Utils.div(rand, r)),
                Utils.div(rand, g),
                Utils.div(rand, b));
    }

    int sum() {
        return r + g + b;
    }

    public java.awt.Color toColor() {
        return new java.awt.Color(r - Byte.MIN_VALUE, g - Byte.MIN_VALUE, b - Byte.MIN_VALUE);
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean unsigned) {
        return "(" + (unsigned ? r - Byte.MIN_VALUE : r) + "," + (unsigned ? g - Byte.MIN_VALUE : g) + "," + (unsigned ? b - Byte.MIN_VALUE : b) + ")";
    }
}
