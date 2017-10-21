package de.diavololoop.chloroplast.effect;

import de.diavololoop.chloroplast.color.ColorModel;
import de.diavololoop.chloroplast.config.SpacePosition;
import org.jtransforms.fft.DoubleFFT_1D;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Chloroplast on 18.10.2017.
 */
public class EffectSpectrum extends Effect {

    public static void main(String[] args) {
        new EffectSpectrum().init(null, null);
    }

    @Override
    public void init(String args, List<SpacePosition> positions) {

/*        try {
            ServerSocket ss = new ServerSocket(1234);


            setupListener(44100, 16, 2, ss.accept());


            Thread.sleep(10 * 1000);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/


        double[] input = new double[20];
        for(int i = 0; i < input.length; ++i){
            double t = i/20.0;
            input[i] = Math.sin(2 * t * 2 * Math.PI) + 0.7 * Math.cos(2*Math.PI * t) + 0.3*Math.cos(9*2*Math.PI*t) + 0.1 * Math.cos(t * Math.PI * 2 * 4);
        }

        System.out.println(Arrays.toString(input));

        DoubleFFT_1D fftDo = new DoubleFFT_1D(input.length);
        double[] fft = new double[100 * 2];
        System.arraycopy(input, 0, fft, 0, input.length);
        //fftDo.realForwardFull(fft);
        fftDo.realForward(fft);

        int index = -1;
        double heighest = Double.NEGATIVE_INFINITY;
        for(int i = 0; i < fft.length; i+=2) {
            if(heighest <= fft[i]){
                heighest = fft[i];
                index = i/2;
            }
        }

        System.out.printf("heigest is %d with amount %f \r\n", index, heighest);

        for(int i = 0; i < input.length; ++i){
            System.out.println(Math.round(fft[2*i]*10000)/10000 +"\t"+ Math.round(fft[2*i+1]*10000)/10000);
        }


    }

    @Override
    public String getName() {
        return "Spectrum";
    }

    @Override
    public String getAuthor() {
        return "Chloroplast";
    }

    @Override
    public String getDescription() {
        return "Realtime Music playing Effect";
    }

    @Override
    public ColorModel update(long time, int step, byte[] data) {
        return ColorModel.RGBW_MODEL;
    }


    private void setupListener(float sampleRate, int bits, int channels, Socket socket) {

        try {

            AudioFormat af = new AudioFormat(sampleRate, bits, channels, true, false);

            SourceDataLine out = AudioSystem.getSourceDataLine(af);
            out.open(af, 1* 2048 * 2 * 2);
            out.start();


            byte[] buffer = new byte[64* 2048 * 2 * 2];
            int len;
            InputStream is = new BufferedInputStream(socket.getInputStream());

            long bufferlen = 0;
            long startTime = System.currentTimeMillis();

            while(-1 != (len = is.read(buffer))) {
                long time = System.currentTimeMillis() - startTime;
                bufferlen += len;

                out.write(buffer, 0, len);

                if((time - 5000) > 1000*bufferlen / (sampleRate * bits/8 * channels)){
                    Thread.sleep(1000);
                    System.out.println("sleeped");
                }else{
                    System.out.println("goodjob");
                }

                System.out.printf("available %d\t%d\t%d\r\n", bufferlen, (long)(sampleRate * bits/8 * channels * time/1000f), (long)(sampleRate * bits/8 * channels * (time+5000)/1000f));

            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void musicListener() {


    }


    private void setupSpectrum(int sampleRate){

    }
}
