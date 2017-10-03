package de.diavololoop.chloroplast.modules.httpserver;

import de.diavololoop.chloroplast.DiscoMaker;
import de.diavololoop.chloroplast.effect.Effect;
import de.diavololoop.chloroplast.modules.Module;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gast2 on 27.09.17.
 */
public class ModuleHTTPServer extends Module {

    private File interfaceDir;
    private ArrayList<ModuleHTTPServer.Connection> connections = new ArrayList<>();
    private ServerSocket server;

    private String site = "HELLO WORLD";
    private int siteLen = site.getBytes().length;

    private int port;


    public ModuleHTTPServer(DiscoMaker discoMaker) {
        super(discoMaker);
    }

    private void readInterface() throws IOException {

        File htmlFile = new File(interfaceDir, "WebInterface.html");

        if(!htmlFile.isFile()){
            htmlFile.createNewFile();
            InputStream input = this.getClass().getResourceAsStream("WebInterface.html");
            OutputStream output = new FileOutputStream(htmlFile);

            byte[] buffer = new byte[1024];
            int len;
            while(-1 != (len = input.read(buffer))){
                output.write(buffer, 0, len);
            }

            output.flush();
            output.close();
            input.close();
        }

        site = new String(Files.readAllBytes(htmlFile.toPath()), StandardCharsets.UTF_8);
        siteLen = site.getBytes(StandardCharsets.UTF_8).length;

        List<File> loadedFiles = Arrays.asList(interfaceDir.listFiles());

        site = site.replaceAll("\\{% load-quickmenu %}", loadAllFilesEndWith(loadedFiles, ".quick.html"));
        site = site.replaceAll("\\{% load-menu %}", loadAllFilesEndWith(loadedFiles, ".menu.html"));
        site = site.replaceAll("\\{% load-scripts %}", loadAllFilesEndWith(loadedFiles, ".js"));
        site = site.replaceAll("\\{% load-css %}", loadAllFilesEndWith(loadedFiles, ".css"));



    }

    private String loadAllFilesEndWith(List<File> loadedFiles, String end){

        return loadedFiles
                .stream()
                .filter(file -> file.getName().endsWith(end))
                .map(file -> {
                    try {
                        return Files.readAllBytes(file.toPath());
                    } catch (IOException e) {
                        return null;
                    }
                })
                .filter(bytes -> bytes!=null)
                .map(bytes -> new String(bytes, StandardCharsets.UTF_8))
                .collect(Collectors.joining("\n"));
                //.replaceAll("\"", "'");
    }

    private void runAccept() {

        while(!Thread.currentThread().isInterrupted()) {

            try {
                connections.removeIf(con -> con.isDead);
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

    synchronized private void requestEffect(String effectName, String args){
        Effect effect = program().getEffectLoader().allEffects().get(effectName);

        if(effect == null){
            System.out.println("Effect "+effectName+" does not exists");
        }else{
            System.out.println("set Effect to " + effect.getName() + "#"+args);
            program().setEffect(effect, args);
        }
    }

    private void request(BufferedReader input, Writer output) throws IOException {
        String line = input.readLine();
        if(line == null){
            throw new IOException("error reading data");
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

    @Override
    public String getKey() {
        return "-http";
    }

    @Override
    public int getParameterCount() {
        return 1;
    }

    @Override
    public String init(String[] args) {
        markLoaded();

        String portString = args[0].trim();

        if(!portString.matches("\\d{1,5}")){
            return "cant start httpserver, given port is not valid: "+portString;
        }

        port = Integer.parseInt(portString);
        return null;
    }

    @Override
    public String onStart(File root) {

        try {
            makeDirectory(root);
            readInterface();
            server = new ServerSocket(port);
            Thread accepter = new Thread(this::runAccept);
            accepter.start();
        } catch (IOException e) {
            return e.getMessage();
        }

        return null;
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
