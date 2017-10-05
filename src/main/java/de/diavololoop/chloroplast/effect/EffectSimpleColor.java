package de.diavololoop.chloroplast.effect;

import de.diavololoop.chloroplast.color.ColorModel;
import de.diavololoop.chloroplast.color.ColorPicker;
import de.diavololoop.chloroplast.config.SpacePosition;


import java.util.List;

/**
 * Created by gast2 on 26.09.17.
 */
public class EffectSimpleColor extends Effect {

    byte[] rgbw = new byte[4];

    @Override
    public String getName() {
        return "SimpleColor";
    }

    @Override
    public String getAuthor() {
        return "Chloroplast";
    }

    @Override
    public String getDescription() {
        return "Simply displays a single color.";
    }

    @Override
    public int getPreferedFPS() {
        return 0;
    }

    @Override
    public void init(String args, List<SpacePosition> positions) {
        ColorPicker.getColorRGBW(args, rgbw, 0);
    }

    @Override
    public ColorModel update(long time, int step, byte[] data) {

        for(int i = 0; i < data.length; ++i){
            data[i] = rgbw[i%4];
        }

        return ColorModel.RGBW_MODEL;
    }

    @Override
    public void kill() {

    }
}