package de.diavololoop.chloroplast;

import de.diavololoop.chloroplast.color.ColorModel;
import de.diavololoop.chloroplast.effect.Effect;
import de.diavololoop.chloroplast.io.Sender;

import java.util.Arrays;

/**
 * Created by gast2 on 27.09.17.
 */
public class EffectPlayer {

    private boolean shouldStop = false;
    private Thread playThread;
    private Sender sender;

    private Effect effect;

    private byte[] data;
    private byte[] dataLast;

    public EffectPlayer(Sender sender, Effect init, int bufferLen){

        this.sender = sender;
        this.effect = init;

        this.data     = new byte[bufferLen];
        this.dataLast = new byte[bufferLen];

        playThread = new Thread(this::run);
        playThread.start();

    }

    public void play(Effect effect){
        this.effect = effect;
        playThread.interrupt();

    }
    public void stop(){

        shouldStop = true;
        playThread.interrupt();

    }

    private void run() {

        while(!shouldStop){

            Effect current = effect;
            int fps = current.getPreferedFPS();

            long ddt = (fps == 0) ? Long.MAX_VALUE : 1000/fps;

            long timeLast = System.currentTimeMillis();
            long timeSinceStart = 0;
            int counter = 0;

            //play through ONE effect
            while(!playThread.interrupted()){

                long time = System.currentTimeMillis();
                long dt = time - timeLast;
                timeLast = time;
                timeSinceStart += dt;

                ColorModel model = current.update(timeSinceStart, counter++, data);

                if(!Arrays.equals(data, dataLast)){
                    sender.send(data, model);
                }

                System.arraycopy(data, 0, dataLast, 0, data.length);

                if(fps == 0){
                    //block until interupt
                    while(!playThread.isInterrupted()){
                        try {
                            Thread.sleep(2 * 60 * 1000);
                        } catch (InterruptedException e) {
                            playThread.interrupt();
                        }
                        sender.send(data, model);
                    }
                }else{
                    long timeToWait = ddt - dt;

                    if(timeToWait > 0){
                        try {
                            Thread.sleep(timeToWait);
                        } catch (InterruptedException e) {
                            playThread.interrupt();
                        }
                    }
                }

            }

        }

    }

}
