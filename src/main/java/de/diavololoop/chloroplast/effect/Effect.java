package de.diavololoop.chloroplast.effect;

import de.diavololoop.chloroplast.color.ColorModel;
import de.diavololoop.chloroplast.config.SpacePosition;

import java.util.List;

/**
 * @author Chloroplast
 *
 * Base Class for Effects. Used to get metadata from an Effect and the implementation
 */
public abstract class Effect {

    /**
     * @return the name of the effect. will be used when searching for effects
     */
    public abstract String getName();

    /**
     *
     * @return the name of the author from the Effect
     */
    public abstract String getAuthor();

    /**
     *
     * @return a short description what the Effect is supposed to do
     */
    public abstract String getDescription();

    /**
     * If there is enough CPU power to run this effect, this is the prefered number of iterations in a second.
     *
     * If fps = 0 then it will be only called once
     *
     * @return preferred fps
     */
    public int getPreferedFPS(){
        return 1;
    }

    /**
     * the effect implementation. will be called every frame
     *
     * @param time time in milliseconds after effect start
     * @param step the number of current iteration
     * @param data the array where to write the color informations.
     * @return the ColorModel used to store colors in the arrays
     */
    public abstract ColorModel update(long time, int step, byte[] data);

    /**
     * every time before an effect is used it must be called the init method.
     *
     * @param args optional arguments for the effect, for Example the color
     * @param positions the position of all LEDs available, in the order the output should be
     */
    public void init(String args, List<SpacePosition> positions){
    }

    /**
     * will be called when the effect is active and another effect is set, or the program is shut down
     */
    public void kill(){
    }

}
