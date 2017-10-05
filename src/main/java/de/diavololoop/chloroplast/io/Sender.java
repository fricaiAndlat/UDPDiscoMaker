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
 * @author Chloroplast
 *
 * Sender which takes colors in an array and sends it over all LED-Stripes with UDP
 */
public class Sender {

    private DatagramSocket clientSocket;
    private Config configuration;

    private Map<Config.Stripe, InetAddress> addresses = new HashMap<>();
    private Map<Config.Stripe, byte[]> buffers = new HashMap<>();

    /**
     * constructor for this Sender
     *
     * @param configuration the configuration the sender to be prepared for
     */
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

    /**
     * convert and sends the color-data to the LED-Stripes configured in the Configuration
     *
     * @param data the array containing all colorbytes
     * @param model the ColorModel used for the data array
     */
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
