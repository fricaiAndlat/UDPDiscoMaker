package de.diavololoop.chloroplast.io;

import de.diavololoop.chloroplast.StripeConfiguration;
import de.diavololoop.chloroplast.color.ColorModel;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gast2 on 26.09.17.
 */
public class Sender {

    private DatagramSocket clientSocket;
    private StripeConfiguration configuration;

    private Map<StripeConfiguration.Stripe, InetAddress> addresses = new HashMap<>();
    private Map<StripeConfiguration.Stripe, byte[]> buffers = new HashMap<>();

    public Sender(StripeConfiguration configuration) throws IOException {

        this.configuration = configuration;

        for(StripeConfiguration.Stripe stripe: configuration.getStripes()){

            try {
                addresses.put(stripe, InetAddress.getByName(stripe.address));
                buffers.put(stripe, new byte[stripe.getByteLength()]);
            } catch (UnknownHostException e) {
                throw new IOException("unknow host: "+stripe.address);
            }

        }

        clientSocket    = new DatagramSocket();

    }

    public void send(byte[] data, ColorModel model){

        for(StripeConfiguration.Stripe stripe: configuration.getStripes()){

            byte[] buffer = buffers.get(stripe);
            InetAddress address = addresses.get(stripe);
            stripe.copyData(data, buffer, model);

            DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, address, stripe.port);
            try {
                clientSocket.send(sendPacket);

            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

        }


    }
}
