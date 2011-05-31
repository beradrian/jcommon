package net.sf.jcommon.ui;

import java.awt.*;

/**
 * A color that can take the same values as in HTML/CSS.
 *
 * @author Adrian BER
 */
public enum HTMLColor {

    steelblue(0x4682B4),
    cornflowerblue(0x6495ED),
    lightsteelblue(0xB0C4DE),
    mediumslateblue(0x7B68EE),
    slateblue(0x6A5ACD),
    darkslateblue(0x483D8B),
    midnightblue(0x191970),
    navy(0x000080),
    darkblue(0x00008B),
    mediumblue(0x0000CD),
    blue(0x0000FF),
    dodgerblue(0x1E90FF),
    deepskyblue(0x00BFFF),
    lightskyblue(0x87CEFA),
    skyblue(0x87CEEB),
    lightblue(0xADD8E6),
    powderblue(0xB0E0E6),
    azure(0xF0FFFF),
    lightcyan(0xE0FFFF),
    paleturquoise(0xAFEEEE),
    mediumturquoise(0x48D1CC),
    lightseagreen(0x20B2AA),
    darkcyan(0x008B8B),
    teal(0x008080),
    cadetblue(0x5F9EA0),
    darkturquoise(0x00CED1),
    aqua(0x00FFFF),
    cyan(0x00FFFF),
    turquoise(0x40E0D0),
    aquamarine(0x7FFFD4),
    mediumaquamarine(0x66CDAA),
    darkseagreen(0x8FBC8F),
    mediumseagreen(0x3CB371),
    seagreen(0x2E8B57),
    darkgreen(0x006400),
    green(0x008000),
    forestgreen(0x228B22),
    limegreen(0x32CD32),
    lime(0x00FF00),
    chartreuse(0x7FFF00),
    lawngreen(0x7CFC00),
    greenyellow(0xADFF2F),
    yellowgreen(0x9ACD32),
    palegreen(0x98FB98),
    lightgreen(0x90EE90),
    springgreen(0x00FF7F),
    mediumspringgreen(0x00FA9A),
    darkolivegreen(0x556B2F),
    olivedrab(0x6B8E23),
    olive(0x808000),
    darkkhaki(0xBDB76B),
    darkgoldenrod(0xB8860B),
    goldenrod(0xDAA520),
    gold(0xFFD700),
    yellow(0xFFFF00),
    khaki(0xF0E68C),
    palegoldenrod(0xEEE8AA),
    blanchedalmond(0xFFEBCD),
    moccasin(0xFFE4B5),
    wheat(0xF5DEB3),
    navajowhite(0xFFDEAD),
    burlywood(0xDEB887),
    tan(0xD2B48C),
    rosybrown(0xBC8F8F),
    sienna(0xA0522D),
    saddlebrown(0x8B4513),
    chocolate(0xD2691E),
    peru(0xCD853F),
    sandybrown(0xF4A460),
    darkred(0x8B0000),
    maroon(0x800000),
    brown(0xA52A2A),
    firebrick(0xB22222),
    indianred(0xCD5C5C),
    lightcoral(0xF08080),
    salmon(0xFA8072),
    darksalmon(0xE9967A),
    lightsalmon(0xFFA07A),
    coral(0xFF7F50),
    tomato(0xFF6347),
    darkorange(0xFF8C00),
    orange(0xFFA500),
    orangered(0xFF4500),
    crimson(0xDC143C),
    red(0xFF0000),
    deeppink(0xFF1493),
    fuchsia(0xFF00FF),
    magenta(0xFF00FF),
    hotpink(0xFF69B4),
    lightpink(0xFFB6C1),
    pink(0xFFC0CB),
    palevioletred(0xDB7093),
    mediumvioletred(0xC71585),
    purple(0x800080),
    darkmagenta(0x8B008B),
    mediumpurple(0x9370DB),
    blueviolet(0x8A2BE2),
    indigo(0x4B0082),
    darkviolet(0x9400D3),
    darkorchid(0x9932CC),
    mediumorchid(0xBA55D3),
    orchid(0xDA70D6),
    violet(0xEE82EE),
    plum(0xDDA0DD),
    thistle(0xD8BFD8),
    lavender(0xE6E6FA),
    ghostwhite(0xF8F8FF),
    aliceblue(0xF0F8FF),
    mintcream(0xF5FFFA),
    honeydew(0xF0FFF0),
    lightgoldenrodyellow(0xFAFAD2),
    lemonchiffon(0xFFFACD),
    cornsilk(0xFFF8DC),
    lightyellow(0xFFFFE0),
    ivory(0xFFFFF0),
    floralwhite(0xFFFAF0),
    linen(0xFAF0E6),
    oldlace(0xFDF5E6),
    antiquewhite(0xFAEBD7),
    bisque(0xFFE4C4),
    peachpuff(0xFFDAB9),
    papayawhip(0xFFEFD5),
    beige(0xF5F5DC),
    seashell(0xFFF5EE),
    lavenderblush(0xFFF0F5),
    mistyrose(0xFFE4E1),
    snow(0xFFFAFA),
    white(0xFFFFFF),
    whitesmoke(0xF5F5F5),
    gainsboro(0xDCDCDC),
    lightgrey(0xD3D3D3),
    silver(0xC0C0C0),
    darkgray(0xA9A9A9),
    gray(0x808080),
    lightslategray(0x778899),
    slategray(0x708090),
    dimgray(0x696969),
    darkslategray(0x2F4F4F),
    black(0x000000);

