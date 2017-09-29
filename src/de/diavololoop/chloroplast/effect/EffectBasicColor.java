package de.diavololoop.chloroplast.effect;

import de.diavololoop.chloroplast.color.ColorModel;
import de.diavololoop.chloroplast.util.SpacePosition;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Created by gast2 on 26.09.17.
 */
public class EffectBasicColor extends Effect {

    byte r, g, b;

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
        try{

            Color color = Color.valueOf(args);
            r = (byte)(color.getRed()*255);
            g = (byte)(color.getGreen()*255);
            b = (byte)(color.getBlue()*255);

        } catch(Exception e){
            r = 0;
            g = 0;
            b = 0;
        }
    }

    @Override
    public ColorModel update(long time, int step, byte[] data) {

        for(int i = 0; i < data.length; ++i){
            if(i%3 == 0){
                data[i] = r;
            }else if(i%3 == 1){
                data[i] = g;
            }else if(i%3 == 2){
                data[i] = b;
            }
        }

        return ColorModel.RGB_MODEL;
    }

    @Override
    public void kill() {

    }
}
