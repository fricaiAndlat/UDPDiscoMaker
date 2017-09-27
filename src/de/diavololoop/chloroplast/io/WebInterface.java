package de.diavololoop.chloroplast.io;

import de.diavololoop.chloroplast.DiscoMaker;
import de.diavololoop.chloroplast.effect.Effect;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Created by gast2 on 27.09.17.
 */
public class WebInterface {

    private File interfaceDir;
    private ArrayList<WebInterface.Connection> connections = new ArrayList<WebInterface.Connection>();
    private ServerSocket server;

    private String site = "HELLO WORLD";
    private int siteLen = site.getBytes().length;

    private DiscoMaker discoMaker;

    public WebInterface(File root, DiscoMaker discoMaker) throws IOException {

        this.discoMaker = discoMaker;

        makeDirectory(root);
        server = new ServerSocket(8080);
        Thread accepter = new Thread(this::runAccept);
        accepter.start();

    }

    private void runAccept() {

        while(!Thread.currentThread().isInterrupted()) {

            try {
                connections.add(new Connection(server.accept()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private  void makeDirectory(File root){
        interfaceDir = new File(root, "webinterface");
        if(interfaceDir.isFile()){
            throw new RuntimeException("can't create directory "+interfaceDir.getAbsolutePath()+" - a file with the same name already exists");
        }
        if(!interfaceDir.exists()){
            boolean success = interfaceDir.mkdirs();

            if(!success) {
                throw new RuntimeException("can't create directory "+interfaceDir.getAbsolutePath());
            }
        }
    }

    /*synchronized*/ private void requestEffect(String effectName, String args){
        Effect effect = discoMaker.getEffectLoader().allEffects().get(effectName);

        if(effect == null){
            System.out.println("Effect "+effectName+" does not exists");
        }else{
            System.out.println("set Effect to " + effect.getName() + "#"+args);
            discoMaker.setEffect(effect, args);
        }
    }

    private void request(BufferedReader input, Writer output) throws IOException {
        String line = input.readLine();
        if(line == null){
            return;//throw new IOException("error reading data");
        }
        String[] head = line.split(" ");
        if(head.length < 2){
            throw new IOException("received Header doesn't match HTTP");
        }
        head[1] = head[1].substring(1);
        int pos = head[1].indexOf("?");
        if(pos == -1){
            requestEffect(head[1], "");
        }else{
            String effectName = head[1].substring(0, pos);
            String effectArgs = head[1].substring(pos+1);
            requestEffect(effectName, effectArgs);
        }



        while(null != (line = input.readLine())){
            if(line.equals("")){
                break;
            }
        }

        output.write("HTTP/1.1 200 OK\n");
        output.write("Content-Type: text/html; charset=UTF-8\n");
        output.write("Content-Encoding: UTF-8\n");
        output.write("Content-Length: " + siteLen + "\n");
        output.write("UDPDiscoMaker WebInterface\n");
        output.write("Connection: keep-alive\n");
        output.write("\n");
        output.write(site);
        //output.write("\n");
        output.flush();


    }

    private class Connection {

        boolean isDead = false;

        private BufferedReader input;
        private Writer output;
        private Socket socket;

        Thread listener;

        public Connection(Socket socket){

            this.socket = socket;

            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                output = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);

                listener = new Thread(this::run);
                listener.start();
            } catch (IOException e) {
                isDead = true;
            }

        }

        private void run() {

            while(!listener.isInterrupted()){

                try {
                    request(input, output);
                    socket.close();
                    listener.interrupt();

                } catch (IOException e) {
                    isDead = true;
                    listener.interrupt();
                    e.printStackTrace();
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

            }

        }


    }


}
