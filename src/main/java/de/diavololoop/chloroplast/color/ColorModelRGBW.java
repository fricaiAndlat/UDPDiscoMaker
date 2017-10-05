package de.diavololoop.chloroplast.color;

/**
 * @author Chloroplast
 *
 *
 * ColorModel for RGBW colors. A color will be interpreted as (red, green, blue, white).
 */
public class ColorModelRGBW extends ColorModel {

    @Override
    public int getLength() {
        return 4;
    }

    @Override
    public ByteOrder getByteOrder() {
        return ByteOrder.RGBW;
    }

    @Override
    public void convert(byte[] in, int offsetIn, byte[] out, int offsetOut, ByteOrder target) {

        switch(target) {

            case RGB:
                toRGB(in, offsetIn, out, offsetOut + 0, offsetOut + 1, offsetOut + 2);
                break;
            case GBR:
                toRGB(in, offsetIn, out, offsetOut + 2, offsetOut + 0, offsetOut + 1);
                break;
            case GRB:
                toRGB(in, offsetIn, out, offsetOut + 1, offsetOut + 0, offsetOut + 2);
                break;
            case BGR:
                toRGB(in, offsetIn, out, offsetOut + 2, offsetOut + 1, offsetOut + 0);
                break;
            case BRG:
                toRGB(in, offsetIn, out, offsetOut + 1, offsetOut + 2, offsetOut + 0);
                break;
            case HSV:
                throw new IllegalArgumentException("RGBW convert to HSV not implemented :(");
            case RGBW:
                out[offsetOut + 0] = in[offsetIn + 0];
                out[offsetOut + 1] = in[offsetIn + 1];
                out[offsetOut + 2] = in[offsetIn + 2];
                out[offsetOut + 3] = in[offsetIn + 3];
                break;
            case GRBW:
                out[offsetOut + 0] = in[offsetIn + 1];
                out[offsetOut + 1] = in[offsetIn + 0];
                out[offsetOut + 2] = in[offsetIn + 2];
                out[offsetOut + 3] = in[offsetIn + 3];
                break;
        }

    }

    private void toRGB(byte[] in, int offsetIn, byte[] out, int posR, int posG, int posB){
        out[posR] = (byte)Math.min(255, (in[offsetIn + 0] & 0xFF) + (in[offsetIn + 3] & 0xFF));
        out[posG] = (byte)Math.min(255, (in[offsetIn + 1] & 0xFF) + (in[offsetIn + 3] & 0xFF));
        out[posB] = (byte)Math.min(255, (in[offsetIn + 2] & 0xFF) + (in[offsetIn + 3] & 0xFF));
    }
}
