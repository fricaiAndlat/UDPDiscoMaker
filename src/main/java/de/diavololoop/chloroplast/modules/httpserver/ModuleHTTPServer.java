package de.diavololoop.chloroplast.modules.httpserver;

import de.diavololoop.chloroplast.DiscoMaker;
import de.diavololoop.chloroplast.effect.Effect;
import de.diavololoop.chloroplast.modules.Module;
import fi.iki.elonen.NanoHTTPD;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Chloroplast
 *
 * Module for creating a HTTP Server. Can switch Effects over GET Requests.
 */
public class ModuleHTTPServer extends Module {

    private File interfaceDir;

    private String site;
    private String siteNotFound;
    private String siteNotImplemented;

    private int port;

    private HTTPServer server;


    public ModuleHTTPServer(DiscoMaker discoMaker) {
        super(discoMaker);
    }

    private void readFiles() throws IOException {

        File htmlFile = new File(interfaceDir, "WebInterface.html");

        if(!htmlFile.isFile()){
            boolean canCreate = htmlFile.createNewFile();
            if (!canCreate) {
                throw new IOException("cant create File " + htmlFile.getAbsolutePath());
            }

            //wait for file to be created
            while(!htmlFile.exists()){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            InputStream input = ModuleHTTPServer.class.getResourceAsStream("WebInterface.html");
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

        List<File> loadedFiles = Arrays.asList(interfaceDir.listFiles());

        site = site.replaceAll("\\{% load-quickmenu %}", loadAllFilesEndWith(loadedFiles, ".quick.html"));
        site = site.replaceAll("\\{% load-menu %}", loadAllFilesEndWith(loadedFiles, ".menu.html"));
        site = site.replaceAll("\\{% load-scripts %}", loadAllFilesEndWith(loadedFiles, ".js"));
        site = site.replaceAll("\\{% load-css %}", loadAllFilesEndWith(loadedFiles, ".css"));

        siteNotFound = "<html><title>404 - NOT FOUND</title><body><h1>Resource not found</h1></body></html>";
        siteNotImplemented = "<html><title>501 - NOT IMPLEMENTED</title><body><h1>501 - Not implemented</h1><h2>this server supports only GET requests</h2></body></html>";

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

    private  void makeDirectory(File root){
        interfaceDir = new File(root, "website");
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
            readFiles();

            server = new HTTPServer(port);
        } catch (IOException e) {
            return e.getMessage();
        }

        return null;
    }

    @Override
    public void onReaload() {

        try {
            readFiles();
        } catch (IOException e) {
            System.err.println("error while reading webserver files");
            program().exit();
        }

    }

    @Override
    public void onQuit() {
        server.stop();
    }

    /**
     * intern class handling HTTP requests
     */
    private class HTTPServer extends NanoHTTPD {

        public HTTPServer(int port) throws IOException {
            super(port);
            start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        }

        @Override
        public Response serve(IHTTPSession session) {
            if (session.getMethod() == Method.GET) {

                if (session.getUri().equals("/")) {
                    return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_HTML, site);
                } else if (session.getUri().equals("/favicon.ico")) {
                    return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_HTML, siteNotFound);
                }

                String effectName = session.getUri().substring(1);
                Effect effect = program().getEffectLoader().allEffects().get(effectName);

                if (effect == null) {
                    return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_HTML, siteNotFound);
                }
                String args = session.getQueryParameterString();
                if (args == null) {
                    program().setEffect(effect, "");
                } else {
                    program().setEffect(effect, args);
                }



                return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_HTML, site);
            }

            return newFixedLengthResponse(Response.Status.NOT_IMPLEMENTED, NanoHTTPD.MIME_HTML, siteNotImplemented);
        }
    }


}
