
/**
 * Created by gast2 on 26.09.17.
 */
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

    public void init(int nleds, String args) {

    }

    public void update(long time, int step, byte[] data) {

        for(int i = 0; i < data.length; ++i){
            data[i] = (byte)((step*4) % 255);
        }

    }
}
