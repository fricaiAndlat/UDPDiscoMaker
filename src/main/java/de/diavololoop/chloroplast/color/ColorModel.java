package de.diavololoop.chloroplast.color;


import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chloroplast
 *
 * In order tho convert between various color representation ColorModel are used to define colors. Only Colors
 * in a ColorModel can be converted to a specific ByteOrder
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

    /**
     * return a ColorModel with the given name
     *
     * @param name name of requested ColorModel
     * @return the requested ColorModel
     *
     * @throws RuntimeException when ColorModel is not found
     */
    public static ColorModel getModel(String name) {
        return colorModels.get(name.toLowerCase());
    }

    /**
     * @return the number of bytes used for one color (3 for RGB)
     */
    public abstract int getLength();

    /**
     * @return the byte ordering this ColorModel expects or is encoded as.
     */
    public abstract ByteOrder getByteOrder();

    /**
     * convert and copies a single color to an other ByteOrdering
     *
     * @param in the buffer to read from. Byte ordering should match byteOrder from this ByteOrder
     * @param offsetIn the position from the first byte in the in array
     * @param out the buffer to write in. ByteOrder will be =target
     * @param offsetOut the position for the first byte in the out array
     * @param target the ByteOrder for the output
     */
    public abstract void convert(byte[] in, int offsetIn, byte[] out, int offsetOut, ByteOrder target);

    /**
     * convert and copies an amount of colors to an other ByteOrdering
     *
     * @param in the buffer to read from. Byte ordering should match byteOrder from this ByteOrder
     * @param offsetIn the position from the first byte in the in array
     * @param out the buffer to write in. ByteOrder will be =target
     * @param offsetOut the position for the first byte in the out array
     * @param length number of colors to be processed
     * @param target the ByteOrder for the output
     */
    public void convert(byte[] in, int offsetIn, byte[] out, int offsetOut, int length, ByteOrder target){

        if(target == getByteOrder()){
            System.arraycopy(in, offsetIn, out, offsetOut, length * target.byteLen);
            return;
        }
        for(int i = 0; i < length; ++i){
            convert(in, offsetIn, out, offsetOut, target);
            offsetIn  += this.getByteOrder().length();
            offsetOut += target.length();

        }

    }

    /**
     *
     * @return the maximum number of bytes used dor a ByteOrder
     */
    public static final int maxByteLength(){
        return Arrays.stream(ColorModel.ByteOrder.values()).mapToInt(o -> o.length()).max().getAsInt();
    }

}
