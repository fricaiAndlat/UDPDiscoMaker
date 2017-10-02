package de.diavololoop.chloroplast;

import de.diavololoop.chloroplast.color.ColorModel;
import de.diavololoop.chloroplast.util.SpacePosition;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gast2 on 28.09.17.
 */
public class StripeConfiguration {

    private List<Stripe> stripeList = new ArrayList<Stripe>();
    private List<SpacePosition> positions;

    public StripeConfiguration(File file, boolean redirectAdressToLocalhost) throws IOException {
        String configuration = null;
        try {
            configuration = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IOException("cant read File "+file.getAbsolutePath());
        }
        configuration = configuration.replaceAll("\r", "\n");
        configuration = configuration.replaceAll("/\\*.*?\\*/", "");
        configuration = configuration.replaceAll(" ", "");
        configuration = configuration.replaceAll("\t", "");

        String[] stripes = configuration.split("\n\n");
        for(String stripe: stripes){

            String[] lines = stripe.split("\n");
            if(lines.length < 1){
                throw new IOException("stripe must start with \"address:port:colormodel\":"+stripe);
            }
            String[] meta = lines[0].split(":");
            if(meta.length != 3){
                throw new IOException("stripe must start with \"address:port:colormodel\":"+stripe);
            }
            String address = redirectAdressToLocalhost ? "localhost" : meta[0];
            int port;
            try{
                port = Integer.parseInt(meta[1]);
            }catch(NumberFormatException e){
                throw new IOException("cant read port:"+meta[1]);
            }
            ColorModel.ByteOrder byteOrder = ColorModel.ByteOrder.get(meta[2]);
            if(byteOrder == null){
                throw new IOException("ByteOrder not implemented: "+meta[2]);
            }

            Stripe result = new Stripe(address, port, byteOrder);
            try{
                for(int i = 1; i < lines.length; ++i){
                    String pos[] = lines[i].split(":");
                    if(pos.length == 2){
                        result.addPosition(new SpacePosition(Float.parseFloat(pos[0]), Float.parseFloat(pos[1])));
                    } else if(pos.length == 3){
                        result.addPosition(new SpacePosition(Float.parseFloat(pos[0]), Float.parseFloat(pos[1]), Float.parseFloat(pos[2])));
                    } else if(pos.length < 2){
                        throw new IOException("position not readable, should be \"x:z\" or \"x:y:z\": "+lines[i]);
                    }
                }
            }catch(NumberFormatException e){
                throw new IOException("position array contains at least one not float vector: "+stripe);
            }
            stripeList.add(result);
        }

        //initialise every stripes offset
        int currentOffset = 0;
        for(Stripe stripe: stripeList){
            stripe.setOffset(currentOffset);
            currentOffset += 3;//stripe.getByteLength();
        }


    }

    public List<SpacePosition> getPositions(){

        if(positions == null) {

            positions = new ArrayList<>();
            for (Stripe stripe : stripeList) {
                positions.addAll(stripe.getPositions());
            }

        }

        return positions;

    }

    public List<Stripe> getStripes(){
        return stripeList;
    }

    public class Stripe {



        public final int port;
        public final String address;
        public final ColorModel.ByteOrder byteOrder;
        private List<SpacePosition> positions = new ArrayList<>();
        private int offset;

        public Stripe(String address, int port, ColorModel.ByteOrder byteOrder){
            this.port      = port;
            this.address   = address;
            this.byteOrder = byteOrder;
        }

        public void addPosition(SpacePosition pos){
            positions.add(pos);
        }

        public List<SpacePosition> getPositions(){
            return positions;
        }

        public void setOffset(int offset){
            this.offset = offset;
        }

        public void copyData(byte[] in, byte[] out, ColorModel source){
            source.convert(in, offset, out, 0, positions.size(), byteOrder);
        }

        public int getByteLength(){
            return positions.size() * byteOrder.length();
        }

    }
}
