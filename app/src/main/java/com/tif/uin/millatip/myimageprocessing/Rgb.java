package com.tif.uin.millatip.myimageprocessing;

public class Rgb {
    private int red;
    private int blue;
    private int green;

    /**
     * Constructor of the class from integers
     *
     * @param red : integer value for red (0...255)
     * @param green : integer value for green (0...255)
     * @param blue : integer value for blue (0...255)
     */
    public Rgb(int red, int green, int blue) {
        this.red = red;
        this.blue = blue;
        this.green = green;
    }

    /**
     * @return red value
     */
    public int getRed() {
        return (this.red);
    }

    /**
     * @return blue value
     */
    public int getBlue() {
        return (this.blue);
    }

    /**
     * @return green value
     */
    public int getGreen() {
        return (this.green);
    }
}
