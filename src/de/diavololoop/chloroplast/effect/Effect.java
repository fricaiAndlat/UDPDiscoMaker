package de.diavololoop.chloroplast.effect;

import de.diavololoop.chloroplast.color.ColorModel;
import de.diavololoop.chloroplast.util.SpacePosition;

import java.util.List;

/**
 * Created by gast2 on 26.09.17.
 */
public abstract class Effect {

    public abstract String getName();
    public abstract String getAuthor();
    public abstract String getDescription();

    public int getPreferedFPS(){
        return 1;
    }

    public abstract ColorModel update(long time, int step, byte[] data);

    public void init(String args, List<SpacePosition> positions){
    }

    public void kill(){
    }

}
