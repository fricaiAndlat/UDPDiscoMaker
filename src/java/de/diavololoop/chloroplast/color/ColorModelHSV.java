package de.diavololoop.chloroplast.color;

/**
 * Created by gast2 on 28.09.17.
 */
public class ColorModelHSV extends ColorModel{

    @Override
    public int getLength() {
        return 3;
    }

    @Override
    public ByteOrder getByteOrder() {
        return ByteOrder.HSV;
    }

    @Override
    public void convert(byte[] in, int offsetIn, byte[] out, int offsetOut, ByteOrder target) {

        switch(target){

            case RGB:
                convertToRGB(out,
                        offsetOut + 0,
                        offsetOut + 1,
                        offsetOut + 2,
                        ((int)in[offsetIn]) & 0xFF,
                        ((int)in[offsetIn + 1]) & 0xFF,
                        ((int)in[offsetIn + 2]) & 0xFF);
                break;
            case GBR:
                convertToRGB(out,
                        offsetOut + 2,
                        offsetOut + 0,
                        offsetOut + 1,
                        ((int)in[offsetIn]) & 0xFF,
                        ((int)in[offsetIn + 1]) & 0xFF,
                        ((int)in[offsetIn + 2]) & 0xFF);
                break;
            case GRB:
                convertToRGB(out,
                        offsetOut + 1,
                        offsetOut + 0,
                        offsetOut + 2,
                        ((int)in[offsetIn]) & 0xFF,
                        ((int)in[offsetIn + 1]) & 0xFF,
                        ((int)in[offsetIn + 2]) & 0xFF);
                break;
            case BGR:
                convertToRGB(out,
                        offsetOut + 2,
                        offsetOut + 1,
                        offsetOut + 0,
                        ((int)in[offsetIn]) & 0xFF,
                        ((int)in[offsetIn + 1]) & 0xFF,
                        ((int)in[offsetIn + 2]) & 0xFF);
                break;
            case BRG:
                convertToRGB(out,
                        offsetOut + 1,
                        offsetOut + 2,
                        offsetOut + 0,
                        ((int)in[offsetIn]) & 0xFF,
                        ((int)in[offsetIn + 1]) & 0xFF,
                        ((int)in[offsetIn + 2]) & 0xFF);
                break;
            case HSV:
                out[offsetOut + 0] = in[offsetIn + 0];
                out[offsetOut + 1] = in[offsetIn + 1];
                out[offsetOut + 2] = in[offsetIn + 2];
                break;
            case RGBW: {
                int s = ((int)in[offsetIn + 1]) & 0xFF;
                int v = ((int)in[offsetIn + 2]) & 0xFF;
                convertToRGB(out,
                        offsetOut + 1,
                        offsetOut + 2,
                        offsetOut + 0,
                        ((int)in[offsetIn]) & 0xFF,
                        ((int)in[offsetIn + 1]) & 0xFF,
                        ((int)in[offsetIn + 2]) & 0xFF);
                out[offsetOut + 3] = (byte)(((255 - s) * v) / 255);
                break;}
            case GRBW: {
                int s = ((int)in[offsetIn + 1]) & 0xFF;
                int v = ((int)in[offsetIn + 2]) & 0xFF;
                convertToRGB(out,
                        offsetOut + 1,
                        offsetOut + 0,
                        offsetOut + 2,
                        ((int)in[offsetIn]) & 0xFF,
                        ((int)in[offsetIn + 1]) & 0xFF,
                        ((int)in[offsetIn + 2]) & 0xFF);
                out[offsetOut + 3] = (byte)(((255 - s) * v) / 255);
                break;}
        }

    }

    private void convertToRGB(byte[] out, int posR, int posG, int posB, int h, int s, int v){

        int hi = (h * 6) / 255;
        float f = (h*6) / 255f - hi;

        int p = (v * (255-s)) / 255;
        int q = (v * (255 - (int)(s * f))) / 255;
        int t = (v * (255 - (int)(s * (1-f)))) / 255;

        if(hi == 0 || hi == 6){
            out[posR] = (byte)v;
            out[posG] = (byte)t;
            out[posB] = (byte)p;
        }else if(hi == 1){
            out[posR] = (byte)q;
            out[posG] = (byte)v;
            out[posB] = (byte)p;
        }else if(hi == 2){
            out[posR] = (byte)p;
            out[posG] = (byte)v;
            out[posB] = (byte)t;
        }else if(hi == 3){
            out[posR] = (byte)p;
            out[posG] = (byte)q;
            out[posB] = (byte)v;
        }else if(hi == 4){
            out[posR] = (byte)t;
            out[posG] = (byte)p;
            out[posB] = (byte)v;
        }else if(hi == 5){
            out[posR] = (byte)v;
            out[posG] = (byte)p;
            out[posB] = (byte)q;
        }

    }
}
