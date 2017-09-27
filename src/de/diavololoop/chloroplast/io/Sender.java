package de.diavololoop.chloroplast.io;

import java.net.*;

/**
 * Created by gast2 on 26.09.17.
 */
public class Sender {

    InetAddress address;
    DatagramSocket clientSocket;

    public Sender(){

        try {

            address         = InetAddress.getByName("10.0.0.204");
            clientSocket    = new DatagramSocket();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void send(byte[] data){

        DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, 1234);
        try {

            clientSocket.send(sendPacket);

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

    }
}
