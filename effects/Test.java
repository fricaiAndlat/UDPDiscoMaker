
/**
 * Created by gast2 on 26.09.17.
 */
public class Test{

    public String getName() {
        return "Basic Color";
    }

    public String getAuthor() {
        return "Chloroplast";
    }

    public String getDescription() {
        return "Simply displays a single color.";
    }

    public int getPreferedFPS() {
        return 0;
    }

    public void init(int nleds, String args) {

    }

    public void update(long time, int step, byte[] data) {

        for(int i = 0; i < data.length; ++i){
            data[i] = 20;
        }

    }
}
