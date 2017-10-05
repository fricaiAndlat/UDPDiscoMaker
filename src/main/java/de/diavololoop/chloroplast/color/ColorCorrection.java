package de.diavololoop.chloroplast.color;

/**
 * @author Chloroplast
 *
 * Provides color correction with a matrix. Colors will be multiplied by this 4x4 Matrix,
 */
public class ColorCorrection {


    /*
         r  g  b  w

    r    0  1  2  3
    g    4  5  6  7
    b    8  9 10 11
    w   12 13 14 15

     */
    private int[] m;

    /**
     * Constructor for 'do nothing'. Equivalent to new ColorCorrection(new int[]{255, 0, 0, 0, 0, 255, 0, 0, 0, 0, 255, 0, 0, 0, 0, 255};
     */
    public ColorCorrection() {
        m = new int[]{
                255, 0, 0, 0,
                0, 255, 0, 0,
                0, 0, 255, 0,
                0, 0, 0, 255};
    }

    /**
     * creates new color correction out of given matrix.
     *
     * @param matrix the matrix to be used. matrix.length must be 16.
     */
    public ColorCorrection(int[] matrix) {
        if (matrix.length != 16) {
           throw new IllegalArgumentException("matrix length must be 16");
        }

        this.m = matrix;
    }

    /**
     * perform correction of RGB values.
     *
     * @param data the data the correction to be performed on. Values will be changed
     * @param offset the starting offset for correction (inclusive). Values before will not be changed
     * @param length the number of colors. Array length must be >= offset + 3*length
     * @param posR position of red in color-tupel. (for rgb  = 0)
     * @param posG position of green in color-tupel. (for rgb  = 1)
     * @param posB position of blue in color-tupel. (for rgb  = 2)
     */
    public void correct3(byte[] data, int offset, int length, int posR, int posG, int posB) {
        int r, g, b;

        for(int i = 0; i < length*3; i += 3){
            r = data[offset + i + posR] & 0xFF;
            g = data[offset + i + posG] & 0xFF;
            b = data[offset + i + posB] & 0xFF;

            data[offset + i + posR] = (byte)Math.min(255, Math.max(0, (m[0]*r + m[1]*g + m[ 2]*b) / 255));
            data[offset + i + posG] = (byte)Math.min(255, Math.max(0, (m[4]*r + m[5]*g + m[ 6]*b) / 255));
            data[offset + i + posB] = (byte)Math.min(255, Math.max(0, (m[8]*r + m[9]*g + m[10]*b) / 255));
        }

    }

    /**
     * perform correction of RGBW values.
     *
     * @param data the data the correction to be performed on. Values will be changed
     * @param offset the starting offset for correction (inclusive). Values before will not be changed
     * @param length the number of colors. Array length must be >= offset + 4*length
     * @param posR position of red in color-tupel. (for rgb  = 0)
     * @param posG position of green in color-tupel. (for rgb  = 1)
     * @param posB position of blue in color-tupel. (for rgb  = 2)
     * @param posW position of white in color-tupel. (for rgb  = 3)
     */
    public void correct4(byte[] data, int offset, int length, int posR, int posG, int posB, int posW) {
        int r, g, b,w;

        for(int i = 0; i < length*4; i += 4){
            r = data[offset + i + posR] & 0xFF;
            g = data[offset + i + posG] & 0xFF;
            b = data[offset + i + posB] & 0xFF;
            w = data[offset + i + posW] & 0xFF;

            data[offset + i + posR] = (byte)Math.min(255, Math.max(0, (m[ 0]*r + m[ 1]*g + m[ 2]*b + m[ 3]*w) / 255));
            data[offset + i + posG] = (byte)Math.min(255, Math.max(0, (m[ 4]*r + m[ 5]*g + m[ 6]*b + m[ 7]*w) / 255));
            data[offset + i + posB] = (byte)Math.min(255, Math.max(0, (m[ 8]*r + m[ 9]*g + m[10]*b + m[11]*w) / 255));
            data[offset + i + posW] = (byte)Math.min(255, Math.max(0, (m[12]*r + m[13]*g + m[14]*b + m[15]*w) / 255));
        }

    }

}
