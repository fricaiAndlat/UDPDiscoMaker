
import de.diavololoop.chloroplast.color.ColorModel;
import de.diavololoop.chloroplast.effect.SimplifiedEffect;
import de.diavololoop.chloroplast.util.SpacePosition;

public class Test2 extends SimplifiedEffect{

    public Test2() {
        super(ColorModel.RGB_MODEL);
    }

    @Override
    public String getName() {
        return "Test2";
    }

    @Override
    public String getAuthor() {
        return "Chloroplast";
    }

    @Override
    public String getDescription() {
        return "Test2 (EffectSimplified)";
    }

    @Override
    public void init(String args) {
    }

    @Override
    public void update(long time, int step, SpacePosition pos, float[] target) {
        target[0] = (step%256)/255f;
        target[1] = (step%256)/255f;
        target[2] = (step%256)/255f;
    }
}
