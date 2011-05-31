package net.sf.jcommon.ui;

import java.awt.color.ColorSpace;
import java.awt.Color;

/**
 * 
 * @author Adrian BER
 */
public class RGBSwapColorSpace extends ColorSpace {

    private int[] indices;

    /** Constructs a color space where RGB are swaped according to the given parameter.
     * @param order any combination of the characters RGB (uppercase, lowercase or mixed) that will
     * dictate the order of the RGB channels
     * @throws IllegalArgumentException if order contains invalid characters or if the length is
     * not equal to 3
     */
    public RGBSwapColorSpace(String order) {
        super(ColorSpace.TYPE_RGB, 3);
        if (order.length() != 3)
            throw new IllegalArgumentException("The order parameter must contain exactly 3 characters.");
        indices = new int[3];
        for (int i = 0; i < 3; i++) {
            char c = order.charAt(i);
            switch(c) {
                case 'r':
                case 'R':
                    indices[i] = 0;
                    break;
                case 'g':
                case 'G':
                    indices[i] = 1;
                    break;
                case 'b':
                case 'B':
                    indices[i] = 2;
                    break;
                default:
                    throw new IllegalArgumentException("The order parameter must contain only 'r', 'R', 'g', 'G', 'b', 'B' characters.");
            }
        }
    }

    public float[] toRGB(float[] colorvalue) {
        float[] x = new float[colorvalue.length];
        for (int i = 0; i < 3; i++) {
            x[indices[i]] = colorvalue[i];
        }
        System.arraycopy(colorvalue, 3, x, 3, colorvalue.length - 3);
        return x;
    }

    public float[] fromRGB(float[] colorvalue) {
        float[] x = new float[colorvalue.length];
        for (int i = 0; i < 3; i++) {
            x[i] = colorvalue[indices[i]];
        }
        System.arraycopy(colorvalue, 3, x, 3, colorvalue.length - 3);
        return x;
    }

    public float[] toCIEXYZ(float[] colorvalue) {
        // swap RGB and create the color
        Color c = new Color(colorvalue[indices[0]], colorvalue[indices[1]], colorvalue[indices[2]]);
        return c.getColorComponents(ColorSpace.getInstance(ColorSpace.CS_CIEXYZ), null);
    }

    public float[] fromCIEXYZ(float[] colorvalue) {
        Color c = new Color(ColorSpace.getInstance(ColorSpace.CS_CIEXYZ), colorvalue, 0);
        // get the RGB values
        float[] rgbValues = c.getRGBColorComponents(null);
        // swap them
        float[] x = new float[3];
        for (int i = 0; i < 3; i++) {
            x[indices[i]] = rgbValues[i];
        }
        return x;
    }

}
