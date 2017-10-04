package de.diavololoop.chloroplast.color;


import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gast2 on 28.09.17.
 */
public abstract class ColorModel {

    public enum ByteOrder implements Serializable {
        RGB("rgb"),
        GBR("gbr"),
        GRB("grb"),
        BGR("bgr"),
        BRG("brg"),
        HSV("hsv"),
        RGBW("rgbw"),
        GRBW("grbw");


        private final String name;
        private final int byteLen;

        ByteOrder(String name) {
            this.name = name;
            byteLen = name.length();
        }

        public int length(){
            return byteLen;
        }

        @Override
        public String toString(){
            return name;
        }

        public static ByteOrder get(String s) {

            try {
                return ByteOrder.valueOf(s);
            } catch (IllegalArgumentException e){
                throw new RuntimeException("unrecognized ByteOrder: "+s);
            }
        }
    }

    public static ColorModel RGB_MODEL = new ColorModelRGB();
    public static ColorModel HSV_MODEL = new ColorModelHSV();
    public static ColorModel RGBW_MODEL = new ColorModelRGBW();

    private static Map<String, ColorModel> colorModels = new HashMap<String, ColorModel>();

    static{
        colorModels.put("rgb", RGB_MODEL);
        colorModels.put("hsv", HSV_MODEL);
        colorModels.put("rgbw", RGBW_MODEL);
    }

    public static ColorModel getModel(String name) {
        return colorModels.get(name.toLowerCase());
    }

    public abstract int getLength();
    public abstract ByteOrder getByteOrder();

    public abstract void convert(byte[] in, int offsetIn, byte[] out, int offsetOut, ByteOrder target);

    public void convert(byte[] in, int offsetIn, byte[] out, int offsetOut, int length, ByteOrder target){

        if(target == getByteOrder()){
            System.arraycopy(in, offsetIn, out, offsetOut, length * target.byteLen);
            return;
        }
        for(int i = 0; i < length; ++i){
            convert(in, offsetIn, out, offsetOut, target);
            offsetIn  += getByteOrder().length();
            offsetOut += target.length();

        }

    }

    public static final int maxByteLength(){
        return Arrays.stream(ColorModel.ByteOrder.values()).mapToInt(o -> o.length()).max().getAsInt();
    }

}
