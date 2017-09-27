package de.diavololoop.chloroplast.effect;

/**
 * Created by gast2 on 26.09.17.
 */
public interface Effect {

    public String getName();
    public String getAuthor();
    public String getDescription();

    public int getPreferedFPS();

    public void init(int nleds, String args);
    public void update(long time, int step, byte[] data);
    public void kill();

}
