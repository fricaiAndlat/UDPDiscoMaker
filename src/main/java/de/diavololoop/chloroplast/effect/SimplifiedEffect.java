package de.diavololoop.chloroplast.effect;

import de.diavololoop.chloroplast.color.ColorModel;
import de.diavololoop.chloroplast.config.SpacePosition;

import java.util.List;

/**
 * Created by gast2 on 28.09.17.
 */
public abstract class SimplifiedEffect extends Effect{

    protected List<SpacePosition> positions;
    protected ColorModel model;

    public SimplifiedEffect(ColorModel model){
        this.model = model;
    }

    @Override
    public int getPreferedFPS() {
        return 30;
    }

    @Override
    public void init(String args, List<SpacePosition> positions) {
        this.positions = positions;
        init(args);
    }

    @Override
    public ColorModel update(long time, int step, byte[] data) {

        int offset = 0;
        float[] buffer = new float[3];

        for(SpacePosition pos: positions){

            update(time, step, pos, buffer);

            if(buffer[0] < 0) buffer[0] = 0;
            if(buffer[1] < 0) buffer[1] = 0;
            if(buffer[2] < 0) buffer[2] = 0;
            if(buffer[0] > 1) buffer[0] = 1;
            if(buffer[1] > 1) buffer[1] = 1;
            if(buffer[2] > 1) buffer[2] = 1;

            data[offset + 0] = (byte)(255 * buffer[0]);
            data[offset + 1] = (byte)(255 * buffer[1]);
            data[offset + 2] = (byte)(255 * buffer[2]);

            offset += 3;
        }

        return model;
    }

    public abstract void init(String args);

    public abstract void update(long time, int step, SpacePosition pos, float[] target);
}
