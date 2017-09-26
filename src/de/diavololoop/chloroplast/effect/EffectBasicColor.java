package de.diavololoop.chloroplast.effect;

/**
 * Created by gast2 on 26.09.17.
 */
public class EffectBasicColor implements Effect {
    @Override
    public String getName() {
        return "Basic Color";
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
    public void init(int nleds, String args) {

    }

    @Override
    public void update(long time, int step, byte[] data) {

        for(int i = 0; i < data.length; ++i){
            data[i] = 20;
        }

    }
}
