package de.diavololoop.chloroplast.effect;

import de.diavololoop.chloroplast.color.ColorModel;
import de.diavololoop.chloroplast.util.ColorPicker;
import de.diavololoop.chloroplast.util.SpacePosition;

import java.util.List;
import java.util.Random;

/**
 * Created by gast2 on 29.09.17.
 */
public class EffectRandomBlink extends Effect{

    private byte[] colorOn = new byte[3];
    private byte[] colorOff = new byte[3];
    private int propability = 50;
    private Random r = new Random();

    @Override
    public String getName() {
        return "RandomBlink";
    }

    @Override
    public String getAuthor() {
        return "Chloroplast";
    }

    @Override
    public String getDescription() {
        return "blinks at random pixels";
    }

    @Override
    public int getPreferedFPS() {
        return 25;
    }

    @Override
    public void init(String args, List<SpacePosition> positions) {
        propability = 50;
        colorOn[0] = colorOn[1] = colorOn[2] = -1;
        colorOff[0] = colorOff[1] = colorOff[2] = 0;

        String[] meta = args.split("&");
        for(String element: meta){

            String[] values = element.split("=");
            if(values.length != 2){
                System.err.println("given parameter could not be read"+element);
                continue;
            }
            try{

                if(values[0].equalsIgnoreCase("color")){
                    ColorPicker.getColor(values[1], colorOn, 0);
                }else if(values[0].equalsIgnoreCase("back")){
                    ColorPicker.getColor(values[1], colorOff, 0);
                }else if(values[0].equalsIgnoreCase("p")){
                    propability = Integer.parseInt(values[1]);
                }

            }catch (Exception e){
                System.err.println("given parameter not contains Float: "+element);
                continue;
            }


        }
    }

    @Override
    public ColorModel update(long time, int step, byte[] data) {

        boolean isOn;

        for(int i = 0; i < data.length; i+=3){

            isOn = /*r.nextInt(1024)*/System.nanoTime() % 0x3FF < propability;

            data[i + 0] = isOn ?  colorOn[0] : colorOff[0];
            data[i + 1] = isOn ?  colorOn[1] : colorOff[1];
            data[i + 2] = isOn ?  colorOn[2] : colorOff[2];

        }

        return ColorModel.RGB_MODEL;
    }
}
