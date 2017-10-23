package de.diavololoop.chloroplast.effect;

import de.diavololoop.chloroplast.color.ColorModel;
import de.diavololoop.chloroplast.color.ColorPicker;
import de.diavololoop.chloroplast.config.SpacePosition;

import java.util.List;

/**
 * @author pizzakatze
 * Effect for colored wave spreading with predefined speed and frequency along the strip
 */
public class EffectColorwave extends Effect {
    List<SpacePosition> positions;
    byte [] colorByte = new byte[4];
    int [] rgbw = new int [4];
    double frequency = 100;
    double speed = 1;

    @Override
    public String getName() {
        return "Colorwave";
    }

    @Override
    public String getAuthor() {
        return "pizzakatze";
    }

    @Override
    public String getDescription() {
        return "Colored wave spreading with predefined speed and frequency along the strip";
    }

    /**
     * every time before an effect is used it must be called the init method.
     *
     * @param args a http parameter encoded list, for example speed=1&freq=100&color=blue
     * @param positionList the position of all LEDs available, in the order the output should be
     */
    @Override
    public void init(String args, List<SpacePosition> positionList){
        this.positions = positionList;
        colorByte[0] = colorByte[1] = colorByte[2] = colorByte[3] = (byte) 0xFF;
        String[] meta = args.split("&");
        for(String element: meta){

            String[] values = element.split("=");
            if(values.length != 2){
                System.err.println("given parameter could not be read"+element);
                continue;
            }
            try{

                if(values[0].equalsIgnoreCase("color")) {
                    ColorPicker.getColorRGBW(values[1], colorByte, 0);
                    for(int i = 0; i < 4; i++){
                        rgbw[i] = colorByte[i] & 0xFF;
                    }
                }else if(values[0].equalsIgnoreCase("freq")){
                    frequency = Double.parseDouble(values[1]);
                }else if(values[0].equalsIgnoreCase("speed")){
                    speed = Double.parseDouble(values[1]);
                }

            }catch (Exception e){
                System.err.println("given parameter not contains Float: "+element);
                continue;
            }


        }
    }

    @Override
    public int getPreferedFPS() {
        return 30;
    }

    @Override
    public ColorModel update(long time, int step, byte[] data) {
        int offset = 0;
        for(SpacePosition pos : positions){
            double waveFunction = (.5 + .5 * Math.sin(((pos.z + pos.y + pos.x) * frequency * .001 - step * 0.01 * speed)));
            for(int i = 0; i < 4 ; i++){
                data[offset + i] = (byte) (rgbw[i] * waveFunction);
            }
            offset += 4;
        }
        return ColorModel.RGBW_MODEL;
    }
}
