package de.diavololoop.chloroplast.config;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import de.diavololoop.chloroplast.color.ColorCorrection;
import de.diavololoop.chloroplast.color.ColorModel;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by gast2 on 28.09.17.
 */
public class Config {

    private List<Stripe> stripeList = new ArrayList<Stripe>();
    private List<SpacePosition> positions;
    private Map<String, ColorCorrection> corrections = new HashMap<>();

    public Config(File file) throws IOException {
        Gson gson = new Gson();

        try {
            String fileContent = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            FileRepresentation fileRepr = gson.fromJson(fileContent, FileRepresentation.class);

            fileRepr.colorcorrection.forEach((key, val) -> corrections.put(key, new ColorCorrection(val.toMatrix())));
            corrections.put("none", new ColorCorrection());

            if (!fileRepr.isValid()) {
                throw new IOException("missing values in file: "+file.getAbsolutePath());
            }

            for (StripeRepresentation stripeRepr: fileRepr.stripes) {

                ColorModel.ByteOrder byteOrder = ColorModel.ByteOrder.get(stripeRepr.byteorder);
                ColorCorrection correction = corrections.get(stripeRepr.cc);

                if(correction == null){
                    System.err.println("warning: setting implicit correction = none");
                    correction = new ColorCorrection();
                }

                Stripe result = new Stripe(stripeRepr.address, stripeRepr.port, byteOrder, correction);


                result.getPositions().addAll(stripeRepr.positions);

                stripeList.add(result);
            }


            //initialise every stripes offset
            int currentOffset = 0;
            for(Stripe stripe: stripeList){
                stripe.setOffset(currentOffset);
                int maxLength = ColorModel.maxByteLength();
                currentOffset += stripe.getPositions().size() * maxLength;
            }

        } catch (IOException e) {
            throw new IOException("cant read File "+file.getAbsolutePath());
        } catch (JsonSyntaxException e) {
            throw new IOException("Syntax error in File "+file.getAbsolutePath());
        }




    }

    public void redirectAllToLocalhost(){
        Set<Integer> portsInUse = new HashSet<>();
        stripeList.stream().forEach(stripe -> {
            while(portsInUse.contains(stripe.port)) {
                ++stripe.port;
            }
            portsInUse.add(stripe.port);
            stripe.address = "localhost";
        });
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

        private int port;
        private String address;
        public final ColorModel.ByteOrder byteOrder;
        private List<SpacePosition> positions = new ArrayList<>();
        private int offset;
        private ColorCorrection colorCorrection;

        public Stripe(String address, int port, ColorModel.ByteOrder byteOrder, ColorCorrection correction){
            this.port      = port;
            this.address   = address;
            this.byteOrder = byteOrder;
            this.colorCorrection = correction;
        }

        public List<SpacePosition> getPositions(){
            return positions;
        }

        public void setOffset(int offset){
            this.offset = offset;
        }

        public void copyData(byte[] in, byte[] out, ColorModel source){
            source.convert(in, offset, out, 0, positions.size(), byteOrder);

            switch (byteOrder) {

                case RGB: colorCorrection.correct3(out, 0, positions.size(), 0, 1, 2); break;
                case GBR: colorCorrection.correct3(out, 0, positions.size(), 2, 0, 1); break;
                case GRB: colorCorrection.correct3(out, 0, positions.size(), 1, 0, 2); break;
                case BGR: colorCorrection.correct3(out, 0, positions.size(), 2, 1, 0); break;
                case BRG: colorCorrection.correct3(out, 0, positions.size(), 1, 2, 0); break;
                case HSV: break;
                case RGBW: colorCorrection.correct4(out, 0, positions.size(), 0, 1, 2,3); break;
                case GRBW: colorCorrection.correct4(out, 0, positions.size(), 1, 0, 2,3); break;
            }
        }

        public int getByteLength(){
            return positions.size() * byteOrder.length();
        }

        public String getAddress(){
            return address;
        }
        public int getPort(){
            return port;
        }
    }

    private class FileRepresentation {

        List<StripeRepresentation> stripes;
        Map<String, CCRepresentation> colorcorrection;

        private boolean isValid(){
            return colorcorrection != null && stripes.stream().allMatch(s -> s.isValid());
        }


    }

    private class StripeRepresentation {

        String address;
        int port = -1;
        String byteorder;
        String cc; //colorcorrection

        List<SpacePosition> positions;

        public boolean isValid() {
            return address != null && port != -1 && byteorder != null && cc != null;
        }
    }

    private class CCRepresentation {

        int rr, gr, br, wr;
        int rg, gg, bg, wg;
        int rb, gb, bb, wb;
        int rw, gw, bw, ww;

        public int[] toMatrix() {
            return new int[]{
                    rr, gr, br, wr,
                    rg, gg, bg, wg,
                    rb, gb, bb, wb,
                    rw, gw, bw, ww};
        }

    }
}
