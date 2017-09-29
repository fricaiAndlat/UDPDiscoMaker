package de.diavololoop.chloroplast.offlinetest;

import de.diavololoop.chloroplast.StripeConfiguration;
import de.diavololoop.chloroplast.color.ColorModel;
import de.diavololoop.chloroplast.util.SpacePosition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;

/**
 * Created by gast2 on 29.09.17.
 */
public class TestGui {

    private final static float border = 0.01f;

    private JFrame frame;
    private JCanvas canvas;

    private BufferedImage image;
    private Graphics graphics;

    private float xMin, xMax;
    private float yMin, yMax;
    private float scale = 0;

    public TestGui(StripeConfiguration config){

        xMin = (float)config.getPositions().stream().mapToDouble(pos -> pos.x).min().orElse(0);
        xMax = (float)config.getPositions().stream().mapToDouble(pos -> pos.x).max().orElse(1);

        yMin = (float)config.getPositions().stream().mapToDouble(pos -> pos.y).min().orElse(0);
        yMax = (float)config.getPositions().stream().mapToDouble(pos -> pos.y).max().orElse(1);

        image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        graphics = image.getGraphics();

        frame = new JFrame("Stripe Debug Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        canvas = new JCanvas();
        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);

        frame.setVisible(true);

        config.getStripes().forEach(this::runServer);


    }

    private void runServer(StripeConfiguration.Stripe stripe){

        try {
            DatagramSocket socket = new DatagramSocket( stripe.port );
            byte[] buf = new byte[stripe.getByteLength()];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            Thread socketThread = new Thread(() ->{
                
                while(!Thread.currentThread().isInterrupted()){
                    
                    try {
                        socket.receive(packet);

                        graphics.setColor(Color.BLACK);
                        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

                        decodeBytes(packet.getData(), stripe.byteOrder, stripe.getPositions());
                        canvas.repaint();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                
            });

            socketThread.start();
            

        } catch (SocketException e) {
            e.printStackTrace();
        }

        

    }

    private void decodeBytes(byte[] data, ColorModel.ByteOrder byteOrder, List<SpacePosition> positions) {

        int offset = 0;
        byte[] buffer = new byte[3];
        for(SpacePosition pos: positions){


            switch(byteOrder){

                case RGB:
                    drawLED(toInt(data[offset + 0]), toInt(data[offset + 1]), toInt(data[offset + 2]), pos.x, pos.z);
                    break;
                case GBR:
                    drawLED(toInt(data[offset + 2]), toInt(data[offset + 0]), toInt(data[offset + 1]), pos.x, pos.z);
                    break;
                case GRB:
                    drawLED(toInt(data[offset + 1]), toInt(data[offset + 0]), toInt(data[offset + 2]), pos.x, pos.z);
                    break;
                case BGR:
                    drawLED(toInt(data[offset + 2]), toInt(data[offset + 1]), toInt(data[offset + 0]), pos.x, pos.z);
                    break;
                case BRG:
                    drawLED(toInt(data[offset + 1]), toInt(data[offset + 2]), toInt(data[offset + 0]), pos.x, pos.z);
                    break;
                case HSV:
                    ColorModel.HSV_MODEL.convert(data, offset, buffer, 0, ColorModel.ByteOrder.RGB);
                    drawLED(toInt(buffer[0]), toInt(buffer[1]), toInt(buffer[2]), pos.x, pos.z);
                    break;
                case RGBW:
                    int r = toInt(data[offset + 0]) / 2;
                    int g = toInt(data[offset + 1]) / 2;
                    int b = toInt(data[offset + 2]) / 2;
                    int w = toInt(data[offset + 3]) / 2;

                    drawLED(r + w, g + w, b + w, pos.x, pos.z);
                    break;
            }
            offset += byteOrder.length();
        }

    }

    private int toInt(byte b) {
        return ((int)b) & 0xFF;
    }

    private synchronized void drawLED(int r, int g, int b, float x, float z){
        graphics.setColor(new Color(r, g, b));
        graphics.fillOval((int)((x - xMin) * scale) +10, (int)((z + yMin) * scale) + 10, 20, 20);
    }



    private class JCanvas extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            if(this.getWidth() != image.getWidth() || this.getHeight() != image.getHeight()){
                image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
                graphics = image.getGraphics();

                scale = (1 - 2 * border) * Math.min(this.getWidth() / (xMax - xMin), this.getHeight() / (yMax - yMin));


            }

            g.drawImage(image, 0, 0, this);
        }
    }


}
