package de.diavololoop.chloroplast.effect;

import de.diavololoop.chloroplast.color.ColorModel;
import de.diavololoop.chloroplast.color.ColorPicker;
import de.diavololoop.chloroplast.config.SpacePosition;

import java.util.List;
import java.util.Random;

/**
 * @author Chloroplast
 *
 * Simple Effect turning randomly every single led on and off
 */
public class EffectRandomBlink extends Effect{

    private byte[] colorOn = new byte[4];
    private byte[] colorOff = new byte[4];
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

    /**
     * every time before an effect is used it must be called the init method.
     *
     * @param args a http parameter encoded list, for example speed=1&light=1&saturation=1&freq=16
     * @param positions the position of all LEDs available, in the order the output should be
     */
    @Override
    public void init(String args, List<SpacePosition> positions) {
        propability = 50;
        colorOn[0] = colorOn[1] = colorOn[2] = colorOn[3] = -1;
        colorOff[0] = colorOff[1] = colorOff[2] = colorOff[3] = 0;

        String[] meta = args.split("&");
        for(String element: meta){

            String[] values = element.split("=");
            if(values.length != 2){
                System.err.println("given parameter could not be read"+element);
                continue;
            }
            try{

                if(values[0].equalsIgnoreCase("color")){
                    ColorPicker.getColorRGBW(values[1], colorOn, 0);
                }else if(values[0].equalsIgnoreCase("back")){
                    ColorPicker.getColorRGBW(values[1], colorOff, 0);
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

        for(int i = 0; i < data.length; i+=4){

            isOn = r.nextInt(1024)/*System.nanoTime() % 0x3FF*/ < propability;

            data[i + 0] = isOn ?  colorOn[0] : colorOff[0];
            data[i + 1] = isOn ?  colorOn[1] : colorOff[1];
            data[i + 2] = isOn ?  colorOn[2] : colorOff[2];
            data[i + 3] = isOn ?  colorOn[3] : colorOff[3];

        }

        return ColorModel.RGBW_MODEL;
    }
}
