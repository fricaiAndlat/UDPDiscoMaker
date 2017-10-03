package de.diavololoop.chloroplast.effect;

import de.diavololoop.chloroplast.color.ColorModel;
import de.diavololoop.chloroplast.config.SpacePosition;

import java.util.List;

/**
 * Created by gast2 on 29.09.17.
 */
public class EffectRainbow extends Effect {

    private List<SpacePosition> positions;
    private float speed;
    private float lightness;
    private float saturation;
    private float frequency;

    @Override
    public String getName() {
        return "Rainbow";
    }

    @Override
    public String getAuthor() {
        return "Chloroplast";
    }

    @Override
    public String getDescription() {
        return "displays a colorfull rainbow";
    }

    @Override
    public int getPreferedFPS() {
        return 30;
    }

    @Override
    public void init(String args, List<SpacePosition> positions) {
        this.positions = positions;

        speed = 1;
        lightness = 1;
        saturation = 1;
        frequency = 16;


        String[] meta = args.split("&");
        for(String element: meta){

            String[] values = element.split("=");
            if(values.length != 2){
                System.err.println("given parameter could not be read by EffectRainbow: "+element);
                continue;
            }
            try{

                if(values[0].equalsIgnoreCase("speed")){
                    speed = Float.parseFloat(values[1]);
                }else if(values[0].equalsIgnoreCase("light")){
                    lightness = Float.parseFloat(values[1]);
                }else if(values[0].equalsIgnoreCase("saturation")){
                    saturation = Float.parseFloat(values[1]);
                }else if(values[0].equalsIgnoreCase("freq")){
                    frequency = Float.parseFloat(values[1]);
                }

            }catch (NumberFormatException e){
                System.err.println("given parameter not contains Float: "+element);
                continue;
            }


        }
    }

    @Override
    public ColorModel update(long time, int step, byte[] data) {
        int offset = 0;

        for(SpacePosition pos: positions){

            data[offset + 0] = (byte)((pos.x + pos.y + pos.z) * frequency + step * speed);
            data[offset + 1] = (byte)(255 * saturation);
            data[offset + 2] = (byte)(255 * lightness);

            offset += 3;
        }

        return ColorModel.HSV_MODEL;
    }
}
