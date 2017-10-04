package de.diavololoop.chloroplast.color;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gast2 on 29.09.17.
 */
public class ColorPicker {


    private final static Map<String, Integer> COLORS = new HashMap<>();

    static{
        COLORS.put("black", 0);
        COLORS.put("white", 0xFFFFFFFF);
        COLORS.put("aqua", 0x00FFFF);
        COLORS.put("aquamarine", 0x7FFFD4);
        COLORS.put("azure", 0xf0ffff);
        COLORS.put("beige", 0xf5f5dc);
        COLORS.put("bisque", 0xffe4c4);
        COLORS.put("black", 0x000000);
        COLORS.put("blanchedalmond", 0xffebcd);
        COLORS.put("blue", 0x0000ff);
        COLORS.put("blueviolet", 0x8a2be2);
        COLORS.put("brown", 0xa52a2a);
        COLORS.put("burlywood", 0xdeb887);
        COLORS.put("cadetblue", 0x5f9ea0);
        COLORS.put("chartreuse", 0x7fff00);
        COLORS.put("chocolate", 0xd2691e);
        COLORS.put("coral", 0xff7f50);
        COLORS.put("cornflowerblue", 0x6495ed);
        COLORS.put("cornsilk", 0xfff8dc);
        COLORS.put("crimson", 0xdc143c);
        COLORS.put("cyan", 0x00ffff);
        COLORS.put("darkblue", 0x00008b);
        COLORS.put("darkcyan", 0x008b8b);
        COLORS.put("darkgoldenrod", 0xb8860b);
        COLORS.put("darkgray", 0xa9a9a9);
        COLORS.put("darkgreen", 0x006400);
        COLORS.put("darkgrey", 0xa9a9a9);
        COLORS.put("darkkhaki", 0xbdb76b);
        COLORS.put("darkmagenta", 0x8b008b);
        COLORS.put("darkolivegreen", 0x556b2f);
        COLORS.put("darkorange", 0xff8c00);
        COLORS.put("darkorchid", 0x9932cc);
        COLORS.put("darkred", 0x8b0000);
        COLORS.put("darksalmon", 0xe9967a);
        COLORS.put("darkseagreen", 0x8fbc8f);
        COLORS.put("darkslateblue", 0x483d8b);
        COLORS.put("darkslategray", 0x2f4f4f);
        COLORS.put("darkslategrey", 0x2f4f4f);
        COLORS.put("darkturquoise", 0x00ced1);
        COLORS.put("darkviolet", 0x9400d3);
        COLORS.put("deeppink", 0xff1493);
        COLORS.put("deepskyblue", 0x00bfff);
        COLORS.put("dimgray", 0x696969);
        COLORS.put("dimgrey", 0x696969);
        COLORS.put("dodgerblue", 0x1e90ff);
        COLORS.put("firebrick", 0xb22222);
        COLORS.put("floralwhite", 0xfffaf0);
        COLORS.put("forestgreen", 0x228b22);
        COLORS.put("fuchsia", 0xff00ff);
        COLORS.put("gainsboro", 0xdcdcdc);
        COLORS.put("ghostwhite", 0xf8f8ff);
        COLORS.put("gold", 0xffd700);
        COLORS.put("goldenrod", 0xdaa520);
        COLORS.put("gray", 0x808080);
        COLORS.put("green", 0x00FF00);
        COLORS.put("greenyellow", 0xadff2f);
        COLORS.put("grey", 0x808080);
        COLORS.put("honeydew", 0xf0fff0);
        COLORS.put("hotpink", 0xff69b4);
        COLORS.put("indianred", 0xcd5c5c);
        COLORS.put("indigo", 0x4b0082);
        COLORS.put("ivory", 0xfffff0);
        COLORS.put("khaki", 0xf0e68c);
        COLORS.put("lavender", 0xe6e6fa);
        COLORS.put("lavenderblush", 0xfff0f5);
        COLORS.put("lawngreen", 0x7cfc00);
        COLORS.put("lemonchiffon", 0xfffacd);
        COLORS.put("lightblue", 0xadd8e6);
        COLORS.put("lightcoral", 0xf08080);
        COLORS.put("lightcyan", 0xe0ffff);
        COLORS.put("lightgoldenrodyellow", 0xfafad2);
        COLORS.put("lightgray", 0xd3d3d3);
        COLORS.put("lightgreen", 0x90ee90);
        COLORS.put("lightgrey", 0xd3d3d3);
        COLORS.put("lightpink", 0xffb6c1);
        COLORS.put("lightsalmon", 0xffa07a);
        COLORS.put("lightseagreen", 0x20b2aa);
        COLORS.put("lightskyblue", 0x87cefa);
        COLORS.put("lightslategray", 0x778899);
        COLORS.put("lightslategrey", 0x778899);
        COLORS.put("lightsteelblue", 0xb0c4de);
        COLORS.put("lightyellow", 0xffffe0);
        COLORS.put("lime", 0x00ff00);
        COLORS.put("limegreen", 0x32cd32);
        COLORS.put("linen", 0xfaf0e6);
        COLORS.put("magenta", 0xff00ff);
        COLORS.put("maroon", 0x800000);
        COLORS.put("mediumaquamarine", 0x66cdaa);
        COLORS.put("mediumblue", 0x0000cd);
        COLORS.put("mediumorchid", 0xba55d3);
        COLORS.put("mediumpurple", 0x9370db);
        COLORS.put("mediumseagreen", 0x3cb371);
        COLORS.put("mediumslateblue", 0x7b68ee);
        COLORS.put("mediumspringgreen", 0x00fa9a);
        COLORS.put("mediumturquoise", 0x48d1cc);
        COLORS.put("mediumvioletred", 0xc71585);
        COLORS.put("midnightblue", 0x191970);
        COLORS.put("mintcream", 0xf5fffa);
        COLORS.put("mistyrose", 0xffe4e1);
        COLORS.put("moccasin", 0xffe4b5);
        COLORS.put("navajowhite", 0xffdead);
        COLORS.put("navy", 0x000080);
        COLORS.put("oldlace", 0xfdf5e6);
        COLORS.put("olive", 0x808000);
        COLORS.put("olivedrab", 0x6b8e23);
        COLORS.put("orange", 0xffa500);
        COLORS.put("orangered", 0xff4500);
        COLORS.put("orchid", 0xda70d6);
        COLORS.put("palegoldenrod", 0xeee8aa);
        COLORS.put("palegreen", 0x98fb98);
        COLORS.put("paleturquoise", 0xafeeee);
        COLORS.put("palevioletred", 0xdb7093);
        COLORS.put("papayawhip", 0xffefd5);
        COLORS.put("peachpuff", 0xffdab9);
        COLORS.put("peru", 0xcd853f);
        COLORS.put("pink", 0xffc0cb);
        COLORS.put("plum", 0xdda0dd);
        COLORS.put("powderblue", 0xb0e0e6);
        COLORS.put("purple", 0x800080);
        COLORS.put("red", 0xff0000);
        COLORS.put("rosybrown", 0xbc8f8f);
        COLORS.put("royalblue", 0x4169e1);
        COLORS.put("saddlebrown", 0x8b4513);
        COLORS.put("salmon", 0xfa8072);
        COLORS.put("sandybrown", 0xf4a460);
        COLORS.put("seagreen", 0x2e8b57);
        COLORS.put("seashell", 0xfff5ee);
        COLORS.put("sienna", 0xa0522d);
        COLORS.put("silver", 0xc0c0c0);
        COLORS.put("skyblue", 0x87ceeb);
        COLORS.put("slateblue", 0x6a5acd);
        COLORS.put("slategray", 0x708090);
        COLORS.put("slategrey", 0x708090);
        COLORS.put("snow", 0xfffafa);
        COLORS.put("springgreen", 0x00ff7f);
        COLORS.put("steelblue", 0x4682b4);
        COLORS.put("tan", 0xd2b48c);
        COLORS.put("teal", 0x008080);
        COLORS.put("thistle", 0xd8bfd8);
        COLORS.put("tomato", 0xff6347);
        COLORS.put("turquoise", 0x40e0d0);
        COLORS.put("violet", 0xee82ee);
        COLORS.put("wheat", 0xf5deb3);
        COLORS.put("white", 0xffffff);
        COLORS.put("whitesmoke", 0xf5f5f5);
        COLORS.put("yellow", 0xffff00);
        COLORS.put("yellowgreen", 0x9acd32);
    }


