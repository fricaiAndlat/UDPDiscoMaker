package de.diavololoop.chloroplast.effect;

import de.diavololoop.chloroplast.color.ColorModel;
import de.diavololoop.chloroplast.util.SpacePosition;
import javafx.scene.paint.Color;

import java.util.Arrays;
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

        Color colorOn = Color.rgb(80,80,0);
        Color colorOff = Color.BLACK;
        propability = 50;

        String[] meta = args.split("&");
        for(String element: meta){

            String[] values = element.split("=");
            if(values.length != 2){
                System.err.println("given parameter could not be read"+element);
                continue;
            }
            try{

                if(values[0].equalsIgnoreCase("color")){
                    colorOn = Color.valueOf(values[1].replace("%", "#"));
                }else if(values[0].equalsIgnoreCase("back")){
                    colorOff = Color.valueOf(values[1].replace("%", "#"));
                }else if(values[0].equalsIgnoreCase("p")){
                    propability = Integer.parseInt(values[1]);
                }

            }catch (Exception e){
                System.err.println("given parameter not contains Float: "+element);
                continue;
            }


        }

        this.colorOn[0] = (byte)(colorOn.getRed()*255);
        this.colorOn[1] = (byte)(colorOn.getGreen()*255);
        this.colorOn[2] = (byte)(colorOn.getBlue()*255);

        this.colorOff[0] = (byte)(colorOff.getRed()*255);
        this.colorOff[1] = (byte)(colorOff.getGreen()*255);
        this.colorOff[2] = (byte)(colorOff.getBlue()*255);

        System.out.println("on: "+ Arrays.toString(this.colorOn));
        System.out.println("off: "+ Arrays.toString(this.colorOff));
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
