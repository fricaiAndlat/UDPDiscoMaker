
import java.util.List;

public class Test{

    public String getName() {
        return "Basic";
    }

    public String getAuthor() {
        return "Test";
    }

    public String getDescription() {
        return "Test";
    }

    public int getPreferedFPS() {
        return 1;
    }

    public void init(String args, List<float[]> positions) {

    }

    public String update(long time, int step, byte[] data) {

        for(int i = 0; i < data.length; ++i){
            data[i] = (byte)((step*4) % 255);
        }
        return "rgb";

    }
}
