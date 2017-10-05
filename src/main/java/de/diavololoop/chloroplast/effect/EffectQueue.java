package de.diavololoop.chloroplast.effect;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import de.diavololoop.chloroplast.color.ColorModel;
import de.diavololoop.chloroplast.color.ColorPicker;
import de.diavololoop.chloroplast.config.SpacePosition;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author Chloroplast
 *
 * A Effect which holds many sub effects in many layers
 */
public class EffectQueue extends Effect {

    private final static Map<String, Supplier<? extends LayerEffect>> SUB_EFFECTS= new HashMap<>();

    private List<SpacePosition> positions;
    private long timeStarted;
    private Gson gson = new Gson();

    private List<Layer> layer;

    public EffectQueue() {
        SUB_EFFECTS.put("none", LayerEffectNone::new);
        SUB_EFFECTS.put("flashing", LayerEffectFlashing::new);
        SUB_EFFECTS.put("colorblink", LayerEffectColorSwitch::new);
    }

    @Override
    public String getName() {
        return "EffectQueue";
    }

    @Override
    public String getAuthor() {
        return "Chloroplast";
    }

    @Override
    public String getDescription() {
        return "Scriptable Queue of Effects";
    }

    @Override
    public int getPreferedFPS() {
        return 30;
    }
    /**
     * every time before an effect is used it must be called the init method.
     *
     * @param args the queue with its layers encoded in json
     * @param positions the position of all LEDs available, in the order the output should be
     */
    @Override
    public void init(String args, List<SpacePosition> positions) {
        this.positions = positions;
        timeStarted = System.currentTimeMillis();

        try {
            QueueDefinition def = gson.fromJson(args, QueueDefinition.class);
            if (def == null) {
                def = new QueueDefinition();
            }

            layer = def.layer;

            layer.forEach(l -> l.effects.forEach(e -> e.init()));
        } catch (JsonSyntaxException e) {
            System.err.println("error EffectQueue Json");
        }
    }

    @Override
    public ColorModel update(long time, int step, byte[] data) {
        for(int i = 0; i < data.length; ++i){
            data[i] = 0;
        }
        layer.forEach(l -> l.update(System.currentTimeMillis() - timeStarted, data));
        return ColorModel.RGBW_MODEL;
    }

    private class QueueDefinition {
        List<Layer> layer = new ArrayList<>();
    }

    private class Layer {

        private List<LayerEffectInfo> effects = new ArrayList<>();

        private Iterator<LayerEffectInfo> iterator;
        private LayerEffectInfo current;
        private LayerEffectInfo last;
        private long effectStartTime = 0;

        private void update(long time, byte[] data){

            if (iterator == null) {
                iterator = effects.iterator();
                effectStartTime = time;
            }
            long effectTime = time - effectStartTime;



            if (current == null) {
                if (!iterator.hasNext()) {
                    return;
                }
                current = iterator.next();
            }
            if (current.length < effectTime) {
                if (!iterator.hasNext()) {
                    return;
                }
                effectStartTime += current.length;
                effectTime -= current.length;
                last = current;
                current = iterator.next();
            }
            if(last != null && last.fade > effectTime){
                float x = (float)effectTime / last.fade;
                current.effect.update(effectTime, data, x);
                last.effect.update(effectTime + last.length, data, 1 - x);
            } else {
                current.effect.update(effectTime, data, 1);
            }
        }
    }

    private class LayerEffectInfo {
        String name;
        String args;
        float x, y, z;
        long cycleLen;
        float scaleX, scaleY, scaleZ;
        float onR, onG, onB, onW;
        float offR, offG, offB, offW;
        long length;
        long fade;

        LayerEffect effect;

        private void init() {
            Supplier<? extends LayerEffect> effectSupplier = SUB_EFFECTS.get(name);
            if (effectSupplier == null) {
                effect = new LayerEffectNone();
                return;
            }
            effect = effectSupplier.get();
            effect.init(this);
        }
    }

