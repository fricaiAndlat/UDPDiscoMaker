package de.diavololoop.chloroplast.effect;

import de.diavololoop.chloroplast.color.ColorModel;
import de.diavololoop.chloroplast.config.SpacePosition;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * @author Chloroplast
 *
 * Wrapper class for external effects.
 *
 * its used to wrap external processes descripted by a .stream File in an Effect class.
 *
 * All .stream Files must have at least two lines.
 * first contains the metadata, defined as "name:description:author"
 * the second line is the command which will be executed. For Example ./effects/MyProgram
 *
 * the external programm must print his output to stdout. Be sure that after every line the stream must be flushed.
 *
 * first it have to print "ColorEffect_v_1" to be sure this is a compatible program.
 * then it must print the preferedFPS as a new line.
 *
 * after this it should read the number of leds, followed by a new line with the effect arguments, followed by a new
 * line with an array of all positions printed as floats, seperated with ':'.
 *
 * after this initialisation the java core program sends every frame a new line
 * in the format "u:(long timeafterstart):(int iteration)".
 *
 * the extern process should then send all color bytes, prefixed with the name of used colormodel, in one line.
 * For Example "rgb:r0:g0:b0:r1:g1:b1:r1: .... :gn:bn"
 *
 * if the extern process receives a 'q' instead of the update line starting with 'u', it should close.
 */
public class EffectWrapperStream extends Effect {

    public final static int VERSION = 1;

    private Process process;

    private BufferedReader input;
    private Writer output;

    private String command;
    private String streamFile;

    private String name;
    private String author;
    private String description;
    private int preferedFPS;

    boolean isClosed = false;

    public EffectWrapperStream(File file) throws IOException {

        streamFile = file.getAbsolutePath();
        String[] streamInfo = new String(Files.readAllBytes(file.toPath()), UTF_8).split("\n");
        if(streamInfo.length < 2){
            throw new IOException("the given meta in stream file not contains name:description:author\\ncommand");
        }
        String[] metaInfo = streamInfo[0].trim().split(":");
        if(metaInfo.length != 3){
            throw new IOException("the given meta in stream file not contains name:description:author\\ncommand");
        }
        name        = metaInfo[0];
        description = metaInfo[1];
        author      = metaInfo[2];

        command     = streamInfo[1];
    }

    private void readMeta() throws IOException {
        String l = input.readLine();

        if(l == null){
            throw new IllegalArgumentException("process do not returned correct preferedFps: "+streamFile);
        }

        if(!l.matches("\\d{1,4}")){
            throw new IllegalArgumentException("preferedFps out of range [0-9999]: "+streamFile);
        }

        preferedFPS = Integer.parseInt(l);


    }

    private void testForVersion() {

        boolean[] isValidVersion = {false};

        Thread pollThread = new Thread(() -> {
            try {
                String l = input.readLine();
                if(l.equalsIgnoreCase("ColorEffect_v_"+VERSION)){

                    isValidVersion[0] = true;
                    synchronized (isValidVersion){
                        isValidVersion.notifyAll();
                    }

                }else{
                    synchronized (isValidVersion){
                        isValidVersion.notifyAll();
                    }
                    throw new IllegalArgumentException("process prints \""+l+"\" instead of \"ColorEffect_v_"+VERSION+"\"");
                }
            } catch (Exception e) {
                synchronized (isValidVersion){
                    isValidVersion.notifyAll();
                }
            }
        });

        pollThread.start();

        synchronized (isValidVersion){
            try {
                isValidVersion.wait(10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if( !isValidVersion[0] ){

            throw new IllegalArgumentException("process not prints \"ColorEffect_v_"+VERSION+"\" in 10s: "+streamFile);

        }




    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getPreferedFPS() {
        return preferedFPS;
    }

    @Override
    public void init(String args, List<SpacePosition> positions) {

        try {

            isClosed = false;
            process = Runtime.getRuntime().exec(command);

            input = new BufferedReader(new InputStreamReader(process.getInputStream(), UTF_8));
            output = new OutputStreamWriter(process.getOutputStream(), UTF_8);

            testForVersion();
            readMeta();
        } catch (Exception e) {
            System.err.println("cant start extern process "+streamFile+": "+e.getMessage());
            isClosed = true;
        }

        if(isClosed){
            return;
        }

        try {

            send(positions.size() + "\n" + (args.equals("") ? "null" : args));
            send(positions.stream().map(pos -> pos.toString()).collect(Collectors.joining(":")));

        } catch (IOException e) {
            e.printStackTrace();
            isClosed = true;
        }
    }

    @Override
    public ColorModel update(long time, int step, byte[] data) {

        if(isClosed){
            return null;
        }

        try {
            send("u:" + time + ":" + step);

            String line = input.readLine();
            if(line == null){
                isClosed = true;
                return ColorModel.RGB_MODEL;
            }
            String in[] = line.split(":");

            if(in.length < data.length){
                System.err.println("the extern process returned an array with wrong length, it should be >="+data.length+": "+streamFile);
            }
            int dataOffset = 0;

            ColorModel model = ColorModel.RGB_MODEL;
            if(!in[0].matches("\\d{1,10}")){
                dataOffset = 1;
                model = ColorModel.getModel(in[0]);
            }

            if(model == null){
                System.err.println("color model " + in[0] + "is not known: "+streamFile);
            }

            for(int i = 0; i < data.length; ++i){

                try {
                    data[i] = (byte) (Integer.parseInt(in[dataOffset + i].trim()) & 0xFF);
                }catch (NumberFormatException e){
                    System.err.println("the extern process returned a value which can not be casted to byte. string was \""+in[i]+"\": "+streamFile);
                }

            }

            return model;


        } catch (IOException e) {
            e.printStackTrace();
            isClosed = true;
        }

        return null;
    }

    @Override
    public void kill() {

        if(process  != null){
            try {

                send("q");
                boolean isEnded = process.waitFor(5, TimeUnit.SECONDS);
                if(!isEnded){
                    process.destroyForcibly();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }

    }

    @Override
    public String toString(){
        return "effect{name="+getName()+", author="+getAuthor()+", description="+getDescription()+", fps="+getPreferedFPS()+", class="+this.getClass().getName()+"}";
    }

    synchronized private void send(String s) throws IOException {
        output.write(s);
        output.write("\n");
        output.flush();
    }
}
