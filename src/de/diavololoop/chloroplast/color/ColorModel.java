package de.diavololoop.chloroplast.color;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gast2 on 28.09.17.
 */
public abstract class ColorModel {

    public enum ByteOrder implements Serializable {
        RGB("rgb"),
        GBR("gbr"),
        BGR("bgr"),
        BRG("brg"),
        HSV("hsv"),
        RGBW("rgbw");


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
                return ByteOrder.RGB;
            }
        }
    }

    public static ColorModel RGB_MODEL = new ColorModelRGB();
    public static ColorModel HSV_MODEL = new ColorModelHSV();

    private static Map<String, ColorModel> colorModels = new HashMap<String, ColorModel>();

    static{
        colorModels.put("rgb", new ColorModelRGB());
        colorModels.put("hsv", new ColorModelHSV());
    }

    public static ColorModel getModel(String name) {
        return colorModels.get(name);
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

}