    private Color color;

    HTMLColor(int hex) {
        this((0xff0000 & hex) >> 16, (0x00ff00 & hex) >> 8, 0x0000ff & hex);
    }

    HTMLColor(int r, int g, int b) {
        color = new Color(r, g, b);
    }

    public Color getColor() {
        // take out the alpha value
        return color;
    }

    public String getHTMLValue() {
        return "#" + toHex(color.getRed()) + toHex(color.getGreen()) + toHex(color.getBlue());
    }

    private static String toHex(int i) {
        String s = Integer.toHexString(i);
        if (s.length() == 1)
            return "0" + s;
        return s;
    }

    /**
     * Returns a RGB value from the given HTML string value.
     *
     * @param name can have the following formats
     *              <ul>
     *              <li><strong>#rrggbb</strong>, where rr, gg and bb are hex digits
     *              representing the red, green and blue component</li>
     *              <li><strong>rrggbb</strong>, where rr, gg and bb are hex digits
     *              representing the red, green and blue component</li>
     *              <li><strong>name</strong>, where name represents the HTML color name.</li>
     *              </ul>
     * @return the RGB value
     * @throws IllegalArgumentException if the color name is not recognized or the value
     *                                  contains invalid digits or the number of digits is less than 6.
     */
    public static Color forName(String name) {
        try {
            return valueOf(name.toLowerCase()).getColor();
        } catch (IllegalArgumentException exc) {
            int start = 0;
            int rgb = 0;
            if (name.charAt(0) == '#') {
                start++;
            }
            if (start + 6 < name.length())
                throw new IllegalArgumentException("String color value too small. It must have at least six hexdigits");
            for (int i = 0; i < 6; i++) {
                rgb = (rgb << 4) + hexDigitValue(name.charAt(i + start));
            }
            return new Color(rgb);
        }
    }

    public static HTMLColor valueOf(Color c) {
        for (HTMLColor hc : values()) {
            if (hc.getColor().equals(c))
                return hc;
        }
        return null;
    }

    /**
     * @param hexDigit a hex digit, case insenssitive
     * @return the integer value for the given hex digit.
     */
    private static int hexDigitValue(char hexDigit) {
        if (hexDigit >= '0' && hexDigit <= '9')
            return hexDigit - '0';
        else if (hexDigit >= 'a' && hexDigit <= 'f')
            return hexDigit - 'a' + 10;
        else if (hexDigit >= 'A' && hexDigit <= 'F')
            return hexDigit - 'A' + 10;
        throw new IllegalArgumentException("Hex digit can be only '0'-'9', 'a'-'f' or 'A'-'F'");
    }

}
