package de.diavololoop.chloroplast.io;

import de.diavololoop.chloroplast.config.Config;
import de.diavololoop.chloroplast.color.ColorModel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gast2 on 26.09.17.
 */
public class Sender {

    private DatagramSocket clientSocket;
    private Config configuration;

    private Map<Config.Stripe, InetAddress> addresses = new HashMap<>();
    private Map<Config.Stripe, byte[]> buffers = new HashMap<>();

    public Sender(Config configuration) throws IOException {

        this.configuration = configuration;

        for(Config.Stripe stripe: configuration.getStripes()){

            try {
                addresses.put(stripe, InetAddress.getByName(stripe.getAddress()));
                buffers.put(stripe, new byte[stripe.getByteLength()]);
            } catch (UnknownHostException e) {
                throw new IOException("unknow host: "+stripe.getAddress());
            }

        }

        clientSocket    = new DatagramSocket();

    }

    public void send(byte[] data, ColorModel model){

        for(Config.Stripe stripe: configuration.getStripes()){

            byte[] buffer = buffers.get(stripe);
            InetAddress address = addresses.get(stripe);
            stripe.copyData(data, buffer, model);

            DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, address, stripe.getPort());
            try {
                clientSocket.send(sendPacket);

            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

        }


    }
}