    private abstract class LayerEffect {
        protected abstract void init(LayerEffectInfo info);
        protected abstract void update(long time, byte[] data, float intensity);
    }

    private class LayerEffectNone extends LayerEffect{

        private LayerEffectNone(){

        }

        @Override
        protected void init(LayerEffectInfo info) {}

        @Override
        protected void update(long time, byte[] data, float intensity) {}
    }

    private class LayerEffectFlashing extends LayerEffect {

        private LayerEffectFlashing(){

        }

        LayerEffectInfo info;

        @Override
        protected void init(LayerEffectInfo info) {
            this.info = info;
        }


        @Override
        protected void update(long time, byte[] data, float intensity) {

            if ((time % info.cycleLen)  < info.scaleX) {
                for(int i = 0; i < data.length; i += 4){
                    data[i + 0] = (byte)Math.min(255, (data[i + 0] & 0xFF) + (int)(255 * info.onR * intensity));
                    data[i + 1] = (byte)Math.min(255, (data[i + 1] & 0xFF) + (int)(255 * info.onG * intensity));
                    data[i + 2] = (byte)Math.min(255, (data[i + 2] & 0xFF) + (int)(255 * info.onB * intensity));
                    data[i + 3] = (byte)Math.min(255, (data[i + 3] & 0xFF) + (int)(255 * info.onW * intensity));
                }
            } else {
                for(int i = 0; i < data.length; i += 4){
                    data[i + 0] = (byte)Math.min(255, (data[i + 0] & 0xFF) + (int)(255 * info.offR * intensity));
                    data[i + 1] = (byte)Math.min(255, (data[i + 1] & 0xFF) + (int)(255 * info.offG * intensity));
                    data[i + 2] = (byte)Math.min(255, (data[i + 2] & 0xFF) + (int)(255 * info.offB * intensity));
                    data[i + 3] = (byte)Math.min(255, (data[i + 3] & 0xFF) + (int)(255 * info.offW * intensity));
                }
            }
        }
    }

    private class LayerEffectColorSwitch extends LayerEffect {

        private LayerEffectColorSwitch() {
        }

        private int colorIndex;
        private byte[][] colors;
        private LayerEffectInfo info;
        private int[] colorBuffer = new int[4];

        private int period;

        private float offsetY;
        private float gradient;

        @Override
        protected void init(LayerEffectInfo info) {
            this.info = info;

            String[] cs = info.args.split(",");
            colors = new byte[cs.length][4];

            for (int i = 0; i < cs.length; ++i) {
                ColorPicker.getColorRGBW(cs[i], colors[i], 0);
            }

            if (info.scaleY == 0) {
                info.scaleY = 1;
            }

            gradient = -1 / info.scaleY;
            offsetY = -(info.scaleX + info.scaleY) * gradient;


        }

        @Override
        protected void update(long time, byte[] data, float intensity) {
            long t = time % info.cycleLen;
            float factor = (float)Math.pow(Math.max(0, Math.min(1, intensity * (offsetY + t * gradient))), 1);

            colorBuffer[0] = (int)((colors[colorIndex][0] & 0xFF) * factor);
            colorBuffer[1] = (int)((colors[colorIndex][1] & 0xFF) * factor);
            colorBuffer[2] = (int)((colors[colorIndex][2] & 0xFF) * factor);
            colorBuffer[3] = (int)((colors[colorIndex][3] & 0xFF) * factor);
            int p = (int)(time / info.cycleLen);
            if(p != period){
               nextColor();
            }
            period = p;

            for (int i = 0; i < data.length; ++i) {
                data[i] = (byte)Math.min(255, (data[i] & 0xFF) + colorBuffer[i%4]);
            }


        }

        private void nextColor(){
            colorIndex = (1 + colorIndex) % colors.length;
        }
    }
}
