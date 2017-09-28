package de.diavololoop.chloroplast.color;

/**
 * Created by gast2 on 28.09.17.
 */
public class ColorModelRGB extends ColorModel {

    @Override
    public int getLength() {
            return 3;
    }

    @Override
    public ByteOrder getByteOrder() {
        return ByteOrder.RGB;
    }

    @Override
    public void convert(byte[] in, int offsetIn, byte[] out, int offsetOut, ByteOrder target) {
        switch(target){

            case RGB:
                out[offsetOut + 0] = in[offsetIn + 0];
                out[offsetOut + 1] = in[offsetIn + 1];
                out[offsetOut + 2] = in[offsetIn + 2];
                return;
            case GBR:
                out[offsetOut + 0] = in[offsetIn + 1];
                out[offsetOut + 1] = in[offsetIn + 2];
                out[offsetOut + 2] = in[offsetIn + 0];
                return;
            case BGR:
                out[offsetOut + 0] = in[offsetIn + 2];
                out[offsetOut + 1] = in[offsetIn + 1];
                out[offsetOut + 2] = in[offsetIn + 0];
                return;
            case BRG:
                out[offsetOut + 0] = in[offsetIn + 2];
                out[offsetOut + 1] = in[offsetIn + 0];
                out[offsetOut + 2] = in[offsetIn + 1];
            case HSV:{
                int r = ((int)in[offsetIn + 0]) & 0xFF;
                int g = ((int)in[offsetIn + 1]) & 0xFF;
                int b = ((int)in[offsetIn + 2]) & 0xFF;
                int max = Math.max(r, Math.max(r, g));
                int min = Math.min(r, Math.min(r, g));

                if(max == min){
                    out[offsetOut + 0] = 0;
                }else if(max == r){
                    out[offsetOut + 0] = (byte)(42*(g - b)/(max-min));
                }else if(max == g){
                    out[offsetOut + 0] = (byte)(42*2 + 42*(b - r)/(max-min));
                }else if(max == b){
                    out[offsetOut + 0] = (byte)(42*4 + 42*(r - g)/(max-min));
                }

                if(max == 0){
                    out[offsetOut + 1] = 0;
                }else{
                    out[offsetOut + 1] = (byte)(255 * (max - min) / max);
                }

                out[offsetOut + 2] = (byte)max;
                break;}
            case RGBW:{
                //slightly modified code from: https://stackoverflow.com/questions/40312216/converting-rgb-to-rgbw
                int r = ((int)in[offsetIn + 0]) & 0xFF;
                int g = ((int)in[offsetIn + 1]) & 0xFF;
                int b = ((int)in[offsetIn + 2]) & 0xFF;
                int max = Math.max(r, Math.max(r, g));
                int min = Math.min(r, Math.min(r, g));

                //If the maximum value is 0, immediately return pure black.
                if(max == 0) {
                    out[offsetOut + 0] = 0;
                    out[offsetOut + 1] = 0;
                    out[offsetOut + 2] = 0;
                    out[offsetOut + 3] = 0;
                }

                //This section serves to figure out what the color with 100% hue is
                float multiplier = 255f / max;
                float hR = r * multiplier;
                float hG = g * multiplier;
                float hB = b * multiplier;

                //This calculates the Whiteness (not strictly speaking Luminance) of the color
                float hMax = Math.max(hR, Math.max(hG, hB));
                float hMin = Math.min(hR, Math.min(hG, hB));
                float luminance = ((hMax + hMin) / 2.0f - 127.5f) * (255.0f/127.5f) / multiplier;

                //Calculate the output values
                r -= (int)luminance;
                g -= (int)luminance;
                b -= (int)luminance;
                int w = (int)luminance;

                //Trim them so that they are all between 0 and 255
                if (r < 0) r = 0;
                if (g < 0) g = 0;
                if (b < 0) b = 0;
                if (w < 0) w = 0;
                if (r > 255) r = 255;
                if (g > 255) g = 255;
                if (b > 255) b = 255;
                if (w > 255) w = 255;
                out[offsetOut + 0] = (byte)r;
                out[offsetOut + 1] = (byte)g;
                out[offsetOut + 2] = (byte)b;
                out[offsetOut + 3] = (byte)w;
                break;}

        }
    }
}
