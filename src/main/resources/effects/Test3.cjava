import de.diavololoop.chloroplast.color.ColorModel;
import de.diavololoop.chloroplast.effect.Effect;
import de.diavololoop.chloroplast.config.SpacePosition;

import java.util.List;

public class Test3 extends Effect {

    public String getName() {
        return "Test3";
    }

    public String getAuthor() {
        return "Chloroplast";
    }

    public String getDescription() {
        return "Test3 (Effect)";
    }

    public int getPreferedFPS() {
        return 30;
    }

    public void init(String args, List<SpacePosition> positions) {
    }

    public ColorModel update(long time, int step, byte[] data) {

        for(int i = 0; i < data.length; i += 3){
            data[i + 0] = (byte)(step);
            data[i + 1] = (byte)(step);
            data[i + 2] = (byte)(step);
        }

        return ColorModel.RGB_MODEL;
    }

}