    public static int getColor(String s){

        Integer result = COLORS.get(s.trim().toLowerCase());
        if(result != null){
            return result;
        }

        s = s.trim().toLowerCase().replaceAll("[^a-f0-9]", "");
        if(s.length() != 6 && s.length() != 8){
            return 0;
        }

        result = 0;
        for(char c: s.toCharArray()){
            switch (c) {
                case '0': result = result << 4; break;
                case '1': result = (result << 4) | 0x1; break;
                case '2': result = (result << 4) | 0x2; break;
                case '3': result = (result << 4) | 0x3; break;
                case '4': result = (result << 4) | 0x4; break;
                case '5': result = (result << 4) | 0x5; break;
                case '6': result = (result << 4) | 0x6; break;
                case '7': result = (result << 4) | 0x7; break;
                case '8': result = (result << 4) | 0x8; break;
                case '9': result = (result << 4) | 0x9; break;
                case 'a': result = (result << 4) | 0xa; break;
                case 'b': result = (result << 4) | 0xb; break;
                case 'c': result = (result << 4) | 0xc; break;
                case 'd': result = (result << 4) | 0xd; break;
                case 'e': result = (result << 4) | 0xe; break;
                case 'f': result = (result << 4) | 0xf; break;
            }
        }
        return result;
    }

    public static void getColor(String color, byte[] target, int offset){
        int c = getColor(color);
        target[offset + 0] = (byte)(c >> 16);
        target[offset + 1] = (byte)(c >> 8);
        target[offset + 2] = (byte)c;
    }
    public static void getColorRGBW(String color, byte[] target, int offset){
        int c = getColor(color);
        target[offset + 0] = (byte)(c >> 16);
        target[offset + 1] = (byte)(c >> 8);
        target[offset + 2] = (byte)c;
        target[offset + 3] = (byte)(c >> 24);
    }

}
