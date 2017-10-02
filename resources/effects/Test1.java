import java.util.List;

public class Test1 {

    public String getName() {
        return "Test1";
    }

    public String getAuthor() {
        return "Chloroplast";
    }

    public String getDescription() {
        return "Test1 (plain java)";
    }

    public int getPreferedFPS() {
        return 20;
    }

    public void init(String args, List<float[]> positions) {
    }

    public String update(long time, int step, byte[] data) {

        for(int i = 0; i < data.length; i += 3){
            data[i + 0] = (byte)(step);
            data[i + 1] = (byte)(step);
            data[i + 2] = (byte)(step);
        }


        return "rgb";
    }

}