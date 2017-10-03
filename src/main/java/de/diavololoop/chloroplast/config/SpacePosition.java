package de.diavololoop.chloroplast.config;

/**
 * Created by gast2 on 28.09.17.
 */
public class SpacePosition {

    public final float x;
    public final float y;
    public final float z;

    public  SpacePosition(float x, float z){
        this(x, 0, z);
    }

    public SpacePosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString(){
        return x + ":" + y + ":" + z;
    }
}
