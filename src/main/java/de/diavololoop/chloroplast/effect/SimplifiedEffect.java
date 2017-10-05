package de.diavololoop.chloroplast.effect;

import de.diavololoop.chloroplast.color.ColorModel;
import de.diavololoop.chloroplast.config.SpacePosition;

import java.util.List;

/**
 * @author Chloroplast
 *
 * the Base class for an Simplified Effect.
 */
public abstract class SimplifiedEffect extends Effect{

    protected List<SpacePosition> positions;
    protected ColorModel model;

    /**
     * Constructor for creating a simplified Effect
     *
     * @param model the color model to be used for the whole effect
     */
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
        float[] buffer = new float[model.getLength()];

        for(SpacePosition pos: positions){

            update(time, step, pos, buffer);

            for(int i = 0; i < model.getLength(); ++i){
                data[offset + i] = (byte)(255 * Math.min(1, Math.max(0, buffer[i])));
            }

            offset += model.getLength();
        }

        return model;
    }

    /**
     * before a Simplified Effect is used, the init method wil be called with the arguments for the effect
     *
     * @param args effect arguments
     */
    public abstract void init(String args);

    /**
     * simplified update method. instead of updating the whole data array the color just be defined for gived position
     *
     * @param time the time since the effect was started in milliseconds
     * @param step the iteration step
     * @param pos the pos of current perforemd LED
     * @param target array to store the color data. range is [0, 1]
     */
    public abstract void update(long time, int step, SpacePosition pos, float[] target);
}
